package com.roki.exception.code.validation;

import com.roki.exception.code.definition.DeclarativeErrorCode;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Validates declarative error code definitions before the application starts serving traffic.
 * 在应用对外提供服务前校验声明式错误码定义。
 */
public final class ErrorCodeDefinitionValidator {

    public void validate(Collection<Class<?>> declarativeErrorCodeClasses) {
        Map<String, String> seenDefinitions = new LinkedHashMap<>();
        for (Class<?> declarativeErrorCodeClass : declarativeErrorCodeClasses) {
            validateDeclarativeErrorCodeClass(declarativeErrorCodeClass, seenDefinitions);
        }
    }

    private void validateDeclarativeErrorCodeClass(Class<?> declarativeErrorCodeClass, Map<String, String> seenDefinitions) {
        if (!declarativeErrorCodeClass.isEnum()) {
            throw new IllegalStateException("DeclarativeErrorCode must be declared on enum types: "
                    + declarativeErrorCodeClass.getName());
        }
        if (!DeclarativeErrorCode.class.isAssignableFrom(declarativeErrorCodeClass)) {
            throw new IllegalStateException("Class does not implement DeclarativeErrorCode: "
                    + declarativeErrorCodeClass.getName());
        }
        Object[] enumConstants = declarativeErrorCodeClass.getEnumConstants();
        if (enumConstants == null) {
            return;
        }
        for (Object enumConstant : enumConstants) {
            DeclarativeErrorCode errorCode = (DeclarativeErrorCode) enumConstant;
            String codeValue = errorCode.getCodeValue();
            String definition = declarativeErrorCodeClass.getName() + "." + ((Enum<?>) enumConstant).name();
            String previousDefinition = seenDefinitions.putIfAbsent(codeValue, definition);
            if (previousDefinition != null) {
                throw new IllegalStateException("Duplicate error code definition " + codeValue
                        + " found in " + previousDefinition + " and " + definition);
            }
        }
    }
}
