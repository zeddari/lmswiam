package com.wiam.lms.domain;

import static com.wiam.lms.domain.PartTestSamples.*;
import static com.wiam.lms.domain.ReviewTestSamples.*;
import static com.wiam.lms.domain.SiteTestSamples.*;
import static com.wiam.lms.domain.UserCustomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.wiam.lms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReviewTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Review.class);
        Review review1 = getReviewSample1();
        Review review2 = new Review();
        assertThat(review1).isNotEqualTo(review2);

        review2.setId(review1.getId());
        assertThat(review1).isEqualTo(review2);

        review2 = getReviewSample2();
        assertThat(review1).isNotEqualTo(review2);
    }

    @Test
    void site3Test() throws Exception {
        Review review = getReviewRandomSampleGenerator();
        Site siteBack = getSiteRandomSampleGenerator();

        review.setSite3(siteBack);
        assertThat(review.getSite3()).isEqualTo(siteBack);

        review.site3(null);
        assertThat(review.getSite3()).isNull();
    }

    @Test
    void part2Test() throws Exception {
        Review review = getReviewRandomSampleGenerator();
        Part partBack = getPartRandomSampleGenerator();

        review.setPart2(partBack);
        assertThat(review.getPart2()).isEqualTo(partBack);

        review.part2(null);
        assertThat(review.getPart2()).isNull();
    }

    @Test
    void userCustom3Test() throws Exception {
        Review review = getReviewRandomSampleGenerator();
        UserCustom userCustomBack = getUserCustomRandomSampleGenerator();

        review.setUserCustom3(userCustomBack);
        assertThat(review.getUserCustom3()).isEqualTo(userCustomBack);

        review.userCustom3(null);
        assertThat(review.getUserCustom3()).isNull();
    }
}
