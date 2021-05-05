package com.rafaeldakaj.authutils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.rafaeldakaj.authutils.reflection.AnnotationReader;

public class TokenParser {

    private final Algorithm algorithm;
    private JWTVerifier verifier;
    private Class<?> type;
    
    public TokenParser(Class<?> type, String secret){
        this.algorithm = Algorithm.HMAC256(secret);
        this.verifier = JWT.require(algorithm).acceptLeeway(1).build();    
    }

    public TokenParser(Class<?> type){
        this(type, AuthenticationStore.getMainSecret());
    }

    @SuppressWarnings("unchecked")
    public <V> V parseToken(String token){
        try{
            DecodedJWT jwt = verifier.verify(token);
            V object = (V) type.getDeclaredConstructor().newInstance();
            AnnotationReader.setSubject(object, jwt.getSubject());
            AnnotationReader.setClaims(object, jwt.getClaims());
            return object;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
