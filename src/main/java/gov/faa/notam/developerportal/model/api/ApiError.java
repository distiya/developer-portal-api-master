package gov.faa.notam.developerportal.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents an error response to the client.
 * <p>
 * Corresponds to the <code>#/components/ApiErrorModel</code> from swagger.json.
 */
@Data
@AllArgsConstructor
public class ApiError {
    /**
     * Error message.
     */
    private String message;
}
