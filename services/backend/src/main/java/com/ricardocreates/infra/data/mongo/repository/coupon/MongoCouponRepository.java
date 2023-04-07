package com.ricardocreates.infra.data.mongo.repository.coupon;

import com.ricardocreates.domain.entity.coupon.Coupon;
import com.ricardocreates.domain.entity.exceptions.BadRequestException;
import com.ricardocreates.domain.entity.exceptions.NotFoundException;
import com.ricardocreates.domain.repository.coupon.CouponRepository;
import com.ricardocreates.infra.data.mongo.document.coupon.CouponDocument;
import com.ricardocreates.infra.data.mongo.mapper.coupon.CouponDocumentMapper;
import com.ricardocreates.infra.data.mongo.repository.util.OperationMongoRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class MongoCouponRepository implements CouponRepository {
    private final MongoCouponClient mongoCouponClient;
    private final ReactiveMongoTemplate mongoTemplate;
    private final CouponDocumentMapper couponDocumentMapper;

    @Override
    public Mono<Coupon> getById(String id) {
        try {
            return mongoCouponClient.findById(String.valueOf(new ObjectId(id)))
                    .switchIfEmpty(Mono.error(new NotFoundException("Coupon not found by id.")))
                    .map(couponDocumentMapper::toDomain);
        } catch (IllegalArgumentException e) {
            return Mono.error(new BadRequestException(e.getMessage()));
        }
    }

    @Override
    public Flux<Coupon> findAll() {
        Query query = new Query();
        return mongoTemplate.find(query, CouponDocument.class)
                .map(couponDocumentMapper::toDomain)
                .sort(Comparator.comparing(o -> o.getName() != null ? o.getName().toUpperCase() : null, Comparator.nullsFirst(Comparator.naturalOrder())));
    }

    @Override
    public Mono<Coupon> add(Coupon coupon) {
        return mongoCouponClient.save(couponDocumentMapper.fromDomain(coupon))
                .map(couponDocumentMapper::toDomain);
    }

    @Override
    public Mono<Void> remove(String id) {
        try {
            return mongoCouponClient.existsById(String.valueOf(new ObjectId(id)))
                    .flatMap(exist -> !exist ?
                            Mono.error(new NotFoundException("Coupon not found by id."))
                            : mongoCouponClient.deleteById(id).then()
                    );
        } catch (IllegalArgumentException e) {
            return Mono.error(new BadRequestException(e.getMessage()));
        }
    }

    @Override
    public Mono<Coupon> update(Coupon coupon) {
        try {
            return mongoCouponClient.findById(String.valueOf(new ObjectId(coupon.getId())))
                    .switchIfEmpty(Mono.error(new NotFoundException("Not found Coupon to update.")))
                    .flatMap(adminDoc -> {
                                Map<String, Object> mapUpdateCoupon = OperationMongoRepository.mapFromObject(coupon);
                                Update userUpdate = OperationMongoRepository.createUpdateFromMap(mapUpdateCoupon);
                                Query userByIdQuery = OperationMongoRepository.createQueryFindById(coupon.getId());
                                return mongoTemplate.updateFirst(userByIdQuery, userUpdate, CouponDocument.class)
                                        .flatMap(u -> mongoCouponClient.findById(coupon.getId()))
                                        .map(couponDocumentMapper::toDomain);
                            }
                    );
        } catch (IllegalArgumentException e) {
            return Mono.error(new BadRequestException(e.getMessage()));
        }
    }

    @Override
    public Mono<Boolean> existsByName(String name, String excludedId) {
        try {
            return mongoCouponClient.existsByNameAndIdNot(name, excludedId);
        } catch (IllegalArgumentException e) {
            return Mono.error(new BadRequestException(e.getMessage()));
        }
    }

    @Override
    public Mono<Boolean> existsByCode(String code, String excludedId) {
        try {
            return mongoCouponClient.existsByCodeAndIdNot(code, excludedId);
        } catch (IllegalArgumentException e) {
            return Mono.error(new BadRequestException(e.getMessage()));
        }
    }
}
