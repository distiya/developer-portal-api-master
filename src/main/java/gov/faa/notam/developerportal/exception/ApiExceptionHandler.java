package gov.faa.notam.developerportal.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.faa.notam.developerportal.model.api.ApiError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.charset.StandardCharsets;

/**
 * Global exception handler.
 */
@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ApiExceptionHandler {

    private final ObjectMapper objectMapper;

    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<?> apiException(ApiException e) {
        if(MediaType.APPLICATION_OCTET_STREAM.equals(e.getMediaType())){
            ApiError apiError = e.toApiError();
            try {
                byte[] responseBytes = objectMapper.writeValueAsString(apiError).getBytes(StandardCharsets.UTF_8);
                return ResponseEntity.status(e.getHttpStatus()).body(responseBytes);
            } catch (JsonProcessingException jsonProcessingException) {
                return ResponseEntity.status(e.getHttpStatus()).body("".getBytes(StandardCharsets.UTF_8));
            }
        }
        return ResponseEntity.status(e.getHttpStatus()).body(e.toApiError());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> invalidRequest(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(e.getMessage()));
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> missingHttpBody(HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(e.getMessage()));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiError> exception(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiError(e.getMessage()));
    }
}
