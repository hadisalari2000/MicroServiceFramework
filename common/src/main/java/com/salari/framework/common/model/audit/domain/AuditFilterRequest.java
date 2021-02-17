package com.salari.framework.common.model.audit.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditFilterRequest {
    private Long startDate;
    private Long endDate;
    private Long userId;
    private String method;
    private String remoteIP;
    private String anonymousUserIP;
    private String microServiceName;
    private String route;
}
