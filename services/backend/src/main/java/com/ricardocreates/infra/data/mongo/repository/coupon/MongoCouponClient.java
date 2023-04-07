package com.ricardocreates.infra.data.mongo.repository.coupon;

import com.ricardocreates.infra.data.mongo.document.coupon.CouponDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface MongoCouponClient extends ReactiveMongoRepository<CouponDocument, String> {
    Mono<Boolean> existsByCodeAndIdNot(String code, String excludedId);

    Mono<Boolean> existsByNameAndIdNot(String name, String excludedId);
}
