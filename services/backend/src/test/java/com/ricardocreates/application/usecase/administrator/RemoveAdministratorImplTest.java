package com.ricardocreates.application.usecase.administrator;

import com.ricardocreates.domain.repository.administrator.AdministratorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
public class RemoveAdministratorImplTest {

    @Mock
    AdministratorRepository administratorRepository;

    @InjectMocks
    RemoveAdministratorImpl removeAdministrator;

    @Test
    public void should_add_administrator() {
        Mockito.when(this.administratorRepository.remove("Login")).thenReturn(Mono.empty());

        // When
        this.removeAdministrator.remove("Login").block();

        Mockito.verify(administratorRepository).remove("Login");
    }
}
