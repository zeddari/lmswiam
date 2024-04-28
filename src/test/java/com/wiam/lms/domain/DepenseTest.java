package com.wiam.lms.domain;

import static com.wiam.lms.domain.DepenseTestSamples.*;
import static com.wiam.lms.domain.UserCustomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.wiam.lms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DepenseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Depense.class);
        Depense depense1 = getDepenseSample1();
        Depense depense2 = new Depense();
        assertThat(depense1).isNotEqualTo(depense2);

        depense2.setId(depense1.getId());
        assertThat(depense1).isEqualTo(depense2);

        depense2 = getDepenseSample2();
        assertThat(depense1).isNotEqualTo(depense2);
    }

    @Test
    void resourceTest() throws Exception {
        Depense depense = getDepenseRandomSampleGenerator();
        UserCustom userCustomBack = getUserCustomRandomSampleGenerator();

        depense.setResource(userCustomBack);
        assertThat(depense.getResource()).isEqualTo(userCustomBack);

        depense.resource(null);
        assertThat(depense.getResource()).isNull();
    }
}
