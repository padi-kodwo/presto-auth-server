package com.presto.auth.controller.advise;

import com.presto.auth.domain.response.ApiResponse;
import com.presto.auth.domain.response.BaseError;
import com.presto.auth.enums.ResponseMessage;
import com.presto.auth.exception.ServiceException;
import feign.FeignException;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.oauth2.client.resource.OAuth2AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.NonUniqueResultException;
import javax.persistence.RollbackException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionAdvice extends ResponseEntityExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionAdvice.class);


    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest webRequest) {
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ApiResponse<Object> errorResponse = getErrorResponse(ex, webRequest, 10);

        return handleExceptionInternal(ex, errorResponse, headers, status, webRequest);
    }

    @ExceptionHandler(value = JpaSystemException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final ResponseEntity<Object> handleJpaSystemException(JpaSystemException ex, WebRequest webRequest) {
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ApiResponse<Object> errorResponse = getErrorResponse(ex, webRequest, 11);

        return handleExceptionInternal(ex, errorResponse, headers, status, webRequest);
    }

    @ExceptionHandler(value = OAuth2AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public final ResponseEntity<Object> handleGlobalException(OAuth2AccessDeniedException ex, WebRequest webRequest) {
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        ApiResponse<Object> errorResponse = getErrorResponse(ex, webRequest, 13);

        return handleExceptionInternal(ex, errorResponse, headers, status, webRequest);
    }

    @ExceptionHandler(value = ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final ResponseEntity<Object> handleServiceException(ServiceException ex, WebRequest webRequest) {
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ApiResponse<Object> errorResponse = getErrorResponse(ex, webRequest, ex.getCode());
        return handleExceptionInternal(ex, errorResponse, headers, status, webRequest);
    }

    @ExceptionHandler(value = FeignException.class)
    @ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
    public final ResponseEntity<Object> handleFeignException(FeignException ex, WebRequest webRequest) {
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.FAILED_DEPENDENCY;

        ApiResponse<Object> errorResponse = getErrorResponse(ex, webRequest, 14);

        return handleExceptionInternal(ex, errorResponse, headers, status, webRequest);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public final ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest webRequest) {
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.CONFLICT;

        ApiResponse<Object> errorResponse = getErrorResponse(ex, webRequest, 15);

        return handleExceptionInternal(ex, errorResponse, headers, status, webRequest);
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public final ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest webRequest) {
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.CONFLICT;

        ApiResponse<Object> errorResponse = getErrorResponse(ex, webRequest, 16);

        return handleExceptionInternal(ex, errorResponse, headers, status, webRequest);
    }

    @ExceptionHandler(value = IncorrectResultSizeDataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final ResponseEntity<Object> handleIncorrectResultSizeDataAccessException(IncorrectResultSizeDataAccessException ex, WebRequest webRequest) {
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ApiResponse<Object> errorResponse = getErrorResponse(ex, webRequest, 17);

        return handleExceptionInternal(ex, errorResponse, headers, status, webRequest);
    }

    @ExceptionHandler(value = EmptyResultDataAccessException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, WebRequest webRequest) {
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.NOT_FOUND;

        ApiResponse<Object> errorResponse = getErrorResponse(ex, webRequest, 18);

        return handleExceptionInternal(ex, errorResponse, headers, status, webRequest);
    }

    @ExceptionHandler(value = NonUniqueResultException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final ResponseEntity<Object> handleNonUniqueResultException(NonUniqueResultException ex, WebRequest webRequest) {
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ApiResponse<Object> errorResponse = getErrorResponse(ex, webRequest, 19);

        return handleExceptionInternal(ex, errorResponse, headers, status, webRequest);
    }

    @ExceptionHandler(value = RollbackException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public final ResponseEntity<Object> handleRollbackException(RollbackException ex, WebRequest webRequest) {
        HttpHeaders headers = new HttpHeaders();
        if (ex.getCause() instanceof ConstraintViolationException) {
            HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
            return handleConstraintException((ConstraintViolationException) ex.getCause(), headers, status, webRequest);
        } else {
            if (logger.isWarnEnabled()) {
                logger.warn("Unknown exception type: " + ex.getClass().getName());
            }
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

            ApiResponse<Object> errorResponse = getErrorResponse(ex, webRequest, 20);

            return handleExceptionInternal(ex, errorResponse, headers, status, webRequest);
        }
    }

    @SuppressWarnings("NullableProblems")
    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest webRequest) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ServiceException serviceException = new ServiceException(21, "invalid argument for " + errors);
        ApiResponse<Object> errorResponse = getErrorResponse(serviceException, webRequest, serviceException.getCode());

        return handleExceptionInternal(ex, errorResponse, headers, status, webRequest);
    }

    private ResponseEntity<Object> handleConstraintException(ConstraintViolationException ex, HttpHeaders headers, HttpStatus
            status, WebRequest webRequest) {

        ApiResponse<Object> errorResponse = getErrorResponse(ex, webRequest, 22);

        return handleExceptionInternal(ex, errorResponse, headers, status, webRequest);
    }

    public ApiResponse<Object> handleSQLError(Exception exception, WebRequest webRequest) {

        ApiResponse<Object> response = new ApiResponse<>();
        if (exception.getCause() instanceof ConstraintViolationException) {
            System.out.println("1: " + ((ConstraintViolationException) exception.getCause()).getSQLException().getMessage());//pass through
            String errorDetail = ((ConstraintViolationException) exception.getCause()).getSQLException().getMessage();

            if (errorDetail.contains("Detail:") && errorDetail.contains("already exists")) {
                try {
                    response = getErrorResponse(((ConstraintViolationException) exception.getCause()).getSQLException(),
                            webRequest, 15);
                } catch (Exception ex) {
                    logger.warn("Exception cleanup error: ", ex);
                    response = getErrorResponse(ex, webRequest, 15);
                }
            } else {
                response = getErrorResponse(exception, webRequest, 15);
            }
        }

        return response;
    }

    private String getUrl(WebRequest webRequest) {
        if (webRequest instanceof ServletWebRequest) {
            ServletWebRequest servletRequest = (ServletWebRequest) webRequest;
            HttpServletRequest request = servletRequest.getNativeRequest(HttpServletRequest.class);
            return request != null ? request.getRequestURI() : "n/a";
        }
        return null;
    }

    private ApiResponse<Object> getErrorResponse(Exception exception, WebRequest request, int errorCode) {
        BaseError baseError = new BaseError();
        baseError.setUrl(getUrl(request));
        baseError.setErrorCode(errorCode);
        baseError.setErrorMessage(exception.getMessage());

        ApiResponse<Object> errorResponse = new ApiResponse<>();
        errorResponse.setCode(ResponseMessage.FAILED.getCode());
        errorResponse.setMessage(ResponseMessage.FAILED.getMessage());
        errorResponse.setError(baseError);
        errorResponse.setRequestId(request.getSessionId());

        logger.error("["+ errorResponse.getRequestId() +"] HTTP ERROR: ", exception.getMessage(), exception);
        return errorResponse;
    }
}
