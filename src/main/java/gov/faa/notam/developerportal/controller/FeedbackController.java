package gov.faa.notam.developerportal.controller;

import javax.validation.Valid;

import gov.faa.notam.developerportal.exception.ApiException;
import gov.faa.notam.developerportal.model.api.SupportFeedbackRequest;
import gov.faa.notam.developerportal.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The feedback endpoint.
 * <p>
 * POST /support/feedback - login user send feedback email.
 * </p>
 */
@RestController
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    @PostMapping("/support/feedback")
    public void sendFeedback(@RequestBody @Valid SupportFeedbackRequest request) throws ApiException {
        feedbackService.sendFeedback(request.getComments());
    }
}
