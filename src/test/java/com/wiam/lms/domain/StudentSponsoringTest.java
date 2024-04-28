package com.wiam.lms.domain;

import static com.wiam.lms.domain.StudentSponsoringTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.wiam.lms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentSponsoringTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentSponsoring.class);
        StudentSponsoring studentSponsoring1 = getStudentSponsoringSample1();
        StudentSponsoring studentSponsoring2 = new StudentSponsoring();
        assertThat(studentSponsoring1).isNotEqualTo(studentSponsoring2);

        studentSponsoring2.setId(studentSponsoring1.getId());
        assertThat(studentSponsoring1).isEqualTo(studentSponsoring2);

        studentSponsoring2 = getStudentSponsoringSample2();
        assertThat(studentSponsoring1).isNotEqualTo(studentSponsoring2);
    }
}
