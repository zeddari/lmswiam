package com.wiam.lms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NationalityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Nationality getNationalitySample1() {
        return new Nationality().id(1L).nameAr("nameAr1").nameLat("nameLat1");
    }

    public static Nationality getNationalitySample2() {
        return new Nationality().id(2L).nameAr("nameAr2").nameLat("nameLat2");
    }

    public static Nationality getNationalityRandomSampleGenerator() {
        return new Nationality().id(longCount.incrementAndGet()).nameAr(UUID.randomUUID().toString()).nameLat(UUID.randomUUID().toString());
    }
}
