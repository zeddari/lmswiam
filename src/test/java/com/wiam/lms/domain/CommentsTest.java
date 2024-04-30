package com.wiam.lms.domain;

import static com.wiam.lms.domain.CommentsTestSamples.*;
import static com.wiam.lms.domain.SessionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.wiam.lms.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CommentsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Comments.class);
        Comments comments1 = getCommentsSample1();
        Comments comments2 = new Comments();
        assertThat(comments1).isNotEqualTo(comments2);

        comments2.setId(comments1.getId());
        assertThat(comments1).isEqualTo(comments2);

        comments2 = getCommentsSample2();
        assertThat(comments1).isNotEqualTo(comments2);
    }

    @Test
    void sessions8Test() throws Exception {
        Comments comments = getCommentsRandomSampleGenerator();
        Session sessionBack = getSessionRandomSampleGenerator();

        comments.addSessions8(sessionBack);
        assertThat(comments.getSessions8s()).containsOnly(sessionBack);

        comments.removeSessions8(sessionBack);
        assertThat(comments.getSessions8s()).doesNotContain(sessionBack);

        comments.sessions8s(new HashSet<>(Set.of(sessionBack)));
        assertThat(comments.getSessions8s()).containsOnly(sessionBack);

        comments.setSessions8s(new HashSet<>());
        assertThat(comments.getSessions8s()).doesNotContain(sessionBack);
    }
}
