package com.ricardocreates.infra.data.mongo.repository.administrator;

import com.ricardocreates.infra.data.mongo.document.administrator.AdministratorDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface MongoAdministratorClient extends ReactiveMongoRepository<AdministratorDocument, String> {

    Mono<AdministratorDocument> findByLogin(String login);

    Mono<AdministratorDocument> findByLoginAndForgetPasswordToken(String login, String forgetPasswordToken);

    Mono<AdministratorDocument> findByEmail(String email);

    Mono<Boolean> existsByEmailAndIdNot(String email, String id);

    Mono<Boolean> existsByLoginAndIdNot(String login, String id);
}
