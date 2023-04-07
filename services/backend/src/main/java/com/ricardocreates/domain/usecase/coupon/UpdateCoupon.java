package com.ricardocreates.domain.usecase.coupon;

import com.ricardocreates.domain.entity.coupon.Coupon;
import reactor.core.publisher.Mono;

public interface UpdateCoupon {

    Mono<Coupon> update(Coupon coupon, String login);

}
