package com.roki.exception.handler;

import com.roki.exception.exception.BusinessException;
import com.roki.exception.code.builtin.SystemErrorCode;
import com.roki.exception.code.builtin.ValidationErrorCode;
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

/**
 * Default REST exception handler provided by the starter.
 * starter 提供的默认 REST 异常处理器。
 *
 * Business projects can reuse it directly or replace it with their own advice if needed.
 * 业务项目可以直接复用，也可以按需替换成自己的异常处理器。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        return Result.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidException(MethodArgumentNotValidException e) {
        return Result.fail(ValidationErrorCode.PARAM_VALIDATION_FAILED.getCode(),
                extractFirstFieldErrorMessage(
                        e.getBindingResult().getFieldError(),
                        ValidationErrorCode.PARAM_VALIDATION_FAILED.getMessage()));
    }

    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException e) {
        return Result.fail(ValidationErrorCode.PARAM_VALIDATION_FAILED.getCode(),
                extractFirstFieldErrorMessage(
                        e.getBindingResult().getFieldError(),
                        ValidationErrorCode.PARAM_VALIDATION_FAILED.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> handleConstraintViolationException(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .findFirst()
                .map(violation -> violation.getMessage())
                .orElse(ValidationErrorCode.PARAM_VALIDATION_FAILED.getMessage());
        return Result.fail(ValidationErrorCode.PARAM_VALIDATION_FAILED.getCode(), message);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<Void> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException e) {
        return Result.fail(ValidationErrorCode.REQUEST_PARAM_MISSING.getCode(),
                e.getParameterName() + " 不能为空");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<Void> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e) {
        return Result.fail(ValidationErrorCode.REQUEST_PARAM_TYPE_MISMATCH.getCode(),
                e.getName() + " 参数格式不正确");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return Result.fail(ValidationErrorCode.REQUEST_BODY_INVALID.getCode(),
                ValidationErrorCode.REQUEST_BODY_INVALID.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        return Result.fail(SystemErrorCode.SYSTEM_ERROR.getCode(),
                SystemErrorCode.SYSTEM_ERROR.getMessage());
    }

    private String extractFirstFieldErrorMessage(FieldError fieldError, String defaultMessage) {
        if (fieldError == null || fieldError.getDefaultMessage() == null
                || fieldError.getDefaultMessage().isBlank()) {
            return defaultMessage;
        }
        return fieldError.getDefaultMessage();
    }
}
