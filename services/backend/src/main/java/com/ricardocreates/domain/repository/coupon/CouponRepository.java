package com.ricardocreates.domain.repository.coupon;

import com.ricardocreates.domain.entity.coupon.Coupon;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CouponRepository {

    Mono<Coupon> getById(String id);

    Flux<Coupon> findAll();

    Mono<Coupon> add(Coupon coupon);

    Mono<Void> remove(String id);

    Mono<Coupon> update(Coupon coupon);

    Mono<Boolean> existsByName(String name, String excludedId);

    Mono<Boolean> existsByCode(String code, String excludedId);

}
