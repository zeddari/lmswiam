package com.wiam.lms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class LanguageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Language getLanguageSample1() {
        return new Language().id(1L).nameAr("nameAr1").nameLat("nameLat1").code("code1");
    }

    public static Language getLanguageSample2() {
        return new Language().id(2L).nameAr("nameAr2").nameLat("nameLat2").code("code2");
    }

    public static Language getLanguageRandomSampleGenerator() {
        return new Language()
            .id(longCount.incrementAndGet())
            .nameAr(UUID.randomUUID().toString())
            .nameLat(UUID.randomUUID().toString())
            .code(UUID.randomUUID().toString());
    }
}
