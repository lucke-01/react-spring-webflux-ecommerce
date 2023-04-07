package com.ricardocreates.application.usecase.administrator;

import com.ricardocreates.application.validator.AdministratorValidator;
import com.ricardocreates.domain.repository.administrator.AdministratorRepository;
import com.ricardocreates.application.service.PBKDF2Encoder;
import com.ricardocreates.domain.entity.administrator.Administrator;
import com.ricardocreates.domain.usecase.administrator.AddAdministratorUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class AddAdministratorImpl implements AddAdministratorUseCase {

    private final AdministratorRepository administratorRepository;

    private final PBKDF2Encoder passwordEncoder;

    private final AdministratorValidator administratorValidator;

    @Override
    public Mono<Administrator> add(Administrator administrator) {
        var password = passwordEncoder.encode(administrator.getPassword());
        return this.administratorValidator.checkDuplicatedEmail(administrator.getEmail(), null)
                .flatMap(result -> this.administratorValidator.checkDuplicatedLogin(administrator.getLogin(), null))
                .flatMap(result ->
                        administratorRepository.add(administrator.toBuilder().password(password).build())
                );
    }

}
