package com.ricardocreates.domain.usecase.coupon;

import reactor.core.publisher.Mono;

public interface RemoveCoupon {

    Mono<Void> remove(String id, String login);

}
