package com.wiam.lms.domain;

import static com.wiam.lms.domain.NationalityTestSamples.*;
import static com.wiam.lms.domain.UserCustomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.wiam.lms.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class NationalityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Nationality.class);
        Nationality nationality1 = getNationalitySample1();
        Nationality nationality2 = new Nationality();
        assertThat(nationality1).isNotEqualTo(nationality2);

        nationality2.setId(nationality1.getId());
        assertThat(nationality1).isEqualTo(nationality2);

        nationality2 = getNationalitySample2();
        assertThat(nationality1).isNotEqualTo(nationality2);
    }

    @Test
    void userCustomTest() throws Exception {
        Nationality nationality = getNationalityRandomSampleGenerator();
        UserCustom userCustomBack = getUserCustomRandomSampleGenerator();

        nationality.addUserCustom(userCustomBack);
        assertThat(nationality.getUserCustoms()).containsOnly(userCustomBack);
        assertThat(userCustomBack.getNationality()).isEqualTo(nationality);

        nationality.removeUserCustom(userCustomBack);
        assertThat(nationality.getUserCustoms()).doesNotContain(userCustomBack);
        assertThat(userCustomBack.getNationality()).isNull();

        nationality.userCustoms(new HashSet<>(Set.of(userCustomBack)));
        assertThat(nationality.getUserCustoms()).containsOnly(userCustomBack);
        assertThat(userCustomBack.getNationality()).isEqualTo(nationality);

        nationality.setUserCustoms(new HashSet<>());
        assertThat(nationality.getUserCustoms()).doesNotContain(userCustomBack);
        assertThat(userCustomBack.getNationality()).isNull();
    }
}
