package com.wiam.lms.domain;

import static com.wiam.lms.domain.AnswerTestSamples.*;
import static com.wiam.lms.domain.QuestionTestSamples.*;
import static com.wiam.lms.domain.SiteTestSamples.*;
import static com.wiam.lms.domain.UserCustomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.wiam.lms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AnswerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Answer.class);
        Answer answer1 = getAnswerSample1();
        Answer answer2 = new Answer();
        assertThat(answer1).isNotEqualTo(answer2);

        answer2.setId(answer1.getId());
        assertThat(answer1).isEqualTo(answer2);

        answer2 = getAnswerSample2();
        assertThat(answer1).isNotEqualTo(answer2);
    }

    @Test
    void site6Test() throws Exception {
        Answer answer = getAnswerRandomSampleGenerator();
        Site siteBack = getSiteRandomSampleGenerator();

        answer.setSite6(siteBack);
        assertThat(answer.getSite6()).isEqualTo(siteBack);

        answer.site6(null);
        assertThat(answer.getSite6()).isNull();
    }

    @Test
    void questionTest() throws Exception {
        Answer answer = getAnswerRandomSampleGenerator();
        Question questionBack = getQuestionRandomSampleGenerator();

        answer.setQuestion(questionBack);
        assertThat(answer.getQuestion()).isEqualTo(questionBack);

        answer.question(null);
        assertThat(answer.getQuestion()).isNull();
    }

    @Test
    void userCustom1Test() throws Exception {
        Answer answer = getAnswerRandomSampleGenerator();
        UserCustom userCustomBack = getUserCustomRandomSampleGenerator();

        answer.setUserCustom1(userCustomBack);
        assertThat(answer.getUserCustom1()).isEqualTo(userCustomBack);

        answer.userCustom1(null);
        assertThat(answer.getUserCustom1()).isNull();
    }
}
