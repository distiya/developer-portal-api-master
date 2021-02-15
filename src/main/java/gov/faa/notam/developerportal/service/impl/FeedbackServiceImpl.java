package gov.faa.notam.developerportal.service.impl;

import java.util.Optional;

import gov.faa.notam.developerportal.exception.ApiException;
import gov.faa.notam.developerportal.security.SecurityUtil;
import gov.faa.notam.developerportal.service.EmailService;
import gov.faa.notam.developerportal.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackServiceImpl implements FeedbackService {
    private final EmailService emailService;

    @Override
    public void sendFeedback(String comments) throws ApiException {
        Optional<String> email = SecurityUtil.getCurrentUserEmail();
        email.ifPresent(e -> {
            emailService.sendFeedback(e, comments);
        });
    }
}
