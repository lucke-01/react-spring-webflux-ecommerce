package com.ricardocreates.application.usecase.administrator;

import com.ricardocreates.domain.repository.administrator.AdministratorRepository;
import com.ricardocreates.domain.entity.administrator.Administrator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class GetAdministratorImplTest {

    @Mock
    AdministratorRepository administratorRepository;

    @InjectMocks
    GetAdministratorImpl getAdministrator;

    @Test
    void should_get_administrator_by_login() {
        Mockito.when(this.administratorRepository.get("Admin")).thenReturn(Mono.just(
                Administrator.builder().login("Admin").build()));

        // When
        final Administrator response = this.getAdministrator.get("Admin").block();

        // Then
        Assertions.assertNotNull(response);
        Assertions.assertEquals("Admin", response.getLogin());
    }

    @Test
    void should_find_administrator() {
        Administrator request = Administrator.builder().build();
        Administrator administrator1 = Administrator.builder().id("1").build();
        Administrator administrator2 = Administrator.builder().id("2").build();
        Mockito.when(this.administratorRepository.find(request)).thenReturn(Flux.just(administrator1, administrator2));

        // When
        var response = this.getAdministrator.find(request).collectList().block();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response, List.of(administrator1, administrator2));
    }
}
