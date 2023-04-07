package com.ricardocreates.domain.usecase.administrator;

import com.ricardocreates.domain.entity.administrator.Administrator;
import reactor.core.publisher.Mono;

public interface UpdateAdministratorUseCase {

  Mono<Administrator> update(Administrator administrator);

}
