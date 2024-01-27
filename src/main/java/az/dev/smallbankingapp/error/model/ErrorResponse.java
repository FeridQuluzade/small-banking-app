package az.dev.smallbankingapp.error.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

import az.dev.smallbankingapp.util.RequestContextUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)
public class ErrorResponse {

    private String traceId;
    private String code;
    private Integer status;
    private String method;
    private String path;
    private String message;
    private LocalDateTime timeStamp;
    private List<ValidationError> errors;
    private Map<String, Object> details;

    public static ErrorResponse fromServiceException(ServiceException ex) {
        return ErrorResponse.builder()
                .traceId(ex.getTraceId())
                .code(ex.getCode())
                .status(ex.getStatus())
                .method(ex.getMethod())
                .path(ex.getPath())
                .message(ex.getMessage())
                .timeStamp(ex.getTimeStamp())
                .errors(ex.getErrors())
                .details(ex.getDetails())
                .build();
    }

    public static ErrorResponse build(String traceId,
                                      HttpStatus status,
                                      String message) {
        return build(traceId, status, message, Collections.emptyList());
    }

    public static ErrorResponse build(String traceId,
                                      HttpStatus status,
                                      String message,
                                      List<ValidationError> errors) {
        return ErrorResponse.builder()
                .traceId(traceId)
                .code(status.name())
                .status(status.value())
                .path(RequestContextUtil.getPath())
                .method(RequestContextUtil.getMethod())
                .message(message)
                .timeStamp(LocalDateTime.now())
                .errors(errors)
                .build();
    }

}