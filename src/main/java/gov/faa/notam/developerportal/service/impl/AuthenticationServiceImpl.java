package gov.faa.notam.developerportal.service.impl;

import gov.faa.notam.developerportal.exception.ApiException;
import gov.faa.notam.developerportal.model.api.LoginRequest;
import gov.faa.notam.developerportal.model.api.LoginResponse;
import gov.faa.notam.developerportal.model.entity.User;
import gov.faa.notam.developerportal.repository.UserRepository;
import gov.faa.notam.developerportal.security.JwtTokenProvider;
import gov.faa.notam.developerportal.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Optional;
import java.util.Set;

/**
 * Implementation of the authentication service.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final String AUTHENTICATION_FAILED =
            "Authentication failed, either the provided email does not exist, or the password is wrong.";

    private static final String USER_NOT_ENABLED = "User is not enabled";
    private static final String USER_NOT_APPROVED = "User is not approved";
    private static final String EMAIL_NOT_CONFIRMED = "Email address is not confirmed";

    /**
     * User repository.
     */
    private final UserRepository userRepository;

    /**
     * Password encoder for checking the password match the saved hash.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * For generating the JWT token.
     */
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * The bean validation instance
     */
    private final Validator validator;

    @Override
    public LoginResponse login(LoginRequest request) throws ApiException {
        validateLoginRequest(request);
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if (user.isEmpty() || !passwordEncoder.matches(request.getPassword(), user.get().getPasswordHash())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, AUTHENTICATION_FAILED);
        }
        validateUserState(user.get());
        return new LoginResponse(jwtTokenProvider.generateToken(user.get()));
    }

    private void validateLoginRequest(LoginRequest loginRequest) throws ApiException{
        Set<ConstraintViolation<LoginRequest>> validationErrors = validator.validate(loginRequest);
        if(Boolean.FALSE.equals(CollectionUtils.isEmpty(validationErrors))){
            ConstraintViolation<LoginRequest> loginRequestConstraintViolation = validationErrors.stream().findFirst().orElse(null);
            throw new ApiException(HttpStatus.BAD_REQUEST, loginRequestConstraintViolation.getMessage());
        }
    }

    private void validateUserState(User user) throws ApiException{
        if(Boolean.FALSE.equals(user.isEnabled())){
            throw new ApiException(HttpStatus.FORBIDDEN, USER_NOT_ENABLED);
        }
        if(user.getApproved() == null || Boolean.FALSE.equals(user.getApproved())){
            throw new ApiException(HttpStatus.FORBIDDEN, USER_NOT_APPROVED);
        }
        if(Boolean.FALSE.equals(user.isEmailConfirmed())){
            throw new ApiException(HttpStatus.FORBIDDEN, EMAIL_NOT_CONFIRMED);
        }
    }
}
