package com.salari.framework.uaa.model.entity.base;
import lombok.Builder;
import lombok.experimental.SuperBuilder;
import javax.persistence.MappedSuperclass;

@SuperBuilder
@MappedSuperclass
public abstract class BaseEntity<T extends Number> extends SimpleBaseEntity<T> {

    @Builder.Default
    protected Boolean deleted=false;
    protected Long creationDate=System.currentTimeMillis();
    protected Long deletionDate;

    public BaseEntity() {
    }

    public BaseEntity(Boolean deleted, Long creationDate, Long deletionDate) {
        this.deleted = deleted;
        this.creationDate = creationDate;
        this.deletionDate = deletionDate;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Long creationDate) {
        this.creationDate = creationDate;
    }

    public Long getDeletionDate() {
        return deletionDate;
    }

    public void setDeletionDate(Long deletionDate) {
        this.deletionDate = deletionDate;
    }
}
