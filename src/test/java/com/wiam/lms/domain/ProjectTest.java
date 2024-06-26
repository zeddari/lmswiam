package com.wiam.lms.domain;

import static com.wiam.lms.domain.ProjectTestSamples.*;
import static com.wiam.lms.domain.SiteTestSamples.*;
import static com.wiam.lms.domain.SponsoringTestSamples.*;
import static com.wiam.lms.domain.TypeProjectTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.wiam.lms.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProjectTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Project.class);
        Project project1 = getProjectSample1();
        Project project2 = new Project();
        assertThat(project1).isNotEqualTo(project2);

        project2.setId(project1.getId());
        assertThat(project1).isEqualTo(project2);

        project2 = getProjectSample2();
        assertThat(project1).isNotEqualTo(project2);
    }

    @Test
    void sponsoringTest() throws Exception {
        Project project = getProjectRandomSampleGenerator();
        Sponsoring sponsoringBack = getSponsoringRandomSampleGenerator();

        project.addSponsoring(sponsoringBack);
        assertThat(project.getSponsorings()).containsOnly(sponsoringBack);
        assertThat(sponsoringBack.getProject()).isEqualTo(project);

        project.removeSponsoring(sponsoringBack);
        assertThat(project.getSponsorings()).doesNotContain(sponsoringBack);
        assertThat(sponsoringBack.getProject()).isNull();

        project.sponsorings(new HashSet<>(Set.of(sponsoringBack)));
        assertThat(project.getSponsorings()).containsOnly(sponsoringBack);
        assertThat(sponsoringBack.getProject()).isEqualTo(project);

        project.setSponsorings(new HashSet<>());
        assertThat(project.getSponsorings()).doesNotContain(sponsoringBack);
        assertThat(sponsoringBack.getProject()).isNull();
    }

    @Test
    void site12Test() throws Exception {
        Project project = getProjectRandomSampleGenerator();
        Site siteBack = getSiteRandomSampleGenerator();

        project.setSite12(siteBack);
        assertThat(project.getSite12()).isEqualTo(siteBack);

        project.site12(null);
        assertThat(project.getSite12()).isNull();
    }

    @Test
    void typeProjectTest() throws Exception {
        Project project = getProjectRandomSampleGenerator();
        TypeProject typeProjectBack = getTypeProjectRandomSampleGenerator();

        project.setTypeProject(typeProjectBack);
        assertThat(project.getTypeProject()).isEqualTo(typeProjectBack);

        project.typeProject(null);
        assertThat(project.getTypeProject()).isNull();
    }
}
