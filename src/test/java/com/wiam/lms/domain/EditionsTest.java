package com.wiam.lms.domain;

import static com.wiam.lms.domain.EditionsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.wiam.lms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EditionsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Editions.class);
        Editions editions1 = getEditionsSample1();
        Editions editions2 = new Editions();
        assertThat(editions1).isNotEqualTo(editions2);

        editions2.setId(editions1.getId());
        assertThat(editions1).isEqualTo(editions2);

        editions2 = getEditionsSample2();
        assertThat(editions1).isNotEqualTo(editions2);
    }
}
