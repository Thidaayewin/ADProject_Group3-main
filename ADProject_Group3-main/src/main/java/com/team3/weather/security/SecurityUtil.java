package com.team3.weather.security;

import java.util.Collection;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static String getSessionUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return currentUserName;
        }
        return null;
    }

    /*
     * In this project's scope, the Collection of GrantedAuthority(s)
     * only contains 1 GrantedAuthority which is "ROLE_ "+the model class name
     * see CustomeUSerDetailsService.java.
     */

    public static String getfirstAuthority(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        GrantedAuthority firstAuthority = authorities.iterator().next();
        return firstAuthority.getAuthority();
    }

}
