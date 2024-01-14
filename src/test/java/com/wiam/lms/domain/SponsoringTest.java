package com.wiam.lms.domain;

import static com.wiam.lms.domain.CurrencyTestSamples.*;
import static com.wiam.lms.domain.PaymentTestSamples.*;
import static com.wiam.lms.domain.ProjectTestSamples.*;
import static com.wiam.lms.domain.SponsoringTestSamples.*;
import static com.wiam.lms.domain.UserCustomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.wiam.lms.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SponsoringTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sponsoring.class);
        Sponsoring sponsoring1 = getSponsoringSample1();
        Sponsoring sponsoring2 = new Sponsoring();
        assertThat(sponsoring1).isNotEqualTo(sponsoring2);

        sponsoring2.setId(sponsoring1.getId());
        assertThat(sponsoring1).isEqualTo(sponsoring2);

        sponsoring2 = getSponsoringSample2();
        assertThat(sponsoring1).isNotEqualTo(sponsoring2);
    }

    @Test
    void paymentTest() throws Exception {
        Sponsoring sponsoring = getSponsoringRandomSampleGenerator();
        Payment paymentBack = getPaymentRandomSampleGenerator();

        sponsoring.addPayment(paymentBack);
        assertThat(sponsoring.getPayments()).containsOnly(paymentBack);
        assertThat(paymentBack.getSponsoring()).isEqualTo(sponsoring);

        sponsoring.removePayment(paymentBack);
        assertThat(sponsoring.getPayments()).doesNotContain(paymentBack);
        assertThat(paymentBack.getSponsoring()).isNull();

        sponsoring.payments(new HashSet<>(Set.of(paymentBack)));
        assertThat(sponsoring.getPayments()).containsOnly(paymentBack);
        assertThat(paymentBack.getSponsoring()).isEqualTo(sponsoring);

        sponsoring.setPayments(new HashSet<>());
        assertThat(sponsoring.getPayments()).doesNotContain(paymentBack);
        assertThat(paymentBack.getSponsoring()).isNull();
    }

    @Test
    void sponsorTest() throws Exception {
        Sponsoring sponsoring = getSponsoringRandomSampleGenerator();
        UserCustom userCustomBack = getUserCustomRandomSampleGenerator();

        sponsoring.setSponsor(userCustomBack);
        assertThat(sponsoring.getSponsor()).isEqualTo(userCustomBack);

        sponsoring.sponsor(null);
        assertThat(sponsoring.getSponsor()).isNull();
    }

    @Test
    void projectTest() throws Exception {
        Sponsoring sponsoring = getSponsoringRandomSampleGenerator();
        Project projectBack = getProjectRandomSampleGenerator();

        sponsoring.setProject(projectBack);
        assertThat(sponsoring.getProject()).isEqualTo(projectBack);

        sponsoring.project(null);
        assertThat(sponsoring.getProject()).isNull();
    }

    @Test
    void currencyTest() throws Exception {
        Sponsoring sponsoring = getSponsoringRandomSampleGenerator();
        Currency currencyBack = getCurrencyRandomSampleGenerator();

        sponsoring.setCurrency(currencyBack);
        assertThat(sponsoring.getCurrency()).isEqualTo(currencyBack);

        sponsoring.currency(null);
        assertThat(sponsoring.getCurrency()).isNull();
    }
}
