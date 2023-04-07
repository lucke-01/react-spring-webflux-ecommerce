package com.ricardocreates.domain.usecase.coupon;

import com.ricardocreates.domain.entity.coupon.Coupon;
import reactor.core.publisher.Mono;

public interface AddCoupon {

    Mono<Coupon> add(Coupon coupon, String login);

}
