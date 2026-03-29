package com.roki.exception.autoconfigure;

import com.roki.exception.code.definition.DeclarativeErrorCode;
import com.roki.exception.code.annotation.ErrorCodeMeta;
import com.roki.exception.code.annotation.ErrorCodeScopeName;
import com.roki.exception.code.validation.ErrorCodeDefinitionValidator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

class DeclarativeErrorCodeValidationRunnerTests {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(GlobalExceptionAutoConfiguration.class))
            .withUserConfiguration(TestApplication.class)
            .withPropertyValues(
                    "roki.exception.error-code.project-code=10",
                    "roki.exception.error-code.default-biz-code=01",
                    "roki.exception.error-code.biz-codes.order=11");

    @Test
    void shouldFailContextStartupWhenDeclarativeErrorCodesAreDuplicated() {
        contextRunner.run(context -> {
            assertThat(context).hasFailed();
            assertThat(context.getStartupFailure())
                    .hasMessageContaining("Duplicate error code definition");
        });
    }

    @Configuration(proxyBeanMethods = false)
    @AutoConfigurationPackage
    static class TestApplication {
    }
}

@ErrorCodeScopeName("order")
enum FirstOrderErrorCode implements DeclarativeErrorCode {
    @ErrorCodeMeta(detailCode = "001", message = "订单不存在")
    ORDER_NOT_FOUND
}

@ErrorCodeScopeName("order")
enum SecondOrderErrorCode implements DeclarativeErrorCode {
    @ErrorCodeMeta(detailCode = "001", message = "订单已关闭")
    ORDER_CLOSED
}
