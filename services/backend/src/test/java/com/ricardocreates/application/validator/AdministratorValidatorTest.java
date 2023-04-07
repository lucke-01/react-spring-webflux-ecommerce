package com.ricardocreates.application.validator;

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
class AdministratorValidatorTest {
    @Mock
    AdministratorRepository administratorRepository;

    @InjectMocks
    AdministratorValidator administratorValidator;

    @Test
    void should_check_email_ok() {
        //given
        given(this.administratorRepository.existsByEmail(any(), any())).willReturn(Mono.just(false));
        String email = "email";
        // When
        Boolean result = this.administratorValidator.checkDuplicatedEmail(email, null).block();

        // Then
        assertThat(result).isTrue();
        verify(this.administratorRepository).existsByEmail(any(), any());
    }

    @Test
    void should_check_nullEmail_ok() {
        // Given
        // When
        Boolean result = this.administratorValidator.checkDuplicatedEmail(null, null).block();
        // Then
        assertThat(result).isTrue();
        verify(this.administratorRepository, times(0)).existsByEmail(any(), any());
    }

    @Test
    void should_check_email_alreadyExist() {
        //given
        given(this.administratorRepository.existsByEmail(any(), any())).willReturn(Mono.just(true));
        String email = "email";
        // When
        Mono<Boolean> result = this.administratorValidator.checkDuplicatedEmail(email, null);
        assertThatThrownBy(result::block)
                .isInstanceOf(BadRequestException.class);

        // Then
        verify(this.administratorRepository).existsByEmail(any(), any());
    }
}