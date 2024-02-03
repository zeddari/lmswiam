package com.wiam.lms.domain;

import static com.wiam.lms.domain.GroupTestSamples.*;
import static com.wiam.lms.domain.QuestionTestSamples.*;
import static com.wiam.lms.domain.QuizResultTestSamples.*;
import static com.wiam.lms.domain.QuizTestSamples.*;
import static com.wiam.lms.domain.SiteTestSamples.*;
import static com.wiam.lms.domain.TopicTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.wiam.lms.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class QuizTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Quiz.class);
        Quiz quiz1 = getQuizSample1();
        Quiz quiz2 = new Quiz();
        assertThat(quiz1).isNotEqualTo(quiz2);

        quiz2.setId(quiz1.getId());
        assertThat(quiz1).isEqualTo(quiz2);

        quiz2 = getQuizSample2();
        assertThat(quiz1).isNotEqualTo(quiz2);
    }

    @Test
    void quizResultTest() throws Exception {
        Quiz quiz = getQuizRandomSampleGenerator();
        QuizResult quizResultBack = getQuizResultRandomSampleGenerator();

        quiz.addQuizResult(quizResultBack);
        assertThat(quiz.getQuizResults()).containsOnly(quizResultBack);
        assertThat(quizResultBack.getQuiz()).isEqualTo(quiz);

        quiz.removeQuizResult(quizResultBack);
        assertThat(quiz.getQuizResults()).doesNotContain(quizResultBack);
        assertThat(quizResultBack.getQuiz()).isNull();

        quiz.quizResults(new HashSet<>(Set.of(quizResultBack)));
        assertThat(quiz.getQuizResults()).containsOnly(quizResultBack);
        assertThat(quizResultBack.getQuiz()).isEqualTo(quiz);

        quiz.setQuizResults(new HashSet<>());
        assertThat(quiz.getQuizResults()).doesNotContain(quizResultBack);
        assertThat(quizResultBack.getQuiz()).isNull();
    }

    @Test
    void groupsTest() throws Exception {
        Quiz quiz = getQuizRandomSampleGenerator();
        Group groupBack = getGroupRandomSampleGenerator();

        quiz.addGroups(groupBack);
        assertThat(quiz.getGroups()).containsOnly(groupBack);

        quiz.removeGroups(groupBack);
        assertThat(quiz.getGroups()).doesNotContain(groupBack);

        quiz.groups(new HashSet<>(Set.of(groupBack)));
        assertThat(quiz.getGroups()).containsOnly(groupBack);

        quiz.setGroups(new HashSet<>());
        assertThat(quiz.getGroups()).doesNotContain(groupBack);
    }

    @Test
    void questionsTest() throws Exception {
        Quiz quiz = getQuizRandomSampleGenerator();
        Question questionBack = getQuestionRandomSampleGenerator();

        quiz.addQuestions(questionBack);
        assertThat(quiz.getQuestions()).containsOnly(questionBack);

        quiz.removeQuestions(questionBack);
        assertThat(quiz.getQuestions()).doesNotContain(questionBack);

        quiz.questions(new HashSet<>(Set.of(questionBack)));
        assertThat(quiz.getQuestions()).containsOnly(questionBack);

        quiz.setQuestions(new HashSet<>());
        assertThat(quiz.getQuestions()).doesNotContain(questionBack);
    }

    @Test
    void site7Test() throws Exception {
        Quiz quiz = getQuizRandomSampleGenerator();
        Site siteBack = getSiteRandomSampleGenerator();

        quiz.setSite7(siteBack);
        assertThat(quiz.getSite7()).isEqualTo(siteBack);

        quiz.site7(null);
        assertThat(quiz.getSite7()).isNull();
    }

    @Test
    void topic1Test() throws Exception {
        Quiz quiz = getQuizRandomSampleGenerator();
        Topic topicBack = getTopicRandomSampleGenerator();

        quiz.setTopic1(topicBack);
        assertThat(quiz.getTopic1()).isEqualTo(topicBack);

        quiz.topic1(null);
        assertThat(quiz.getTopic1()).isNull();
    }
}
