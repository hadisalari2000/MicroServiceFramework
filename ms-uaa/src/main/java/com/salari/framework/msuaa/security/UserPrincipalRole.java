package com.salari.framework.msuaa.security;
import com.salari.framework.msuaa.model.entity.Api;
import java.util.List;

public class UserPrincipalRole {

    private Integer id;
    private String title;
    private String description;
    private List<Api> accesses;

    UserPrincipalRole(Integer id, String title, String description, List<Api> accesses) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.accesses = accesses;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Api> getAccesses() {
        return accesses;
    }

    public void setAccesses(List<Api> accesses) {
        this.accesses = accesses;
    }
}