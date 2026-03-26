package com.uplift.backend.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FareCalculator {

    // simple base + per-km rule for example
    public static BigDecimal calculateInstantFare(double distanceKm,
                                                  BigDecimal baseFare,
                                                  BigDecimal perKmRate) {
        BigDecimal d = BigDecimal.valueOf(distanceKm);
        BigDecimal fare = baseFare.add(perKmRate.multiply(d));
        return fare.setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal calculateScheduledSeatPrice(BigDecimal baseSeatPrice,
                                                         int seats) {
        return baseSeatPrice
                .multiply(BigDecimal.valueOf(seats))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
