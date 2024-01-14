package com.wiam.lms.domain;

import static com.wiam.lms.domain.SessionInstanceTestSamples.*;
import static com.wiam.lms.domain.SessionLinkTestSamples.*;
import static com.wiam.lms.domain.SessionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.wiam.lms.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SessionLinkTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SessionLink.class);
        SessionLink sessionLink1 = getSessionLinkSample1();
        SessionLink sessionLink2 = new SessionLink();
        assertThat(sessionLink1).isNotEqualTo(sessionLink2);

        sessionLink2.setId(sessionLink1.getId());
        assertThat(sessionLink1).isEqualTo(sessionLink2);

        sessionLink2 = getSessionLinkSample2();
        assertThat(sessionLink1).isNotEqualTo(sessionLink2);
    }

    @Test
    void sessions4Test() throws Exception {
        SessionLink sessionLink = getSessionLinkRandomSampleGenerator();
        SessionInstance sessionInstanceBack = getSessionInstanceRandomSampleGenerator();

        sessionLink.addSessions4(sessionInstanceBack);
        assertThat(sessionLink.getSessions4s()).containsOnly(sessionInstanceBack);
        assertThat(sessionInstanceBack.getLinks()).containsOnly(sessionLink);

        sessionLink.removeSessions4(sessionInstanceBack);
        assertThat(sessionLink.getSessions4s()).doesNotContain(sessionInstanceBack);
        assertThat(sessionInstanceBack.getLinks()).doesNotContain(sessionLink);

        sessionLink.sessions4s(new HashSet<>(Set.of(sessionInstanceBack)));
        assertThat(sessionLink.getSessions4s()).containsOnly(sessionInstanceBack);
        assertThat(sessionInstanceBack.getLinks()).containsOnly(sessionLink);

        sessionLink.setSessions4s(new HashSet<>());
        assertThat(sessionLink.getSessions4s()).doesNotContain(sessionInstanceBack);
        assertThat(sessionInstanceBack.getLinks()).doesNotContain(sessionLink);
    }

    @Test
    void sessions7Test() throws Exception {
        SessionLink sessionLink = getSessionLinkRandomSampleGenerator();
        Session sessionBack = getSessionRandomSampleGenerator();

        sessionLink.addSessions7(sessionBack);
        assertThat(sessionLink.getSessions7s()).containsOnly(sessionBack);
        assertThat(sessionBack.getLinks()).containsOnly(sessionLink);

        sessionLink.removeSessions7(sessionBack);
        assertThat(sessionLink.getSessions7s()).doesNotContain(sessionBack);
        assertThat(sessionBack.getLinks()).doesNotContain(sessionLink);

        sessionLink.sessions7s(new HashSet<>(Set.of(sessionBack)));
        assertThat(sessionLink.getSessions7s()).containsOnly(sessionBack);
        assertThat(sessionBack.getLinks()).containsOnly(sessionLink);

        sessionLink.setSessions7s(new HashSet<>());
        assertThat(sessionLink.getSessions7s()).doesNotContain(sessionBack);
        assertThat(sessionBack.getLinks()).doesNotContain(sessionLink);
    }
}
