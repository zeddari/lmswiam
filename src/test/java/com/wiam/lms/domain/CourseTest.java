package com.wiam.lms.domain;

import static com.wiam.lms.domain.CourseTestSamples.*;
import static com.wiam.lms.domain.EnrolementTestSamples.*;
import static com.wiam.lms.domain.PartTestSamples.*;
import static com.wiam.lms.domain.SiteTestSamples.*;
import static com.wiam.lms.domain.TopicTestSamples.*;
import static com.wiam.lms.domain.UserCustomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.wiam.lms.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CourseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Course.class);
        Course course1 = getCourseSample1();
        Course course2 = new Course();
        assertThat(course1).isNotEqualTo(course2);

        course2.setId(course1.getId());
        assertThat(course1).isEqualTo(course2);

        course2 = getCourseSample2();
        assertThat(course1).isNotEqualTo(course2);
    }

    @Test
    void partTest() throws Exception {
        Course course = getCourseRandomSampleGenerator();
        Part partBack = getPartRandomSampleGenerator();

        course.addPart(partBack);
        assertThat(course.getParts()).containsOnly(partBack);
        assertThat(partBack.getCourse()).isEqualTo(course);

        course.removePart(partBack);
        assertThat(course.getParts()).doesNotContain(partBack);
        assertThat(partBack.getCourse()).isNull();

        course.parts(new HashSet<>(Set.of(partBack)));
        assertThat(course.getParts()).containsOnly(partBack);
        assertThat(partBack.getCourse()).isEqualTo(course);

        course.setParts(new HashSet<>());
        assertThat(course.getParts()).doesNotContain(partBack);
        assertThat(partBack.getCourse()).isNull();
    }

    @Test
    void enrolementTest() throws Exception {
        Course course = getCourseRandomSampleGenerator();
        Enrolement enrolementBack = getEnrolementRandomSampleGenerator();

        course.addEnrolement(enrolementBack);
        assertThat(course.getEnrolements()).containsOnly(enrolementBack);
        assertThat(enrolementBack.getCourse()).isEqualTo(course);

        course.removeEnrolement(enrolementBack);
        assertThat(course.getEnrolements()).doesNotContain(enrolementBack);
        assertThat(enrolementBack.getCourse()).isNull();

        course.enrolements(new HashSet<>(Set.of(enrolementBack)));
        assertThat(course.getEnrolements()).containsOnly(enrolementBack);
        assertThat(enrolementBack.getCourse()).isEqualTo(course);

        course.setEnrolements(new HashSet<>());
        assertThat(course.getEnrolements()).doesNotContain(enrolementBack);
        assertThat(enrolementBack.getCourse()).isNull();
    }

    @Test
    void professorsTest() throws Exception {
        Course course = getCourseRandomSampleGenerator();
        UserCustom userCustomBack = getUserCustomRandomSampleGenerator();

        course.addProfessors(userCustomBack);
        assertThat(course.getProfessors()).containsOnly(userCustomBack);

        course.removeProfessors(userCustomBack);
        assertThat(course.getProfessors()).doesNotContain(userCustomBack);

        course.professors(new HashSet<>(Set.of(userCustomBack)));
        assertThat(course.getProfessors()).containsOnly(userCustomBack);

        course.setProfessors(new HashSet<>());
        assertThat(course.getProfessors()).doesNotContain(userCustomBack);
    }

    @Test
    void site1Test() throws Exception {
        Course course = getCourseRandomSampleGenerator();
        Site siteBack = getSiteRandomSampleGenerator();

        course.setSite1(siteBack);
        assertThat(course.getSite1()).isEqualTo(siteBack);

        course.site1(null);
        assertThat(course.getSite1()).isNull();
    }

    @Test
    void topic3Test() throws Exception {
        Course course = getCourseRandomSampleGenerator();
        Topic topicBack = getTopicRandomSampleGenerator();

        course.setTopic3(topicBack);
        assertThat(course.getTopic3()).isEqualTo(topicBack);

        course.topic3(null);
        assertThat(course.getTopic3()).isNull();
    }
}
