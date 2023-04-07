package com.ricardocreates.domain.usecase.coupon;

import com.ricardocreates.domain.entity.coupon.Coupon;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GetCoupon {

    Mono<Coupon> get(String id, String login);

    Flux<Coupon> findAll(String login);

}
