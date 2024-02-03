package com.wiam.lms.domain;

import static com.wiam.lms.domain.ProgressionTestSamples.*;
import static com.wiam.lms.domain.SessionInstanceTestSamples.*;
import static com.wiam.lms.domain.SessionLinkTestSamples.*;
import static com.wiam.lms.domain.SessionTestSamples.*;
import static com.wiam.lms.domain.SiteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.wiam.lms.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SessionInstanceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SessionInstance.class);
        SessionInstance sessionInstance1 = getSessionInstanceSample1();
        SessionInstance sessionInstance2 = new SessionInstance();
        assertThat(sessionInstance1).isNotEqualTo(sessionInstance2);

        sessionInstance2.setId(sessionInstance1.getId());
        assertThat(sessionInstance1).isEqualTo(sessionInstance2);

        sessionInstance2 = getSessionInstanceSample2();
        assertThat(sessionInstance1).isNotEqualTo(sessionInstance2);
    }

    @Test
    void progressionTest() throws Exception {
        SessionInstance sessionInstance = getSessionInstanceRandomSampleGenerator();
        Progression progressionBack = getProgressionRandomSampleGenerator();

        sessionInstance.addProgression(progressionBack);
        assertThat(sessionInstance.getProgressions()).containsOnly(progressionBack);
        assertThat(progressionBack.getSessionInstance()).isEqualTo(sessionInstance);

        sessionInstance.removeProgression(progressionBack);
        assertThat(sessionInstance.getProgressions()).doesNotContain(progressionBack);
        assertThat(progressionBack.getSessionInstance()).isNull();

        sessionInstance.progressions(new HashSet<>(Set.of(progressionBack)));
        assertThat(sessionInstance.getProgressions()).containsOnly(progressionBack);
        assertThat(progressionBack.getSessionInstance()).isEqualTo(sessionInstance);

        sessionInstance.setProgressions(new HashSet<>());
        assertThat(sessionInstance.getProgressions()).doesNotContain(progressionBack);
        assertThat(progressionBack.getSessionInstance()).isNull();
    }

    @Test
    void linksTest() throws Exception {
        SessionInstance sessionInstance = getSessionInstanceRandomSampleGenerator();
        SessionLink sessionLinkBack = getSessionLinkRandomSampleGenerator();

        sessionInstance.addLinks(sessionLinkBack);
        assertThat(sessionInstance.getLinks()).containsOnly(sessionLinkBack);

        sessionInstance.removeLinks(sessionLinkBack);
        assertThat(sessionInstance.getLinks()).doesNotContain(sessionLinkBack);

        sessionInstance.links(new HashSet<>(Set.of(sessionLinkBack)));
        assertThat(sessionInstance.getLinks()).containsOnly(sessionLinkBack);

        sessionInstance.setLinks(new HashSet<>());
        assertThat(sessionInstance.getLinks()).doesNotContain(sessionLinkBack);
    }

    @Test
    void site16Test() throws Exception {
        SessionInstance sessionInstance = getSessionInstanceRandomSampleGenerator();
        Site siteBack = getSiteRandomSampleGenerator();

        sessionInstance.setSite16(siteBack);
        assertThat(sessionInstance.getSite16()).isEqualTo(siteBack);

        sessionInstance.site16(null);
        assertThat(sessionInstance.getSite16()).isNull();
    }

    @Test
    void session1Test() throws Exception {
        SessionInstance sessionInstance = getSessionInstanceRandomSampleGenerator();
        Session sessionBack = getSessionRandomSampleGenerator();

        sessionInstance.setSession1(sessionBack);
        assertThat(sessionInstance.getSession1()).isEqualTo(sessionBack);

        sessionInstance.session1(null);
        assertThat(sessionInstance.getSession1()).isNull();
    }
}
