package com.ricardocreates.application.usecase.administrator;

import com.ricardocreates.domain.repository.administrator.AdministratorRepository;
import com.ricardocreates.domain.entity.administrator.Administrator;
import com.ricardocreates.domain.usecase.administrator.GetAdministratorUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class GetAdministratorImpl implements GetAdministratorUseCase {

    private final AdministratorRepository administratorMongoRepository;

    @Override
    public Mono<Administrator> get(String id) {
        return administratorMongoRepository.get(id);
    }

    @Override
    public Flux<Administrator> find(Administrator administrator) {
        return administratorMongoRepository.find(administrator);
    }
}
