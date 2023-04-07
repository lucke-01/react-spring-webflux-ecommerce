package com.ricardocreates.application.service.administrator;

import com.ricardocreates.application.service.GetLoginImpl;
import com.ricardocreates.application.service.JWTUtil;
import com.ricardocreates.application.service.PBKDF2Encoder;
import com.ricardocreates.domain.entity.administrator.LoginRequest;
import com.ricardocreates.domain.entity.administrator.LoginResponse;
import com.ricardocreates.domain.repository.administrator.AdministratorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
public class GetLoginImplTest {

    @Mock
    AdministratorRepository administratorRepository;

    @Mock
    PBKDF2Encoder passwordEncoder;

    @Mock
    JWTUtil jwtUtil;

    @InjectMocks
    GetLoginImpl getLogin;

    @Test
    public void should_get_login() {
        Mockito.when(this.administratorRepository.getLogin("Admin")).thenReturn(Mono.just(
                LoginResponse.builder().login("Admin").password("asdasdasdadasdasd").build()));
        Mockito.when(this.passwordEncoder.encode("admin")).thenReturn("asdasdasdadasdasd");
        Mockito.when(this.jwtUtil.generateToken(LoginResponse
                        .builder().login("Admin").password("asdasdasdadasdasd").build()))
                .thenReturn("token");

        // When
        final var response = this.getLogin.get(LoginRequest.builder()
                .login("Admin").password("admin").build()).block();

        // Then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(response, LoginResponse
                .builder().login("Admin").password("asdasdasdadasdasd").token("token").build());
    }
}
