package com.wiam.lms.domain;

import static com.wiam.lms.domain.CertificateTestSamples.*;
import static com.wiam.lms.domain.CourseTestSamples.*;
import static com.wiam.lms.domain.QuizTestSamples.*;
import static com.wiam.lms.domain.TopicTestSamples.*;
import static com.wiam.lms.domain.TopicTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.wiam.lms.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TopicTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Topic.class);
        Topic topic1 = getTopicSample1();
        Topic topic2 = new Topic();
        assertThat(topic1).isNotEqualTo(topic2);

        topic2.setId(topic1.getId());
        assertThat(topic1).isEqualTo(topic2);

        topic2 = getTopicSample2();
        assertThat(topic1).isNotEqualTo(topic2);
    }

    @Test
    void certificateTest() throws Exception {
        Topic topic = getTopicRandomSampleGenerator();
        Certificate certificateBack = getCertificateRandomSampleGenerator();

        topic.addCertificate(certificateBack);
        assertThat(topic.getCertificates()).containsOnly(certificateBack);
        assertThat(certificateBack.getTopic4()).isEqualTo(topic);

        topic.removeCertificate(certificateBack);
        assertThat(topic.getCertificates()).doesNotContain(certificateBack);
        assertThat(certificateBack.getTopic4()).isNull();

        topic.certificates(new HashSet<>(Set.of(certificateBack)));
        assertThat(topic.getCertificates()).containsOnly(certificateBack);
        assertThat(certificateBack.getTopic4()).isEqualTo(topic);

        topic.setCertificates(new HashSet<>());
        assertThat(topic.getCertificates()).doesNotContain(certificateBack);
        assertThat(certificateBack.getTopic4()).isNull();
    }

    @Test
    void quizTest() throws Exception {
        Topic topic = getTopicRandomSampleGenerator();
        Quiz quizBack = getQuizRandomSampleGenerator();

        topic.addQuiz(quizBack);
        assertThat(topic.getQuizzes()).containsOnly(quizBack);
        assertThat(quizBack.getTopic1()).isEqualTo(topic);

        topic.removeQuiz(quizBack);
        assertThat(topic.getQuizzes()).doesNotContain(quizBack);
        assertThat(quizBack.getTopic1()).isNull();

        topic.quizzes(new HashSet<>(Set.of(quizBack)));
        assertThat(topic.getQuizzes()).containsOnly(quizBack);
        assertThat(quizBack.getTopic1()).isEqualTo(topic);

        topic.setQuizzes(new HashSet<>());
        assertThat(topic.getQuizzes()).doesNotContain(quizBack);
        assertThat(quizBack.getTopic1()).isNull();
    }

    @Test
    void topicTest() throws Exception {
        Topic topic = getTopicRandomSampleGenerator();
        Topic topicBack = getTopicRandomSampleGenerator();

        topic.addTopic(topicBack);
        assertThat(topic.getTopics()).containsOnly(topicBack);
        assertThat(topicBack.getTopic2()).isEqualTo(topic);

        topic.removeTopic(topicBack);
        assertThat(topic.getTopics()).doesNotContain(topicBack);
        assertThat(topicBack.getTopic2()).isNull();

        topic.topics(new HashSet<>(Set.of(topicBack)));
        assertThat(topic.getTopics()).containsOnly(topicBack);
        assertThat(topicBack.getTopic2()).isEqualTo(topic);

        topic.setTopics(new HashSet<>());
        assertThat(topic.getTopics()).doesNotContain(topicBack);
        assertThat(topicBack.getTopic2()).isNull();
    }

    @Test
    void courseTest() throws Exception {
        Topic topic = getTopicRandomSampleGenerator();
        Course courseBack = getCourseRandomSampleGenerator();

        topic.addCourse(courseBack);
        assertThat(topic.getCourses()).containsOnly(courseBack);
        assertThat(courseBack.getTopic3()).isEqualTo(topic);

        topic.removeCourse(courseBack);
        assertThat(topic.getCourses()).doesNotContain(courseBack);
        assertThat(courseBack.getTopic3()).isNull();

        topic.courses(new HashSet<>(Set.of(courseBack)));
        assertThat(topic.getCourses()).containsOnly(courseBack);
        assertThat(courseBack.getTopic3()).isEqualTo(topic);

        topic.setCourses(new HashSet<>());
        assertThat(topic.getCourses()).doesNotContain(courseBack);
        assertThat(courseBack.getTopic3()).isNull();
    }

    @Test
    void topic2Test() throws Exception {
        Topic topic = getTopicRandomSampleGenerator();
        Topic topicBack = getTopicRandomSampleGenerator();

        topic.setTopic2(topicBack);
        assertThat(topic.getTopic2()).isEqualTo(topicBack);

        topic.topic2(null);
        assertThat(topic.getTopic2()).isNull();
    }
}
