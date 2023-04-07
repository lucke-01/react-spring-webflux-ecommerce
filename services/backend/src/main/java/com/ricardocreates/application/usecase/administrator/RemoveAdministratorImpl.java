package com.ricardocreates.application.usecase.administrator;

import com.ricardocreates.domain.repository.administrator.AdministratorRepository;
import com.ricardocreates.domain.usecase.administrator.RemoveAdministratorUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class RemoveAdministratorImpl implements RemoveAdministratorUseCase {

    private final AdministratorRepository administratorRepository;


    @Override
    public Mono<Void> remove(String id) {
        return administratorRepository.remove(id);
    }
}
