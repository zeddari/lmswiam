package com.wiam.lms.domain;

import static com.wiam.lms.domain.SessionCoursesTestSamples.*;
import static com.wiam.lms.domain.SessionInstanceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.wiam.lms.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SessionCoursesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SessionCourses.class);
        SessionCourses sessionCourses1 = getSessionCoursesSample1();
        SessionCourses sessionCourses2 = new SessionCourses();
        assertThat(sessionCourses1).isNotEqualTo(sessionCourses2);

        sessionCourses2.setId(sessionCourses1.getId());
        assertThat(sessionCourses1).isEqualTo(sessionCourses2);

        sessionCourses2 = getSessionCoursesSample2();
        assertThat(sessionCourses1).isNotEqualTo(sessionCourses2);
    }

    @Test
    void sessionsInstance5Test() throws Exception {
        SessionCourses sessionCourses = getSessionCoursesRandomSampleGenerator();
        SessionInstance sessionInstanceBack = getSessionInstanceRandomSampleGenerator();

        sessionCourses.addSessionsInstance5(sessionInstanceBack);
        assertThat(sessionCourses.getSessionsInstance5s()).containsOnly(sessionInstanceBack);
        assertThat(sessionInstanceBack.getCourses()).containsOnly(sessionCourses);

        sessionCourses.removeSessionsInstance5(sessionInstanceBack);
        assertThat(sessionCourses.getSessionsInstance5s()).doesNotContain(sessionInstanceBack);
        assertThat(sessionInstanceBack.getCourses()).doesNotContain(sessionCourses);

        sessionCourses.sessionsInstance5s(new HashSet<>(Set.of(sessionInstanceBack)));
        assertThat(sessionCourses.getSessionsInstance5s()).containsOnly(sessionInstanceBack);
        assertThat(sessionInstanceBack.getCourses()).containsOnly(sessionCourses);

        sessionCourses.setSessionsInstance5s(new HashSet<>());
        assertThat(sessionCourses.getSessionsInstance5s()).doesNotContain(sessionInstanceBack);
        assertThat(sessionInstanceBack.getCourses()).doesNotContain(sessionCourses);
    }
}
