package com.ricardocreates.infra.email;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import reactor.core.publisher.Mono;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {
    private static final String TO = "test@gmail.com";
    private static final String SUBJECT = "subject";
    private static final String TEXT = "text";
    @Mock
    private JavaMailSender emailSender;
    @InjectMocks
    private EmailServiceImpl emailServiceImpl;

    @Test
    void shouldSendEmail() throws MessagingException {
        //given
        given(emailSender.createMimeMessage()).willReturn(Mockito.mock(MimeMessage.class));
        //when
        Boolean result = emailServiceImpl.sendSimpleMessage(TO, SUBJECT, TEXT).block();
        //then
        assertThat(result).isTrue();
        verify(emailSender).send(any(MimeMessage.class));
    }

    @Test
    void shouldNotSendEmailMailException() {
        //given
        given(emailSender.createMimeMessage()).willReturn(Mockito.mock(MimeMessage.class));
        Mockito.doThrow(new MailException("") {
        }).when(emailSender).send(any(MimeMessage.class));
        //when
        Mono<Boolean> result = emailServiceImpl.sendSimpleMessage(TO, SUBJECT, TEXT);
        assertThatThrownBy(result::block)
                .isInstanceOf(MailException.class);
        //then
        verify(emailSender).send(any(MimeMessage.class));
    }

    @Test
    void shouldNotSendEmailUnexpectedException() {
        //given
        given(emailSender.createMimeMessage()).willReturn(Mockito.mock(MimeMessage.class));
        Mockito.doThrow(new RuntimeException(""))
                .when(emailSender).send(any(MimeMessage.class));
        //when
        Mono<Boolean> result = emailServiceImpl.sendSimpleMessage(TO, SUBJECT, TEXT);
        assertThatThrownBy(result::block)
                .isInstanceOf(RuntimeException.class);
        //then
        verify(emailSender).send(any(MimeMessage.class));
    }
}
