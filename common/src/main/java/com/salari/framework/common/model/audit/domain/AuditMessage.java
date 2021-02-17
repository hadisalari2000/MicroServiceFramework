package com.salari.framework.common.model.audit.domain;

public class AuditMessage {

    private Long timeMillis;
    private String thread;
    private String level;
    private String loggerName;
    private String message;
    private Boolean endOfBatch;
    private String loggerFqcn;
    private Integer threadId;
    private Integer threadPriority;

    public AuditMessage() {
    }

    public AuditMessage(String thread, String level) {
        this.thread = thread;
        this.level = level;
    }

    public AuditMessage(Long timeMillis, String thread, String level, String loggerName, String message, Boolean endOfBatch, String loggerFqcn, Integer threadId, Integer threadPriority) {
        this.timeMillis = timeMillis;
        this.thread = thread;
        this.level = level;
        this.loggerName = loggerName;
        this.message = message;
        this.endOfBatch = endOfBatch;
        this.loggerFqcn = loggerFqcn;
        this.threadId = threadId;
        this.threadPriority = threadPriority;
    }

    public Long getTimeMillis() {
        return timeMillis;
    }

    public void setTimeMillis(Long timeMillis) {
        this.timeMillis = timeMillis;
    }

    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLoggerName() {
        return loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getEndOfBatch() {
        return endOfBatch;
    }

    public void setEndOfBatch(Boolean endOfBatch) {
        this.endOfBatch = endOfBatch;
    }

    public String getLoggerFqcn() {
        return loggerFqcn;
    }

    public void setLoggerFqcn(String loggerFqcn) {
        this.loggerFqcn = loggerFqcn;
    }

    public Integer getThreadId() {
        return threadId;
    }

    public void setThreadId(Integer threadId) {
        this.threadId = threadId;
    }

    public Integer getThreadPriority() {
        return threadPriority;
    }

    public void setThreadPriority(Integer threadPriority) {
        this.threadPriority = threadPriority;
    }
}
