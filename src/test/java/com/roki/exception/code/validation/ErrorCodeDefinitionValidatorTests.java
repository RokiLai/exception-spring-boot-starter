package com.roki.exception.code.validation;

import com.roki.exception.code.annotation.ErrorCodeMeta;
import com.roki.exception.code.annotation.ErrorCodeScopeName;
import com.roki.exception.code.definition.DeclarativeErrorCode;
import com.roki.exception.code.scope.ErrorCodeScopes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ErrorCodeDefinitionValidatorTests {

    private final ErrorCodeDefinitionValidator validator = new ErrorCodeDefinitionValidator();

    @BeforeEach
    void setUp() {
        ErrorCodeScopes.configure("10", "01", Map.of(
                "order", "11",
                "payment", "12",
                "common", "01",
                "validation", "02",
                "system", "99"));
    }

    @Test
    void shouldAcceptUniqueDeclarativeErrorCodes() {
        assertThatCode(() -> validator.validate(List.of(UniqueOrderErrorCode.class, UniquePaymentErrorCode.class)))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldRejectDuplicateDeclarativeErrorCodes() {
        assertThatThrownBy(() -> validator.validate(List.of(UniqueOrderErrorCode.class, DuplicateOrderErrorCode.class)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Duplicate error code definition")
                .hasMessageContaining("1011001");
    }

    @ErrorCodeScopeName("order")
    private enum UniqueOrderErrorCode implements DeclarativeErrorCode {
        @ErrorCodeMeta(detailCode = "001", message = "订单不存在")
        ORDER_NOT_FOUND
    }

    @ErrorCodeScopeName("payment")
    private enum UniquePaymentErrorCode implements DeclarativeErrorCode {
        @ErrorCodeMeta(detailCode = "001", message = "支付不存在")
        PAYMENT_NOT_FOUND
    }

    @ErrorCodeScopeName("order")
    private enum DuplicateOrderErrorCode implements DeclarativeErrorCode {
        @ErrorCodeMeta(detailCode = "001", message = "订单已关闭")
        ORDER_CLOSED
    }
}
