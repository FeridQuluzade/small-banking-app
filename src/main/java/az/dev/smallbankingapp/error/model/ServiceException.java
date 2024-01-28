package az.dev.smallbankingapp.error.model;

import az.dev.smallbankingapp.util.MessageUtil;
import az.dev.smallbankingapp.util.RequestContextUtil;
import feign.error.FeignExceptionConstructor;
import feign.error.ResponseBody;
import feign.error.ResponseHeaders;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ServiceException extends RuntimeException {

    private String traceId;
    private Integer status;
    private String method;
    private String path;
    private String message;
    private LocalDateTime timestamp;
    private String code;
    private List<ValidationError> errors;
    private Map<String, Object> details = Collections.emptyMap();

    public ServiceException(String code, String message) {
        super(message);
        this.traceId = UUID.randomUUID().toString();
        this.code = code;
        this.status = HttpStatus.BAD_REQUEST.value();
        this.path = RequestContextUtil.getPath();
        this.method = RequestContextUtil.getMethod();
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    @FeignExceptionConstructor
    public ServiceException(@ResponseBody ErrorResponse errorResponse,
                            @ResponseHeaders Map<String, Collection<String>> headers) {
        if (errorResponse != null) {
            this.traceId = errorResponse.getTraceId();
            this.code = errorResponse.getCode();
            this.status = errorResponse.getStatus();
            this.path = errorResponse.getPath();
            this.method = errorResponse.getMethod();
            this.message = errorResponse.getMessage();
            this.timestamp = errorResponse.getTimeStamp();
            this.errors = errorResponse.getErrors();
            this.details = errorResponse.getDetails();
        }
    }

    public static ServiceException of(ErrorCode errorCode) {
        return new ServiceException(errorCode.code(), errorCode.message());
    }

    public static ServiceException of(ErrorCode errorCode, Object... args) {
        return new ServiceException(errorCode.code(),
                resolveMessage(errorCode.message(), args));
    }

    public void addDetail(String key, Object value) {
        if (details.isEmpty()) {
            details = new HashMap<>();
        }
        details.put(key, value);
    }

    public void setDetails(Map<String, Object> details) {
        if (details != null && details.isEmpty()) {
            this.details = details;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getDetail(String name) {
        return (T) details.get(name);
    }

    public boolean is(ErrorCode errorCode) {
        return errorCode != null && errorCode.code().equals(this.code);
    }

    public boolean is(HttpStatus httpStatus) {
        return httpStatus != null && httpStatus.name().equals(this.code);
    }

    public String formatProperties() {
        return Optional.ofNullable(details)
                .map(Map::keySet)
                .orElse(Collections.emptySet())
                .stream()
                .map(this::formatProperty)
                .collect(Collectors.joining(", "));
    }

    private static String resolveMessage(String message, Object... args) {
        return MessageUtil.resolveMessage(message, args);
    }

    private String formatProperty(String key) {
        return key + ": " + details.get(key);
    }

}