package com.wiam.lms.domain;

import static com.wiam.lms.domain.CertificateTestSamples.*;
import static com.wiam.lms.domain.GroupTestSamples.*;
import static com.wiam.lms.domain.SiteTestSamples.*;
import static com.wiam.lms.domain.TopicTestSamples.*;
import static com.wiam.lms.domain.UserCustomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.wiam.lms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CertificateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Certificate.class);
        Certificate certificate1 = getCertificateSample1();
        Certificate certificate2 = new Certificate();
        assertThat(certificate1).isNotEqualTo(certificate2);

        certificate2.setId(certificate1.getId());
        assertThat(certificate1).isEqualTo(certificate2);

        certificate2 = getCertificateSample2();
        assertThat(certificate1).isNotEqualTo(certificate2);
    }

    @Test
    void site19Test() throws Exception {
        Certificate certificate = getCertificateRandomSampleGenerator();
        Site siteBack = getSiteRandomSampleGenerator();

        certificate.setSite19(siteBack);
        assertThat(certificate.getSite19()).isEqualTo(siteBack);

        certificate.site19(null);
        assertThat(certificate.getSite19()).isNull();
    }

    @Test
    void userCustom6Test() throws Exception {
        Certificate certificate = getCertificateRandomSampleGenerator();
        UserCustom userCustomBack = getUserCustomRandomSampleGenerator();

        certificate.setUserCustom6(userCustomBack);
        assertThat(certificate.getUserCustom6()).isEqualTo(userCustomBack);

        certificate.userCustom6(null);
        assertThat(certificate.getUserCustom6()).isNull();
    }

    @Test
    void comitteTest() throws Exception {
        Certificate certificate = getCertificateRandomSampleGenerator();
        Group groupBack = getGroupRandomSampleGenerator();

        certificate.setComitte(groupBack);
        assertThat(certificate.getComitte()).isEqualTo(groupBack);

        certificate.comitte(null);
        assertThat(certificate.getComitte()).isNull();
    }

    @Test
    void topic4Test() throws Exception {
        Certificate certificate = getCertificateRandomSampleGenerator();
        Topic topicBack = getTopicRandomSampleGenerator();

        certificate.setTopic4(topicBack);
        assertThat(certificate.getTopic4()).isEqualTo(topicBack);

        certificate.topic4(null);
        assertThat(certificate.getTopic4()).isNull();
    }
}
