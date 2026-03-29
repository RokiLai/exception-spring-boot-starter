# exception-spring-boot-starter

[English](README.md) | [简体中文](README.zh-CN.md)

A reusable Spring Boot starter for unified API responses and global exception handling.

## Coordinates

```xml
<dependency>
    <groupId>com.roki</groupId>
    <artifactId>exception-spring-boot-starter</artifactId>
    <version>2.0.0</version>
</dependency>
```

## Features

- `Result<T>` and richer `PageResult<T>` pagination metadata
- Structured `ErrorCode` and `BusinessException`
- Global Spring MVC exception handling
- Spring Boot auto-configuration

## Usage

Define your own business error code with `projectCode + bizCode + detailCode`.
To reduce boilerplate, move `projectCode` and `bizCode` into configuration, and keep only a stable business scope name in code.

Recommended convention:

- `projectCode`: 2 digits, identifies the project or bounded context
- `bizCode`: 2 digits, identifies the business domain or module
- `detailCode`: 3 digits, identifies the concrete error within that domain

Configuration example:

Put the following configuration in the consuming application's `application.yml`:

```yaml
roki:
  exception:
    error-code:
      project-code: 10
      default-biz-code: 01
      biz-codes:
        order: 11
        payment: 12
        common: 01
        validation: 02
        system: 99
```

Recommended style is a declarative enum. It is easier to evolve later if you add metadata such as `i18nKey`, `httpStatus`, or `retryable`.

```java
package com.example.order.error;

import com.roki.exception.code.definition.DeclarativeErrorCode;
import com.roki.exception.code.annotation.ErrorCodeMeta;
import com.roki.exception.code.annotation.ErrorCodeScopeName;

@ErrorCodeScopeName("order")
public enum OrderErrorCode implements DeclarativeErrorCode {

    @ErrorCodeMeta(
            detailCode = "001",
            message = "Order does not exist",
            i18nKey = "order.not_found",
            httpStatus = 404)
    ORDER_NOT_FOUND,

    @ErrorCodeMeta(detailCode = "002", message = "Order has been closed")
    ORDER_CLOSED
}
```

If `project-code=10` and `biz-codes.order=11`,
`OrderErrorCode.ORDER_NOT_FOUND.getCodeValue()` returns `1011001`.
`OrderErrorCode.ORDER_NOT_FOUND.getCode()` returns `1011001`.

Use it in your service:

```java
throw new BusinessException(OrderErrorCode.ORDER_NOT_FOUND);
```

If you prefer lower writing cost, a constants class is also supported:

```java
import com.roki.exception.api.ErrorCode;
import com.roki.exception.factory.ErrorCodes;

public final class OrderErrors {

    private OrderErrors() {
    }

    public static final ErrorCode ORDER_NOT_FOUND = ErrorCodes.of("order", "001", "Order does not exist");
    public static final ErrorCode ORDER_CLOSED = ErrorCodes.of("order", "002", "Order has been closed");
}
```

If you want even less boilerplate, use the exception factory directly:

```java
throw BusinessExceptions.of("order", "003", "Order status is invalid");
```

Return unified responses in your controller:

```java
return Result.success(data);
```

## Conventions

Recommended constraints:

- Each project should configure its own unique `project-code`
- Each business domain should be registered in `biz-codes` before being referenced by `scopeName`
- `detailCode` must be 3 digits and should remain unique within the same business domain
- Declarative enums are the default recommendation; constants classes remain a lighter alternative
- Avoid hard-coded integer error codes in business code

The starter now validates declarative error code definitions during startup:

- The same `projectCode + bizCode + detailCode` cannot be defined twice
- Missing `@ErrorCodeMeta` or `@ErrorCodeScopeName` causes validation or access failures
- Referencing an unconfigured `scopeName` fails fast

Recommended ownership model:

- `project-code` is owned by the project configuration
- `biz-codes` are owned by module boundaries inside the project
- `detailCode` values are owned within each business domain, typically starting from `001`
- Add new error codes to centralized definitions instead of creating them ad hoc inside services

## Publish

This repository is configured for GitHub Packages.

Release documentation:

- [CHANGELOG.md](CHANGELOG.md)
- [docs/UPGRADE.md](docs/UPGRADE.md)

Repository URL:

```text
https://maven.pkg.github.com/RokiLai/exception-spring-boot-starter
```

Local publish:

```bash
mvn deploy -DskipTests
```

`settings.xml` example:

```xml
<settings>
  <servers>
    <server>
      <id>github</id>
      <username>YOUR_GITHUB_USERNAME</username>
      <password>YOUR_CLASSIC_PAT</password>
    </server>
  </servers>
</settings>
```
