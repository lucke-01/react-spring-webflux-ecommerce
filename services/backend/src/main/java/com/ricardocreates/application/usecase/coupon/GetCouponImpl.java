package com.ricardocreates.application.usecase.coupon;

import com.ricardocreates.domain.entity.coupon.Coupon;
import com.ricardocreates.domain.repository.coupon.CouponRepository;
import com.ricardocreates.domain.usecase.coupon.GetCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class GetCouponImpl implements GetCoupon {

    private final CouponRepository couponRepository;

    @Override
    public Mono<Coupon> get(String id, String login) {
        return couponRepository.getById(id);
    }

    @Override
    public Flux<Coupon> findAll(String login) {
        return couponRepository.findAll();
    }
}
