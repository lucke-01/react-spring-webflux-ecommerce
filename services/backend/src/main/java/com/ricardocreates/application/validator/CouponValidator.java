package com.ricardocreates.application.validator;

import com.ricardocreates.domain.entity.exceptions.BadRequestException;
import com.ricardocreates.domain.repository.coupon.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class CouponValidator {
    private final CouponRepository couponRepository;

    public Mono<Boolean> checkDuplicatedName(String name, String excludedId) {
        if (name != null) {
            return couponRepository
                    .existsByName(name, excludedId)
                    .flatMap(exist -> {
                        if (Boolean.TRUE.equals(exist)) {
                            throw new BadRequestException("duplicated name");
                        } else {
                            return Mono.just(true);
                        }
                    });
        } else {
            return Mono.just(true);
        }
    }

    public Mono<Boolean> checkDuplicatedCode(String code, String excludedId) {
        if (code != null) {
            return couponRepository
                    .existsByCode(code, excludedId)
                    .flatMap(exist -> {
                        if (Boolean.TRUE.equals(exist)) {
                            throw new BadRequestException("duplicated code");
                        } else {
                            return Mono.just(true);
                        }
                    });
        } else {
            return Mono.just(true);
        }
    }
}
