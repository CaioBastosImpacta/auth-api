package com.bastos.app.auth.usecase.gateway;

import com.bastos.app.auth.usecase.domain.UserDomain;

public interface UserGateway {

    UserDomain findByEmail(String email);
}
