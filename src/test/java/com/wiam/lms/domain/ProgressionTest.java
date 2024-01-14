package com.wiam.lms.domain;

import static com.wiam.lms.domain.ProgressionTestSamples.*;
import static com.wiam.lms.domain.SessionInstanceTestSamples.*;
import static com.wiam.lms.domain.UserCustomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.wiam.lms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProgressionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Progression.class);
        Progression progression1 = getProgressionSample1();
        Progression progression2 = new Progression();
        assertThat(progression1).isNotEqualTo(progression2);

        progression2.setId(progression1.getId());
        assertThat(progression1).isEqualTo(progression2);

        progression2 = getProgressionSample2();
        assertThat(progression1).isNotEqualTo(progression2);
    }

    @Test
    void sessionInstanceTest() throws Exception {
        Progression progression = getProgressionRandomSampleGenerator();
        SessionInstance sessionInstanceBack = getSessionInstanceRandomSampleGenerator();

        progression.setSessionInstance(sessionInstanceBack);
        assertThat(progression.getSessionInstance()).isEqualTo(sessionInstanceBack);

        progression.sessionInstance(null);
        assertThat(progression.getSessionInstance()).isNull();
    }

    @Test
    void studentTest() throws Exception {
        Progression progression = getProgressionRandomSampleGenerator();
        UserCustom userCustomBack = getUserCustomRandomSampleGenerator();

        progression.setStudent(userCustomBack);
        assertThat(progression.getStudent()).isEqualTo(userCustomBack);

        progression.student(null);
        assertThat(progression.getStudent()).isNull();
    }
}
