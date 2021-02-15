package gov.faa.notam.developerportal.service;

import gov.faa.notam.developerportal.exception.ApiException;

/**
 * Feedback service for send user feedback.
 */
public interface FeedbackService {
    /**
     * Send feedback to the configured feedback address. Include the current user's email in the content.
     *
     * @param comments - user submitted comments.
     * @throws ApiException - if any error occurs.
     */
    void sendFeedback(String comments) throws ApiException;
}
