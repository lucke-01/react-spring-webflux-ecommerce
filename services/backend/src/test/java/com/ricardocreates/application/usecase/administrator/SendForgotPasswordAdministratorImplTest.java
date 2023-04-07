package com.ricardocreates.application.usecase.administrator;

import com.ricardocreates.domain.email.EmailService;
import com.ricardocreates.domain.entity.administrator.Administrator;
import com.ricardocreates.domain.entity.exceptions.BadRequestException;
import com.ricardocreates.domain.repository.administrator.AdministratorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SendForgotPasswordAdministratorImplTest {
    private static final String ADMINISTRATOR_ID = "1";
    private static final String ADMINISTRATOR_LOGIN = "login";
    @Mock
    private AdministratorRepository administratorRepository;
    @Mock
    private EmailService emailService;
    @InjectMocks
    private SendForgotPasswordAdministratorImpl sendForgotPasswordAdministratorImpl;

    @Test
    @DisplayName("should findLogin sendMessage and Update")
    void should_findLogin_sendMessage_andUpdate() {
        //given
        given(administratorRepository.findByEmail(any())).willReturn(Mono.just(mockAdministrator()));
        given(emailService.sendSimpleMessage(any(), any(), any())).willReturn(Mono.just(true));
        given(administratorRepository.updateAdministratorForgotToken(any(), any())).willReturn(Mono.just(mockAdministrator()));
        //when
        sendForgotPasswordAdministratorImpl.sendForgotPassword("", ADMINISTRATOR_LOGIN).block();
        //then
        verify(administratorRepository).findByEmail(any());
        verify(emailService).sendSimpleMessage(any(), any(), any());
        verify(administratorRepository).updateAdministratorForgotToken(any(), any());
    }

    @Test
    @DisplayName("should not findLogin")
    void should_notFindLogin() {
        //given
        given(administratorRepository.findByEmail(any())).willReturn(Mono.error(new BadRequestException("")));
        //when
        Mono<Void> sendForgotPassword = this.sendForgotPasswordAdministratorImpl.sendForgotPassword("", ADMINISTRATOR_LOGIN);
        assertThatThrownBy(sendForgotPassword::block)
                .isInstanceOf(BadRequestException.class);
        //then
        verify(administratorRepository).findByEmail(any());
        verify(emailService, times(0)).sendSimpleMessage(any(), any(), any());
        verify(administratorRepository, times(0)).updateAdministratorForgotToken(any(), any());
    }

    @Test
    @DisplayName("should error email")
    void should_error_email() {
        //given
        given(administratorRepository.findByEmail(any())).willReturn(Mono.just(mockAdministrator()));
        given(emailService.sendSimpleMessage(any(), any(), any())).willReturn(Mono.error(new MailException("") {
        }));
        //when
        Mono<Void> sendForgotPassword = this.sendForgotPasswordAdministratorImpl.sendForgotPassword("", ADMINISTRATOR_LOGIN);
        assertThatThrownBy(sendForgotPassword::block)
                .isInstanceOf(MailException.class);
        //then
        verify(administratorRepository).findByEmail(any());
        verify(emailService).sendSimpleMessage(any(), any(), any());
        verify(administratorRepository, times(0)).updateAdministratorForgotToken(any(), any());
    }

    @Test
    @DisplayName("should error updateUserToken")
    void should_error_updateUserToken() {
        //given
        given(administratorRepository.findByEmail(any())).willReturn(Mono.just(mockAdministrator()));
        given(emailService.sendSimpleMessage(any(), any(), any())).willReturn(Mono.just(true));
        given(administratorRepository.updateAdministratorForgotToken(any(), any())).willReturn(Mono.error(new BadRequestException("")));
        //when
        Mono<Void> sendForgotPassword = this.sendForgotPasswordAdministratorImpl.sendForgotPassword("", ADMINISTRATOR_LOGIN);
        assertThatThrownBy(sendForgotPassword::block)
                .isInstanceOf(BadRequestException.class);
        //then
        verify(administratorRepository).findByEmail(any());
        verify(emailService).sendSimpleMessage(any(), any(), any());
        verify(administratorRepository).updateAdministratorForgotToken(any(), any());
    }

    private Administrator mockAdministrator() {
        Administrator admin = Administrator.builder().build();
        admin.setId(ADMINISTRATOR_ID);
        admin.setLogin(ADMINISTRATOR_LOGIN);
        admin.setPassword("test");
        return admin;
    }
}
