package com.wiam.lms.domain;

import static com.wiam.lms.domain.CertificateTestSamples.*;
import static com.wiam.lms.domain.GroupTestSamples.*;
import static com.wiam.lms.domain.GroupTestSamples.*;
import static com.wiam.lms.domain.QuizTestSamples.*;
import static com.wiam.lms.domain.SessionTestSamples.*;
import static com.wiam.lms.domain.UserCustomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.wiam.lms.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class GroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Group.class);
        Group group1 = getGroupSample1();
        Group group2 = new Group();
        assertThat(group1).isNotEqualTo(group2);

        group2.setId(group1.getId());
        assertThat(group1).isEqualTo(group2);

        group2 = getGroupSample2();
        assertThat(group1).isNotEqualTo(group2);
    }

    @Test
    void certificateTest() throws Exception {
        Group group = getGroupRandomSampleGenerator();
        Certificate certificateBack = getCertificateRandomSampleGenerator();

        group.addCertificate(certificateBack);
        assertThat(group.getCertificates()).containsOnly(certificateBack);
        assertThat(certificateBack.getComitte()).isEqualTo(group);

        group.removeCertificate(certificateBack);
        assertThat(group.getCertificates()).doesNotContain(certificateBack);
        assertThat(certificateBack.getComitte()).isNull();

        group.certificates(new HashSet<>(Set.of(certificateBack)));
        assertThat(group.getCertificates()).containsOnly(certificateBack);
        assertThat(certificateBack.getComitte()).isEqualTo(group);

        group.setCertificates(new HashSet<>());
        assertThat(group.getCertificates()).doesNotContain(certificateBack);
        assertThat(certificateBack.getComitte()).isNull();
    }

    @Test
    void groupTest() throws Exception {
        Group group = getGroupRandomSampleGenerator();
        Group groupBack = getGroupRandomSampleGenerator();

        group.addGroup(groupBack);
        assertThat(group.getGroups()).containsOnly(groupBack);
        assertThat(groupBack.getGroup1()).isEqualTo(group);

        group.removeGroup(groupBack);
        assertThat(group.getGroups()).doesNotContain(groupBack);
        assertThat(groupBack.getGroup1()).isNull();

        group.groups(new HashSet<>(Set.of(groupBack)));
        assertThat(group.getGroups()).containsOnly(groupBack);
        assertThat(groupBack.getGroup1()).isEqualTo(group);

        group.setGroups(new HashSet<>());
        assertThat(group.getGroups()).doesNotContain(groupBack);
        assertThat(groupBack.getGroup1()).isNull();
    }

    @Test
    void elementsTest() throws Exception {
        Group group = getGroupRandomSampleGenerator();
        UserCustom userCustomBack = getUserCustomRandomSampleGenerator();

        group.addElements(userCustomBack);
        assertThat(group.getElements()).containsOnly(userCustomBack);

        group.removeElements(userCustomBack);
        assertThat(group.getElements()).doesNotContain(userCustomBack);

        group.elements(new HashSet<>(Set.of(userCustomBack)));
        assertThat(group.getElements()).containsOnly(userCustomBack);

        group.setElements(new HashSet<>());
        assertThat(group.getElements()).doesNotContain(userCustomBack);
    }

    @Test
    void group1Test() throws Exception {
        Group group = getGroupRandomSampleGenerator();
        Group groupBack = getGroupRandomSampleGenerator();

        group.setGroup1(groupBack);
        assertThat(group.getGroup1()).isEqualTo(groupBack);

        group.group1(null);
        assertThat(group.getGroup1()).isNull();
    }

    @Test
    void quizTest() throws Exception {
        Group group = getGroupRandomSampleGenerator();
        Quiz quizBack = getQuizRandomSampleGenerator();

        group.addQuiz(quizBack);
        assertThat(group.getQuizzes()).containsOnly(quizBack);
        assertThat(quizBack.getGroups()).containsOnly(group);

        group.removeQuiz(quizBack);
        assertThat(group.getQuizzes()).doesNotContain(quizBack);
        assertThat(quizBack.getGroups()).doesNotContain(group);

        group.quizzes(new HashSet<>(Set.of(quizBack)));
        assertThat(group.getQuizzes()).containsOnly(quizBack);
        assertThat(quizBack.getGroups()).containsOnly(group);

        group.setQuizzes(new HashSet<>());
        assertThat(group.getQuizzes()).doesNotContain(quizBack);
        assertThat(quizBack.getGroups()).doesNotContain(group);
    }

    @Test
    void sessions5Test() throws Exception {
        Group group = getGroupRandomSampleGenerator();
        Session sessionBack = getSessionRandomSampleGenerator();

        group.addSessions5(sessionBack);
        assertThat(group.getSessions5s()).containsOnly(sessionBack);
        assertThat(sessionBack.getGroups()).containsOnly(group);

        group.removeSessions5(sessionBack);
        assertThat(group.getSessions5s()).doesNotContain(sessionBack);
        assertThat(sessionBack.getGroups()).doesNotContain(group);

        group.sessions5s(new HashSet<>(Set.of(sessionBack)));
        assertThat(group.getSessions5s()).containsOnly(sessionBack);
        assertThat(sessionBack.getGroups()).containsOnly(group);

        group.setSessions5s(new HashSet<>());
        assertThat(group.getSessions5s()).doesNotContain(sessionBack);
        assertThat(sessionBack.getGroups()).doesNotContain(group);
    }
}
