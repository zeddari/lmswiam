package com.wiam.lms.domain;

import static com.wiam.lms.domain.AnswerTestSamples.*;
import static com.wiam.lms.domain.CertificateTestSamples.*;
import static com.wiam.lms.domain.CityTestSamples.*;
import static com.wiam.lms.domain.ClassroomTestSamples.*;
import static com.wiam.lms.domain.CourseTestSamples.*;
import static com.wiam.lms.domain.DiplomaTestSamples.*;
import static com.wiam.lms.domain.EnrolementTestSamples.*;
import static com.wiam.lms.domain.GroupTestSamples.*;
import static com.wiam.lms.domain.PartTestSamples.*;
import static com.wiam.lms.domain.PaymentTestSamples.*;
import static com.wiam.lms.domain.ProgressionTestSamples.*;
import static com.wiam.lms.domain.ProjectTestSamples.*;
import static com.wiam.lms.domain.QuestionTestSamples.*;
import static com.wiam.lms.domain.QuizResultTestSamples.*;
import static com.wiam.lms.domain.QuizTestSamples.*;
import static com.wiam.lms.domain.ReviewTestSamples.*;
import static com.wiam.lms.domain.SessionInstanceTestSamples.*;
import static com.wiam.lms.domain.SessionLinkTestSamples.*;
import static com.wiam.lms.domain.SessionTestSamples.*;
import static com.wiam.lms.domain.SiteTestSamples.*;
import static com.wiam.lms.domain.SponsoringTestSamples.*;
import static com.wiam.lms.domain.TicketsTestSamples.*;
import static com.wiam.lms.domain.UserCustomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.wiam.lms.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SiteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Site.class);
        Site site1 = getSiteSample1();
        Site site2 = new Site();
        assertThat(site1).isNotEqualTo(site2);

        site2.setId(site1.getId());
        assertThat(site1).isEqualTo(site2);

        site2 = getSiteSample2();
        assertThat(site1).isNotEqualTo(site2);
    }

    @Test
    void classroomTest() throws Exception {
        Site site = getSiteRandomSampleGenerator();
        Classroom classroomBack = getClassroomRandomSampleGenerator();

        site.addClassroom(classroomBack);
        assertThat(site.getClassrooms()).containsOnly(classroomBack);
        assertThat(classroomBack.getSite()).isEqualTo(site);

        site.removeClassroom(classroomBack);
        assertThat(site.getClassrooms()).doesNotContain(classroomBack);
        assertThat(classroomBack.getSite()).isNull();

        site.classrooms(new HashSet<>(Set.of(classroomBack)));
        assertThat(site.getClassrooms()).containsOnly(classroomBack);
        assertThat(classroomBack.getSite()).isEqualTo(site);

        site.setClassrooms(new HashSet<>());
        assertThat(site.getClassrooms()).doesNotContain(classroomBack);
        assertThat(classroomBack.getSite()).isNull();
    }

    @Test
    void courseTest() throws Exception {
        Site site = getSiteRandomSampleGenerator();
        Course courseBack = getCourseRandomSampleGenerator();

        site.addCourse(courseBack);
        assertThat(site.getCourses()).containsOnly(courseBack);
        assertThat(courseBack.getSite1()).isEqualTo(site);

        site.removeCourse(courseBack);
        assertThat(site.getCourses()).doesNotContain(courseBack);
        assertThat(courseBack.getSite1()).isNull();

        site.courses(new HashSet<>(Set.of(courseBack)));
        assertThat(site.getCourses()).containsOnly(courseBack);
        assertThat(courseBack.getSite1()).isEqualTo(site);

        site.setCourses(new HashSet<>());
        assertThat(site.getCourses()).doesNotContain(courseBack);
        assertThat(courseBack.getSite1()).isNull();
    }

    @Test
    void partTest() throws Exception {
        Site site = getSiteRandomSampleGenerator();
        Part partBack = getPartRandomSampleGenerator();

        site.addPart(partBack);
        assertThat(site.getParts()).containsOnly(partBack);
        assertThat(partBack.getSite2()).isEqualTo(site);

        site.removePart(partBack);
        assertThat(site.getParts()).doesNotContain(partBack);
        assertThat(partBack.getSite2()).isNull();

        site.parts(new HashSet<>(Set.of(partBack)));
        assertThat(site.getParts()).containsOnly(partBack);
        assertThat(partBack.getSite2()).isEqualTo(site);

        site.setParts(new HashSet<>());
        assertThat(site.getParts()).doesNotContain(partBack);
        assertThat(partBack.getSite2()).isNull();
    }

    @Test
    void reviewTest() throws Exception {
        Site site = getSiteRandomSampleGenerator();
        Review reviewBack = getReviewRandomSampleGenerator();

        site.addReview(reviewBack);
        assertThat(site.getReviews()).containsOnly(reviewBack);
        assertThat(reviewBack.getSite3()).isEqualTo(site);

        site.removeReview(reviewBack);
        assertThat(site.getReviews()).doesNotContain(reviewBack);
        assertThat(reviewBack.getSite3()).isNull();

        site.reviews(new HashSet<>(Set.of(reviewBack)));
        assertThat(site.getReviews()).containsOnly(reviewBack);
        assertThat(reviewBack.getSite3()).isEqualTo(site);

        site.setReviews(new HashSet<>());
        assertThat(site.getReviews()).doesNotContain(reviewBack);
        assertThat(reviewBack.getSite3()).isNull();
    }

    @Test
    void enrolementTest() throws Exception {
        Site site = getSiteRandomSampleGenerator();
        Enrolement enrolementBack = getEnrolementRandomSampleGenerator();

        site.addEnrolement(enrolementBack);
        assertThat(site.getEnrolements()).containsOnly(enrolementBack);
        assertThat(enrolementBack.getSite4()).isEqualTo(site);

        site.removeEnrolement(enrolementBack);
        assertThat(site.getEnrolements()).doesNotContain(enrolementBack);
        assertThat(enrolementBack.getSite4()).isNull();

        site.enrolements(new HashSet<>(Set.of(enrolementBack)));
        assertThat(site.getEnrolements()).containsOnly(enrolementBack);
        assertThat(enrolementBack.getSite4()).isEqualTo(site);

        site.setEnrolements(new HashSet<>());
        assertThat(site.getEnrolements()).doesNotContain(enrolementBack);
        assertThat(enrolementBack.getSite4()).isNull();
    }

    @Test
    void questionTest() throws Exception {
        Site site = getSiteRandomSampleGenerator();
        Question questionBack = getQuestionRandomSampleGenerator();

        site.addQuestion(questionBack);
        assertThat(site.getQuestions()).containsOnly(questionBack);
        assertThat(questionBack.getSite5()).isEqualTo(site);

        site.removeQuestion(questionBack);
        assertThat(site.getQuestions()).doesNotContain(questionBack);
        assertThat(questionBack.getSite5()).isNull();

        site.questions(new HashSet<>(Set.of(questionBack)));
        assertThat(site.getQuestions()).containsOnly(questionBack);
        assertThat(questionBack.getSite5()).isEqualTo(site);

        site.setQuestions(new HashSet<>());
        assertThat(site.getQuestions()).doesNotContain(questionBack);
        assertThat(questionBack.getSite5()).isNull();
    }

    @Test
    void answerTest() throws Exception {
        Site site = getSiteRandomSampleGenerator();
        Answer answerBack = getAnswerRandomSampleGenerator();

        site.addAnswer(answerBack);
        assertThat(site.getAnswers()).containsOnly(answerBack);
        assertThat(answerBack.getSite6()).isEqualTo(site);

        site.removeAnswer(answerBack);
        assertThat(site.getAnswers()).doesNotContain(answerBack);
        assertThat(answerBack.getSite6()).isNull();

        site.answers(new HashSet<>(Set.of(answerBack)));
        assertThat(site.getAnswers()).containsOnly(answerBack);
        assertThat(answerBack.getSite6()).isEqualTo(site);

        site.setAnswers(new HashSet<>());
        assertThat(site.getAnswers()).doesNotContain(answerBack);
        assertThat(answerBack.getSite6()).isNull();
    }

    @Test
    void quizTest() throws Exception {
        Site site = getSiteRandomSampleGenerator();
        Quiz quizBack = getQuizRandomSampleGenerator();

        site.addQuiz(quizBack);
        assertThat(site.getQuizzes()).containsOnly(quizBack);
        assertThat(quizBack.getSite7()).isEqualTo(site);

        site.removeQuiz(quizBack);
        assertThat(site.getQuizzes()).doesNotContain(quizBack);
        assertThat(quizBack.getSite7()).isNull();

        site.quizzes(new HashSet<>(Set.of(quizBack)));
        assertThat(site.getQuizzes()).containsOnly(quizBack);
        assertThat(quizBack.getSite7()).isEqualTo(site);

        site.setQuizzes(new HashSet<>());
        assertThat(site.getQuizzes()).doesNotContain(quizBack);
        assertThat(quizBack.getSite7()).isNull();
    }

    @Test
    void quizResultTest() throws Exception {
        Site site = getSiteRandomSampleGenerator();
        QuizResult quizResultBack = getQuizResultRandomSampleGenerator();

        site.addQuizResult(quizResultBack);
        assertThat(site.getQuizResults()).containsOnly(quizResultBack);
        assertThat(quizResultBack.getSite8()).isEqualTo(site);

        site.removeQuizResult(quizResultBack);
        assertThat(site.getQuizResults()).doesNotContain(quizResultBack);
        assertThat(quizResultBack.getSite8()).isNull();

        site.quizResults(new HashSet<>(Set.of(quizResultBack)));
        assertThat(site.getQuizResults()).containsOnly(quizResultBack);
        assertThat(quizResultBack.getSite8()).isEqualTo(site);

        site.setQuizResults(new HashSet<>());
        assertThat(site.getQuizResults()).doesNotContain(quizResultBack);
        assertThat(quizResultBack.getSite8()).isNull();
    }

    @Test
    void paymentTest() throws Exception {
        Site site = getSiteRandomSampleGenerator();
        Payment paymentBack = getPaymentRandomSampleGenerator();

        site.addPayment(paymentBack);
        assertThat(site.getPayments()).containsOnly(paymentBack);
        assertThat(paymentBack.getSite9()).isEqualTo(site);

        site.removePayment(paymentBack);
        assertThat(site.getPayments()).doesNotContain(paymentBack);
        assertThat(paymentBack.getSite9()).isNull();

        site.payments(new HashSet<>(Set.of(paymentBack)));
        assertThat(site.getPayments()).containsOnly(paymentBack);
        assertThat(paymentBack.getSite9()).isEqualTo(site);

        site.setPayments(new HashSet<>());
        assertThat(site.getPayments()).doesNotContain(paymentBack);
        assertThat(paymentBack.getSite9()).isNull();
    }

    @Test
    void sponsoringTest() throws Exception {
        Site site = getSiteRandomSampleGenerator();
        Sponsoring sponsoringBack = getSponsoringRandomSampleGenerator();

        site.addSponsoring(sponsoringBack);
        assertThat(site.getSponsorings()).containsOnly(sponsoringBack);
        assertThat(sponsoringBack.getSite10()).isEqualTo(site);

        site.removeSponsoring(sponsoringBack);
        assertThat(site.getSponsorings()).doesNotContain(sponsoringBack);
        assertThat(sponsoringBack.getSite10()).isNull();

        site.sponsorings(new HashSet<>(Set.of(sponsoringBack)));
        assertThat(site.getSponsorings()).containsOnly(sponsoringBack);
        assertThat(sponsoringBack.getSite10()).isEqualTo(site);

        site.setSponsorings(new HashSet<>());
        assertThat(site.getSponsorings()).doesNotContain(sponsoringBack);
        assertThat(sponsoringBack.getSite10()).isNull();
    }

    @Test
    void groupTest() throws Exception {
        Site site = getSiteRandomSampleGenerator();
        Group groupBack = getGroupRandomSampleGenerator();

        site.addGroup(groupBack);
        assertThat(site.getGroups()).containsOnly(groupBack);
        assertThat(groupBack.getSite11()).isEqualTo(site);

        site.removeGroup(groupBack);
        assertThat(site.getGroups()).doesNotContain(groupBack);
        assertThat(groupBack.getSite11()).isNull();

        site.groups(new HashSet<>(Set.of(groupBack)));
        assertThat(site.getGroups()).containsOnly(groupBack);
        assertThat(groupBack.getSite11()).isEqualTo(site);

        site.setGroups(new HashSet<>());
        assertThat(site.getGroups()).doesNotContain(groupBack);
        assertThat(groupBack.getSite11()).isNull();
    }

    @Test
    void projectTest() throws Exception {
        Site site = getSiteRandomSampleGenerator();
        Project projectBack = getProjectRandomSampleGenerator();

        site.addProject(projectBack);
        assertThat(site.getProjects()).containsOnly(projectBack);
        assertThat(projectBack.getSite12()).isEqualTo(site);

        site.removeProject(projectBack);
        assertThat(site.getProjects()).doesNotContain(projectBack);
        assertThat(projectBack.getSite12()).isNull();

        site.projects(new HashSet<>(Set.of(projectBack)));
        assertThat(site.getProjects()).containsOnly(projectBack);
        assertThat(projectBack.getSite12()).isEqualTo(site);

        site.setProjects(new HashSet<>());
        assertThat(site.getProjects()).doesNotContain(projectBack);
        assertThat(projectBack.getSite12()).isNull();
    }

    @Test
    void userCustomTest() throws Exception {
        Site site = getSiteRandomSampleGenerator();
        UserCustom userCustomBack = getUserCustomRandomSampleGenerator();

        site.addUserCustom(userCustomBack);
        assertThat(site.getUserCustoms()).containsOnly(userCustomBack);
        assertThat(userCustomBack.getSite13()).isEqualTo(site);

        site.removeUserCustom(userCustomBack);
        assertThat(site.getUserCustoms()).doesNotContain(userCustomBack);
        assertThat(userCustomBack.getSite13()).isNull();

        site.userCustoms(new HashSet<>(Set.of(userCustomBack)));
        assertThat(site.getUserCustoms()).containsOnly(userCustomBack);
        assertThat(userCustomBack.getSite13()).isEqualTo(site);

        site.setUserCustoms(new HashSet<>());
        assertThat(site.getUserCustoms()).doesNotContain(userCustomBack);
        assertThat(userCustomBack.getSite13()).isNull();
    }

    @Test
    void sessionTest() throws Exception {
        Site site = getSiteRandomSampleGenerator();
        Session sessionBack = getSessionRandomSampleGenerator();

        site.addSession(sessionBack);
        assertThat(site.getSessions()).containsOnly(sessionBack);
        assertThat(sessionBack.getSite14()).isEqualTo(site);

        site.removeSession(sessionBack);
        assertThat(site.getSessions()).doesNotContain(sessionBack);
        assertThat(sessionBack.getSite14()).isNull();

        site.sessions(new HashSet<>(Set.of(sessionBack)));
        assertThat(site.getSessions()).containsOnly(sessionBack);
        assertThat(sessionBack.getSite14()).isEqualTo(site);

        site.setSessions(new HashSet<>());
        assertThat(site.getSessions()).doesNotContain(sessionBack);
        assertThat(sessionBack.getSite14()).isNull();
    }

    @Test
    void sessionLinkTest() throws Exception {
        Site site = getSiteRandomSampleGenerator();
        SessionLink sessionLinkBack = getSessionLinkRandomSampleGenerator();

        site.addSessionLink(sessionLinkBack);
        assertThat(site.getSessionLinks()).containsOnly(sessionLinkBack);
        assertThat(sessionLinkBack.getSite15()).isEqualTo(site);

        site.removeSessionLink(sessionLinkBack);
        assertThat(site.getSessionLinks()).doesNotContain(sessionLinkBack);
        assertThat(sessionLinkBack.getSite15()).isNull();

        site.sessionLinks(new HashSet<>(Set.of(sessionLinkBack)));
        assertThat(site.getSessionLinks()).containsOnly(sessionLinkBack);
        assertThat(sessionLinkBack.getSite15()).isEqualTo(site);

        site.setSessionLinks(new HashSet<>());
        assertThat(site.getSessionLinks()).doesNotContain(sessionLinkBack);
        assertThat(sessionLinkBack.getSite15()).isNull();
    }

    @Test
    void sessionInstanceTest() throws Exception {
        Site site = getSiteRandomSampleGenerator();
        SessionInstance sessionInstanceBack = getSessionInstanceRandomSampleGenerator();

        site.addSessionInstance(sessionInstanceBack);
        assertThat(site.getSessionInstances()).containsOnly(sessionInstanceBack);
        assertThat(sessionInstanceBack.getSite16()).isEqualTo(site);

        site.removeSessionInstance(sessionInstanceBack);
        assertThat(site.getSessionInstances()).doesNotContain(sessionInstanceBack);
        assertThat(sessionInstanceBack.getSite16()).isNull();

        site.sessionInstances(new HashSet<>(Set.of(sessionInstanceBack)));
        assertThat(site.getSessionInstances()).containsOnly(sessionInstanceBack);
        assertThat(sessionInstanceBack.getSite16()).isEqualTo(site);

        site.setSessionInstances(new HashSet<>());
        assertThat(site.getSessionInstances()).doesNotContain(sessionInstanceBack);
        assertThat(sessionInstanceBack.getSite16()).isNull();
    }

    @Test
    void progressionTest() throws Exception {
        Site site = getSiteRandomSampleGenerator();
        Progression progressionBack = getProgressionRandomSampleGenerator();

        site.addProgression(progressionBack);
        assertThat(site.getProgressions()).containsOnly(progressionBack);
        assertThat(progressionBack.getSite17()).isEqualTo(site);

        site.removeProgression(progressionBack);
        assertThat(site.getProgressions()).doesNotContain(progressionBack);
        assertThat(progressionBack.getSite17()).isNull();

        site.progressions(new HashSet<>(Set.of(progressionBack)));
        assertThat(site.getProgressions()).containsOnly(progressionBack);
        assertThat(progressionBack.getSite17()).isEqualTo(site);

        site.setProgressions(new HashSet<>());
        assertThat(site.getProgressions()).doesNotContain(progressionBack);
        assertThat(progressionBack.getSite17()).isNull();
    }

    @Test
    void ticketsTest() throws Exception {
        Site site = getSiteRandomSampleGenerator();
        Tickets ticketsBack = getTicketsRandomSampleGenerator();

        site.addTickets(ticketsBack);
        assertThat(site.getTickets()).containsOnly(ticketsBack);
        assertThat(ticketsBack.getSite18()).isEqualTo(site);

        site.removeTickets(ticketsBack);
        assertThat(site.getTickets()).doesNotContain(ticketsBack);
        assertThat(ticketsBack.getSite18()).isNull();

        site.tickets(new HashSet<>(Set.of(ticketsBack)));
        assertThat(site.getTickets()).containsOnly(ticketsBack);
        assertThat(ticketsBack.getSite18()).isEqualTo(site);

        site.setTickets(new HashSet<>());
        assertThat(site.getTickets()).doesNotContain(ticketsBack);
        assertThat(ticketsBack.getSite18()).isNull();
    }

    @Test
    void certificateTest() throws Exception {
        Site site = getSiteRandomSampleGenerator();
        Certificate certificateBack = getCertificateRandomSampleGenerator();

        site.addCertificate(certificateBack);
        assertThat(site.getCertificates()).containsOnly(certificateBack);
        assertThat(certificateBack.getSite19()).isEqualTo(site);

        site.removeCertificate(certificateBack);
        assertThat(site.getCertificates()).doesNotContain(certificateBack);
        assertThat(certificateBack.getSite19()).isNull();

        site.certificates(new HashSet<>(Set.of(certificateBack)));
        assertThat(site.getCertificates()).containsOnly(certificateBack);
        assertThat(certificateBack.getSite19()).isEqualTo(site);

        site.setCertificates(new HashSet<>());
        assertThat(site.getCertificates()).doesNotContain(certificateBack);
        assertThat(certificateBack.getSite19()).isNull();
    }

    @Test
    void diplomaTest() throws Exception {
        Site site = getSiteRandomSampleGenerator();
        Diploma diplomaBack = getDiplomaRandomSampleGenerator();

        site.addDiploma(diplomaBack);
        assertThat(site.getDiplomas()).containsOnly(diplomaBack);
        assertThat(diplomaBack.getSite20()).isEqualTo(site);

        site.removeDiploma(diplomaBack);
        assertThat(site.getDiplomas()).doesNotContain(diplomaBack);
        assertThat(diplomaBack.getSite20()).isNull();

        site.diplomas(new HashSet<>(Set.of(diplomaBack)));
        assertThat(site.getDiplomas()).containsOnly(diplomaBack);
        assertThat(diplomaBack.getSite20()).isEqualTo(site);

        site.setDiplomas(new HashSet<>());
        assertThat(site.getDiplomas()).doesNotContain(diplomaBack);
        assertThat(diplomaBack.getSite20()).isNull();
    }

    @Test
    void cityTest() throws Exception {
        Site site = getSiteRandomSampleGenerator();
        City cityBack = getCityRandomSampleGenerator();

        site.setCity(cityBack);
        assertThat(site.getCity()).isEqualTo(cityBack);

        site.city(null);
        assertThat(site.getCity()).isNull();
    }
}
