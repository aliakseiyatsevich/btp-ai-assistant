package com.github.marshallbaby.assistant.util;

import lombok.experimental.UtilityClass;

import static java.lang.String.format;

@UtilityClass
public class TenantSchemaNameBuilder {

    public static String buildSchemaName(String tenantId) {
        return format("tenant_%s", tenantId.replace("-", "_")).toUpperCase();
    }

}
