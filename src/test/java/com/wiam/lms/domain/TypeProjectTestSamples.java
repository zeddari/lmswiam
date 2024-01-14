package com.wiam.lms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TypeProjectTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TypeProject getTypeProjectSample1() {
        return new TypeProject().id(1L).nameAr("nameAr1").nameLat("nameLat1");
    }

    public static TypeProject getTypeProjectSample2() {
        return new TypeProject().id(2L).nameAr("nameAr2").nameLat("nameLat2");
    }

    public static TypeProject getTypeProjectRandomSampleGenerator() {
        return new TypeProject().id(longCount.incrementAndGet()).nameAr(UUID.randomUUID().toString()).nameLat(UUID.randomUUID().toString());
    }
}
