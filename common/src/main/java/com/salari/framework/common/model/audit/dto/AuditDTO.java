package com.salari.framework.common.model.audit.dto;

import com.salari.framework.common.model.audit.domain.AuditItemRequest;

import java.util.List;

public class AuditDTO {
    private String rrn;
    private String remoteIP;
    private Long userId;
    private List<AuditItemRequest> objects;
    private String route;
    private String method;
    private Long duration;

    public AuditDTO() {
    }


    public AuditDTO(String rrn, String remoteIP, Long userId, String route, String method, Long duration) {
        this.rrn = rrn;
        this.remoteIP = remoteIP;
        this.userId = userId;
        this.route = route;
        this.method = method;
        this.duration = duration;
    }
}
