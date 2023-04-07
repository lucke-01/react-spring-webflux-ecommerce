package com.ricardocreates.application.usecase.coupon;

import com.ricardocreates.application.validator.CouponValidator;
import com.ricardocreates.domain.entity.coupon.Coupon;
import com.ricardocreates.domain.repository.coupon.CouponRepository;
import com.ricardocreates.domain.usecase.coupon.AddCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class AddCouponImpl implements AddCoupon {
    private final CouponRepository couponRepository;
    private final CouponValidator couponValidator;

    @Override
    public Mono<Coupon> add(Coupon coupon, String login) {
        return this.couponValidator.checkDuplicatedName(coupon.getName(), null)
                .flatMap(result -> this.couponValidator.checkDuplicatedCode(coupon.getCode(), null))
                .flatMap(result -> couponRepository.add(coupon));
    }

}
