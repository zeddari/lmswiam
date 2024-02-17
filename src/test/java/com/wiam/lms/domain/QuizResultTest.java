package com.wiam.lms.domain;

import static com.wiam.lms.domain.QuizResultTestSamples.*;
import static com.wiam.lms.domain.QuizTestSamples.*;
import static com.wiam.lms.domain.SiteTestSamples.*;
import static com.wiam.lms.domain.UserCustomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.wiam.lms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuizResultTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuizResult.class);
        QuizResult quizResult1 = getQuizResultSample1();
        QuizResult quizResult2 = new QuizResult();
        assertThat(quizResult1).isNotEqualTo(quizResult2);

        quizResult2.setId(quizResult1.getId());
        assertThat(quizResult1).isEqualTo(quizResult2);

        quizResult2 = getQuizResultSample2();
        assertThat(quizResult1).isNotEqualTo(quizResult2);
    }

    @Test
    void site8Test() throws Exception {
        QuizResult quizResult = getQuizResultRandomSampleGenerator();
        Site siteBack = getSiteRandomSampleGenerator();

        quizResult.setSite8(siteBack);
        assertThat(quizResult.getSite8()).isEqualTo(siteBack);

        quizResult.site8(null);
        assertThat(quizResult.getSite8()).isNull();
    }

    @Test
    void quizTest() throws Exception {
        QuizResult quizResult = getQuizResultRandomSampleGenerator();
        Quiz quizBack = getQuizRandomSampleGenerator();

        quizResult.setQuiz(quizBack);
        assertThat(quizResult.getQuiz()).isEqualTo(quizBack);

        quizResult.quiz(null);
        assertThat(quizResult.getQuiz()).isNull();
    }

    @Test
    void userCustom2Test() throws Exception {
        QuizResult quizResult = getQuizResultRandomSampleGenerator();
        UserCustom userCustomBack = getUserCustomRandomSampleGenerator();

        quizResult.setUserCustom2(userCustomBack);
        assertThat(quizResult.getUserCustom2()).isEqualTo(userCustomBack);

        quizResult.userCustom2(null);
        assertThat(quizResult.getUserCustom2()).isNull();
    }
}
