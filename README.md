# exception-spring-boot-starter

A reusable Spring Boot starter for unified API responses and global exception handling.

## Coordinates

```xml
<dependency>
    <groupId>com.roki</groupId>
    <artifactId>exception-spring-boot-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## Features

- `Result<T>` and `PageResult<T>`
- `ErrorCode` and `BusinessException`
- Global Spring MVC exception handling
- Spring Boot auto-configuration

## Usage

Define your own business error code:

```java
package com.example.order.common;

import com.roki.exception.ErrorCode;

public enum OrderErrorCode implements ErrorCode {

    ORDER_NOT_FOUND(51001, "订单不存在");

    private final int code;
    private final String message;

    OrderErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
```

Throw business exceptions in your service:

```java
throw new BusinessException(OrderErrorCode.ORDER_NOT_FOUND);
```

Return unified responses in your controller:

```java
return Result.success(data);
```

## Publish

This repository is configured for GitHub Packages.

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
