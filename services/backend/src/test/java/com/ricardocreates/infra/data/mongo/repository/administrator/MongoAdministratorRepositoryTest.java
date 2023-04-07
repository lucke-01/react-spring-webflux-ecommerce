package com.ricardocreates.infra.data.mongo.repository.administrator;

import com.ricardocreates.domain.entity.administrator.Administrator;
import com.ricardocreates.domain.entity.administrator.LoginResponse;
import com.ricardocreates.domain.entity.exceptions.BadRequestException;
import com.ricardocreates.domain.entity.exceptions.NotFoundException;
import com.ricardocreates.infra.data.mongo.document.administrator.AdminRole;
import com.ricardocreates.infra.data.mongo.document.administrator.AdministratorDocument;
import com.ricardocreates.infra.data.mongo.mapper.UtilsMapper;
import com.ricardocreates.infra.data.mongo.mapper.administrator.AdministratorDocumentMapper;
import com.ricardocreates.infra.data.mongo.mapper.administrator.AdministratorDocumentMapperImpl;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MongoAdministratorRepositoryTest {
    private static final String LOGIN = "login";
    private static final String USER_TOKEN = "userToken";
    private static final String NEW_PASSWORD = "newPassword";
    private static final ObjectId ADMINISTRATOR_ID = new ObjectId();

    private static final String ADMINISTRATOR_LOGIN = "test";
    private static final String EMAIL_FORGOTTEN = "test@gmail.com";

    @Mock
    private MongoAdministratorClient mongoAdministratorClient;

    @Mock
    private ReactiveMongoTemplate mongoTemplate;

    @Spy
    private UtilsMapper utilsMapper = Mappers.getMapper(UtilsMapper.class);

    @Spy
    @InjectMocks
    private AdministratorDocumentMapper administratorDocumentMapper = new AdministratorDocumentMapperImpl();

    @InjectMocks
    private MongoAdministratorRepository mongoAdministratorRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void should_get_login() {
        //given
        final String login = "ricardo";
        AdministratorDocument administratorDocument = AdministratorDocument.builder()
                .id(new ObjectId("634923e3c79fcb6a76a85a41"))
                .login("ricardo")
                .password("PR+zRt45rXA24IraD659zQVYw3NgKE7wiIcgp9v8rps=")
                .role(AdminRole.ADMIN)
                .build();
        given(this.mongoAdministratorClient.findByLogin(login))
                .willReturn(Mono.just(administratorDocument));
        given(this.mongoAdministratorClient.save(any()))
                .willReturn(Mono.just(administratorDocument));

        //when
        var response = mongoAdministratorRepository.getLogin(login).block();

        //then
        LoginResponse expected = LoginResponse.builder()
                .login(login)
                .password("PR+zRt45rXA24IraD659zQVYw3NgKE7wiIcgp9v8rps=")
                .roles(List.of(AdminRole.ADMIN.toString()))
                .role("ADMIN")
                .build();
        assertThat(response).isEqualTo(expected);
    }

    @Test
    @DisplayName("should restore password")
    void should_restorePassword() {
        //given
        final AdministratorDocument adminDoc = mockAdministratorDocument();
        given(mongoAdministratorClient.findByLoginAndForgetPasswordToken(any(), any()))
                .willReturn(Mono.just(adminDoc));
        given(mongoAdministratorClient.save(any()))
                .willReturn(Mono.just(adminDoc));
        //when
        mongoAdministratorRepository.restorePassword(LOGIN, USER_TOKEN, NEW_PASSWORD).block();
        //then
        verify(mongoAdministratorClient).findByLoginAndForgetPasswordToken(LOGIN, USER_TOKEN);
        verify(mongoAdministratorClient).save(any());
    }

    @Test
    @DisplayName("should not restore password notFound")
    void should_not_restorePassword_notFound() {
        //given
        final AdministratorDocument adminDoc = mockAdministratorDocument();
        given(mongoAdministratorClient.findByLoginAndForgetPasswordToken(any(), any()))
                .willReturn(Mono.empty());
        //when
        Mono<Administrator> result = mongoAdministratorRepository.restorePassword(LOGIN, USER_TOKEN, NEW_PASSWORD);
        assertThatThrownBy(result::block)
                .isInstanceOf(NotFoundException.class);
        //then
        verify(mongoAdministratorClient).findByLoginAndForgetPasswordToken(LOGIN, USER_TOKEN);
        verify(mongoAdministratorClient, times(0)).save(any());
    }

    @Test
    @DisplayName("should not restore password repository error")
    void should_not_restorePassword_repositoryError() {
        //given
        final AdministratorDocument adminDoc = mockAdministratorDocument();
        given(mongoAdministratorClient.findByLoginAndForgetPasswordToken(any(), any()))
                .willReturn(Mono.just(adminDoc));
        given(mongoAdministratorClient.save(any()))
                .willThrow(new IllegalArgumentException(""));
        //when
        Mono<Administrator> result = mongoAdministratorRepository.restorePassword(LOGIN, USER_TOKEN, NEW_PASSWORD);
        assertThatThrownBy(result::block)
                .isInstanceOf(BadRequestException.class);
        //then
        verify(mongoAdministratorClient).findByLoginAndForgetPasswordToken(LOGIN, USER_TOKEN);
        verify(mongoAdministratorClient).save(any());
    }

    private AdministratorDocument mockAdministratorDocument() {
        AdministratorDocument admin = new AdministratorDocument();
        admin.setId(ADMINISTRATOR_ID);
        admin.setLogin(ADMINISTRATOR_LOGIN);
        admin.setEmail(EMAIL_FORGOTTEN);
        admin.setPassword("test");
        admin.setRole(AdminRole.ADMIN);
        return admin;
    }

}