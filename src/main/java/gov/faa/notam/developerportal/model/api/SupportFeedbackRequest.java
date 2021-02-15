package gov.faa.notam.developerportal.model.api;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Request to send feedback.
 */
@Data
public class SupportFeedbackRequest {
    @NotBlank
    private String comments;
}
