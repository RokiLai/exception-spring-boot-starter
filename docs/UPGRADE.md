# Upgrade Guide

This document explains how to describe version changes clearly when publishing to GitHub Packages and how downstream projects should upgrade.

## Recommended Release Structure

When publishing a new version, keep the release notes in this order:

1. Overview
2. Highlights
3. Breaking Changes
4. Compatibility
5. Migration Guide
6. Validation

## Release Notes Template

Use the following template in GitHub Releases:

```md
## Overview
This release refactors the starter around structured error codes, package layout, startup validation, and richer pagination support.

本次版本围绕结构化错误码、分包结构、启动校验和更完整的分页模型进行了重构。

## Highlights
- Added structured error codes based on `projectCode + bizCode + detailCode`
- Added configuration-driven business scope management
- Added declarative enum-based error code definitions
- Added startup duplicate validation for declarative error codes
- Reorganized packages by responsibility
- Enhanced `PageResult<T>` with richer pagination metadata

## Breaking Changes
- `ErrorCode` usage model changed from flat integer-oriented semantics to structured code semantics
- Existing custom error code enums/classes may require refactoring
- Package names of many classes changed
- Built-in common error codes were reorganized into dedicated built-in enums

## Compatibility
### Compatible
- `Result<T>` basic success/fail usage
- Global exception handling behavior

### Partially Compatible
- `PageResult.of(total, records)` still works, but `PageResult<T>` now includes additional pagination fields

### Breaking
- Old custom `ErrorCode` implementations based on flat integer semantics
- Old imports under the previous package layout

## Migration Guide
1. Configure `project-code` and `biz-codes`
2. Replace old flat error code definitions with:
   - declarative enums, or
   - `ErrorCodes.of(...)` constant definitions
3. Update imports to the new package structure
4. If using pagination, review the enriched `PageResult<T>` model

## Validation
- `mvn test` passed
- 24 tests passed
```

## Migration Checklist

For downstream projects upgrading to `2.0.1`:

1. Add `roki.exception.error-code.project-code`
2. Add `roki.exception.error-code.biz-codes`
3. Refactor old error code definitions into:
   - declarative enums using `@ErrorCodeScopeName` + `@ErrorCodeMeta`, or
   - constant definitions using `ErrorCodes.of(...)`
4. Update imports to the new package layout
5. Review any pagination response serialization if the client is sensitive to extra fields

## Versioning Advice

Because this release changes the error code contract and package paths, it should be published as a major version.

Recommended version:

- `2.0.1`

Use a minor version only if you are certain there are no real downstream users depending on the previous layout and semantics.
