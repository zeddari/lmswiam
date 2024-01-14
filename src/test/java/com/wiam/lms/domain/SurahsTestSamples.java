package com.wiam.lms.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class SurahsTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Surahs getSurahsSample1() {
        return new Surahs().id(1).number(1);
    }

    public static Surahs getSurahsSample2() {
        return new Surahs().id(2).number(2);
    }

    public static Surahs getSurahsRandomSampleGenerator() {
        return new Surahs().id(intCount.incrementAndGet()).number(intCount.incrementAndGet());
    }
}
