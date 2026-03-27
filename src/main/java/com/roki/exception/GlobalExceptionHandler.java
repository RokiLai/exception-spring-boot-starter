package com.roki.exception;

import com.roki.exception.result.Result;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        return Result.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidException(MethodArgumentNotValidException e) {
        return Result.fail(CommonErrorCode.PARAM_VALIDATION_FAILED.getCode(),
                extractFirstFieldErrorMessage(
                        e.getBindingResult().getFieldError(),
                        CommonErrorCode.PARAM_VALIDATION_FAILED.getMessage()));
    }

    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException e) {
        return Result.fail(CommonErrorCode.PARAM_VALIDATION_FAILED.getCode(),
                extractFirstFieldErrorMessage(
                        e.getBindingResult().getFieldError(),
                        CommonErrorCode.PARAM_VALIDATION_FAILED.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> handleConstraintViolationException(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .findFirst()
                .map(violation -> violation.getMessage())
                .orElse(CommonErrorCode.PARAM_VALIDATION_FAILED.getMessage());
        return Result.fail(CommonErrorCode.PARAM_VALIDATION_FAILED.getCode(), message);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<Void> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException e) {
        return Result.fail(CommonErrorCode.REQUEST_PARAM_MISSING.getCode(),
                e.getParameterName() + " 不能为空");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<Void> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e) {
        return Result.fail(CommonErrorCode.REQUEST_PARAM_TYPE_MISMATCH.getCode(),
                e.getName() + " 参数格式不正确");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return Result.fail(CommonErrorCode.REQUEST_BODY_INVALID.getCode(),
                CommonErrorCode.REQUEST_BODY_INVALID.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        return Result.fail(CommonErrorCode.SYSTEM_ERROR.getCode(),
                CommonErrorCode.SYSTEM_ERROR.getMessage());
    }

    private String extractFirstFieldErrorMessage(FieldError fieldError, String defaultMessage) {
        if (fieldError == null || fieldError.getDefaultMessage() == null
                || fieldError.getDefaultMessage().isBlank()) {
            return defaultMessage;
        }
        return fieldError.getDefaultMessage();
    }
}
