package com.ricardocreates.application.usecase.coupon;

import com.ricardocreates.application.validator.CouponValidator;
import com.ricardocreates.domain.entity.coupon.Coupon;
import com.ricardocreates.domain.entity.exceptions.BadRequestException;
import com.ricardocreates.domain.repository.coupon.CouponRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AddCouponImplTest {
    private static final String COUPON_NAME = "name";
    private static final String COUPON_CODE = "code";

    @Mock
    private CouponRepository couponRepository;
    @Mock
    private CouponValidator couponValidator;

    @InjectMocks
    private AddCouponImpl addCoupon;


    @Test
    void should_add_coupon() {
        //given
        String login = "login";
        Coupon request = mockCoupon();
        given(this.couponRepository.add(request)).willReturn(Mono.just(mockCoupon()));
        given(this.couponValidator.checkDuplicatedName(eq(COUPON_NAME), isNull())).willReturn(Mono.just(true));
        given(this.couponValidator.checkDuplicatedCode(eq(COUPON_CODE), isNull())).willReturn(Mono.just(true));

        // When
        var response = this.addCoupon.add(request, login).block();

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(COUPON_NAME);
        verify(this.couponValidator).checkDuplicatedName(eq(COUPON_NAME), isNull());
        verify(this.couponValidator).checkDuplicatedCode(eq(COUPON_CODE), isNull());
        verify(this.couponRepository).add(any());
    }

    @Test
    void should_not_add_coupon_uniqueName() {
        //given
        String login = "login";
        Coupon request = mockCoupon();
        given(this.couponValidator.checkDuplicatedName(eq(COUPON_NAME), isNull())).willReturn(Mono.error(new BadRequestException("duplicated email")));
        // When
        var response = this.addCoupon.add(request, login);
        assertThatThrownBy(response::block)
                .isInstanceOf(BadRequestException.class);
        // Then
        verify(this.couponValidator).checkDuplicatedName(eq(COUPON_NAME), isNull());
        verify(this.couponValidator, times(0)).checkDuplicatedCode(any(), any());
        verify(this.couponRepository, times(0)).add(any());
    }

    @Test
    void should_not_add_coupon_uniqueCode() {
        //given
        String login = "login";
        Coupon request = mockCoupon();
        given(this.couponValidator.checkDuplicatedName(eq(COUPON_NAME), isNull())).willReturn(Mono.just(true));
        given(this.couponValidator.checkDuplicatedCode(eq(COUPON_CODE), isNull())).willReturn(Mono.error(new BadRequestException("duplicated email")));
        // When
        var response = this.addCoupon.add(request, login);
        assertThatThrownBy(response::block)
                .isInstanceOf(BadRequestException.class);

        // Then
        verify(this.couponValidator).checkDuplicatedName(eq(COUPON_NAME), isNull());
        verify(this.couponValidator).checkDuplicatedCode(eq(COUPON_CODE), isNull());
        verify(this.couponRepository, times(0)).add(any());
    }

    Coupon mockCoupon() {
        return Coupon.builder()
                .name(COUPON_NAME)
                .code(COUPON_CODE)
                .build();
    }
}
