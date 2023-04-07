package com.ricardocreates.domain.usecase.administrator;

import reactor.core.publisher.Mono;

public interface RemoveAdministratorUseCase {

    Mono<Void> remove(String id);

}
