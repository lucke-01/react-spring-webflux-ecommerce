package com.ricardocreates.application.usecase.administrator;

import com.ricardocreates.application.service.PBKDF2Encoder;
import com.ricardocreates.application.validator.AdministratorValidator;
import com.ricardocreates.domain.entity.administrator.Administrator;
import com.ricardocreates.domain.entity.exceptions.BadRequestException;
import com.ricardocreates.domain.repository.administrator.AdministratorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AddAdministratorImplTest {

    @Mock
    AdministratorRepository administratorRepository;

    @Mock
    PBKDF2Encoder passwordEncoder;

    @Mock
    AdministratorValidator administratorValidator;

    @InjectMocks
    AddAdministratorImpl addAdministrator;


    @Test
    void should_add_administrator() {
        //given
        final String login = "Login";
        Administrator request = Administrator.builder().login(login).password("asdasdasdadasdasd").build();
        given(this.passwordEncoder.encode("password")).willReturn("asdasdasdadasdasd");
        given(this.administratorRepository.add(request)).willReturn(Mono.just(Administrator.builder().login("Login").build()));
        given(this.administratorValidator.checkDuplicatedEmail(any(), any())).willReturn(Mono.just(true));
        given(this.administratorValidator.checkDuplicatedLogin(any(), any())).willReturn(Mono.just(true));
        // When
        final Administrator response = this.addAdministrator.add(Administrator.builder().login(login).password("password").build()).block();

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getLogin()).isEqualTo(login);
        verify(this.administratorValidator).checkDuplicatedEmail(any(), any());
        verify(this.administratorValidator).checkDuplicatedLogin(any(), any());
        verify(this.administratorRepository).add(any());
    }

    @Test
    void should_not_add_administrator_uniqueEmail() {
        //given
        final String login = "Login";
        given(this.passwordEncoder.encode("password")).willReturn("asdasdasdadasdasd");
        given(this.administratorValidator.checkDuplicatedEmail(any(), any())).willReturn(Mono.error(new BadRequestException("duplicated email")));
        // When
        final Mono<Administrator> response = this.addAdministrator.add(Administrator.builder().login(login).password("password").build());
        assertThatThrownBy(response::block)
                .isInstanceOf(BadRequestException.class);
        // Then
        verify(this.administratorValidator).checkDuplicatedEmail(any(), any());
        verify(this.administratorRepository, times(0)).existsByLogin(any(), any());
        verify(this.administratorRepository, times(0)).add(any());
    }

    @Test
    void should_not_add_administrator_uniqueLogin() {
        //given
        final String login = "Login";
        given(this.passwordEncoder.encode("password")).willReturn("asdasdasdadasdasd");
        given(this.administratorValidator.checkDuplicatedEmail(any(), any())).willReturn(Mono.just(true));
        given(this.administratorValidator.checkDuplicatedLogin(any(), any())).willReturn(Mono.error(new BadRequestException("duplicated login")));
        // When
        final Mono<Administrator> response = this.addAdministrator.add(Administrator.builder().login(login).password("password").build());
        assertThatThrownBy(response::block)
                .isInstanceOf(BadRequestException.class);
        // Then
        verify(this.administratorValidator).checkDuplicatedEmail(any(), any());
        verify(this.administratorValidator).checkDuplicatedLogin(any(), any());
        verify(this.administratorRepository, times(0)).add(any());
    }
}
