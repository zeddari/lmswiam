package com.wiam.lms.domain;

import static com.wiam.lms.domain.AnswerTestSamples.*;
import static com.wiam.lms.domain.QuestionTestSamples.*;
import static com.wiam.lms.domain.QuizTestSamples.*;
import static com.wiam.lms.domain.SiteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.wiam.lms.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class QuestionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Question.class);
        Question question1 = getQuestionSample1();
        Question question2 = new Question();
        assertThat(question1).isNotEqualTo(question2);

        question2.setId(question1.getId());
        assertThat(question1).isEqualTo(question2);

        question2 = getQuestionSample2();
        assertThat(question1).isNotEqualTo(question2);
    }

    @Test
    void answerTest() throws Exception {
        Question question = getQuestionRandomSampleGenerator();
        Answer answerBack = getAnswerRandomSampleGenerator();

        question.addAnswer(answerBack);
        assertThat(question.getAnswers()).containsOnly(answerBack);
        assertThat(answerBack.getQuestion()).isEqualTo(question);

        question.removeAnswer(answerBack);
        assertThat(question.getAnswers()).doesNotContain(answerBack);
        assertThat(answerBack.getQuestion()).isNull();

        question.answers(new HashSet<>(Set.of(answerBack)));
        assertThat(question.getAnswers()).containsOnly(answerBack);
        assertThat(answerBack.getQuestion()).isEqualTo(question);

        question.setAnswers(new HashSet<>());
        assertThat(question.getAnswers()).doesNotContain(answerBack);
        assertThat(answerBack.getQuestion()).isNull();
    }

    @Test
    void site5Test() throws Exception {
        Question question = getQuestionRandomSampleGenerator();
        Site siteBack = getSiteRandomSampleGenerator();

        question.setSite5(siteBack);
        assertThat(question.getSite5()).isEqualTo(siteBack);

        question.site5(null);
        assertThat(question.getSite5()).isNull();
    }

    @Test
    void quizTest() throws Exception {
        Question question = getQuestionRandomSampleGenerator();
        Quiz quizBack = getQuizRandomSampleGenerator();

        question.addQuiz(quizBack);
        assertThat(question.getQuizzes()).containsOnly(quizBack);
        assertThat(quizBack.getQuestions()).containsOnly(question);

        question.removeQuiz(quizBack);
        assertThat(question.getQuizzes()).doesNotContain(quizBack);
        assertThat(quizBack.getQuestions()).doesNotContain(question);

        question.quizzes(new HashSet<>(Set.of(quizBack)));
        assertThat(question.getQuizzes()).containsOnly(quizBack);
        assertThat(quizBack.getQuestions()).containsOnly(question);

        question.setQuizzes(new HashSet<>());
        assertThat(question.getQuizzes()).doesNotContain(quizBack);
        assertThat(quizBack.getQuestions()).doesNotContain(question);
    }
}
