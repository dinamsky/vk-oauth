package com.example.security;

import com.example.model.AppUser;
import com.example.model.Privilege;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyUserPrincipal implements UserDetails {
    private AppUser appUser;

    public MyUserPrincipal(AppUser appUser) {
        this.appUser = appUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (final Privilege privilege : appUser.getPrivileges()) {
            authorities.add(new SimpleGrantedAuthority(privilege.getName()));
        }
        return authorities;
    }
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        final List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("User"));
//        return authorities;
//    }


    @Override
    public String getPassword() {
        return appUser.getPassword();
    }

    @Override
    public String getUsername() {
        return appUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
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
    public AppUser getUser() {
        return appUser;
    }

}
