package com.wiam.lms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SessionInstanceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SessionInstance getSessionInstanceSample1() {
        return new SessionInstance().id(1L).title("title1").duration(1).justifRef("justifRef1");
    }

    public static SessionInstance getSessionInstanceSample2() {
        return new SessionInstance().id(2L).title("title2").duration(2).justifRef("justifRef2");
    }

    public static SessionInstance getSessionInstanceRandomSampleGenerator() {
        return new SessionInstance()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .duration(intCount.incrementAndGet())
            .justifRef(UUID.randomUUID().toString());
    }
}
