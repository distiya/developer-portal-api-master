package gov.faa.notam.developerportal.service;

import gov.faa.notam.developerportal.exception.ApiException;
import gov.faa.notam.developerportal.model.api.*;
import org.springframework.security.access.annotation.Secured;

import javax.transaction.Transactional;

/**
 * User service for user registration and management.
 */
@Transactional
public interface UserService {
    String ROLE_ADMIN = "ROLE_ADMIN";

    /**
     * Register a new regular user. Initially the user is unapproved and disabled.
     *
     * @param request - the user registration request.
     * @throws ApiException - if any error occurs.
     */
    void registerUser(RegisterUserRequest request) throws ApiException;

    /**
     * Get user by the id.
     * Admins can access any user; Regular user can only see their own information.
     *
     * @param userId - id of the user.
     * @return the user.
     * @throws ApiException - if any error occurs.
     */
    UserModel getUser(Long userId) throws ApiException;

    /**
     * Update user by the id.
     * Admins can update any user; Regular user can only update their own information.
     *
     * @param userId  - id of the user.
     * @param request - user update request.
     * @throws ApiException - if any error occurs.
     */
    void updateUser(Long userId, UpdateUserRequest request) throws ApiException;

    /**
     * Verify user email by the id and code.
     * Query the database for the user with the given id and check if the email confirmation code matches the code
     * submitted by the user. If it matches, set the user's emailConfirmed field to true and clears the email confirmation code.
     *
     * @param userId - id of the user.
     * @param code   - confirmation code submitted by the user.
     * @throws ApiException - if any error occurs.
     */
    void verifyEmail(Long userId, String code) throws ApiException;

    void changePassword(Long userId, ChangePasswordRequest request) throws ApiException;

    void resetPassword(Long userId, ResetPasswordRequest request) throws ApiException;

    void forgotPassword(ForgotPasswordRequest request) throws ApiException;

    @Secured({ ROLE_ADMIN })
    void registerAdmin(RegisterAdminRequest request) throws ApiException;

    @Secured({ ROLE_ADMIN })
    void deleteAdmin(Long userId) throws ApiException;

    @Secured({ ROLE_ADMIN })
    void approveUser(Long userId) throws ApiException;

    @Secured({ ROLE_ADMIN })
    void denyUser(Long userId) throws ApiException;

    @Secured({ ROLE_ADMIN })
    void enableUser(Long userId) throws ApiException;

    @Secured({ ROLE_ADMIN })
    void disableUser(Long userId) throws ApiException;

    @Secured({ ROLE_ADMIN })
    SearchResponse<UserModel> searchUser(SearchUserRequest request) throws ApiException;
}
