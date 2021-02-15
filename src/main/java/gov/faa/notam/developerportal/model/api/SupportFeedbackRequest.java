package gov.faa.notam.developerportal.model.api;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * Request to send feedback.
 */
@Data
public class SupportFeedbackRequest {
    @NotBlank
    private String comments;
}
