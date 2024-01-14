package com.wiam.lms.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class JuzsTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Juzs getJuzsSample1() {
        return new Juzs().id(1);
    }

    public static Juzs getJuzsSample2() {
        return new Juzs().id(2);
    }

    public static Juzs getJuzsRandomSampleGenerator() {
        return new Juzs().id(intCount.incrementAndGet());
    }
}
