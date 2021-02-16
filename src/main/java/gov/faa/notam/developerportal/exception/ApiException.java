package gov.faa.notam.developerportal.exception;

import gov.faa.notam.developerportal.model.api.ApiError;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

/**
 * Generic API exception.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ApiException extends Exception {
    /**
     * The corresponding HTTP status code.
     */
    private HttpStatus httpStatus;

    private MediaType mediaType;

    public ApiException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public ApiException(MediaType mediaType, HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.mediaType = mediaType;
    }

    public ApiException(HttpStatus httpStatus, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    /**
     * Convert to {@link ApiError} object.
     *
     * @return the ApiError object.
     */
    public ApiError toApiError() {
        return new ApiError(getMessage());
    }
}
