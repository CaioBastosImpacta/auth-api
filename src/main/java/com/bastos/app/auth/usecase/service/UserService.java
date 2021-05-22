package com.bastos.app.auth.usecase.service;

import com.bastos.app.auth.usecase.domain.AuthUserDomain;
import com.bastos.app.auth.usecase.domain.UserDomain;
import com.bastos.app.auth.usecase.gateway.UserGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserService implements UserDetailsService {

    @Autowired
    UserGateway userGateway;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDomain userDomain = userGateway.findByEmail(username);

        return new AuthUserDomain(userDomain);
    }
}
