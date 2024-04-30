package com.wiam.lms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CommentsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Comments getCommentsSample1() {
        return new Comments().id(1L).pseudoName("pseudoName1").title("title1");
    }

    public static Comments getCommentsSample2() {
        return new Comments().id(2L).pseudoName("pseudoName2").title("title2");
    }

    public static Comments getCommentsRandomSampleGenerator() {
        return new Comments().id(longCount.incrementAndGet()).pseudoName(UUID.randomUUID().toString()).title(UUID.randomUUID().toString());
    }
}
