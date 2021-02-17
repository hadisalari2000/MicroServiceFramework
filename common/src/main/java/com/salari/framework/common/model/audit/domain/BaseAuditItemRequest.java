package com.salari.framework.common.model.audit.domain;

import com.salari.framework.common.model.audit.enums.RequestStatus;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseAuditItemRequest {
    private String rrn;
    private String remoteIp;
    private String route;
    private String userId;
    private String method;
    private String status;
    private Long requestTime;
    private Long responseTime;
    private RequestStatus requestStatus;
    private AuditItemRequest auditItemRequest;

    // -> BaseAuditItemRequest constructor for Gateway request
    public BaseAuditItemRequest(String rrn, String remoteIp, String route, String userId, String method, Long requestTime, RequestStatus requestStatus) {
        this.rrn = rrn;
        this.remoteIp = remoteIp;
        this.route = route;
        this.userId = userId;
        this.method = method;
        this.requestTime = requestTime;
        this.requestStatus = requestStatus;
    }

    // -> BaseAuditItemRequest constructor for send to Kafka (Before, AfterReturning, AfterException)
    public BaseAuditItemRequest(String rrn, AuditItemRequest auditItemRequest) {
        this.rrn = rrn;
        this.auditItemRequest = auditItemRequest;
    }

    // -> BaseAuditItemRequest constructor for Gateway response(AfterReturning / AfterException)
    public BaseAuditItemRequest(String rrn, Long responseTime, RequestStatus requestStatus) {
        this.rrn = rrn;
        this.responseTime = responseTime;
        this.requestStatus = requestStatus;
    }
}
