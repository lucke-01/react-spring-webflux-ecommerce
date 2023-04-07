package com.ricardocreates.domain.entity.administrator;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Builder(toBuilder = true)
@Data
public class LoginResponse implements UserDetails {

    private static final String ROLE_PREFIX = "ROLE_";
    private String login;

    private String password;

    private List<String> roles;

    private String role;

    private String token;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public List<String> getRolesWithPrefix() {
        return this.roles.stream().map(role -> String.format("%s%s", ROLE_PREFIX, role)).toList();
    }

    @Override
    public String getUsername() {
        return login;
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
