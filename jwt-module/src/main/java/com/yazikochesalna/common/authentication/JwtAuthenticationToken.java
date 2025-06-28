package com.yazikochesalna.common.authentication;


import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final String principal;
    private final long userId;
    private final String credentials;
    private final String token;

    public JwtAuthenticationToken(String token, long userId,
                                  Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.token = token;
        this.userId = userId;

        this.principal = String.valueOf(userId);
        this.credentials = token;
        super.setAuthenticated(true);
    }
}