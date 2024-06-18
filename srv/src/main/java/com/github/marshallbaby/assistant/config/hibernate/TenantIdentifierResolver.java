package com.github.marshallbaby.assistant.config.hibernate;

import com.sap.cloud.security.xsuaa.token.AuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver<String> {

    private static final String SAP_PROVISIONING_TENANT_ID = "sap-provisioning";

    @Value("${multitenant.defaultTenant}")
    private String defaultTenant;


    @Override
    public String resolveCurrentTenantIdentifier() {

        AuthenticationToken authToken = (AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        if (Objects.isNull(authToken)) {
            return defaultTenant;
        }

        var attributes = authToken.getTokenAttributes();

        String tenantId = (String) attributes.get("zid");
        String resolvedTenantId = resolveTenantId(tenantId);

        log.info("Resolved tenant id: [{}].", resolvedTenantId);

        return resolvedTenantId;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }


    private String resolveTenantId(String tenantId) {

        return Objects.nonNull(tenantId) && !SAP_PROVISIONING_TENANT_ID.equals(tenantId)
                ? tenantId : defaultTenant;
    }

}
