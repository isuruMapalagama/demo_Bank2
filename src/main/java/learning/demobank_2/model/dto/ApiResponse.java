package learning.demobank_2.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private int statusCode;
    private String message;
    private T data;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

//    Create success response with data and custom message

    public static <T> ApiResponse<T> success(T data, String message){
        return ApiResponse.<T>builder()
                .statusCode(HttpStatus.OK.value())
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

//    Success response with default message
    public static <T> ApiResponse<T> success(T data){
        return success(data, "Success");
    }

//     Create created response for resource creation
    public static <T> ApiResponse<T> created(T data, String message){
        return ApiResponse.<T>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

//    Error response with custom status and message
    public static <T> ApiResponse<T> error(HttpStatus status, String message){
        return ApiResponse.<T>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(message)
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
    }
//    Not found error response
    public static <T> ApiResponse<T> notFound(String message) {
        return error(HttpStatus.NOT_FOUND, message);
    }
}
