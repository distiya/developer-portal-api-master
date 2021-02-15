package gov.faa.notam.developerportal.service;

/**
 * Email service is responsible for sending emails.
 */
public interface EmailService {
    /**
     * Send email confirmation email.
     *
     * @param userId           - id of the user
     * @param email            - email of the user
     * @param confirmationCode - random confirmation code.
     */
    void sendConfirmationMail(Long userId, String email, String confirmationCode);

    /**
     * Send password reset email.
     *
     * @param userId     - id of the user
     * @param email      - email of the user
     * @param resetToken - password reset token
     */
    void sendPasswordResetMail(Long userId, String email, String resetToken);

    /**
     * Send feedback email.
     *
     * @param userEmail - user email.
     * @param comments  - comments.
     */
    void sendFeedback(String userEmail, String comments);
}
