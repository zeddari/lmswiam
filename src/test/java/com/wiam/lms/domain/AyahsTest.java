package com.wiam.lms.domain;

import static com.wiam.lms.domain.AyahsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.wiam.lms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AyahsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ayahs.class);
        Ayahs ayahs1 = getAyahsSample1();
        Ayahs ayahs2 = new Ayahs();
        assertThat(ayahs1).isNotEqualTo(ayahs2);

        ayahs2.setId(ayahs1.getId());
        assertThat(ayahs1).isEqualTo(ayahs2);

        ayahs2 = getAyahsSample2();
        assertThat(ayahs1).isNotEqualTo(ayahs2);
    }
}
