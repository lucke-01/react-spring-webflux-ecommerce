package com.ricardocreates.domain.entity.coupon;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder(toBuilder = true)
@Data
public class LocationProduct {
    private String idLocation;

    private List<String> idProducts;

    private Boolean allProducts;
}
