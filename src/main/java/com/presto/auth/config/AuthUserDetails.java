package com.presto.auth.config;



import com.presto.auth.entity.Role;
import com.presto.auth.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class AuthUserDetails implements UserDetails {

    private static final long serialVersionUID = 1L;
    private Collection<? extends GrantedAuthority> authorities;
    private String password;
    private String phone;

    public AuthUserDetails(User user) {
        this.phone = user.getPhone();
        this.password = user.getPassword();
        this.authorities = translate(user.getRoles());
    }

    private Collection<? extends GrantedAuthority> translate(Set<Role> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {

            String name = role.getName().toUpperCase();
            if (!name.startsWith("ROLE_")) {
                name = "ROLE_" + name;
            }
            authorities.add(new SimpleGrantedAuthority(name));
        }
        return authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return phone;
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
}
