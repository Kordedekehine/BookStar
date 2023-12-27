package com.artistbooking.BookArtist.enums;

import lombok.Getter;

@Getter
public enum PricingPlanType {

    BASIC("Basic"),
    STANDARD("Standard"),
    PREMIUM("Premium");

    private final String value;
    PricingPlanType(String value) {
        this.value = value;
    }

}