package com.ricardocreates.application.usecase.administrator;

import com.ricardocreates.domain.email.EmailService;
import com.ricardocreates.domain.repository.administrator.AdministratorRepository;
import com.ricardocreates.domain.usecase.administrator.SendForgotPasswordAdministrator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class SendForgotPasswordAdministratorImpl implements SendForgotPasswordAdministrator {
    private static final String MAIL_SUBJECT = "Ecommerce restore password";
    private static final String MAIL_RESTORE_API = "restore-password";
    private static final String MAIL_BODY = """
            To restore your password please click on the following link<br/>
            <a href="%s/%s/%s" target="_blank">Click here to restore</a>
            """;
    private final AdministratorRepository administratorRepository;
    private final EmailService emailService;

    @Override
    public Mono<Void> sendForgotPassword(String host, String email) {
        String randomToken = UUID.randomUUID().toString();
        String mailBody = String.format(MAIL_BODY, host, MAIL_RESTORE_API, randomToken);
        return administratorRepository.findByEmail(email)
                .flatMap(admin -> emailService.sendSimpleMessage(admin.getEmail(), MAIL_SUBJECT, mailBody))
                .flatMap(ok -> administratorRepository.updateAdministratorForgotToken(email, randomToken))
                .then();
    }
}
