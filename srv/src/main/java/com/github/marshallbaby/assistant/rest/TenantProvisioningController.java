package com.github.marshallbaby.assistant.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.marshallbaby.assistant.service.subscribtion.TenantProvisioningService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/callback/v1.0/tenants/{tenantId}")
public class TenantProvisioningController {

    private final TenantProvisioningService tenantProvisioningService;

    @PutMapping
    public String onSubscription(@PathVariable String tenantId, @RequestBody JsonNode requestBody) {

        log.info("Handling subscription callback for tenant: [{}].", tenantId);
        return tenantProvisioningService.onSubscription(requestBody, tenantId);
    }

}
