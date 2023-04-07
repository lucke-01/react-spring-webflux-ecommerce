package com.ricardocreates.application.usecase.administrator;

import com.ricardocreates.domain.repository.administrator.AdministratorRepository;
import com.ricardocreates.application.service.PBKDF2Encoder;
import com.ricardocreates.domain.usecase.administrator.RestorePassword;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class RestorePasswordImpl implements RestorePassword {
    private final AdministratorRepository administratorRepository;
    private final PBKDF2Encoder pBKDF2Encoder;

    @Override
    public Mono<Void> restorePassword(String login, String userToken, String newPassword) {
        return this.administratorRepository
                .restorePassword(login, userToken, pBKDF2Encoder.encode(newPassword))
                .then();
    }
}
