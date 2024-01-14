package com.wiam.lms.domain;

import static com.wiam.lms.domain.SurahsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.wiam.lms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SurahsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Surahs.class);
        Surahs surahs1 = getSurahsSample1();
        Surahs surahs2 = new Surahs();
        assertThat(surahs1).isNotEqualTo(surahs2);

        surahs2.setId(surahs1.getId());
        assertThat(surahs1).isEqualTo(surahs2);

        surahs2 = getSurahsSample2();
        assertThat(surahs1).isNotEqualTo(surahs2);
    }
}
