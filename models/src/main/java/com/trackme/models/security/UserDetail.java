package com.trackme.models.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDetail implements UserDetails {

    private Collection<? extends GrantedAuthority> authorities;
    private String password;
    private String username;

    public UserDetail(UserEntity user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authorities = translate(user.getRoles());
    }

    private Collection<? extends GrantedAuthority> translate(List<RoleEntity> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (RoleEntity role : roles) {
            String name = role.getRoleName().toUpperCase();
            if (!name.startsWith("ROLE_")) {
                name = "ROLE_" + name;
            }
            authorities.add(new SimpleGrantedAuthority(name));

            for (AuthorityEntity authority : role.getAuthorities()) {
                authorities.add(new SimpleGrantedAuthority(authority.getPermission()));
            }
        }
        return authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
