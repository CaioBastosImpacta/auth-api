package com.bastos.app.auth.dataprovider.implementation;

import com.bastos.app.auth.dataprovider.mapper.UserMapper;
import com.bastos.app.auth.dataprovider.repository.UserRepository;
import com.bastos.app.auth.dataprovider.repository.entity.UserEntity;
import com.bastos.app.auth.usecase.domain.UserDomain;
import com.bastos.app.auth.usecase.gateway.UserGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserImplementation implements UserGateway {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDomain findByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario nao encontrado."));

        return UserMapper.convertEntityToDomain(userEntity);
    }
}
