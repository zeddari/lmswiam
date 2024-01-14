package com.wiam.lms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CertificateTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Certificate getCertificateSample1() {
        return new Certificate().id(1L).title("title1").miqdar(1);
    }

    public static Certificate getCertificateSample2() {
        return new Certificate().id(2L).title("title2").miqdar(2);
    }

    public static Certificate getCertificateRandomSampleGenerator() {
        return new Certificate().id(longCount.incrementAndGet()).title(UUID.randomUUID().toString()).miqdar(intCount.incrementAndGet());
    }
}
