# exception-spring-boot-starter

[English](README.md) | [简体中文](README.zh-CN.md)

一个可复用的 Spring Boot Starter，用于统一 API 返回结构和全局异常处理。

## Maven 坐标

```xml
<dependency>
    <groupId>com.roki</groupId>
    <artifactId>exception-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 功能特性

- `Result<T>` 和更完整的 `PageResult<T>` 分页元数据
- 结构化 `ErrorCode` 和 `BusinessException`
- Spring MVC 全局异常处理
- Spring Boot 自动配置

## 使用方式

先按 `项目码 + 业务码 + 具体错误码` 定义业务错误码。
为了降低使用成本，建议把 `projectCode` 和 `bizCode` 放到配置里，代码中只保留稳定的业务域名称。

推荐约定：

- `projectCode`：2 位数字，用于标识项目或业务边界
- `bizCode`：2 位数字，用于标识业务域或模块
- `detailCode`：3 位数字，用于标识该业务域下的具体错误

配置示例：

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

默认推荐写法是声明式枚举。如果后续要增加 `i18nKey`、`httpStatus`、`retryable` 这类元数据，它会更容易扩展。

```java
package com.example.order.error;

import com.roki.exception.code.definition.DeclarativeErrorCode;
import com.roki.exception.code.annotation.ErrorCodeMeta;
import com.roki.exception.code.annotation.ErrorCodeScopeName;

@ErrorCodeScopeName("order")
public enum OrderErrorCode implements DeclarativeErrorCode {

    @ErrorCodeMeta(
            detailCode = "001",
            message = "订单不存在",
            i18nKey = "order.not_found",
            httpStatus = 404)
    ORDER_NOT_FOUND,

    @ErrorCodeMeta(detailCode = "002", message = "订单已关闭")
    ORDER_CLOSED
}
```

如果配置中 `project-code=10` 且 `biz-codes.order=11`，
`OrderErrorCode.ORDER_NOT_FOUND.getCodeValue()` 会返回 `1011001`。
`OrderErrorCode.ORDER_NOT_FOUND.getCode()` 会返回 `1011001`。

服务层直接使用：

```java
throw new BusinessException(OrderErrorCode.ORDER_NOT_FOUND);
```

如果你更看重书写成本，也支持常量类写法：

```java
import com.roki.exception.api.ErrorCode;
import com.roki.exception.factory.ErrorCodes;

public final class OrderErrors {

    private OrderErrors() {
    }

    public static final ErrorCode ORDER_NOT_FOUND = ErrorCodes.of("order", "001", "订单不存在");
    public static final ErrorCode ORDER_CLOSED = ErrorCodes.of("order", "002", "订单已关闭");
}
```

如果你想再少写一点，也可以直接用异常工厂：

```java
throw BusinessExceptions.of("order", "003", "订单状态非法");
```

控制器中返回统一响应：

```java
return Result.success(data);
```

## 接入规范

推荐按下面的约束接入：

- 每个项目必须配置唯一的 `project-code`
- 每个业务域必须先在 `biz-codes` 中注册，再在代码里通过 `scopeName` 引用
- `detailCode` 固定为 3 位数字，并且应在同一个业务域内保持唯一
- 默认推荐使用声明式枚举；如果更看重书写成本，可以使用常量类
- 不建议在业务代码里直接写裸整数错误码

Starter 当前会在启动阶段扫描声明式错误码定义，并校验重复错误码：

- 同一个 `projectCode + bizCode + detailCode` 不能重复
- 缺少 `@ErrorCodeMeta` 或 `@ErrorCodeScopeName` 会在访问或校验时失败
- `scopeName` 未在配置中注册时会直接失败

建议的落地方式：

- `project-code` 由项目配置维护
- `biz-codes` 由项目模块边界统一维护
- `detailCode` 由各业务域内部自行编号，例如从 `001` 开始递增
- 新增错误码时，优先补充到集中定义位置，而不是分散在服务代码里临时创建

## 发布

当前仓库已经配置为发布到 GitHub Packages。

发布文档：

- [CHANGELOG.md](CHANGELOG.md)
- [docs/UPGRADE.md](docs/UPGRADE.md)

仓库地址：

```text
https://maven.pkg.github.com/RokiLai/exception-spring-boot-starter
```

本地发布：

```bash
mvn deploy -DskipTests
```

`settings.xml` 示例：

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
