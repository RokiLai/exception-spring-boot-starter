package com.roki.exception.autoconfigure;

import com.roki.exception.code.scope.ErrorCodeScopes;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorCodePropertiesBindingTests {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(GlobalExceptionAutoConfiguration.class))
            .withPropertyValues(
                    "roki.exception.error-code.project-code=10",
                    "roki.exception.error-code.default-biz-code=01",
                    "roki.exception.error-code.biz-codes.order=11",
                    "roki.exception.error-code.biz-codes.payment=12");

    @Test
    void shouldBindPropertiesAndInitializeScopes() {
        contextRunner.run(context -> {
            assertThat(context).hasNotFailed();

            ErrorCodeProperties properties = context.getBean(ErrorCodeProperties.class);
            assertThat(properties.getProjectCode()).isEqualTo("10");
            assertThat(properties.getDefaultBizCode()).isEqualTo("01");
            assertThat(properties.getBizCodes()).containsEntry("order", "11");
            assertThat(properties.getBizCodes()).containsEntry("payment", "12");

            assertThat(ErrorCodeScopes.named("order").getProjectCode()).isEqualTo("10");
            assertThat(ErrorCodeScopes.named("order").getBizCode()).isEqualTo("11");
            assertThat(ErrorCodeScopes.defaultScope().getBizCode()).isEqualTo("01");
        });
    }
}
