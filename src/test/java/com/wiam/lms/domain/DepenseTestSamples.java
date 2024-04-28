package com.wiam.lms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DepenseTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Depense getDepenseSample1() {
        return new Depense().id(1L).ref("ref1").message("message1");
    }

    public static Depense getDepenseSample2() {
        return new Depense().id(2L).ref("ref2").message("message2");
    }

    public static Depense getDepenseRandomSampleGenerator() {
        return new Depense().id(longCount.incrementAndGet()).ref(UUID.randomUUID().toString()).message(UUID.randomUUID().toString());
    }
}
