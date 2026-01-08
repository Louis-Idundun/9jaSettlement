package ng.com.louis.app9jasettlement.test.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Map;

/**
 * Standard error response DTO.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private String error;
    private String message;
    private Map<String, String> details;
    private Instant timestamp;

    public ErrorResponse() {
        this.timestamp = Instant.now();
    }

    public ErrorResponse(String error, String message) {
        this();
        this.error = error;
        this.message = message;
    }

    public ErrorResponse(String error, String message, Map<String, String> details) {
        this(error, message);
        this.details = details;
    }

}

