package az.dev.smallbankingapp.error.model;

import az.dev.smallbankingapp.util.RequestContextUtil;
import feign.error.FeignExceptionConstructor;
import feign.error.ResponseBody;
import feign.error.ResponseHeaders;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
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
    private String code;
    private Integer status;
    private String path;
    private String method;
    private String message;
    private LocalDateTime timeStamp;
    private List<ValidationError> errors;
    private Map<String, Object> details;

    public ServiceException(String code, String message) {
        super(message);
        this.traceId = UUID.randomUUID().toString();
        this.code = code;
        this.status = HttpStatus.BAD_REQUEST.value();
        this.path = RequestContextUtil.getPath();
        this.method = RequestContextUtil.getMethod();
        this.message = message;
        this.timeStamp = LocalDateTime.now();
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
            this.timeStamp = errorResponse.getTimeStamp();
            this.errors = errorResponse.getErrors();
            this.details = errorResponse.getDetails();
        }
    }

    public static ServiceException of(ErrorCode errorCode, String errorMessage) {
        return new ServiceException(errorCode.code(), errorMessage);
    }

    public ServiceException set(String name, Object value) {
        details.put(name, value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        return (T) details.get(name);
    }

    public boolean is(ErrorCode errorCode) {
        return errorCode != null && errorCode.code().equals(this.code);
    }

    public String formatProperties() {
        return Optional.ofNullable(details)
                .map(Map::keySet)
                .orElse(Collections.emptySet())
                .stream()
                .map(this::formatProperty)
                .collect(Collectors.joining(", "));
    }

    private String formatProperty(String key) {
        return key + ": " + details.get(key);
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}