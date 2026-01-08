package ng.com.louis.app9jasettlement.test.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ApiResponse<T> {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime requestTime;

    private String requestType;

    private String referenceId;

    private boolean status;

    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;

    /**
     * Factory method for success responses
     */
    public static <T> ApiResponse<T> success(String requestType, T data, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setRequestTime(LocalDateTime.now());
        response.setReferenceId(UUID.randomUUID().toString());
        response.setRequestType(requestType);
        response.setStatus(true);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    /**
     * Factory method for error responses
     */
    public static <T> ApiResponse<T> error(String requestType, String message, String error) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setRequestTime(LocalDateTime.now());
        response.setReferenceId(UUID.randomUUID().toString());
        response.setRequestType(requestType);
        response.setStatus(false);
        response.setMessage(message);
        response.setError(error);
        return response;
    }
}
