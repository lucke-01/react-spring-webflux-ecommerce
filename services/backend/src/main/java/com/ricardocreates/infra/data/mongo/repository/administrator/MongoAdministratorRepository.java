package com.ricardocreates.infra.data.mongo.repository.administrator;

import com.ricardocreates.domain.entity.administrator.Administrator;
import com.ricardocreates.domain.entity.administrator.LoginResponse;
import com.ricardocreates.domain.entity.exceptions.BadRequestException;
import com.ricardocreates.domain.entity.exceptions.NotFoundException;
import com.ricardocreates.domain.entity.exceptions.ServiceUnavailableException;
import com.ricardocreates.domain.repository.administrator.AdministratorRepository;
import com.ricardocreates.infra.data.mongo.document.administrator.AdministratorDocument;
import com.ricardocreates.infra.data.mongo.mapper.administrator.AdministratorDocumentMapper;
import com.ricardocreates.infra.data.mongo.repository.util.OperationMongoRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Comparator;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class MongoAdministratorRepository implements AdministratorRepository {

    private final MongoAdministratorClient mongoAdministratorClient;
    private final ReactiveMongoTemplate mongoTemplate;
    private final AdministratorDocumentMapper administratorDocumentMapper;

    @Override
    public Mono<Administrator> get(String id) {
        try {
            return mongoAdministratorClient.findById(String.valueOf(new ObjectId(id)))
                    .switchIfEmpty(Mono.error(new NotFoundException("Not found Administrator by id")))
                    .map(administratorDocumentMapper::toDomain);
        } catch (IllegalArgumentException e) {
            return Mono.error(new BadRequestException(e.getMessage()));
        }
    }

    @Override
    public Mono<Administrator> findByLogin(String login) {
        try {
            return mongoAdministratorClient.findByLogin(login)
                    .switchIfEmpty(Mono.error(new NotFoundException("Not found Administrator by login")))
                    .map(administratorDocumentMapper::toDomain);
        } catch (IllegalArgumentException e) {
            return Mono.error(new BadRequestException(e.getMessage()));
        }
    }

    @Override
    public Mono<Administrator> findByEmail(String email) {
        try {
            return mongoAdministratorClient.findByEmail(email)
                    .switchIfEmpty(Mono.error(new NotFoundException("administrator email Not found")))
                    .map(administratorDocumentMapper::toDomain);
        } catch (IllegalArgumentException e) {
            return Mono.error(new BadRequestException(e.getMessage()));
        }
    }

    @Override
    public Mono<Boolean> existsByEmail(String email, String excludedId) {
        try {
            return mongoAdministratorClient.existsByEmailAndIdNot(email, excludedId);
        } catch (IllegalArgumentException e) {
            return Mono.error(new BadRequestException(e.getMessage()));
        }
    }

    @Override
    public Mono<Boolean> existsByLogin(String login, String excludedId) {
        try {
            return mongoAdministratorClient.existsByLoginAndIdNot(login, excludedId);
        } catch (IllegalArgumentException e) {
            return Mono.error(new BadRequestException(e.getMessage()));
        }
    }

    @Override
    public Flux<Administrator> find(Administrator administrator) {
        Query query = new Query();
        return mongoTemplate.find(query, AdministratorDocument.class)
                .map(administratorDocumentMapper::toDomain)
                .sort(Comparator.comparing(o -> o.getLogin() != null ? o.getLogin().toUpperCase() : null, Comparator.nullsFirst(Comparator.naturalOrder())))
                ;
    }

    @Override
    public Mono<LoginResponse> getLogin(String login) {
        try {
            return mongoAdministratorClient.findByLogin(login)
                    .switchIfEmpty(Mono.error(new NotFoundException("Not found Administrator")))
                    .flatMap(administratorDocument -> mongoAdministratorClient.save(administratorDocument
                            .toBuilder().lastLogin(Instant.now()).build()))
                    .map(administratorDocumentMapper::toDomainLogin);
        } catch (IllegalArgumentException e) {
            return Mono.error(new BadRequestException(e.getMessage()));
        }
    }

    @Override
    public Mono<Administrator> add(Administrator administrator) {
        return mongoAdministratorClient.save(administratorDocumentMapper.fromDomain(administrator))
                .map(administratorDocumentMapper::toDomain);

    }

    @Override
    public Mono<Void> remove(String id) {
        try {
            return mongoAdministratorClient.existsById(String.valueOf(new ObjectId(id)))
                    .flatMap(exist -> !exist ?
                            Mono.error(new NotFoundException("Not found Administrator to delete."))
                            : mongoAdministratorClient.deleteById(id).then()
                    );
        } catch (IllegalArgumentException e) {
            return Mono.error(new BadRequestException(e.getMessage()));
        }
    }

    @Override
    public Mono<Administrator> update(Administrator administrator) {
        try {
            return mongoAdministratorClient.findById(String.valueOf(new ObjectId(administrator.getId())))
                    .switchIfEmpty(Mono.error(new NotFoundException("Not found Administrator to update.")))
                    .flatMap(adminDoc -> {
                                Map<String, Object> mapUpdateAdmin = OperationMongoRepository.mapFromObject(administrator);
                                //remove password if it is not set to do not update it
                                if (StringUtils.isEmpty(administrator.getPassword())) {
                                    mapUpdateAdmin.remove("password");
                                }
                                Update userUpdate = OperationMongoRepository.createUpdateFromMap(mapUpdateAdmin);
                                Query userByIdQuery = OperationMongoRepository.createQueryFindById(administrator.getId());
                                return mongoTemplate.updateFirst(userByIdQuery, userUpdate, AdministratorDocument.class)
                                        .flatMap(u -> mongoAdministratorClient.findById(administrator.getId()))
                                        .map(administratorDocumentMapper::toDomain);
                            }
                    );
        } catch (IllegalArgumentException e) {
            return Mono.error(new BadRequestException(e.getMessage()));
        }
    }

    @Override
    public Mono<Administrator> updateAdministratorForgotToken(String email, String forgetToken) {
        try {
            return mongoAdministratorClient.findByEmail(email).flatMap(admin -> {
                        admin.setForgetPasswordToken(forgetToken);
                        return mongoAdministratorClient.save(admin);
                    })
                    .map(administratorDocumentMapper::toDomain);
        } catch (IllegalArgumentException e) {
            return Mono.error(new BadRequestException(e.getMessage()));
        } catch (Exception e) {
            return Mono.error(new ServiceUnavailableException(e.getMessage()));
        }
    }

    @Override
    public Mono<Administrator> restorePassword(String login, String userToken, String encodedNewPassword) {
        try {
            return mongoAdministratorClient.findByLoginAndForgetPasswordToken(login, userToken)
                    .switchIfEmpty(Mono.error(new NotFoundException("login or userToken does not match")))
                    .flatMap(adminDocFound -> saveAdministratorDocument(adminDocFound.toBuilder().password(encodedNewPassword).forgetPasswordToken(null).build()))
                    .map(administratorDocumentMapper::toDomain);
        } catch (IllegalArgumentException e) {
            return Mono.error(new BadRequestException(e.getMessage()));
        } catch (Exception e) {
            return Mono.error(new ServiceUnavailableException(e.getMessage()));
        }
    }

    private Mono<AdministratorDocument> saveAdministratorDocument(AdministratorDocument adminDoc) {
        try {
            return mongoAdministratorClient.save(adminDoc);
        } catch (IllegalArgumentException e) {
            return Mono.error(new BadRequestException(e.getMessage()));
        } catch (Exception e) {
            return Mono.error(new ServiceUnavailableException(e.getMessage()));
        }
    }
}
