package com.ricardocreates.application.validator;

import com.ricardocreates.domain.entity.exceptions.BadRequestException;
import com.ricardocreates.domain.repository.administrator.AdministratorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class AdministratorValidator {
    private final AdministratorRepository administratorRepository;

    public Mono<Boolean> checkDuplicatedEmail(String email, String excludedId) {
        if (email != null) {
            return administratorRepository
                    .existsByEmail(email, excludedId)
                    .flatMap(existsAdm -> {
                        if (Boolean.TRUE.equals(existsAdm)) {
                            throw new BadRequestException("duplicated email");
                        } else {
                            return Mono.just(true);
                        }
                    });
        } else {
            return Mono.just(true);
        }
    }

    public Mono<Boolean> checkDuplicatedLogin(String login, String excludedId) {
        if (login != null) {
            return administratorRepository
                    .existsByLogin(login, excludedId)
                    .flatMap(existsAdm -> {
                        if (Boolean.TRUE.equals(existsAdm)) {
                            throw new BadRequestException("duplicated login");
                        } else {
                            return Mono.just(true);
                        }
                    });
        } else {
            return Mono.just(true);
        }
    }
}
