package com.wiam.lms.domain;

import static com.wiam.lms.domain.LanguageTestSamples.*;
import static com.wiam.lms.domain.UserCustomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.wiam.lms.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class LanguageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Language.class);
        Language language1 = getLanguageSample1();
        Language language2 = new Language();
        assertThat(language1).isNotEqualTo(language2);

        language2.setId(language1.getId());
        assertThat(language1).isEqualTo(language2);

        language2 = getLanguageSample2();
        assertThat(language1).isNotEqualTo(language2);
    }

    @Test
    void userCustom8Test() throws Exception {
        Language language = getLanguageRandomSampleGenerator();
        UserCustom userCustomBack = getUserCustomRandomSampleGenerator();

        language.addUserCustom8(userCustomBack);
        assertThat(language.getUserCustom8s()).containsOnly(userCustomBack);
        assertThat(userCustomBack.getLanguages()).containsOnly(language);

        language.removeUserCustom8(userCustomBack);
        assertThat(language.getUserCustom8s()).doesNotContain(userCustomBack);
        assertThat(userCustomBack.getLanguages()).doesNotContain(language);

        language.userCustom8s(new HashSet<>(Set.of(userCustomBack)));
        assertThat(language.getUserCustom8s()).containsOnly(userCustomBack);
        assertThat(userCustomBack.getLanguages()).containsOnly(language);

        language.setUserCustom8s(new HashSet<>());
        assertThat(language.getUserCustom8s()).doesNotContain(userCustomBack);
        assertThat(userCustomBack.getLanguages()).doesNotContain(language);
    }
}
