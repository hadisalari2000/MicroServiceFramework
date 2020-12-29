package com.salari.framework.uaa.model.entity.base;
import lombok.experimental.SuperBuilder;
import javax.persistence.*;

@SuperBuilder
@MappedSuperclass
public abstract class SimpleBaseEntity<T extends Number> {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected T id;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    public SimpleBaseEntity() {
    }
}
