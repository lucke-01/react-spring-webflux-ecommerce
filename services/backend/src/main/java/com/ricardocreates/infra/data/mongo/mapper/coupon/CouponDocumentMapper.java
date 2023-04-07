package com.ricardocreates.infra.data.mongo.mapper.coupon;

import com.ricardocreates.infra.data.mongo.document.coupon.CouponDocument;
import com.ricardocreates.domain.entity.coupon.Coupon;
import com.ricardocreates.domain.entity.coupon.LocationProduct;
import com.ricardocreates.infra.data.mongo.document.coupon.LocationProductDocument;
import com.ricardocreates.infra.data.mongo.mapper.UtilsMapper;
import org.mapstruct.Mapper;
import reactor.core.publisher.Flux;

@Mapper(componentModel = "spring", uses = UtilsMapper.class)
public interface CouponDocumentMapper {

    CouponDocument fromDomain(Coupon coupon);

    default Flux<Coupon> toDomain(Flux<CouponDocument> couponDocument) {
        return couponDocument.hasElements()
                .flatMapMany(exist -> !exist ? Flux.empty() : couponDocument.map(this::toDomain));
    }

    Coupon toDomain(CouponDocument couponDocument);

    LocationProductDocument map(LocationProduct value);

    LocationProduct map(LocationProductDocument value);
}
