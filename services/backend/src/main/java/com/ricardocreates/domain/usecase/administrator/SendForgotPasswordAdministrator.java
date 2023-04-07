package com.ricardocreates.domain.usecase.administrator;

import reactor.core.publisher.Mono;

public interface SendForgotPasswordAdministrator {
    Mono<Void> sendForgotPassword(String host, String email);
}
