package com.wiam.lms.domain;

import static com.wiam.lms.domain.JuzsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.wiam.lms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JuzsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Juzs.class);
        Juzs juzs1 = getJuzsSample1();
        Juzs juzs2 = new Juzs();
        assertThat(juzs1).isNotEqualTo(juzs2);

        juzs2.setId(juzs1.getId());
        assertThat(juzs1).isEqualTo(juzs2);

        juzs2 = getJuzsSample2();
        assertThat(juzs1).isNotEqualTo(juzs2);
    }
}
