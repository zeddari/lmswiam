package com.wiam.lms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StudentSponsoringTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static StudentSponsoring getStudentSponsoringSample1() {
        return new StudentSponsoring().id(1L).ref("ref1");
    }

    public static StudentSponsoring getStudentSponsoringSample2() {
        return new StudentSponsoring().id(2L).ref("ref2");
    }

    public static StudentSponsoring getStudentSponsoringRandomSampleGenerator() {
        return new StudentSponsoring().id(longCount.incrementAndGet()).ref(UUID.randomUUID().toString());
    }
}
