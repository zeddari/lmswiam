package com.wiam.lms.domain;

import static com.wiam.lms.domain.ProjectTestSamples.*;
import static com.wiam.lms.domain.TypeProjectTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.wiam.lms.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TypeProjectTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeProject.class);
        TypeProject typeProject1 = getTypeProjectSample1();
        TypeProject typeProject2 = new TypeProject();
        assertThat(typeProject1).isNotEqualTo(typeProject2);

        typeProject2.setId(typeProject1.getId());
        assertThat(typeProject1).isEqualTo(typeProject2);

        typeProject2 = getTypeProjectSample2();
        assertThat(typeProject1).isNotEqualTo(typeProject2);
    }

    @Test
    void projectTest() throws Exception {
        TypeProject typeProject = getTypeProjectRandomSampleGenerator();
        Project projectBack = getProjectRandomSampleGenerator();

        typeProject.addProject(projectBack);
        assertThat(typeProject.getProjects()).containsOnly(projectBack);
        assertThat(projectBack.getTypeProject()).isEqualTo(typeProject);

        typeProject.removeProject(projectBack);
        assertThat(typeProject.getProjects()).doesNotContain(projectBack);
        assertThat(projectBack.getTypeProject()).isNull();

        typeProject.projects(new HashSet<>(Set.of(projectBack)));
        assertThat(typeProject.getProjects()).containsOnly(projectBack);
        assertThat(projectBack.getTypeProject()).isEqualTo(typeProject);

        typeProject.setProjects(new HashSet<>());
        assertThat(typeProject.getProjects()).doesNotContain(projectBack);
        assertThat(projectBack.getTypeProject()).isNull();
    }
}
