package com.ricardocreates.infra.server.rest.controller.v1.api.coupon.mapper;

import com.ricardocreates.domain.entity.coupon.Coupon;
import com.ricardocreates.domain.entity.coupon.LocationProduct;
import com.swagger.client.codegen.rest.model.CouponDto;
import com.swagger.client.codegen.rest.model.LocationProductDto;
import org.mapstruct.Mapper;
import reactor.core.publisher.Flux;

@Mapper(componentModel = "spring")
public interface CouponDtoMapper {
    default Flux<CouponDto> fromDomain(Flux<Coupon> coupon) {
        return coupon.map(this::fromDomain);
    }

    Coupon toDomain(CouponDto couponDto);

    CouponDto fromDomain(Coupon coupon);

    LocationProduct locationProductToDomain(LocationProductDto value);
}
