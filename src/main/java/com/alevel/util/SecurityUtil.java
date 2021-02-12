package com.alevel.util;

import com.alevel.presistence.type.RoleType;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

public class SecurityUtil {

    public static Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static String getUsername(){
        Authentication authentication = SecurityUtil.getAuthentication();
        User principal = (User) authentication.getPrincipal();
        return principal.getUsername();
    }

    public static RoleType getRoleType(){
        Authentication authentication = SecurityUtil.getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Collection<GrantedAuthority> authorities = principal.getAuthorities();
        GrantedAuthority authority = authorities.stream().findFirst().get();
        return RoleType.valueOf(authority.getAuthority());
    }

    public static boolean hasRole(String role){
        Authentication authentication = SecurityUtil.getAuthentication();
        AtomicBoolean result = new AtomicBoolean(false);
        authentication.getAuthorities()
                .forEach(authority -> result.set(authority.getAuthority().equalsIgnoreCase(role)));
        return result.get();
    }
}
