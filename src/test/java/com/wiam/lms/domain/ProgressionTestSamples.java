package com.wiam.lms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProgressionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Progression getProgressionSample1() {
        return new Progression().id(1L).justifRef("justifRef1").fromAyaNum(1).toAyaNum(1).tajweedScore(1).hifdScore(1).adaeScore(1);
    }

    public static Progression getProgressionSample2() {
        return new Progression().id(2L).justifRef("justifRef2").fromAyaNum(2).toAyaNum(2).tajweedScore(2).hifdScore(2).adaeScore(2);
    }

    public static Progression getProgressionRandomSampleGenerator() {
        return new Progression()
            .id(longCount.incrementAndGet())
            .justifRef(UUID.randomUUID().toString())
            .fromAyaNum(intCount.incrementAndGet())
            .toAyaNum(intCount.incrementAndGet())
            .tajweedScore(intCount.incrementAndGet())
            .hifdScore(intCount.incrementAndGet())
            .adaeScore(intCount.incrementAndGet());
    }
}
