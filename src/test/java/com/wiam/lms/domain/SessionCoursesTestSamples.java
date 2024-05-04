package com.wiam.lms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SessionCoursesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SessionCourses getSessionCoursesSample1() {
        return new SessionCourses().id(1L).title("title1").resourceLink("resourceLink1");
    }

    public static SessionCourses getSessionCoursesSample2() {
        return new SessionCourses().id(2L).title("title2").resourceLink("resourceLink2");
    }

    public static SessionCourses getSessionCoursesRandomSampleGenerator() {
        return new SessionCourses()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .resourceLink(UUID.randomUUID().toString());
    }
}
