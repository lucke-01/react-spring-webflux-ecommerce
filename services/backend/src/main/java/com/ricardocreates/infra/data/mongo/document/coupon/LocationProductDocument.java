package com.ricardocreates.infra.data.mongo.document.coupon;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder(toBuilder = true)
@Data
public class LocationProductDocument {
    private String idLocation;

    private List<String> idProducts;

    private Boolean allProducts;
}
