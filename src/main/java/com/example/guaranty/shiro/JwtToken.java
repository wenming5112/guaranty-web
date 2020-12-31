package com.example.guaranty.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * JwtToken
 *
 * @author ming
 * @version 1.0.0
 * @date 2020/6/16 17:47
 */
public class JwtToken implements AuthenticationToken {

    private static final long serialVersionUID = 4214295721581364321L;
    /**
     * token
     */
    private String token;

    JwtToken(String token) {
        super();
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
