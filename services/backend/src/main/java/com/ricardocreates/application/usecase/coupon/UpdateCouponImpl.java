package com.ricardocreates.application.usecase.coupon;

import com.ricardocreates.application.validator.CouponValidator;
import com.ricardocreates.domain.entity.coupon.Coupon;
import com.ricardocreates.domain.repository.coupon.CouponRepository;
import com.ricardocreates.domain.usecase.coupon.UpdateCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class UpdateCouponImpl implements UpdateCoupon {

    private final CouponRepository couponRepository;
    private final CouponValidator couponValidator;

    @Override
    public Mono<Coupon> update(Coupon coupon, String login) {
        return this.couponValidator.checkDuplicatedName(coupon.getName(), coupon.getId())
                .flatMap(result -> this.couponValidator.checkDuplicatedCode(coupon.getCode(), coupon.getId()))
                .flatMap(result -> couponRepository.update(coupon));
    }
}
