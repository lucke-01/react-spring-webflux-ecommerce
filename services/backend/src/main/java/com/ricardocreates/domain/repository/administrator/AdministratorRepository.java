package com.ricardocreates.domain.repository.administrator;

import com.ricardocreates.domain.entity.administrator.Administrator;
import com.ricardocreates.domain.entity.administrator.LoginResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AdministratorRepository {

    Mono<Administrator> get(String id);

    Mono<Administrator> findByLogin(String login);

    Mono<Administrator> findByEmail(String email);

    Mono<Boolean> existsByEmail(String email, String excludedId);

    Mono<Boolean> existsByLogin(String login, String excludedId);

    Flux<Administrator> find(Administrator administrator);

    Mono<LoginResponse> getLogin(String login);

    Mono<Administrator> add(Administrator administrator);

    Mono<Void> remove(String id);

    Mono<Administrator> update(Administrator administrator);

    Mono<Administrator> updateAdministratorForgotToken(String login, String forgetToken);

    /**
     * given a login and a userToken which matches update user password and set userToken to null
     *
     * @param login              login
     * @param userToken          userToken generated when forget password
     * @param encodedNewPassword newPassword encoded
     * @return a Administrator
     */
    Mono<Administrator> restorePassword(String login, String userToken, String encodedNewPassword);
}
