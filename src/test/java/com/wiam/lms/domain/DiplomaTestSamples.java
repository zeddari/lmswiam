package com.wiam.lms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DiplomaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Diploma getDiplomaSample1() {
        return new Diploma().id(1L).title("title1").grade("grade1");
    }

    public static Diploma getDiplomaSample2() {
        return new Diploma().id(2L).title("title2").grade("grade2");
    }

    public static Diploma getDiplomaRandomSampleGenerator() {
        return new Diploma().id(longCount.incrementAndGet()).title(UUID.randomUUID().toString()).grade(UUID.randomUUID().toString());
    }
}
