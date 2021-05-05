package com.rafaeldakaj.authutils.token;

import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.rafaeldakaj.authutils.reflection.AnnotationReader;

public class TokenFactory {

    private final Algorithm algorithm;
    
    public TokenFactory(){
        this(AuthenticationStore.getMainSecret());
    }

    public TokenFactory(String secret){
        this.algorithm = Algorithm.HMAC256(secret);
    }

    public String toJWTToken(Object object){
        Map<String, Object> claims = AnnotationReader.getClaims(object);
        return JWT.create().withSubject(AnnotationReader.getSubject(object)).withPayload(claims).withIssuer("moneyman.root").sign(algorithm);
    }

}
