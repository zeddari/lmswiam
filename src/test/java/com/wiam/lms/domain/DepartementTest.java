package com.wiam.lms.domain;

import static com.wiam.lms.domain.DepartementTestSamples.*;
import static com.wiam.lms.domain.DepartementTestSamples.*;
import static com.wiam.lms.domain.UserCustomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.wiam.lms.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DepartementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Departement.class);
        Departement departement1 = getDepartementSample1();
        Departement departement2 = new Departement();
        assertThat(departement1).isNotEqualTo(departement2);

        departement2.setId(departement1.getId());
        assertThat(departement1).isEqualTo(departement2);

        departement2 = getDepartementSample2();
        assertThat(departement1).isNotEqualTo(departement2);
    }

    @Test
    void departementTest() throws Exception {
        Departement departement = getDepartementRandomSampleGenerator();
        Departement departementBack = getDepartementRandomSampleGenerator();

        departement.addDepartement(departementBack);
        assertThat(departement.getDepartements()).containsOnly(departementBack);
        assertThat(departementBack.getDepartement1()).isEqualTo(departement);

        departement.removeDepartement(departementBack);
        assertThat(departement.getDepartements()).doesNotContain(departementBack);
        assertThat(departementBack.getDepartement1()).isNull();

        departement.departements(new HashSet<>(Set.of(departementBack)));
        assertThat(departement.getDepartements()).containsOnly(departementBack);
        assertThat(departementBack.getDepartement1()).isEqualTo(departement);

        departement.setDepartements(new HashSet<>());
        assertThat(departement.getDepartements()).doesNotContain(departementBack);
        assertThat(departementBack.getDepartement1()).isNull();
    }

    @Test
    void userCustomTest() throws Exception {
        Departement departement = getDepartementRandomSampleGenerator();
        UserCustom userCustomBack = getUserCustomRandomSampleGenerator();

        departement.addUserCustom(userCustomBack);
        assertThat(departement.getUserCustoms()).containsOnly(userCustomBack);
        assertThat(userCustomBack.getDepartement2()).isEqualTo(departement);

        departement.removeUserCustom(userCustomBack);
        assertThat(departement.getUserCustoms()).doesNotContain(userCustomBack);
        assertThat(userCustomBack.getDepartement2()).isNull();

        departement.userCustoms(new HashSet<>(Set.of(userCustomBack)));
        assertThat(departement.getUserCustoms()).containsOnly(userCustomBack);
        assertThat(userCustomBack.getDepartement2()).isEqualTo(departement);

        departement.setUserCustoms(new HashSet<>());
        assertThat(departement.getUserCustoms()).doesNotContain(userCustomBack);
        assertThat(userCustomBack.getDepartement2()).isNull();
    }

    @Test
    void departement1Test() throws Exception {
        Departement departement = getDepartementRandomSampleGenerator();
        Departement departementBack = getDepartementRandomSampleGenerator();

        departement.setDepartement1(departementBack);
        assertThat(departement.getDepartement1()).isEqualTo(departementBack);

        departement.departement1(null);
        assertThat(departement.getDepartement1()).isNull();
    }
}
