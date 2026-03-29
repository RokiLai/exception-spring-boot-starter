package com.roki.exception.result;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PageResultTests {

    @Test
    void shouldBuildFullPaginationPayload() {
        PageResult<String> result = PageResult.of(2L, 10L, 25L, List.of("A", "B"));

        assertThat(result.getPageNo()).isEqualTo(2L);
        assertThat(result.getPageSize()).isEqualTo(10L);
        assertThat(result.getTotal()).isEqualTo(25L);
        assertThat(result.getTotalPages()).isEqualTo(3L);
        assertThat(result.getHasNext()).isTrue();
        assertThat(result.getHasPrevious()).isTrue();
        assertThat(result.getRecords()).containsExactly("A", "B");
    }

    @Test
    void shouldKeepSimpleFactoryForCompatibility() {
        PageResult<String> result = PageResult.of(2L, List.of("A", "B"));

        assertThat(result.getPageNo()).isEqualTo(1L);
        assertThat(result.getPageSize()).isEqualTo(2L);
        assertThat(result.getTotal()).isEqualTo(2L);
        assertThat(result.getTotalPages()).isEqualTo(1L);
        assertThat(result.getHasNext()).isFalse();
        assertThat(result.getHasPrevious()).isFalse();
        assertThat(result.getRecords()).containsExactly("A", "B");
    }
}
