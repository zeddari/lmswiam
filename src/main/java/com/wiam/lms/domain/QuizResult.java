package com.wiam.lms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A QuizResult.
 */
@Entity
@Table(name = "quiz_result")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "quizresult")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuizResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "result", nullable = false)
    private Double result;

    @NotNull
    @Column(name = "submitted_at", nullable = false)
    private ZonedDateTime submittedAT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "classrooms",
            "courses",
            "parts",
            "reviews",
            "enrolements",
            "questions",
            "answers",
            "quizzes",
            "quizResults",
            "payments",
            "sponsorings",
            "groups",
            "projects",
            "userCustoms",
            "sessions",
            "sessionLinks",
            "sessionInstances",
            "progressions",
            "tickets",
            "certificates",
            "diplomas",
            "city",
        },
        allowSetters = true
    )
    private Site site8;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "quizResults", "groups", "questions", "site7", "topic1" }, allowSetters = true)
    private Quiz quiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "user",
            "certificates",
            "answers",
            "quizResults",
            "reviews",
            "enrolements",
            "progressions",
            "tickets",
            "sponsorings",
            "depenses",
            "diplomas",
            "languages",
            "site13",
            "country",
            "nationality",
            "job",
            "departement2",
            "groups",
            "courses",
            "sessions2s",
            "sessions3s",
        },
        allowSetters = true
    )
    private UserCustom userCustom2;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public QuizResult id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getResult() {
        return this.result;
    }

    public QuizResult result(Double result) {
        this.setResult(result);
        return this;
    }

    public void setResult(Double result) {
        this.result = result;
    }

    public ZonedDateTime getSubmittedAT() {
        return this.submittedAT;
    }

    public QuizResult submittedAT(ZonedDateTime submittedAT) {
        this.setSubmittedAT(submittedAT);
        return this;
    }

    public void setSubmittedAT(ZonedDateTime submittedAT) {
        this.submittedAT = submittedAT;
    }

    public Site getSite8() {
        return this.site8;
    }

    public void setSite8(Site site) {
        this.site8 = site;
    }

    public QuizResult site8(Site site) {
        this.setSite8(site);
        return this;
    }

    public Quiz getQuiz() {
        return this.quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public QuizResult quiz(Quiz quiz) {
        this.setQuiz(quiz);
        return this;
    }

    public UserCustom getUserCustom2() {
        return this.userCustom2;
    }

    public void setUserCustom2(UserCustom userCustom) {
        this.userCustom2 = userCustom;
    }

    public QuizResult userCustom2(UserCustom userCustom) {
        this.setUserCustom2(userCustom);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuizResult)) {
            return false;
        }
        return getId() != null && getId().equals(((QuizResult) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuizResult{" +
            "id=" + getId() +
            ", result=" + getResult() +
            ", submittedAT='" + getSubmittedAT() + "'" +
            "}";
    }
}
