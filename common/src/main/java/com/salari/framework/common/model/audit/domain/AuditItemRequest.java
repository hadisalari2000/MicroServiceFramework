package com.salari.framework.common.model.audit.domain;

import com.salari.framework.common.model.audit.enums.AuditType;
import lombok.*;

import java.util.Map;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditItemRequest {
    private Long datetime;
    private AuditType type;
    private Map params;
    private Object result;
    private String microServiceName;
    private String path;

    // -> AuditItemRequest constructor before any restControllers in AuditingHandler(@Before)
    public AuditItemRequest(Long datetime, AuditType type, Map params, String microServiceName, String path) {
        this.datetime = datetime;
        this.type = type;
        this.params = params;
        this.microServiceName = microServiceName;
        this.path = path;
    }

    // -> AuditItemRequest constructor after any restControllers in AuditingHandler(@AfterReturning)
    public AuditItemRequest(Long datetime, AuditType type, Object result, String microServiceName, String path) {
        this.datetime = datetime;
        this.type = type;
        this.result = result;
        this.microServiceName = microServiceName;
        this.path = path;
    }
}
