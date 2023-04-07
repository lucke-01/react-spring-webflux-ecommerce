package com.ricardocreates.application.validator;

import com.ricardocreates.domain.entity.exceptions.BadRequestException;
import com.ricardocreates.domain.repository.coupon.CouponRepository;
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
class CouponValidatorTest {
    @Mock
    CouponRepository couponRepository;

    @InjectMocks
    CouponValidator couponValidator;

    @Test
    void should_check_name_ok() {
        //given
        given(this.couponRepository.existsByName(any(), any())).willReturn(Mono.just(false));
        String name = "name";
        // When
        Boolean result = this.couponValidator.checkDuplicatedName(name, null).block();

        // Then
        assertThat(result).isTrue();
        verify(this.couponRepository).existsByName(any(), any());
    }

    @Test
    void should_check_nullName_ok() {
        // Given
        // When
        Boolean result = this.couponValidator.checkDuplicatedName(null, null).block();
        // Then
        assertThat(result).isTrue();
        verify(this.couponRepository, times(0)).existsByName(any(), any());
    }

    @Test
    void should_check_name_alreadyExist() {
        //given
        given(this.couponRepository.existsByName(any(), any())).willReturn(Mono.just(true));
        String name = "name";
        // When
        Mono<Boolean> result = this.couponValidator.checkDuplicatedName(name, null);
        assertThatThrownBy(result::block)
                .isInstanceOf(BadRequestException.class);

        // Then
        verify(this.couponRepository).existsByName(any(), any());
    }

    @Test
    void should_check_code_ok() {
        //given
        given(this.couponRepository.existsByCode(any(), any())).willReturn(Mono.just(false));
        String code = "code";
        // When
        Boolean result = this.couponValidator.checkDuplicatedCode(code, null).block();

        // Then
        assertThat(result).isTrue();
        verify(this.couponRepository).existsByCode(any(), any());
    }

    @Test
    void should_check_nullCode_ok() {
        // Given
        // When
        Boolean result = this.couponValidator.checkDuplicatedCode(null, null).block();
        // Then
        assertThat(result).isTrue();
        verify(this.couponRepository, times(0)).existsByCode(any(), any());
    }

    @Test
    void should_check_code_alreadyExist() {
        //given
        given(this.couponRepository.existsByCode(any(), any())).willReturn(Mono.just(true));
        String code = "code";
        // When
        Mono<Boolean> result = this.couponValidator.checkDuplicatedCode(code, null);
        assertThatThrownBy(result::block)
                .isInstanceOf(BadRequestException.class);

        // Then
        verify(this.couponRepository).existsByCode(any(), any());
    }

}