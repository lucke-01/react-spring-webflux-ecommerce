package com.ricardocreates.infra.data.mongo.document.coupon;

import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class DayOfWeekDocument {
    @Builder.Default
    private Boolean monday = false;
    @Builder.Default
    private Boolean tuesday = false;
    @Builder.Default
    private Boolean wednesday = false;
    @Builder.Default
    private Boolean thursday = false;
    @Builder.Default
    private Boolean friday = false;
    @Builder.Default
    private Boolean saturday = false;
    @Builder.Default
    private Boolean sunday = false;
}
