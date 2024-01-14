package com.wiam.lms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wiam.lms.domain.enumeration.ExamType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Quiz.
 */
@Entity
@Table(name = "quiz")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "quiz")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Quiz implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "quiz_type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private ExamType quizType;

    @NotNull
    @Size(max = 100)
    @Column(name = "quiz_title", length = 100, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String quizTitle;

    @Lob
    @Column(name = "quiz_description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String quizDescription;

    @NotNull
    @Column(name = "deadline", nullable = false)
    private ZonedDateTime deadline;

    @NotNull
    @Column(name = "is_active", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isActive;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "quiz")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "quiz", "userCustom2" }, allowSetters = true)
    private Set<QuizResult> quizResults = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "rel_quiz__groups", joinColumns = @JoinColumn(name = "quiz_id"), inverseJoinColumns = @JoinColumn(name = "groups_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "certificates", "groups", "elements", "group1", "quizzes", "sessions5s" }, allowSetters = true)
    private Set<Group> groups = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_quiz__questions",
        joinColumns = @JoinColumn(name = "quiz_id"),
        inverseJoinColumns = @JoinColumn(name = "questions_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "answers", "quizzes" }, allowSetters = true)
    private Set<Question> questions = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "certificates", "quizzes", "topics", "courses", "topic2" }, allowSetters = true)
    private Topic topic1;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Quiz id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ExamType getQuizType() {
        return this.quizType;
    }

    public Quiz quizType(ExamType quizType) {
        this.setQuizType(quizType);
        return this;
    }

    public void setQuizType(ExamType quizType) {
        this.quizType = quizType;
    }

    public String getQuizTitle() {
        return this.quizTitle;
    }

    public Quiz quizTitle(String quizTitle) {
        this.setQuizTitle(quizTitle);
        return this;
    }

    public void setQuizTitle(String quizTitle) {
        this.quizTitle = quizTitle;
    }

    public String getQuizDescription() {
        return this.quizDescription;
    }

    public Quiz quizDescription(String quizDescription) {
        this.setQuizDescription(quizDescription);
        return this;
    }

    public void setQuizDescription(String quizDescription) {
        this.quizDescription = quizDescription;
    }

    public ZonedDateTime getDeadline() {
        return this.deadline;
    }

    public Quiz deadline(ZonedDateTime deadline) {
        this.setDeadline(deadline);
        return this;
    }

    public void setDeadline(ZonedDateTime deadline) {
        this.deadline = deadline;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Quiz isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<QuizResult> getQuizResults() {
        return this.quizResults;
    }

    public void setQuizResults(Set<QuizResult> quizResults) {
        if (this.quizResults != null) {
            this.quizResults.forEach(i -> i.setQuiz(null));
        }
        if (quizResults != null) {
            quizResults.forEach(i -> i.setQuiz(this));
        }
        this.quizResults = quizResults;
    }

    public Quiz quizResults(Set<QuizResult> quizResults) {
        this.setQuizResults(quizResults);
        return this;
    }

    public Quiz addQuizResult(QuizResult quizResult) {
        this.quizResults.add(quizResult);
        quizResult.setQuiz(this);
        return this;
    }

    public Quiz removeQuizResult(QuizResult quizResult) {
        this.quizResults.remove(quizResult);
        quizResult.setQuiz(null);
        return this;
    }

    public Set<Group> getGroups() {
        return this.groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public Quiz groups(Set<Group> groups) {
        this.setGroups(groups);
        return this;
    }

    public Quiz addGroups(Group group) {
        this.groups.add(group);
        return this;
    }

    public Quiz removeGroups(Group group) {
        this.groups.remove(group);
        return this;
    }

    public Set<Question> getQuestions() {
        return this.questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public Quiz questions(Set<Question> questions) {
        this.setQuestions(questions);
        return this;
    }

    public Quiz addQuestions(Question question) {
        this.questions.add(question);
        return this;
    }

    public Quiz removeQuestions(Question question) {
        this.questions.remove(question);
        return this;
    }

    public Topic getTopic1() {
        return this.topic1;
    }

    public void setTopic1(Topic topic) {
        this.topic1 = topic;
    }

    public Quiz topic1(Topic topic) {
        this.setTopic1(topic);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Quiz)) {
            return false;
        }
        return getId() != null && getId().equals(((Quiz) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Quiz{" +
            "id=" + getId() +
            ", quizType='" + getQuizType() + "'" +
            ", quizTitle='" + getQuizTitle() + "'" +
            ", quizDescription='" + getQuizDescription() + "'" +
            ", deadline='" + getDeadline() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
