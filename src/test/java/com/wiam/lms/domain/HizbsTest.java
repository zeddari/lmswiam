package com.wiam.lms.domain;

import static com.wiam.lms.domain.HizbsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.wiam.lms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HizbsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Hizbs.class);
        Hizbs hizbs1 = getHizbsSample1();
        Hizbs hizbs2 = new Hizbs();
        assertThat(hizbs1).isNotEqualTo(hizbs2);

        hizbs2.setId(hizbs1.getId());
        assertThat(hizbs1).isEqualTo(hizbs2);

        hizbs2 = getHizbsSample2();
        assertThat(hizbs1).isNotEqualTo(hizbs2);
    }
}
