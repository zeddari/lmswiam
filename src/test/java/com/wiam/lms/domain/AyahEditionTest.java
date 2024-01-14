package com.wiam.lms.domain;

import static com.wiam.lms.domain.AyahEditionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.wiam.lms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AyahEditionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AyahEdition.class);
        AyahEdition ayahEdition1 = getAyahEditionSample1();
        AyahEdition ayahEdition2 = new AyahEdition();
        assertThat(ayahEdition1).isNotEqualTo(ayahEdition2);

        ayahEdition2.setId(ayahEdition1.getId());
        assertThat(ayahEdition1).isEqualTo(ayahEdition2);

        ayahEdition2 = getAyahEditionSample2();
        assertThat(ayahEdition1).isNotEqualTo(ayahEdition2);
    }
}
