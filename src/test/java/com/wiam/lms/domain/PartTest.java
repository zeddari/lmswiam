package com.wiam.lms.domain;

import static com.wiam.lms.domain.CourseTestSamples.*;
import static com.wiam.lms.domain.PartTestSamples.*;
import static com.wiam.lms.domain.PartTestSamples.*;
import static com.wiam.lms.domain.ReviewTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.wiam.lms.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PartTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Part.class);
        Part part1 = getPartSample1();
        Part part2 = new Part();
        assertThat(part1).isNotEqualTo(part2);

        part2.setId(part1.getId());
        assertThat(part1).isEqualTo(part2);

        part2 = getPartSample2();
        assertThat(part1).isNotEqualTo(part2);
    }

    @Test
    void partTest() throws Exception {
        Part part = getPartRandomSampleGenerator();
        Part partBack = getPartRandomSampleGenerator();

        part.addPart(partBack);
        assertThat(part.getParts()).containsOnly(partBack);
        assertThat(partBack.getPart1()).isEqualTo(part);

        part.removePart(partBack);
        assertThat(part.getParts()).doesNotContain(partBack);
        assertThat(partBack.getPart1()).isNull();

        part.parts(new HashSet<>(Set.of(partBack)));
        assertThat(part.getParts()).containsOnly(partBack);
        assertThat(partBack.getPart1()).isEqualTo(part);

        part.setParts(new HashSet<>());
        assertThat(part.getParts()).doesNotContain(partBack);
        assertThat(partBack.getPart1()).isNull();
    }

    @Test
    void reviewTest() throws Exception {
        Part part = getPartRandomSampleGenerator();
        Review reviewBack = getReviewRandomSampleGenerator();

        part.addReview(reviewBack);
        assertThat(part.getReviews()).containsOnly(reviewBack);
        assertThat(reviewBack.getPart2()).isEqualTo(part);

        part.removeReview(reviewBack);
        assertThat(part.getReviews()).doesNotContain(reviewBack);
        assertThat(reviewBack.getPart2()).isNull();

        part.reviews(new HashSet<>(Set.of(reviewBack)));
        assertThat(part.getReviews()).containsOnly(reviewBack);
        assertThat(reviewBack.getPart2()).isEqualTo(part);

        part.setReviews(new HashSet<>());
        assertThat(part.getReviews()).doesNotContain(reviewBack);
        assertThat(reviewBack.getPart2()).isNull();
    }

    @Test
    void courseTest() throws Exception {
        Part part = getPartRandomSampleGenerator();
        Course courseBack = getCourseRandomSampleGenerator();

        part.setCourse(courseBack);
        assertThat(part.getCourse()).isEqualTo(courseBack);

        part.course(null);
        assertThat(part.getCourse()).isNull();
    }

    @Test
    void part1Test() throws Exception {
        Part part = getPartRandomSampleGenerator();
        Part partBack = getPartRandomSampleGenerator();

        part.setPart1(partBack);
        assertThat(part.getPart1()).isEqualTo(partBack);

        part.part1(null);
        assertThat(part.getPart1()).isNull();
    }
}
