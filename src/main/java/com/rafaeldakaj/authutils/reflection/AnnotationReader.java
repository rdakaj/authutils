package com.rafaeldakaj.authutils.reflection;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.rafaeldakaj.authutils.annotation.Claim;
import com.rafaeldakaj.authutils.annotation.Subject;
import com.rafaeldakaj.authutils.exception.AnnotationNotProvidedException;

public class AnnotationReader {

    public static Map<String, Object> getClaims(Object object){
        Map<String, Object> map = new HashMap<>();
        for(Field f : object.getClass().getDeclaredFields()){
            Claim claim = f.getAnnotation(Claim.class);
            if(claim != null){
                try {
                    if(claim.value().equals("")) map.put(f.getName(), f.get(object));
                    else map.put(claim.value(), f.get(object));
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        if(map.size() != 0) return map;
        else throw new AnnotationNotProvidedException(Claim.class, object.getClass());
    }

    public static String getSubject(Object object){
        for(Field f : object.getClass().getDeclaredFields()){
            Subject subject = f.getAnnotation(Subject.class);
            if(subject != null){
                try {
                    return f.get(object).toString();
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        throw new AnnotationNotProvidedException(Subject.class, object.getClass());
    }

    public static <V> V setClaims(V object, Map<String, com.auth0.jwt.interfaces.Claim> claims){
        for(String key : claims.keySet()){
            com.auth0.jwt.interfaces.Claim claim = claims.get(key);
            for(Field f : object.getClass().getFields()){
                Claim annotation = f.getAnnotation(Claim.class);
                if(annotation != null && ((annotation.value().equals("") && f.getName().equals(key)) || annotation.value().equals(key))){
                    try {
                        boolean wasAccessible = f.canAccess(object);
                        if(!wasAccessible) f.setAccessible(true);
                        f.set(object, claim.as(f.getType()));
                        if(!wasAccessible) f.setAccessible(false);
                    } catch (JWTDecodeException | IllegalArgumentException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return object;
    }

    public static <V> V setSubject(V object, String subject){
        for(Field f : object.getClass().getFields()){
            Subject annotation = f.getAnnotation(Subject.class);
            if(annotation != null) {
                try {
                    boolean wasAccessible = f.canAccess(object);
                    if(!wasAccessible) f.setAccessible(true);
                    f.set(object, subject);
                    if(!wasAccessible) f.setAccessible(false);
                } catch (JWTDecodeException | IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return object;
    }
    
}
