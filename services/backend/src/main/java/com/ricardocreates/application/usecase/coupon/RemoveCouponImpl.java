package com.ricardocreates.application.usecase.coupon;

import com.ricardocreates.domain.repository.coupon.CouponRepository;
import com.ricardocreates.domain.usecase.coupon.RemoveCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class RemoveCouponImpl implements RemoveCoupon {

    private final CouponRepository couponRepository;

    @Override
    public Mono<Void> remove(String id, String login) {
        return this.couponRepository.remove(id);
    }
}
