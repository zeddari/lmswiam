package com.wiam.lms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class AyahsTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Ayahs getAyahsSample1() {
        return new Ayahs().id(1).number(1).textdesc("textdesc1").numberInSurah(1).page(1).surahId(1).hizbId(1).juzId(1);
    }

    public static Ayahs getAyahsSample2() {
        return new Ayahs().id(2).number(2).textdesc("textdesc2").numberInSurah(2).page(2).surahId(2).hizbId(2).juzId(2);
    }

    public static Ayahs getAyahsRandomSampleGenerator() {
        return new Ayahs()
            .id(intCount.incrementAndGet())
            .number(intCount.incrementAndGet())
            .textdesc(UUID.randomUUID().toString())
            .numberInSurah(intCount.incrementAndGet())
            .page(intCount.incrementAndGet())
            .surahId(intCount.incrementAndGet())
            .hizbId(intCount.incrementAndGet())
            .juzId(intCount.incrementAndGet());
    }
}
