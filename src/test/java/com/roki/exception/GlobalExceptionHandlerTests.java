package com.roki.exception;

import com.roki.exception.result.Result;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.validation.metadata.ConstraintDescriptor;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTests {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldReturnBusinessCodeForBusinessException() {
        Result<Void> result = handler.handleBusinessException(
                new BusinessException(CommonErrorCode.BUSINESS_ERROR));

        assertThat(result.getCode()).isEqualTo(CommonErrorCode.BUSINESS_ERROR.getCode());
        assertThat(result.getMessage()).isEqualTo(CommonErrorCode.BUSINESS_ERROR.getMessage());
    }

    @Test
    void shouldReturnFieldMessageForMethodArgumentNotValidException() throws NoSuchMethodException {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "target");
        bindingResult.addError(new FieldError("target", "name", "名称不能为空"));
        Method method = TestController.class.getDeclaredMethod("save", String.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);

        Result<Void> result = handler.handleValidException(
                new MethodArgumentNotValidException(methodParameter, bindingResult));

        assertThat(result.getCode()).isEqualTo(CommonErrorCode.PARAM_VALIDATION_FAILED.getCode());
        assertThat(result.getMessage()).isEqualTo("名称不能为空");
    }

    @Test
    void shouldReturnFieldMessageForBindException() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "target");
        bindingResult.addError(new FieldError("target", "name", "名称不能为空"));

        Result<Void> result = handler.handleBindException(new BindException(bindingResult));

        assertThat(result.getCode()).isEqualTo(CommonErrorCode.PARAM_VALIDATION_FAILED.getCode());
        assertThat(result.getMessage()).isEqualTo("名称不能为空");
    }

    @Test
    void shouldReturnViolationMessageForConstraintViolationException() {
        ConstraintViolation<Object> violation = new SimpleConstraintViolation("租客ID不能为空");
        Set<ConstraintViolation<?>> violations = Collections.singleton(violation);

        Result<Void> result = handler.handleConstraintViolationException(
                new ConstraintViolationException("invalid", violations));

        assertThat(result.getCode()).isEqualTo(CommonErrorCode.PARAM_VALIDATION_FAILED.getCode());
        assertThat(result.getMessage()).isEqualTo("租客ID不能为空");
    }

    @Test
    void shouldReturnMissingParamCodeForMissingServletRequestParameterException() {
        Result<Void> result = handler.handleMissingServletRequestParameterException(
                new MissingServletRequestParameterException("id", "Long"));

        assertThat(result.getCode()).isEqualTo(CommonErrorCode.REQUEST_PARAM_MISSING.getCode());
        assertThat(result.getMessage()).isEqualTo("id 不能为空");
    }

    @Test
    void shouldReturnTypeMismatchCodeForMethodArgumentTypeMismatchException() throws NoSuchMethodException {
        Method method = TestController.class.getDeclaredMethod("save", String.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);
        MethodArgumentTypeMismatchException exception = new MethodArgumentTypeMismatchException(
                "abc", Long.class, "id", methodParameter, new IllegalArgumentException("type mismatch"));

        Result<Void> result = handler.handleMethodArgumentTypeMismatchException(exception);

        assertThat(result.getCode()).isEqualTo(CommonErrorCode.REQUEST_PARAM_TYPE_MISMATCH.getCode());
        assertThat(result.getMessage()).isEqualTo("id 参数格式不正确");
    }

    @Test
    void shouldReturnBodyInvalidCodeForHttpMessageNotReadableException() {
        Result<Void> result = handler.handleHttpMessageNotReadableException(
                new HttpMessageNotReadableException("invalid body", (Throwable) null));

        assertThat(result.getCode()).isEqualTo(CommonErrorCode.REQUEST_BODY_INVALID.getCode());
        assertThat(result.getMessage()).isEqualTo(CommonErrorCode.REQUEST_BODY_INVALID.getMessage());
    }

    @Test
    void shouldReturnSystemErrorForUnexpectedException() {
        Result<Void> result = handler.handleException(new RuntimeException("boom"));

        assertThat(result.getCode()).isEqualTo(CommonErrorCode.SYSTEM_ERROR.getCode());
        assertThat(result.getMessage()).isEqualTo(CommonErrorCode.SYSTEM_ERROR.getMessage());
    }

    static class TestController {
        void save(String value) {
        }
    }

    private static final class SimpleConstraintViolation implements ConstraintViolation<Object> {

        private final String message;

        private SimpleConstraintViolation(String message) {
            this.message = message;
        }

        @Override
        public String getMessage() {
            return message;
        }

        @Override
        public String getMessageTemplate() {
            return message;
        }

        @Override
        public Object getRootBean() {
            return null;
        }

        @Override
        public Class<Object> getRootBeanClass() {
            return Object.class;
        }

        @Override
        public Object getLeafBean() {
            return null;
        }

        @Override
        public Object[] getExecutableParameters() {
            return new Object[0];
        }

        @Override
        public Object getExecutableReturnValue() {
            return null;
        }

        @Override
        public Path getPropertyPath() {
            return null;
        }

        @Override
        public Object getInvalidValue() {
            return null;
        }

        @Override
        public ConstraintDescriptor<?> getConstraintDescriptor() {
            return null;
        }

        @Override
        public <U> U unwrap(Class<U> type) {
            throw new UnsupportedOperationException("unwrap is not supported in this test");
        }
    }
}
