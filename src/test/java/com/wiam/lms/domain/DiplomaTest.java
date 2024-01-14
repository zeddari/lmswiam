package com.wiam.lms.domain;

import static com.wiam.lms.domain.DiplomaTestSamples.*;
import static com.wiam.lms.domain.UserCustomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.wiam.lms.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DiplomaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Diploma.class);
        Diploma diploma1 = getDiplomaSample1();
        Diploma diploma2 = new Diploma();
        assertThat(diploma1).isNotEqualTo(diploma2);

        diploma2.setId(diploma1.getId());
        assertThat(diploma1).isEqualTo(diploma2);

        diploma2 = getDiplomaSample2();
        assertThat(diploma1).isNotEqualTo(diploma2);
    }

    @Test
    void userCustom7Test() throws Exception {
        Diploma diploma = getDiplomaRandomSampleGenerator();
        UserCustom userCustomBack = getUserCustomRandomSampleGenerator();

        diploma.addUserCustom7(userCustomBack);
        assertThat(diploma.getUserCustom7s()).containsOnly(userCustomBack);
        assertThat(userCustomBack.getDiplomas()).containsOnly(diploma);

        diploma.removeUserCustom7(userCustomBack);
        assertThat(diploma.getUserCustom7s()).doesNotContain(userCustomBack);
        assertThat(userCustomBack.getDiplomas()).doesNotContain(diploma);

        diploma.userCustom7s(new HashSet<>(Set.of(userCustomBack)));
        assertThat(diploma.getUserCustom7s()).containsOnly(userCustomBack);
        assertThat(userCustomBack.getDiplomas()).containsOnly(diploma);

        diploma.setUserCustom7s(new HashSet<>());
        assertThat(diploma.getUserCustom7s()).doesNotContain(userCustomBack);
        assertThat(userCustomBack.getDiplomas()).doesNotContain(diploma);
    }
}
