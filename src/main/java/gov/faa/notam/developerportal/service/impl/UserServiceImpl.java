package gov.faa.notam.developerportal.service.impl;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

import gov.faa.notam.developerportal.configuration.PaginationConfig;
import gov.faa.notam.developerportal.exception.ApiException;
import gov.faa.notam.developerportal.model.api.AbstractRegisterRequest;
import gov.faa.notam.developerportal.model.api.ChangePasswordRequest;
import gov.faa.notam.developerportal.model.api.ForgotPasswordRequest;
import gov.faa.notam.developerportal.model.api.RegisterAdminRequest;
import gov.faa.notam.developerportal.model.api.RegisterUserRequest;
import gov.faa.notam.developerportal.model.api.ResetPasswordRequest;
import gov.faa.notam.developerportal.model.api.SearchResponse;
import gov.faa.notam.developerportal.model.api.SearchUserRequest;
import gov.faa.notam.developerportal.model.api.UpdateUserRequest;
import gov.faa.notam.developerportal.model.api.UserModel;
import gov.faa.notam.developerportal.model.entity.User;
import gov.faa.notam.developerportal.model.entity.UserRole;
import gov.faa.notam.developerportal.repository.UserRepository;
import gov.faa.notam.developerportal.security.PasswordPolicy;
import gov.faa.notam.developerportal.security.ReCaptcha;
import gov.faa.notam.developerportal.security.SecurityUtil;
import gov.faa.notam.developerportal.service.EmailService;
import gov.faa.notam.developerportal.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Setter
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private static final int VERIFICATION_CODE_BYTES = 16;

    /**
     * User repository to access the user table.
     */
    private final UserRepository userRepository;

    /**
     * ReCaptcha verification.
     */
    private final ReCaptcha reCaptcha;

    /**
     * Password policy.
     */
    private final PasswordPolicy passwordPolicy;

    /**
     * Password encoder for generating hashed password.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Email service for generating and sending emails.
     */
    private final EmailService emailService;

    /**
     * Page size configuration.
     */
    private final PaginationConfig paginationConfig;

    /**
     * Secure random for generating secure email verification code.
     */
    private SecureRandom secureRandom = new SecureRandom();

    @Override
    public void registerUser(RegisterUserRequest request) throws ApiException {
        if (!reCaptcha.verifyResponse(request.getReCaptchaResponse())) {
            throw new ApiException(HttpStatus.FORBIDDEN, "ReCaptcha failed.");
        }

        User user;

        Optional<User> retrieved = userRepository.findByEmailIncludingDeleted(request.getEmail());
        if (retrieved.isPresent()) {
            if (!retrieved.get().isDeleted()) {
                user = retrieved.get();
                if (user.getApproved() != null) {
                    throw new ApiException(HttpStatus.CONFLICT, "Email already registered.");
                }
            } else {
                user = new User();
                user.setEmail(request.getEmail());
                User deleted = retrieved.get();
                user.setId(deleted.getId());
                user.setCreatedAt(new Date());
                user.setCreatedBy(SecurityUtil.getCurrentUserId().orElse(null));
            }
        } else {
            user = new User();
            user.setEmail(request.getEmail());
        }

        user.setRole(UserRole.USER);

        copyToUser(request, user);

        user.setApproved(null);
        user.setEnabled(false);
        user.setEmailConfirmed(false);
        user.setDeleted(false);

        String code = generateOneTimeCode();
        user.setEmailConfirmationCode(code);
        user = userRepository.save(user);

        if (user != null)
        {
        	emailService.sendConfirmationMail(user.getId(), user.getEmail(), code);
        }
    }

    @Override
    public void registerAdmin(RegisterAdminRequest request) throws ApiException {
        Optional<User> retrieved = userRepository.findByEmailIncludingDeleted(request.getEmail());
        if (retrieved.isPresent() && !retrieved.get().isDeleted()) {
            throw new ApiException(HttpStatus.CONFLICT, "Email address already exists.");
        }

        User user = new User();
        retrieved.ifPresent(deleted -> {
            user.setId(deleted.getId());
            user.setCreatedAt(new Date());
            user.setCreatedBy(SecurityUtil.getCurrentUserId().orElse(null));
        });
        user.setEmail(request.getEmail());
        user.setRole(UserRole.ADMIN);

        copyToUser(request, user);

        user.setApproved(true);
        user.setEnabled(true);
        user.setEmailConfirmed(true);
        user.setDeleted(false);

        try {
            userRepository.save(user);
        } catch (ConstraintViolationException e) {
            throw new ApiException(HttpStatus.CONFLICT, "Could not create user: " + user.getEmail());
        }
    }

    @Override
    public void deleteAdmin(Long userId) throws ApiException {
        if (isSelf(userId)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "You cannot delete yourself.");
        }

        User user = getUserById(userId);
        if (user.getRole() != UserRole.ADMIN) {
            throw new ApiException(HttpStatus.FORBIDDEN, "You cannot delete regular user.");
        }

        user.setDeleted(true);
        userRepository.save(user);
    }

    @Override
    public void approveUser(Long userId) throws ApiException {
        User user = getUserById(userId);
        if (user.getApproved() != null) {
            throw new ApiException(HttpStatus.CONFLICT, "User already approved or denied: " + userId);
        }

        user.setApproved(true);
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    public void denyUser(Long userId) throws ApiException {
        User user = getUserById(userId);
        if (user.getApproved() != null) {
            throw new ApiException(HttpStatus.CONFLICT, "User already approved or denied: " + userId);
        }

        user.setApproved(false);
        userRepository.save(user);
    }

    @Override
    public void enableUser(Long userId) throws ApiException {
        User user = getUserById(userId);
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    public void disableUser(Long userId) throws ApiException {
        if (isSelf(userId)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Cannot disable yourself.");
        }

        User user = getUserById(userId);
        user.setEnabled(false);
        userRepository.save(user);
    }

    @Override
    public SearchResponse<UserModel> searchUser(SearchUserRequest request) throws ApiException {
        try {
            UserSpecification spec = new UserSpecification(request);

            Page<User> page = userRepository.findAll(spec, paginationConfig.toPageRequest(request));

            SearchResponse<UserModel> response = new SearchResponse<>();
            response.setTotalCount(page.getTotalElements());
            response.setItems(page.get().map(UserModel::new).collect(Collectors.toList()));
            return response;
        } catch (Exception e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to search user.", e);
        }
    }

    @Override
    public UserModel getUser(Long userId) throws ApiException {
        checkPermission(userId);

        return new UserModel(getUserById(userId));
    }

    @Override
    public void updateUser(Long userId, UpdateUserRequest request) throws ApiException {
        checkPermission(userId);

        User user = getUserById(userId);

        if (request.getPassword() != null) {
            user.setPasswordHash(validatePassword(user.getEmail(), request.getPassword()));
        }
        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        if (request.getCompany() != null) {
            user.setCompany(request.getCompany());
        }
        if (request.getAddress() != null) {
            user.setAddress(request.getAddress());
        }
        if (request.getCity() != null) {
            user.setCity(request.getCity());
        }
        if (request.getState() != null) {
            user.setState(request.getState());
        }
        if (request.getCountry() != null) {
            user.setCountry(request.getCountry());
        }
        if (request.getZipCode() != null) {
            user.setZipCode(request.getZipCode());
        }
        if (request.getPrimaryPhone() != null) {
            user.setPrimaryPhone(request.getPrimaryPhone());
        }
        if (request.getAlternatePhone() != null) {
            user.setAlternatePhone(request.getAlternatePhone());
        }
        if (request.getNotamDataIntendedUsage() != null) {
            user.setNotamDataIntendedUsage(request.getNotamDataIntendedUsage());
        }
        userRepository.save(user);
    }

    @Override
    public void verifyEmail(Long userId, String code) throws ApiException {
        User user = getUserById(userId);
        if (!code.equals(user.getEmailConfirmationCode())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "User not found.");
        }
        user.setEmailConfirmed(true);
        user.setEmailConfirmationCode(null);
        userRepository.save(user);
    }

    @Override
    public void changePassword(Long userId, ChangePasswordRequest request) throws ApiException {
        if (!isSelf(userId)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Can only change password of yourself.");
        }

        User user = getUserById(userId);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Wrong password.");
        }

        user.setPasswordHash(validatePassword(user.getEmail(), request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void resetPassword(Long userId, ResetPasswordRequest request) throws ApiException {
        User user = getUserById(userId);
        if (!request.getToken().equals(user.getPasswordResetToken())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "User not found.");
        }

        user.setPasswordResetToken(null);
        user.setPasswordHash(validatePassword(user.getEmail(), request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) throws ApiException {
        Optional<User> retrieved = userRepository.findByEmail(request.getEmail());
        if (retrieved.isEmpty()) {
            // do nothing if email not found.
            return;
        }
        User user = retrieved.get();
        if (user.getApproved() == null) {
            throw new ApiException(HttpStatus.CONFLICT, "User not approved yet.");
        }

        String code = generateOneTimeCode();
        user.setPasswordResetToken(code);
        userRepository.save(user);

        emailService.sendPasswordResetMail(user.getId(), user.getEmail(), code);
    }

    /**
     * Get user by id. Throws 404 if user id not found.
     *
     * @param userId - id of the user
     * @return user object from db.
     * @throws ApiException - if user id not found.
     */
    private User getUserById(Long userId) throws ApiException {
        Optional<User> retrieved = userRepository.findById(userId);
        if (retrieved.isEmpty()) {
            throw new ApiException(HttpStatus.NOT_FOUND, "User not found: " + userId);
        }
        return retrieved.get();
    }

    /**
     * Checks if the given user id is the current user from security context.
     *
     * @param userId - the user id to check against current user.
     * @return true if the given user id equals the current user id.
     */
    private boolean isSelf(Long userId) {
        Long currentUserId = SecurityUtil.getCurrentUserId().orElse(null);
        return userId.equals(currentUserId);
    }

    /**
     * Check current user's permission to access a given user object.
     *
     * @param userId - id of the user to read/update.
     * @throws ApiException - if the current user don't have permission to access the given user.
     */
    private void checkPermission(Long userId) throws ApiException {
        if (SecurityUtil.getCurrentUserRole() != UserRole.ADMIN && !isSelf(userId)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Regular user can access self information.");
        }
    }

    /**
     * Copy register request fields to user.
     *
     * @param request request object.
     * @param user    user to update.
     */
    private void copyToUser(AbstractRegisterRequest request, User user) throws ApiException {
        user.setFullName(request.getFullName());
        user.setPasswordHash(validatePassword(request.getEmail(), request.getPassword()));
        user.setCompany(request.getCompany());
        user.setAddress(request.getAddress());
        user.setCity(request.getCity());
        user.setState(request.getState());
        user.setZipCode(request.getZipCode());
        user.setCountry(request.getCountry());
        user.setPrimaryPhone(request.getPrimaryPhone());
        user.setAlternatePhone(request.getAlternatePhone());
        user.setNotamDataIntendedUsage(request.getNotamDataIntendedUsage());
    }

    /**
     * Validate password.
     *
     * @param username - username
     * @param password - password
     * @throws ApiException - if password is invalid according to the configured rules.
     */
    private String validatePassword(String username, String password) throws ApiException {
        if (!passwordPolicy.validatePassword(username, password)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Password validation failed.");
        }
        return passwordEncoder.encode(password);
    }

    /**
     * Generate a secure email verification code.
     *
     * @return the email verification code.
     */
    private String generateOneTimeCode() {
        byte[] raw = new byte[VERIFICATION_CODE_BYTES];
        secureRandom.nextBytes(raw);
        return Base64.getUrlEncoder().encodeToString(raw);
    }
}
