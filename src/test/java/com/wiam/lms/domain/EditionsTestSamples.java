package com.wiam.lms.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class EditionsTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Editions getEditionsSample1() {
        return new Editions().id(1);
    }

    public static Editions getEditionsSample2() {
        return new Editions().id(2);
    }

    public static Editions getEditionsRandomSampleGenerator() {
        return new Editions().id(intCount.incrementAndGet());
    }
}
