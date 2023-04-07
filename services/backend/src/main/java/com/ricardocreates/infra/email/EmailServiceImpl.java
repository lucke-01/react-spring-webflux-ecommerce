package com.ricardocreates.infra.email;

import com.ricardocreates.domain.email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.mail.internet.MimeMessage;

@RequiredArgsConstructor
@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender emailSender;

    @Override
    public Mono<Boolean> sendSimpleMessage(String to, String subject, String text) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            helper.setFrom("noreply@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);
            this.emailSender.send(message);
            return Mono.just(true);
        } catch (MailException e) {
            log.error(String.format("error sending email to %s", to), e);
            return Mono.error(e);
        } catch (Exception e) {
            log.error(String.format("unexpected error sending email to %s", to), e);
            return Mono.error(e);
        }
    }
}
