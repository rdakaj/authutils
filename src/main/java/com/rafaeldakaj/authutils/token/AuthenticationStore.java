package com.rafaeldakaj.authutils.token;

public class AuthenticationStore {

    private static String secret;

    public static void setMainSecret(String secret){
        AuthenticationStore.secret = secret;
    }

    public static String getMainSecret(){
        return secret;
    }

}
