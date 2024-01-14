package com.wiam.lms.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class QuizResultTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static QuizResult getQuizResultSample1() {
        return new QuizResult().id(1L);
    }

    public static QuizResult getQuizResultSample2() {
        return new QuizResult().id(2L);
    }

    public static QuizResult getQuizResultRandomSampleGenerator() {
        return new QuizResult().id(longCount.incrementAndGet());
    }
}
