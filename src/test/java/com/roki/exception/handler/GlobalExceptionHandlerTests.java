package com.roki.exception.handler;

import com.roki.exception.exception.BusinessException;
import com.roki.exception.api.ErrorCode;
import com.roki.exception.factory.BusinessExceptions;
import com.roki.exception.factory.ErrorCodes;
import com.roki.exception.code.builtin.CommonBusinessErrorCode;
import com.roki.exception.code.definition.DeclarativeErrorCode;
import com.roki.exception.code.annotation.ErrorCodeMeta;
import com.roki.exception.code.scope.ErrorCodeScope;
import com.roki.exception.code.annotation.ErrorCodeScopeName;
import com.roki.exception.code.scope.ErrorCodeScopes;
import com.roki.exception.code.definition.SimpleErrorCode;
import com.roki.exception.code.builtin.SystemErrorCode;
import com.roki.exception.code.builtin.ValidationErrorCode;
import com.roki.exception.result.Result;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.validation.metadata.ConstraintDescriptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GlobalExceptionHandlerTests {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @BeforeEach
    void setUp() {
        ErrorCodeScopes.configure("10", "01", Map.of(
                "common", "01",
                "order", "11"));
    }

    @Test
    void shouldReturnBusinessCodeForBusinessException() {
        Result<Void> result = handler.handleBusinessException(
                new BusinessException(CommonBusinessErrorCode.BUSINESS_ERROR));

        assertThat(result.getCode()).isEqualTo(9001001);
        assertThat(result.getMessage()).isEqualTo(CommonBusinessErrorCode.BUSINESS_ERROR.getMessage());
    }

    @Test
    void shouldExposeFixedBuiltInCodesAcrossProjects() {
        assertThat(CommonBusinessErrorCode.BUSINESS_ERROR.getCode()).isEqualTo(9001001);
        assertThat(ValidationErrorCode.PARAM_VALIDATION_FAILED.getCode()).isEqualTo(9001101);
        assertThat(ValidationErrorCode.REQUEST_PARAM_MISSING.getCode()).isEqualTo(9001102);
        assertThat(ValidationErrorCode.REQUEST_PARAM_TYPE_MISMATCH.getCode()).isEqualTo(9001103);
        assertThat(ValidationErrorCode.REQUEST_BODY_INVALID.getCode()).isEqualTo(9001104);
        assertThat(SystemErrorCode.SYSTEM_ERROR.getCode()).isEqualTo(9001901);
    }

    @Test
    void shouldComposeStructuredErrorCode() {
        ErrorCode errorCode = SimpleErrorCode.of("10", "02", "001", "订单不存在");

        assertThat(errorCode.getProjectCode()).isEqualTo("10");
        assertThat(errorCode.getBizCode()).isEqualTo("02");
        assertThat(errorCode.getDetailCode()).isEqualTo("001");
        assertThat(errorCode.getCodeValue()).isEqualTo("1002001");
        assertThat(errorCode.getCode()).isEqualTo(1002001);
    }

    @Test
    void shouldComposeErrorCodeFromScope() {
        ErrorCodeScope scope = ErrorCodeScope.of("10", "02");
        ErrorCode errorCode = scope.error("003", "订单已取消");

        assertThat(errorCode.getCodeValue()).isEqualTo("1002003");
        assertThat(errorCode.getCode()).isEqualTo(1002003);
        assertThat(errorCode.getMessage()).isEqualTo("订单已取消");
    }

    @Test
    void shouldSupportScopedEnumWithLowBoilerplate() {
        ErrorCode errorCode = DeclarativeTestOrderErrorCode.ORDER_NOT_FOUND;

        assertThat(errorCode.getProjectCode()).isEqualTo("10");
        assertThat(errorCode.getBizCode()).isEqualTo("11");
        assertThat(errorCode.getDetailCode()).isEqualTo("001");
        assertThat(errorCode.getCodeValue()).isEqualTo("1011001");
        assertThat(errorCode.getMessage()).isEqualTo("订单不存在");
    }

    @Test
    void shouldResolveScopeFromAnnotationAutomatically() {
        assertThat(DeclarativeTestOrderErrorCode.ORDER_CLOSED.getScope().getProjectCode()).isEqualTo("10");
        assertThat(DeclarativeTestOrderErrorCode.ORDER_CLOSED.getScope().getBizCode()).isEqualTo("11");
    }

    @Test
    void shouldSupportNamedScopeFromConfiguration() {
        ErrorCode errorCode = ErrorCodeScopes.named("order").error("003", "订单状态非法");

        assertThat(errorCode.getCodeValue()).isEqualTo("1011003");
        assertThat(errorCode.getCode()).isEqualTo(1011003);
    }

    @Test
    void shouldCreateErrorCodeThroughFactory() {
        ErrorCode errorCode = ErrorCodes.of("order", "004", "订单不可重复提交");

        assertThat(errorCode.getCodeValue()).isEqualTo("1011004");
        assertThat(errorCode.getMessage()).isEqualTo("订单不可重复提交");
    }

    @Test
    void shouldCreateBusinessExceptionThroughFactory() {
        BusinessException exception = BusinessExceptions.of("order", "005", "订单已完成");

        assertThat(exception.getCodeValue()).isEqualTo("1011005");
        assertThat(exception.getCode()).isEqualTo(1011005);
        assertThat(exception.getMessage()).isEqualTo("订单已完成");
    }

    @Test
    void shouldRejectIllegalErrorCodeSegment() {
        ErrorCode invalidErrorCode = SimpleErrorCode.of("A1", "02", "001", "订单不存在");

        assertThatThrownBy(invalidErrorCode::getCodeValue)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("projectCode");
    }

    @Test
    void shouldFailWhenDeclarativeMetadataIsMissing() {
        assertThatThrownBy(InvalidDeclarativeOrderErrorCode.ORDER_NOT_FOUND::getMessage)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("@ErrorCodeMeta");
    }

    @Test
    void shouldExposeExtendedMetadataForFutureEvolution() throws NoSuchFieldException {
        Field field = ExtendedDeclarativeOrderErrorCode.class.getField("ORDER_NOT_FOUND");
        ErrorCodeMeta meta = field.getAnnotation(ErrorCodeMeta.class);

        assertThat(meta.i18nKey()).isEqualTo("order.not_found");
        assertThat(meta.httpStatus()).isEqualTo(404);
        assertThat(meta.retryable()).isFalse();
    }

    @Test
    void shouldAllowOverrideMessageWhileKeepingStructuredCode() {
        ErrorCode errorCode = SimpleErrorCode.of("10", "02", "002", "订单状态非法");

        BusinessException exception = new BusinessException(errorCode, "订单已关闭，不能再次支付");

        assertThat(exception.getCode()).isEqualTo(1002002);
        assertThat(exception.getCodeValue()).isEqualTo("1002002");
        assertThat(exception.getMessage()).isEqualTo("订单已关闭，不能再次支付");
    }

    @Test
    void shouldReturnFieldMessageForMethodArgumentNotValidException() throws NoSuchMethodException {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "target");
        bindingResult.addError(new FieldError("target", "name", "名称不能为空"));
        Method method = TestController.class.getDeclaredMethod("save", String.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);

        Result<Void> result = handler.handleValidException(
                new MethodArgumentNotValidException(methodParameter, bindingResult));

        assertThat(result.getCode()).isEqualTo(ValidationErrorCode.PARAM_VALIDATION_FAILED.getCode());
        assertThat(result.getMessage()).isEqualTo("名称不能为空");
    }

    @Test
    void shouldReturnFieldMessageForBindException() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "target");
        bindingResult.addError(new FieldError("target", "name", "名称不能为空"));

        Result<Void> result = handler.handleBindException(new BindException(bindingResult));

        assertThat(result.getCode()).isEqualTo(ValidationErrorCode.PARAM_VALIDATION_FAILED.getCode());
        assertThat(result.getMessage()).isEqualTo("名称不能为空");
    }

    @Test
    void shouldReturnViolationMessageForConstraintViolationException() {
        ConstraintViolation<Object> violation = new SimpleConstraintViolation("租客ID不能为空");
        Set<ConstraintViolation<?>> violations = Collections.singleton(violation);

        Result<Void> result = handler.handleConstraintViolationException(
                new ConstraintViolationException("invalid", violations));

        assertThat(result.getCode()).isEqualTo(ValidationErrorCode.PARAM_VALIDATION_FAILED.getCode());
        assertThat(result.getMessage()).isEqualTo("租客ID不能为空");
    }

    @Test
    void shouldReturnMissingParamCodeForMissingServletRequestParameterException() {
        Result<Void> result = handler.handleMissingServletRequestParameterException(
                new MissingServletRequestParameterException("id", "Long"));

        assertThat(result.getCode()).isEqualTo(ValidationErrorCode.REQUEST_PARAM_MISSING.getCode());
        assertThat(result.getMessage()).isEqualTo("id 不能为空");
    }

    @Test
    void shouldReturnTypeMismatchCodeForMethodArgumentTypeMismatchException() throws NoSuchMethodException {
        Method method = TestController.class.getDeclaredMethod("save", String.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);
        MethodArgumentTypeMismatchException exception = new MethodArgumentTypeMismatchException(
                "abc", Long.class, "id", methodParameter, new IllegalArgumentException("type mismatch"));

        Result<Void> result = handler.handleMethodArgumentTypeMismatchException(exception);

        assertThat(result.getCode()).isEqualTo(ValidationErrorCode.REQUEST_PARAM_TYPE_MISMATCH.getCode());
        assertThat(result.getMessage()).isEqualTo("id 参数格式不正确");
    }

    @Test
    void shouldReturnBodyInvalidCodeForHttpMessageNotReadableException() {
        Result<Void> result = handler.handleHttpMessageNotReadableException(
                new HttpMessageNotReadableException("invalid body", (Throwable) null));

        assertThat(result.getCode()).isEqualTo(ValidationErrorCode.REQUEST_BODY_INVALID.getCode());
        assertThat(result.getMessage()).isEqualTo(ValidationErrorCode.REQUEST_BODY_INVALID.getMessage());
    }

    @Test
    void shouldReturnSystemErrorForUnexpectedException() {
        Result<Void> result = handler.handleException(new RuntimeException("boom"));

        assertThat(result.getCode()).isEqualTo(SystemErrorCode.SYSTEM_ERROR.getCode());
        assertThat(result.getMessage()).isEqualTo(SystemErrorCode.SYSTEM_ERROR.getMessage());
    }

    static class TestController {
        void save(String value) {
        }
    }

    @ErrorCodeScopeName("order")
    private enum DeclarativeTestOrderErrorCode implements DeclarativeErrorCode {

        @ErrorCodeMeta(detailCode = "001", message = "订单不存在")
        ORDER_NOT_FOUND,

        @ErrorCodeMeta(detailCode = "002", message = "订单已关闭")
        ORDER_CLOSED
    }

    @ErrorCodeScopeName("order")
    private enum InvalidDeclarativeOrderErrorCode implements DeclarativeErrorCode {
        ORDER_NOT_FOUND
    }

    @ErrorCodeScopeName("order")
    private enum ExtendedDeclarativeOrderErrorCode implements DeclarativeErrorCode {

        @ErrorCodeMeta(
                detailCode = "003",
                message = "订单不存在",
                i18nKey = "order.not_found",
                httpStatus = 404,
                retryable = false)
        ORDER_NOT_FOUND
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
