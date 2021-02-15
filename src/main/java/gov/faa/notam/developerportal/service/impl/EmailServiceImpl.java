package gov.faa.notam.developerportal.service.impl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import gov.faa.notam.developerportal.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

/**
 * Implementation of the email service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    /**
     * The email sender.
     */
    private final JavaMailSender javaMailSender;

    /**
     * Template engine.
     */
    private final SpringTemplateEngine springTemplateEngine;

    /**
     * Email from address.
     */
    @Value("${mail.from}")
    private String from;

    /**
     * Feedback address.
     */
    @Value("${mail.feedback}")
    private String feedbackEmail;

    @Override
    public void sendConfirmationMail(Long userId, String email, String confirmationCode) {
        Context context = new Context();
        context.setVariable("userId", userId);
        context.setVariable("email", email);
        context.setVariable("code", confirmationCode);
        String body = springTemplateEngine.process("confirmation/content.html", context);
        String subject = springTemplateEngine.process("confirmation/subject.txt", context);
        try {
            sendHtmlMessage(email, subject, body);
        } catch (MessagingException e) {
            log.error("Failed to send confirmation email to {}", email, e);
        }
    }

    @Override
    public void sendPasswordResetMail(Long userId, String email, String resetToken) {
        Context context = new Context();
        context.setVariable("userId", userId);
        context.setVariable("email", email);
        context.setVariable("code", resetToken);
        String body = springTemplateEngine.process("passwordReset/content.html", context);
        String subject = springTemplateEngine.process("passwordReset/subject.txt", context);
        try {
            sendHtmlMessage(email, subject, body);
        } catch (MessagingException e) {
            log.error("Failed to send password reset email to {}", email, e);
        }
    }

    @Override
    public void sendFeedback(String userEmail, String comments) {
        Context context = new Context();
        context.setVariable("email", userEmail);
        context.setVariable("comments", comments);
        String body = springTemplateEngine.process("feedback/content.html", context);
        String subject = springTemplateEngine.process("feedback/subject.txt", context);
        try {
            sendHtmlMessage(feedbackEmail, subject, body);
        } catch (MessagingException e) {
            log.error("Failed to send feedback email for user {}", userEmail, e);
        }
    }

    /**
     * Send HTML email.
     *
     * @param to      - recipient address
     * @param subject - subject
     * @param body    - mail body
     * @throws MessagingException                     - if any error occurs during message construction.
     * @throws org.springframework.mail.MailException - if any error occurs sending the message.
     */
    private void sendHtmlMessage(String to, String subject, String body) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setFrom(from);
        helper.setSubject(subject);
        helper.setText(body, true);
        javaMailSender.send(message);
    }
}
