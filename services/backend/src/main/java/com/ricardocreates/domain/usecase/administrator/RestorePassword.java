package com.ricardocreates.domain.usecase.administrator;

import reactor.core.publisher.Mono;

public interface RestorePassword {
    /**
     * given a login and a userToken which matches update user password and set userToken to null
     *
     * @param login       login
     * @param userToken   userToken generated when forget password
     * @param newPassword newPassword
     * @return a Administrator
     */
    Mono<Void> restorePassword(String login, String userToken, String newPassword);
}
