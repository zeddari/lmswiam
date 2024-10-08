package com.wiam.lms.domain;

import static com.wiam.lms.domain.SiteTestSamples.*;
import static com.wiam.lms.domain.TicketsTestSamples.*;
import static com.wiam.lms.domain.UserCustomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.wiam.lms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TicketsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tickets.class);
        Tickets tickets1 = getTicketsSample1();
        Tickets tickets2 = new Tickets();
        assertThat(tickets1).isNotEqualTo(tickets2);

        tickets2.setId(tickets1.getId());
        assertThat(tickets1).isEqualTo(tickets2);

        tickets2 = getTicketsSample2();
        assertThat(tickets1).isNotEqualTo(tickets2);
    }

    @Test
    void site18Test() throws Exception {
        Tickets tickets = getTicketsRandomSampleGenerator();
        Site siteBack = getSiteRandomSampleGenerator();

        tickets.setSite18(siteBack);
        assertThat(tickets.getSite18()).isEqualTo(siteBack);

        tickets.site18(null);
        assertThat(tickets.getSite18()).isNull();
    }

    @Test
    void userCustom5Test() throws Exception {
        Tickets tickets = getTicketsRandomSampleGenerator();
        UserCustom userCustomBack = getUserCustomRandomSampleGenerator();

        tickets.setUserCustom5(userCustomBack);
        assertThat(tickets.getUserCustom5()).isEqualTo(userCustomBack);

        tickets.userCustom5(null);
        assertThat(tickets.getUserCustom5()).isNull();
    }
}
