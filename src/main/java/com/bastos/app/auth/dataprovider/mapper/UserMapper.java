package com.bastos.app.auth.dataprovider.mapper;

import com.bastos.app.auth.dataprovider.repository.entity.UserEntity;
import com.bastos.app.auth.usecase.domain.UserDomain;

public class UserMapper {

    public static UserDomain convertEntityToDomain(UserEntity userEntity) {
        return UserDomain.builder()
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .build();
    }
}
