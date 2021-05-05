package com.rafaeldakaj.authutils.exception;

public class AnnotationNotProvidedException extends RuntimeException{

    public AnnotationNotProvidedException(Class<?> annotationClass, Class<?> object){
        super("Required annotation " + annotationClass.getName() + " is not present in the class "  + object.getName());
    }
    
}
