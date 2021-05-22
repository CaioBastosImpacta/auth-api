package com.bastos.app.auth.usecase.domain;

import lombok.Getter;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

@Getter
public class AuthUserDomain extends User {

    private String name;

    public AuthUserDomain(UserDomain userDomain) {
        super(userDomain.getEmail(), userDomain.getPassword(), Collections.emptyList());

        this.name = userDomain.getName();
    }
}
