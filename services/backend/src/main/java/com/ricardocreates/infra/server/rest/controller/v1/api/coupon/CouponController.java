package com.ricardocreates.infra.server.rest.controller.v1.api.coupon;

import com.ricardocreates.domain.usecase.coupon.AddCoupon;
import com.ricardocreates.domain.usecase.coupon.UpdateCoupon;
import com.ricardocreates.infra.server.rest.controller.v1.api.coupon.mapper.CouponDtoMapper;
import com.ricardocreates.domain.usecase.coupon.GetCoupon;
import com.ricardocreates.domain.usecase.coupon.RemoveCoupon;
import com.ricardocreates.infra.server.rest.controller.v1.utils.HttpUtil;
import com.swagger.client.codegen.rest.CouponApi;
import com.swagger.client.codegen.rest.model.CouponDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CouponController implements CouponApi {
    private final AddCoupon addCoupon;
    private final GetCoupon getCoupon;
    private final RemoveCoupon removeCoupon;
    private final UpdateCoupon updateCoupon;
    private final CouponDtoMapper couponDtoMapper;
    private final HttpUtil httpUtil;

    @PreAuthorize("hasAnyRole('ADMIN', 'ADMIN_ORGANISATION','MANAGEMENT')")
    public Mono<ResponseEntity<CouponDto>> addCoupon(Mono<CouponDto> couponDto, ServerWebExchange exchange) {
        String login = httpUtil.getLogin(exchange);
        return couponDto.map(couponDtoMapper::toDomain)
                .flatMap(coupon -> addCoupon.add(coupon, login))
                .map(couponDtoMapper::fromDomain)
                .map(ResponseEntity::ok);
    }

    public Mono<ResponseEntity<Flux<CouponDto>>> findCoupon(ServerWebExchange exchange) {
        String login = httpUtil.getLogin(exchange);
        return Mono.just(getCoupon.findAll(login))
                .map(couponDtoMapper::fromDomain)
                .map(ResponseEntity::ok);
    }

    public Mono<ResponseEntity<CouponDto>> getCoupon(String id, ServerWebExchange exchange) {
        String login = httpUtil.getLogin(exchange);
        return getCoupon.get(id, login)
                .map(couponDtoMapper::fromDomain)
                .map(ResponseEntity::ok);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ADMIN_ORGANISATION','MANAGEMENT')")
    public Mono<ResponseEntity<Void>> removeCoupon(String id, ServerWebExchange exchange) {
        String login = httpUtil.getLogin(exchange);
        return removeCoupon.remove(id, login)
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ADMIN_ORGANISATION','MANAGEMENT')")
    public Mono<ResponseEntity<CouponDto>> updateCoupon(Mono<CouponDto> updateCouponDto, ServerWebExchange exchange) {
        String login = httpUtil.getLogin(exchange);
        return updateCouponDto.map(couponDtoMapper::toDomain)
                .flatMap(coupon -> this.updateCoupon.update(coupon, login))
                .map(couponDtoMapper::fromDomain)
                .map(ResponseEntity::ok);
    }
}
