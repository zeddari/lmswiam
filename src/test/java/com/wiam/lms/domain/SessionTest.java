package com.wiam.lms.domain;

import static com.wiam.lms.domain.ClassroomTestSamples.*;
import static com.wiam.lms.domain.GroupTestSamples.*;
import static com.wiam.lms.domain.PaymentTestSamples.*;
import static com.wiam.lms.domain.SessionInstanceTestSamples.*;
import static com.wiam.lms.domain.SessionLinkTestSamples.*;
import static com.wiam.lms.domain.SessionTestSamples.*;
import static com.wiam.lms.domain.SiteTestSamples.*;
import static com.wiam.lms.domain.UserCustomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.wiam.lms.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SessionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Session.class);
        Session session1 = getSessionSample1();
        Session session2 = new Session();
        assertThat(session1).isNotEqualTo(session2);

        session2.setId(session1.getId());
        assertThat(session1).isEqualTo(session2);

        session2 = getSessionSample2();
        assertThat(session1).isNotEqualTo(session2);
    }

    @Test
    void sessionInstanceTest() throws Exception {
        Session session = getSessionRandomSampleGenerator();
        SessionInstance sessionInstanceBack = getSessionInstanceRandomSampleGenerator();

        session.addSessionInstance(sessionInstanceBack);
        assertThat(session.getSessionInstances()).containsOnly(sessionInstanceBack);
        assertThat(sessionInstanceBack.getSession1()).isEqualTo(session);

        session.removeSessionInstance(sessionInstanceBack);
        assertThat(session.getSessionInstances()).doesNotContain(sessionInstanceBack);
        assertThat(sessionInstanceBack.getSession1()).isNull();

        session.sessionInstances(new HashSet<>(Set.of(sessionInstanceBack)));
        assertThat(session.getSessionInstances()).containsOnly(sessionInstanceBack);
        assertThat(sessionInstanceBack.getSession1()).isEqualTo(session);

        session.setSessionInstances(new HashSet<>());
        assertThat(session.getSessionInstances()).doesNotContain(sessionInstanceBack);
        assertThat(sessionInstanceBack.getSession1()).isNull();
    }

    @Test
    void paymentTest() throws Exception {
        Session session = getSessionRandomSampleGenerator();
        Payment paymentBack = getPaymentRandomSampleGenerator();

        session.addPayment(paymentBack);
        assertThat(session.getPayments()).containsOnly(paymentBack);
        assertThat(paymentBack.getSession()).isEqualTo(session);

        session.removePayment(paymentBack);
        assertThat(session.getPayments()).doesNotContain(paymentBack);
        assertThat(paymentBack.getSession()).isNull();

        session.payments(new HashSet<>(Set.of(paymentBack)));
        assertThat(session.getPayments()).containsOnly(paymentBack);
        assertThat(paymentBack.getSession()).isEqualTo(session);

        session.setPayments(new HashSet<>());
        assertThat(session.getPayments()).doesNotContain(paymentBack);
        assertThat(paymentBack.getSession()).isNull();
    }

    @Test
    void classroomsTest() throws Exception {
        Session session = getSessionRandomSampleGenerator();
        Classroom classroomBack = getClassroomRandomSampleGenerator();

        session.addClassrooms(classroomBack);
        assertThat(session.getClassrooms()).containsOnly(classroomBack);

        session.removeClassrooms(classroomBack);
        assertThat(session.getClassrooms()).doesNotContain(classroomBack);

        session.classrooms(new HashSet<>(Set.of(classroomBack)));
        assertThat(session.getClassrooms()).containsOnly(classroomBack);

        session.setClassrooms(new HashSet<>());
        assertThat(session.getClassrooms()).doesNotContain(classroomBack);
    }

    @Test
    void groupsTest() throws Exception {
        Session session = getSessionRandomSampleGenerator();
        Group groupBack = getGroupRandomSampleGenerator();

        session.addGroups(groupBack);
        assertThat(session.getGroups()).containsOnly(groupBack);

        session.removeGroups(groupBack);
        assertThat(session.getGroups()).doesNotContain(groupBack);

        session.groups(new HashSet<>(Set.of(groupBack)));
        assertThat(session.getGroups()).containsOnly(groupBack);

        session.setGroups(new HashSet<>());
        assertThat(session.getGroups()).doesNotContain(groupBack);
    }

    @Test
    void professorsTest() throws Exception {
        Session session = getSessionRandomSampleGenerator();
        UserCustom userCustomBack = getUserCustomRandomSampleGenerator();

        session.addProfessors(userCustomBack);
        assertThat(session.getProfessors()).containsOnly(userCustomBack);

        session.removeProfessors(userCustomBack);
        assertThat(session.getProfessors()).doesNotContain(userCustomBack);

        session.professors(new HashSet<>(Set.of(userCustomBack)));
        assertThat(session.getProfessors()).containsOnly(userCustomBack);

        session.setProfessors(new HashSet<>());
        assertThat(session.getProfessors()).doesNotContain(userCustomBack);
    }

    @Test
    void employeesTest() throws Exception {
        Session session = getSessionRandomSampleGenerator();
        UserCustom userCustomBack = getUserCustomRandomSampleGenerator();

        session.addEmployees(userCustomBack);
        assertThat(session.getEmployees()).containsOnly(userCustomBack);

        session.removeEmployees(userCustomBack);
        assertThat(session.getEmployees()).doesNotContain(userCustomBack);

        session.employees(new HashSet<>(Set.of(userCustomBack)));
        assertThat(session.getEmployees()).containsOnly(userCustomBack);

        session.setEmployees(new HashSet<>());
        assertThat(session.getEmployees()).doesNotContain(userCustomBack);
    }

    @Test
    void linksTest() throws Exception {
        Session session = getSessionRandomSampleGenerator();
        SessionLink sessionLinkBack = getSessionLinkRandomSampleGenerator();

        session.addLinks(sessionLinkBack);
        assertThat(session.getLinks()).containsOnly(sessionLinkBack);

        session.removeLinks(sessionLinkBack);
        assertThat(session.getLinks()).doesNotContain(sessionLinkBack);

        session.links(new HashSet<>(Set.of(sessionLinkBack)));
        assertThat(session.getLinks()).containsOnly(sessionLinkBack);

        session.setLinks(new HashSet<>());
        assertThat(session.getLinks()).doesNotContain(sessionLinkBack);
    }

    @Test
    void site14Test() throws Exception {
        Session session = getSessionRandomSampleGenerator();
        Site siteBack = getSiteRandomSampleGenerator();

        session.setSite14(siteBack);
        assertThat(session.getSite14()).isEqualTo(siteBack);

        session.site14(null);
        assertThat(session.getSite14()).isNull();
    }
}
