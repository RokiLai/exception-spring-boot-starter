# Changelog

All notable changes to this project will be documented in this file.

## [2.0.0] - 2026-03-29

### Added

- Added a structured error code model based on `projectCode + bizCode + detailCode`
- Added configuration-driven project code and business code registration
- Added declarative enum-based error code definitions with `@ErrorCodeScopeName` and `@ErrorCodeMeta`
- Added lightweight factories `ErrorCodes` and `BusinessExceptions`
- Added startup scanning and duplicate validation for declarative error codes
- Added richer pagination metadata to `PageResult<T>`
- Added bilingual Chinese/English documentation comments for core classes
- Added Chinese README while keeping English README as the default landing page

### Changed

- Reorganized packages by responsibility to make public API and internals easier to understand
- Converted built-in starter error codes to the same declarative definition model used by business projects
- Updated the recommended integration path to configuration-driven scopes plus declarative definitions
- Updated documentation for package layout, validation rules, and pagination usage

### Breaking

- `ErrorCode` is now centered around structured code segments rather than old flat integer-style business definitions
- Existing custom error code enums/classes may require migration
- Many class package paths changed due to the new package layout
- Built-in common error code types were split into dedicated built-in enums
- `PageResult<T>` is no longer just `total + records`; it now carries richer pagination semantics

### Compatibility

Compatible:

- `Result<T>` success/fail basic usage remains available
- Global exception handling behavior remains available

Partially compatible:

- `PageResult.of(total, records)` remains available, but the model now contains additional pagination fields

Not compatible without migration:

- Old custom `ErrorCode` implementations based on flat integer semantics
- Old imports under the previous package layout

### Validation

- Verified by `mvn test`
- 24 tests passed
