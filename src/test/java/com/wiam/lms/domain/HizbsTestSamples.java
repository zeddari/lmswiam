package com.wiam.lms.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class HizbsTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Hizbs getHizbsSample1() {
        return new Hizbs().id(1);
    }

    public static Hizbs getHizbsSample2() {
        return new Hizbs().id(2);
    }

    public static Hizbs getHizbsRandomSampleGenerator() {
        return new Hizbs().id(intCount.incrementAndGet());
    }
}
