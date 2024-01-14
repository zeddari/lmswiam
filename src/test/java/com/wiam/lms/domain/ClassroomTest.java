package com.wiam.lms.domain;

import static com.wiam.lms.domain.ClassroomTestSamples.*;
import static com.wiam.lms.domain.SessionTestSamples.*;
import static com.wiam.lms.domain.SiteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.wiam.lms.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ClassroomTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Classroom.class);
        Classroom classroom1 = getClassroomSample1();
        Classroom classroom2 = new Classroom();
        assertThat(classroom1).isNotEqualTo(classroom2);

        classroom2.setId(classroom1.getId());
        assertThat(classroom1).isEqualTo(classroom2);

        classroom2 = getClassroomSample2();
        assertThat(classroom1).isNotEqualTo(classroom2);
    }

    @Test
    void siteTest() throws Exception {
        Classroom classroom = getClassroomRandomSampleGenerator();
        Site siteBack = getSiteRandomSampleGenerator();

        classroom.setSite(siteBack);
        assertThat(classroom.getSite()).isEqualTo(siteBack);

        classroom.site(null);
        assertThat(classroom.getSite()).isNull();
    }

    @Test
    void sessions6Test() throws Exception {
        Classroom classroom = getClassroomRandomSampleGenerator();
        Session sessionBack = getSessionRandomSampleGenerator();

        classroom.addSessions6(sessionBack);
        assertThat(classroom.getSessions6s()).containsOnly(sessionBack);
        assertThat(sessionBack.getClassrooms()).containsOnly(classroom);

        classroom.removeSessions6(sessionBack);
        assertThat(classroom.getSessions6s()).doesNotContain(sessionBack);
        assertThat(sessionBack.getClassrooms()).doesNotContain(classroom);

        classroom.sessions6s(new HashSet<>(Set.of(sessionBack)));
        assertThat(classroom.getSessions6s()).containsOnly(sessionBack);
        assertThat(sessionBack.getClassrooms()).containsOnly(classroom);

        classroom.setSessions6s(new HashSet<>());
        assertThat(classroom.getSessions6s()).doesNotContain(sessionBack);
        assertThat(sessionBack.getClassrooms()).doesNotContain(classroom);
    }
}
