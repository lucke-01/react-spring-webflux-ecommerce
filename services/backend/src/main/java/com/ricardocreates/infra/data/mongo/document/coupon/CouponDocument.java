package com.ricardocreates.infra.data.mongo.document.coupon;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.List;

@Document("coupon")
@Builder(toBuilder = true)
@Data
public class CouponDocument {
    @Id
    @Field("_id")
    private ObjectId id;

    private String name;

    private String code;

    private Boolean active;

    private String activeFrom;

    private String activeTo;

    private BigDecimal discount;

    private DiscountTypeDocument discountType;

    private DayOfWeekDocument dayOfWeek;

    private String shortDescription;

    private List<LocationProductDocument> locationProducts;
}
