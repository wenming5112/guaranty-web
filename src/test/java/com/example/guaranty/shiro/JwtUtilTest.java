package com.example.guaranty.shiro;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.Test;

public class JwtUtilTest {

    @Test
    public void test() {
        Algorithm algorithm = Algorithm.HMAC256("123456");
        // 附带username信息
        System.out.println(JWT.create()
                .withClaim("username", "m")
                .sign(algorithm));
    }

    @Test
    public void jwtDecode() {
        DecodedJWT jwt = JWT.decode("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6Ind6bSJ9.m39RimgJqCb-Jyi4MHbQw9sNYWVi4dDKGc78mVemr5s");

        System.out.println(jwt.getClaim("username").asString());
        System.out.println(jwt.getHeader());
        System.out.println(jwt.getPayload());
        System.out.println(jwt.getSignature());
        System.out.println(jwt.getExpiresAt());
        System.out.println();
    }

}