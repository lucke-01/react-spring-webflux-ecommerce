package com.ricardocreates.application.usecase.coupon;

import com.ricardocreates.application.validator.CouponValidator;
import com.ricardocreates.domain.repository.coupon.CouponRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RemoveCouponImplTest {

    @Mock
    private CouponRepository couponRepository;
    @Mock
    private CouponValidator couponValidator;
    @InjectMocks
    private RemoveCouponImpl removeCoupon;

    @Test
    public void should_remove_coupon() {
        // Given
        String login = "login";
        String id = "id";
        given(this.couponRepository.remove(eq(id))).willReturn(Mono.empty());

        // When
        var response = this.removeCoupon.remove(id, login).block();

        // Then
        verify(this.couponRepository).remove(eq(id));
    }
}
