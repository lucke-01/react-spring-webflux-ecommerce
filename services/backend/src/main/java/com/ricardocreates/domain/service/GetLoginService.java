package com.ricardocreates.domain.service;

import com.ricardocreates.domain.entity.administrator.LoginRequest;
import com.ricardocreates.domain.entity.administrator.LoginResponse;
import reactor.core.publisher.Mono;

public interface GetLoginService {

    Mono<LoginResponse> get(LoginRequest loginRequest);

}
