package com.wiam.lms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SponsoringTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Sponsoring getSponsoringSample1() {
        return new Sponsoring().id(1L).ref("ref1");
    }

    public static Sponsoring getSponsoringSample2() {
        return new Sponsoring().id(2L).ref("ref2");
    }

    public static Sponsoring getSponsoringRandomSampleGenerator() {
        return new Sponsoring().id(longCount.incrementAndGet()).ref(UUID.randomUUID().toString());
    }
}
