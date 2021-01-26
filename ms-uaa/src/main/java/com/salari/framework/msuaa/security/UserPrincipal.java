package com.salari.framework.msuaa.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.salari.framework.msuaa.model.entity.Role;
import com.salari.framework.msuaa.model.entity.User;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {

    private Integer id;
    private String username;
    @JsonIgnore
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private Collection<? extends UserPrincipalRole> roles;

    private UserPrincipal(Integer id, String username, String password, Collection<? extends GrantedAuthority> authorities, Collection<? extends UserPrincipalRole> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.roles = roles;
    }

    @Synchronized
    static UserPrincipal create(User user) {

        List<Role> userRoles = user.getRoles();
        List<UserPrincipalRole> userPrincipalRoles=new ArrayList<>();
        List<GrantedAuthority> authorityList=new ArrayList<>();

        if(!userRoles.isEmpty()) {
            authorityList = userRoles.stream().map(role -> new SimpleGrantedAuthority(role.getTitle())).collect(Collectors.toList());

            userPrincipalRoles = userRoles.stream()
                    .map(role -> new UserPrincipalRole(role.getId(), role.getTitle(), role.getDescription(), null))
                    .collect(Collectors.toList());
        }
        return new UserPrincipal(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                authorityList,
                userPrincipalRoles
        );
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public Collection<? extends UserPrincipalRole> getRoles() {
        return roles;
    }

    public void setRoles(Collection<? extends UserPrincipalRole> roles) {
        this.roles = roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPrincipal that = (UserPrincipal) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

}