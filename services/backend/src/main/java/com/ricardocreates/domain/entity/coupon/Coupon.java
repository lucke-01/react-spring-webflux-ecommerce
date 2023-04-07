package com.ricardocreates.domain.entity.coupon;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Builder(toBuilder = true)
@Data
public class Coupon {
    private String id;

    private String name;

    private String code;

    private Boolean active;

    private String activeFrom;

    private String activeTo;

    private BigDecimal discount;

    private DiscountType discountType;

    private DayOfWeek dayOfWeek;

    private String shortDescription;

    private List<LocationProduct> locationProducts;
}
