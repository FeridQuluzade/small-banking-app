package az.dev.smallbankingapp.error;

import az.dev.smallbankingapp.error.model.ErrorLevel;
import az.dev.smallbankingapp.error.model.ErrorResponse;
import az.dev.smallbankingapp.error.model.ServiceException;
import az.dev.smallbankingapp.error.model.UnauthorizedException;
import az.dev.smallbankingapp.error.model.ValidationError;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class CommonErrorHandler extends ResponseEntityExceptionHandler {

    @Resource
    private MessageSource messageSource;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServiceException.class)
    public ErrorResponse handleServiceExceptions(ServiceException ex) {
        log.error("Service error, status: {}, traceId: {}, code: {}, message: {}, {}",
                ex.getStatus(),
                ex.getTraceId(),
                ex.getCode(),
                ex.getMessage(),
                ex.formatProperties());
        return ErrorResponse.fromServiceException(ex);
    }

    @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
    @ExceptionHandler({ConnectException.class, SocketTimeoutException.class})
    public ErrorResponse handleServiceTimeout(Exception ex) {
        String traceId = UUID.randomUUID().toString();
        log.error("Service timeout error, traceId: {}, code: {}, message: {}",
                traceId, HttpStatus.GATEWAY_TIMEOUT, ex.getMessage());
        return ErrorResponse.build(
                traceId,
                HttpStatus.GATEWAY_TIMEOUT,
                "Service timeout error"
        );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleInternalServerErrors(Exception ex) {
        String traceId = UUID.randomUUID().toString();
        log.error("Error unexpected internal server error, traceId: {}, code: {}, message: {}",
                traceId, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return ErrorResponse.build(
                traceId,
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Service Error"
        );
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResponse handleAccessDeniedException(AccessDeniedException ex) {
        String traceId = UUID.randomUUID().toString();
        log.error("Forbidden, traceId: {}, message: {}", traceId, ex.getMessage());
        return ErrorResponse.build(
                traceId,
                HttpStatus.FORBIDDEN,
                ex.getMessage()
        );
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ErrorResponse handleUnauthorizedException() {
        String traceId = UUID.randomUUID().toString();
        log.error("Provider authorization error, traceId: {}", traceId);
        return ErrorResponse.build(traceId,
                HttpStatus.UNAUTHORIZED,
                "Service provider authorization has failed");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ErrorResponse handleMaxSizeException(MaxUploadSizeExceededException ex) {
        String traceId = UUID.randomUUID().toString();
        log.error("File upload error, traceId: {}, message: {}", traceId, ex.getMessage());
        return ErrorResponse.build(
                traceId,
                HttpStatus.BAD_REQUEST,
                "Maximum allowed file size exceeded"
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException ex) {
        String traceId = UUID.randomUUID().toString();
        log.error("Constraint violation error, traceId: {}, message: {}", traceId, ex.getMessage());
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();

        List<ValidationError> errors = constraintViolations.stream()
                .map(constraintViolation ->
                        new ValidationError(ErrorLevel.ERROR,
                                getField(constraintViolation),
                                messageSource.getMessage(constraintViolation.getMessage(),
                                        null,
                                        Locale.ENGLISH)))
                .collect(Collectors.toList());

        return ErrorResponse.build(
                traceId,
                HttpStatus.BAD_REQUEST,
                "Invalid argument values",
                errors
        );
    }

    private static String getField(ConstraintViolation<?> constraintViolation) {
        String propertyPath = constraintViolation.getPropertyPath().toString();
        int lastDotIndex = propertyPath.lastIndexOf(".");
        return propertyPath.substring(lastDotIndex + 1);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex,
                                                         HttpHeaders headers,
                                                         HttpStatus status,
                                                         WebRequest request) {
        return handleBindingException(ex, ex.getBindingResult(), headers);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        return handleBindingException(ex, ex.getBindingResult(), headers);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        String traceId = UUID.randomUUID().toString();
        log.error("Missing request param error, traceId: {}, message: {}",
                traceId,
                ex.getMessage());
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        var errorResponse = ErrorResponse.build(
                traceId,
                badRequest,
                ex.getMessage()
        );

        return ResponseEntity
                .status(badRequest)
                .headers(headers)
                .body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex,
                                                               HttpHeaders headers,
                                                               HttpStatus status,
                                                               WebRequest request) {
        String traceId = UUID.randomUUID().toString();
        log.error("Missing path variable error, traceId: {}, message: {}",
                traceId,
                ex.getMessage());
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        var errorResponse = ErrorResponse.build(
                traceId,
                badRequest,
                ex.getMessage()
        );

        return ResponseEntity
                .status(badRequest)
                .headers(headers)
                .body(errorResponse);
    }

    private ResponseEntity<Object> handleBindingException(Throwable ex,
                                                          BindingResult bindingResult,
                                                          HttpHeaders headers) {
        String traceId = UUID.randomUUID().toString();
        log.error("Method argument not valid, traceId: {}, message: {}", traceId, ex.getMessage());
        List<ValidationError> errors = new ArrayList<>();
        errors.addAll(bindingResult.getFieldErrors().stream()
                .map(fieldError -> new ValidationError(ErrorLevel.ERROR,
                        fieldError.getField(),
                        errorMessage(fieldError))).collect(Collectors.toList()));

        errors.addAll(bindingResult.getGlobalErrors().stream()
                .map(globalError -> new ValidationError(ErrorLevel.ERROR,
                        globalError.getObjectName(),
                        errorMessage(globalError)))
                .collect(Collectors.toList()));

        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        var errorResponse = ErrorResponse.build(
                traceId,
                badRequest,
                "Invalid Arguments",
                errors
        );

        return ResponseEntity
                .status(badRequest)
                .headers(headers)
                .body(errorResponse);
    }

    private String errorMessage(ObjectError objectError) {
        return messageSource.getMessage(
                Objects.requireNonNull(objectError.getDefaultMessage()),
                objectError.getArguments(),
                Locale.ENGLISH);
    }

}