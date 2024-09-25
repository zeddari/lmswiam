package com.wiam.lms.domain;

import static com.wiam.lms.domain.CountryTestSamples.*;
import static com.wiam.lms.domain.UserCustomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.wiam.lms.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CountryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Country.class);
    }

    @Test
    void userCustomTest() throws Exception {
        Country country = getCountryRandomSampleGenerator();
        UserCustom userCustomBack = getUserCustomRandomSampleGenerator();

        country.addUserCustom(userCustomBack);
        assertThat(country.getUserCustoms()).containsOnly(userCustomBack);
        assertThat(userCustomBack.getCountry()).isEqualTo(country);

        country.removeUserCustom(userCustomBack);
        assertThat(country.getUserCustoms()).doesNotContain(userCustomBack);
        assertThat(userCustomBack.getCountry()).isNull();

        country.userCustoms(new HashSet<>(Set.of(userCustomBack)));
        assertThat(country.getUserCustoms()).containsOnly(userCustomBack);
        assertThat(userCustomBack.getCountry()).isEqualTo(country);

        country.setUserCustoms(new HashSet<>());
        assertThat(country.getUserCustoms()).doesNotContain(userCustomBack);
        assertThat(userCustomBack.getCountry()).isNull();
    }
}
