package com.bikerental.security;

import org.springframework.security.core.GrantedAuthority;

public class Authority implements GrantedAuthority {

    private String authoritys;

    public Authority(String auth){
        super();
        this.authoritys= auth;
    }


    @Override
    public String getAuthority() {
        return this.authoritys;
    }
    
}
