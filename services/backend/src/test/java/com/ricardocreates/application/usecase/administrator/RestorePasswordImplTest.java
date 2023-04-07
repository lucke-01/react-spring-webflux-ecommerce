package com.ricardocreates.application.usecase.administrator;

import com.ricardocreates.application.service.PBKDF2Encoder;
import com.ricardocreates.domain.entity.administrator.Administrator;
import com.ricardocreates.domain.entity.exceptions.BadRequestException;
import com.ricardocreates.domain.repository.administrator.AdministratorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RestorePasswordImplTest {
    private static final String LOGIN = "login";
    private static final String USER_TOKEN = "userToken";
    private static final String NEW_PASSWORD = "newPassword";
    @Mock
    private PBKDF2Encoder pBKDF2Encoder;
    @Mock
    private AdministratorRepository administratorRepository;
    @InjectMocks
    private RestorePasswordImpl restorePasswordImpl;

    @Test
    @DisplayName("should restore password")
    void should_restorePassword() {
        //given
        String passwordEncoded = "passwordEncoded";
        given(pBKDF2Encoder.encode(any()))
                .willReturn(passwordEncoded);
        given(administratorRepository.restorePassword(any(), any(), any()))
                .willReturn(Mono.just(Administrator.builder().build()));
        //when
        this.restorePasswordImpl.restorePassword(LOGIN, USER_TOKEN, NEW_PASSWORD).block();
        //then
        verify(pBKDF2Encoder).encode(NEW_PASSWORD);
        verify(administratorRepository).restorePassword(LOGIN, USER_TOKEN, passwordEncoded);
    }

    @Test
    @DisplayName("should not restore password repository fail")
    void should_not_restorePassword_repositoryFail() {
        //given
        String passwordEncoded = "passwordEncoded";
        given(pBKDF2Encoder.encode(any()))
                .willReturn(passwordEncoded);
        given(administratorRepository.restorePassword(any(), any(), any()))
                .willReturn(Mono.error(new BadRequestException("")));
        //when
        Mono<Void> result = this.restorePasswordImpl.restorePassword(LOGIN, USER_TOKEN, NEW_PASSWORD);
        //then
        assertThatThrownBy(result::block)
                .isInstanceOf(BadRequestException.class);
        verify(pBKDF2Encoder).encode(NEW_PASSWORD);
        verify(administratorRepository).restorePassword(LOGIN, USER_TOKEN, passwordEncoded);
    }

    @Test
    @DisplayName("should not restore password encode fail")
    void should_not_restorePassword_encodeFail() {
        //given
        String passwordEncoded = "passwordEncoded";
        given(pBKDF2Encoder.encode(any()))
                .willThrow(new IllegalArgumentException(""));
        //when then
        assertThatThrownBy(
                () -> this.restorePasswordImpl.restorePassword(LOGIN, USER_TOKEN, NEW_PASSWORD))
                .isInstanceOf(IllegalArgumentException.class);
        verify(pBKDF2Encoder).encode(NEW_PASSWORD);
        verify(administratorRepository, times(0)).restorePassword(LOGIN, USER_TOKEN, passwordEncoded);
    }
}
