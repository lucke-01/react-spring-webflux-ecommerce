package com.ricardocreates.application.usecase.administrator;

import com.ricardocreates.application.validator.AdministratorValidator;
import com.ricardocreates.domain.repository.administrator.AdministratorRepository;
import com.ricardocreates.application.service.PBKDF2Encoder;
import com.ricardocreates.domain.entity.administrator.Administrator;
import com.ricardocreates.domain.usecase.administrator.UpdateAdministratorUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class UpdateAdministratorImpl implements UpdateAdministratorUseCase {

    private final AdministratorRepository administratorRepository;

    private final PBKDF2Encoder passwordEncoder;
    private final AdministratorValidator administratorValidator;

    @Override
    public Mono<Administrator> update(Administrator administrator) {
        Administrator.AdministratorBuilder adminToSend = administrator.toBuilder();
        if (Objects.nonNull(administrator.getPassword())) {
            adminToSend.password(passwordEncoder.encode(administrator.getPassword()));
        }
        return this.administratorValidator.checkDuplicatedEmail(administrator.getEmail(), administrator.getId())
                .flatMap(result -> this.administratorValidator.checkDuplicatedLogin(administrator.getLogin(), administrator.getId()))
                .flatMap(result ->
                        administratorRepository.update(adminToSend.build())
                );
    }
}
