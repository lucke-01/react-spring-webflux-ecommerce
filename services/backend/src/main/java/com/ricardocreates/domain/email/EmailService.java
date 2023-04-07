package com.ricardocreates.domain.email;

import reactor.core.publisher.Mono;

public interface EmailService {
    Mono<Boolean> sendSimpleMessage(String to, String subject, String text);
}
