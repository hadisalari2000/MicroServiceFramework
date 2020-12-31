package com.salari.framework.uaa.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.salari.framework.uaa.model.entity.Role;
import com.salari.framework.uaa.model.entity.User;
import com.salari.framework.uaa.repository.RoleRepository;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Data
@Builder
public class UserPrincipal implements UserDetails {


    private static RoleRepository roleRepository;
    private Integer id;
    private String username;
    @JsonIgnore
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private Collection<? extends UserPrincipalRole> roles;

    public UserPrincipal(RoleRepository roleRepository,Integer id,String username,String password,Collection<? extends GrantedAuthority> authorities, Collection<? extends UserPrincipalRole> roles) {
        this.roleRepository=roleRepository;
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.roles = roles;
    }

    public static UserPrincipal create(User user) {

        Optional<List<Role>> userRoles = roleRepository.findAllByUsers_Id(user.getId());
        List<UserPrincipalRole> userPrincipalRoles=new ArrayList<>();
        List<GrantedAuthority> authorityList=new ArrayList<>();

        if(userRoles.isPresent()) {
            authorityList = userRoles.get().stream().map(role -> new SimpleGrantedAuthority(role.getTitle())).collect(Collectors.toList());

            userPrincipalRoles = userRoles.get().stream()
                    .map(role -> new UserPrincipalRole(role.getId(), role.getTitle(), role.getDescription(), null))
                    .collect(Collectors.toList());
        }
        return UserPrincipal.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorityList)
                .roles(userPrincipalRoles)
                .build();
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