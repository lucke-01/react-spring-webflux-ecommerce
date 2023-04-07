package com.ricardocreates.domain.usecase.administrator;

import com.ricardocreates.domain.entity.administrator.Administrator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GetAdministratorUseCase {

    Mono<Administrator> get(String id);

    Flux<Administrator> find(Administrator administrator);

}
