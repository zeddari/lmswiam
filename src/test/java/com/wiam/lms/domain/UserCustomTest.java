package com.wiam.lms.domain;

import static com.wiam.lms.domain.AnswerTestSamples.*;
import static com.wiam.lms.domain.CertificateTestSamples.*;
import static com.wiam.lms.domain.CountryTestSamples.*;
import static com.wiam.lms.domain.CourseTestSamples.*;
import static com.wiam.lms.domain.DepartementTestSamples.*;
import static com.wiam.lms.domain.DepenseTestSamples.*;
import static com.wiam.lms.domain.DiplomaTestSamples.*;
import static com.wiam.lms.domain.EnrolementTestSamples.*;
import static com.wiam.lms.domain.GroupTestSamples.*;
import static com.wiam.lms.domain.JobTestSamples.*;
import static com.wiam.lms.domain.LanguageTestSamples.*;
import static com.wiam.lms.domain.NationalityTestSamples.*;
import static com.wiam.lms.domain.ProgressionTestSamples.*;
import static com.wiam.lms.domain.QuizResultTestSamples.*;
import static com.wiam.lms.domain.ReviewTestSamples.*;
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

class UserCustomTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserCustom.class);
        UserCustom userCustom1 = getUserCustomSample1();
        UserCustom userCustom2 = new UserCustom();
        assertThat(userCustom1).isNotEqualTo(userCustom2);

        userCustom2.setId(userCustom1.getId());
        assertThat(userCustom1).isEqualTo(userCustom2);

        userCustom2 = getUserCustomSample2();
        assertThat(userCustom1).isNotEqualTo(userCustom2);
    }

    @Test
    void certificateTest() throws Exception {
        UserCustom userCustom = getUserCustomRandomSampleGenerator();
        Certificate certificateBack = getCertificateRandomSampleGenerator();

        userCustom.addCertificate(certificateBack);
        assertThat(userCustom.getCertificates()).containsOnly(certificateBack);
        assertThat(certificateBack.getUserCustom6()).isEqualTo(userCustom);

        userCustom.removeCertificate(certificateBack);
        assertThat(userCustom.getCertificates()).doesNotContain(certificateBack);
        assertThat(certificateBack.getUserCustom6()).isNull();

        userCustom.certificates(new HashSet<>(Set.of(certificateBack)));
        assertThat(userCustom.getCertificates()).containsOnly(certificateBack);
        assertThat(certificateBack.getUserCustom6()).isEqualTo(userCustom);

        userCustom.setCertificates(new HashSet<>());
        assertThat(userCustom.getCertificates()).doesNotContain(certificateBack);
        assertThat(certificateBack.getUserCustom6()).isNull();
    }

    @Test
    void answerTest() throws Exception {
        UserCustom userCustom = getUserCustomRandomSampleGenerator();
        Answer answerBack = getAnswerRandomSampleGenerator();

        userCustom.addAnswer(answerBack);
        assertThat(userCustom.getAnswers()).containsOnly(answerBack);
        assertThat(answerBack.getUserCustom1()).isEqualTo(userCustom);

        userCustom.removeAnswer(answerBack);
        assertThat(userCustom.getAnswers()).doesNotContain(answerBack);
        assertThat(answerBack.getUserCustom1()).isNull();

        userCustom.answers(new HashSet<>(Set.of(answerBack)));
        assertThat(userCustom.getAnswers()).containsOnly(answerBack);
        assertThat(answerBack.getUserCustom1()).isEqualTo(userCustom);

        userCustom.setAnswers(new HashSet<>());
        assertThat(userCustom.getAnswers()).doesNotContain(answerBack);
        assertThat(answerBack.getUserCustom1()).isNull();
    }

    @Test
    void quizResultTest() throws Exception {
        UserCustom userCustom = getUserCustomRandomSampleGenerator();
        QuizResult quizResultBack = getQuizResultRandomSampleGenerator();

        userCustom.addQuizResult(quizResultBack);
        assertThat(userCustom.getQuizResults()).containsOnly(quizResultBack);
        assertThat(quizResultBack.getUserCustom2()).isEqualTo(userCustom);

        userCustom.removeQuizResult(quizResultBack);
        assertThat(userCustom.getQuizResults()).doesNotContain(quizResultBack);
        assertThat(quizResultBack.getUserCustom2()).isNull();

        userCustom.quizResults(new HashSet<>(Set.of(quizResultBack)));
        assertThat(userCustom.getQuizResults()).containsOnly(quizResultBack);
        assertThat(quizResultBack.getUserCustom2()).isEqualTo(userCustom);

        userCustom.setQuizResults(new HashSet<>());
        assertThat(userCustom.getQuizResults()).doesNotContain(quizResultBack);
        assertThat(quizResultBack.getUserCustom2()).isNull();
    }

    @Test
    void reviewTest() throws Exception {
        UserCustom userCustom = getUserCustomRandomSampleGenerator();
        Review reviewBack = getReviewRandomSampleGenerator();

        userCustom.addReview(reviewBack);
        assertThat(userCustom.getReviews()).containsOnly(reviewBack);
        assertThat(reviewBack.getUserCustom3()).isEqualTo(userCustom);

        userCustom.removeReview(reviewBack);
        assertThat(userCustom.getReviews()).doesNotContain(reviewBack);
        assertThat(reviewBack.getUserCustom3()).isNull();

        userCustom.reviews(new HashSet<>(Set.of(reviewBack)));
        assertThat(userCustom.getReviews()).containsOnly(reviewBack);
        assertThat(reviewBack.getUserCustom3()).isEqualTo(userCustom);

        userCustom.setReviews(new HashSet<>());
        assertThat(userCustom.getReviews()).doesNotContain(reviewBack);
        assertThat(reviewBack.getUserCustom3()).isNull();
    }

    @Test
    void enrolementTest() throws Exception {
        UserCustom userCustom = getUserCustomRandomSampleGenerator();
        Enrolement enrolementBack = getEnrolementRandomSampleGenerator();

        userCustom.addEnrolement(enrolementBack);
        assertThat(userCustom.getEnrolements()).containsOnly(enrolementBack);
        assertThat(enrolementBack.getUserCustom4()).isEqualTo(userCustom);

        userCustom.removeEnrolement(enrolementBack);
        assertThat(userCustom.getEnrolements()).doesNotContain(enrolementBack);
        assertThat(enrolementBack.getUserCustom4()).isNull();

        userCustom.enrolements(new HashSet<>(Set.of(enrolementBack)));
        assertThat(userCustom.getEnrolements()).containsOnly(enrolementBack);
        assertThat(enrolementBack.getUserCustom4()).isEqualTo(userCustom);

        userCustom.setEnrolements(new HashSet<>());
        assertThat(userCustom.getEnrolements()).doesNotContain(enrolementBack);
        assertThat(enrolementBack.getUserCustom4()).isNull();
    }

    @Test
    void progressionTest() throws Exception {
        UserCustom userCustom = getUserCustomRandomSampleGenerator();
        Progression progressionBack = getProgressionRandomSampleGenerator();

        userCustom.addProgression(progressionBack);
        assertThat(userCustom.getProgressions()).containsOnly(progressionBack);
        assertThat(progressionBack.getStudent()).isEqualTo(userCustom);

        userCustom.removeProgression(progressionBack);
        assertThat(userCustom.getProgressions()).doesNotContain(progressionBack);
        assertThat(progressionBack.getStudent()).isNull();

        userCustom.progressions(new HashSet<>(Set.of(progressionBack)));
        assertThat(userCustom.getProgressions()).containsOnly(progressionBack);
        assertThat(progressionBack.getStudent()).isEqualTo(userCustom);

        userCustom.setProgressions(new HashSet<>());
        assertThat(userCustom.getProgressions()).doesNotContain(progressionBack);
        assertThat(progressionBack.getStudent()).isNull();
    }

    @Test
    void ticketsTest() throws Exception {
        UserCustom userCustom = getUserCustomRandomSampleGenerator();
        Tickets ticketsBack = getTicketsRandomSampleGenerator();

        userCustom.addTickets(ticketsBack);
        assertThat(userCustom.getTickets()).containsOnly(ticketsBack);
        assertThat(ticketsBack.getUserCustom5()).isEqualTo(userCustom);

        userCustom.removeTickets(ticketsBack);
        assertThat(userCustom.getTickets()).doesNotContain(ticketsBack);
        assertThat(ticketsBack.getUserCustom5()).isNull();

        userCustom.tickets(new HashSet<>(Set.of(ticketsBack)));
        assertThat(userCustom.getTickets()).containsOnly(ticketsBack);
        assertThat(ticketsBack.getUserCustom5()).isEqualTo(userCustom);

        userCustom.setTickets(new HashSet<>());
        assertThat(userCustom.getTickets()).doesNotContain(ticketsBack);
        assertThat(ticketsBack.getUserCustom5()).isNull();
    }

    @Test
    void sponsoringTest() throws Exception {
        UserCustom userCustom = getUserCustomRandomSampleGenerator();
        Sponsoring sponsoringBack = getSponsoringRandomSampleGenerator();

        userCustom.addSponsoring(sponsoringBack);
        assertThat(userCustom.getSponsorings()).containsOnly(sponsoringBack);
        assertThat(sponsoringBack.getSponsor()).isEqualTo(userCustom);

        userCustom.removeSponsoring(sponsoringBack);
        assertThat(userCustom.getSponsorings()).doesNotContain(sponsoringBack);
        assertThat(sponsoringBack.getSponsor()).isNull();

        userCustom.sponsorings(new HashSet<>(Set.of(sponsoringBack)));
        assertThat(userCustom.getSponsorings()).containsOnly(sponsoringBack);
        assertThat(sponsoringBack.getSponsor()).isEqualTo(userCustom);

        userCustom.setSponsorings(new HashSet<>());
        assertThat(userCustom.getSponsorings()).doesNotContain(sponsoringBack);
        assertThat(sponsoringBack.getSponsor()).isNull();
    }

    @Test
    void depenseTest() throws Exception {
        UserCustom userCustom = getUserCustomRandomSampleGenerator();
        Depense depenseBack = getDepenseRandomSampleGenerator();

        userCustom.addDepense(depenseBack);
        assertThat(userCustom.getDepenses()).containsOnly(depenseBack);
        assertThat(depenseBack.getResource()).isEqualTo(userCustom);

        userCustom.removeDepense(depenseBack);
        assertThat(userCustom.getDepenses()).doesNotContain(depenseBack);
        assertThat(depenseBack.getResource()).isNull();

        userCustom.depenses(new HashSet<>(Set.of(depenseBack)));
        assertThat(userCustom.getDepenses()).containsOnly(depenseBack);
        assertThat(depenseBack.getResource()).isEqualTo(userCustom);

        userCustom.setDepenses(new HashSet<>());
        assertThat(userCustom.getDepenses()).doesNotContain(depenseBack);
        assertThat(depenseBack.getResource()).isNull();
    }

    @Test
    void diplomasTest() throws Exception {
        UserCustom userCustom = getUserCustomRandomSampleGenerator();
        Diploma diplomaBack = getDiplomaRandomSampleGenerator();

        userCustom.addDiplomas(diplomaBack);
        assertThat(userCustom.getDiplomas()).containsOnly(diplomaBack);

        userCustom.removeDiplomas(diplomaBack);
        assertThat(userCustom.getDiplomas()).doesNotContain(diplomaBack);

        userCustom.diplomas(new HashSet<>(Set.of(diplomaBack)));
        assertThat(userCustom.getDiplomas()).containsOnly(diplomaBack);

        userCustom.setDiplomas(new HashSet<>());
        assertThat(userCustom.getDiplomas()).doesNotContain(diplomaBack);
    }

    @Test
    void languagesTest() throws Exception {
        UserCustom userCustom = getUserCustomRandomSampleGenerator();
        Language languageBack = getLanguageRandomSampleGenerator();

        userCustom.addLanguages(languageBack);
        assertThat(userCustom.getLanguages()).containsOnly(languageBack);

        userCustom.removeLanguages(languageBack);
        assertThat(userCustom.getLanguages()).doesNotContain(languageBack);

        userCustom.languages(new HashSet<>(Set.of(languageBack)));
        assertThat(userCustom.getLanguages()).containsOnly(languageBack);

        userCustom.setLanguages(new HashSet<>());
        assertThat(userCustom.getLanguages()).doesNotContain(languageBack);
    }

    @Test
    void site13Test() throws Exception {
        UserCustom userCustom = getUserCustomRandomSampleGenerator();
        Site siteBack = getSiteRandomSampleGenerator();

        userCustom.setSite13(siteBack);
        assertThat(userCustom.getSite13()).isEqualTo(siteBack);

        userCustom.site13(null);
        assertThat(userCustom.getSite13()).isNull();
    }

    @Test
    void countryTest() throws Exception {
        UserCustom userCustom = getUserCustomRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        userCustom.setCountry(countryBack);
        assertThat(userCustom.getCountry()).isEqualTo(countryBack);

        userCustom.country(null);
        assertThat(userCustom.getCountry()).isNull();
    }

    @Test
    void nationalityTest() throws Exception {
        UserCustom userCustom = getUserCustomRandomSampleGenerator();
        Nationality nationalityBack = getNationalityRandomSampleGenerator();

        userCustom.setNationality(nationalityBack);
        assertThat(userCustom.getNationality()).isEqualTo(nationalityBack);

        userCustom.nationality(null);
        assertThat(userCustom.getNationality()).isNull();
    }

    @Test
    void jobTest() throws Exception {
        UserCustom userCustom = getUserCustomRandomSampleGenerator();
        Job jobBack = getJobRandomSampleGenerator();

        userCustom.setJob(jobBack);
        assertThat(userCustom.getJob()).isEqualTo(jobBack);

        userCustom.job(null);
        assertThat(userCustom.getJob()).isNull();
    }

    @Test
    void departement2Test() throws Exception {
        UserCustom userCustom = getUserCustomRandomSampleGenerator();
        Departement departementBack = getDepartementRandomSampleGenerator();

        userCustom.setDepartement2(departementBack);
        assertThat(userCustom.getDepartement2()).isEqualTo(departementBack);

        userCustom.departement2(null);
        assertThat(userCustom.getDepartement2()).isNull();
    }

    @Test
    void groupsTest() throws Exception {
        UserCustom userCustom = getUserCustomRandomSampleGenerator();
        Group groupBack = getGroupRandomSampleGenerator();

        userCustom.addGroups(groupBack);
        assertThat(userCustom.getGroups()).containsOnly(groupBack);
        assertThat(groupBack.getElements()).containsOnly(userCustom);

        userCustom.removeGroups(groupBack);
        assertThat(userCustom.getGroups()).doesNotContain(groupBack);
        assertThat(groupBack.getElements()).doesNotContain(userCustom);

        userCustom.groups(new HashSet<>(Set.of(groupBack)));
        assertThat(userCustom.getGroups()).containsOnly(groupBack);
        assertThat(groupBack.getElements()).containsOnly(userCustom);

        userCustom.setGroups(new HashSet<>());
        assertThat(userCustom.getGroups()).doesNotContain(groupBack);
        assertThat(groupBack.getElements()).doesNotContain(userCustom);
    }

    @Test
    void coursesTest() throws Exception {
        UserCustom userCustom = getUserCustomRandomSampleGenerator();
        Course courseBack = getCourseRandomSampleGenerator();

        userCustom.addCourses(courseBack);
        assertThat(userCustom.getCourses()).containsOnly(courseBack);
        assertThat(courseBack.getProfessors()).containsOnly(userCustom);

        userCustom.removeCourses(courseBack);
        assertThat(userCustom.getCourses()).doesNotContain(courseBack);
        assertThat(courseBack.getProfessors()).doesNotContain(userCustom);

        userCustom.courses(new HashSet<>(Set.of(courseBack)));
        assertThat(userCustom.getCourses()).containsOnly(courseBack);
        assertThat(courseBack.getProfessors()).containsOnly(userCustom);

        userCustom.setCourses(new HashSet<>());
        assertThat(userCustom.getCourses()).doesNotContain(courseBack);
        assertThat(courseBack.getProfessors()).doesNotContain(userCustom);
    }

    @Test
    void sessions2Test() throws Exception {
        UserCustom userCustom = getUserCustomRandomSampleGenerator();
        Session sessionBack = getSessionRandomSampleGenerator();

        userCustom.addSessions2(sessionBack);
        assertThat(userCustom.getSessions2s()).containsOnly(sessionBack);
        assertThat(sessionBack.getProfessors()).containsOnly(userCustom);

        userCustom.removeSessions2(sessionBack);
        assertThat(userCustom.getSessions2s()).doesNotContain(sessionBack);
        assertThat(sessionBack.getProfessors()).doesNotContain(userCustom);

        userCustom.sessions2s(new HashSet<>(Set.of(sessionBack)));
        assertThat(userCustom.getSessions2s()).containsOnly(sessionBack);
        assertThat(sessionBack.getProfessors()).containsOnly(userCustom);

        userCustom.setSessions2s(new HashSet<>());
        assertThat(userCustom.getSessions2s()).doesNotContain(sessionBack);
        assertThat(sessionBack.getProfessors()).doesNotContain(userCustom);
    }

    @Test
    void sessions3Test() throws Exception {
        UserCustom userCustom = getUserCustomRandomSampleGenerator();
        Session sessionBack = getSessionRandomSampleGenerator();

        userCustom.addSessions3(sessionBack);
        assertThat(userCustom.getSessions3s()).containsOnly(sessionBack);
        assertThat(sessionBack.getEmployees()).containsOnly(userCustom);

        userCustom.removeSessions3(sessionBack);
        assertThat(userCustom.getSessions3s()).doesNotContain(sessionBack);
        assertThat(sessionBack.getEmployees()).doesNotContain(userCustom);

        userCustom.sessions3s(new HashSet<>(Set.of(sessionBack)));
        assertThat(userCustom.getSessions3s()).containsOnly(sessionBack);
        assertThat(sessionBack.getEmployees()).containsOnly(userCustom);

        userCustom.setSessions3s(new HashSet<>());
        assertThat(userCustom.getSessions3s()).doesNotContain(sessionBack);
        assertThat(sessionBack.getEmployees()).doesNotContain(userCustom);
    }
}
