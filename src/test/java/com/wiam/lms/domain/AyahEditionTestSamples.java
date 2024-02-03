package com.wiam.lms.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class AyahEditionTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AyahEdition getAyahEditionSample1() {
        return new AyahEdition().id(1).ayahId(1).editionId(1);
    }

    public static AyahEdition getAyahEditionSample2() {
        return new AyahEdition().id(2).ayahId(2).editionId(2);
    }

    public static AyahEdition getAyahEditionRandomSampleGenerator() {
        return new AyahEdition().id(intCount.incrementAndGet()).ayahId(intCount.incrementAndGet()).editionId(intCount.incrementAndGet());
    }
}
