package com.wiam.lms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Question.
 */
@Entity
@Table(name = "question")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "question")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "question", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String question;

    @Lob
    @Column(name = "details")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String details;

    @Lob
    @Column(name = "a_1", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String a1;

    @NotNull
    @Column(name = "a_1_v", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean a1v;

    @Lob
    @Column(name = "a_2", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String a2;

    @NotNull
    @Column(name = "a_2_v", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean a2v;

    @Lob
    @Column(name = "a_3")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String a3;

    @Column(name = "a_3_v")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean a3v;

    @Lob
    @Column(name = "a_4")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String a4;

    @Column(name = "a_4_v")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean a4v;

    @NotNull
    @Column(name = "is_active", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isActive;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "question", "userCustom1" }, allowSetters = true)
    private Set<Answer> answers = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "questions")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "quizResults", "groups", "questions", "topic1" }, allowSetters = true)
    private Set<Quiz> quizzes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Question id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return this.question;
    }

    public Question question(String question) {
        this.setQuestion(question);
        return this;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getDetails() {
        return this.details;
    }

    public Question details(String details) {
        this.setDetails(details);
        return this;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String geta1() {
        return this.a1;
    }

    public Question a1(String a1) {
        this.seta1(a1);
        return this;
    }

    public void seta1(String a1) {
        this.a1 = a1;
    }

    public Boolean geta1v() {
        return this.a1v;
    }

    public Question a1v(Boolean a1v) {
        this.seta1v(a1v);
        return this;
    }

    public void seta1v(Boolean a1v) {
        this.a1v = a1v;
    }

    public String geta2() {
        return this.a2;
    }

    public Question a2(String a2) {
        this.seta2(a2);
        return this;
    }

    public void seta2(String a2) {
        this.a2 = a2;
    }

    public Boolean geta2v() {
        return this.a2v;
    }

    public Question a2v(Boolean a2v) {
        this.seta2v(a2v);
        return this;
    }

    public void seta2v(Boolean a2v) {
        this.a2v = a2v;
    }

    public String geta3() {
        return this.a3;
    }

    public Question a3(String a3) {
        this.seta3(a3);
        return this;
    }

    public void seta3(String a3) {
        this.a3 = a3;
    }

    public Boolean geta3v() {
        return this.a3v;
    }

    public Question a3v(Boolean a3v) {
        this.seta3v(a3v);
        return this;
    }

    public void seta3v(Boolean a3v) {
        this.a3v = a3v;
    }

    public String geta4() {
        return this.a4;
    }

    public Question a4(String a4) {
        this.seta4(a4);
        return this;
    }

    public void seta4(String a4) {
        this.a4 = a4;
    }

    public Boolean geta4v() {
        return this.a4v;
    }

    public Question a4v(Boolean a4v) {
        this.seta4v(a4v);
        return this;
    }

    public void seta4v(Boolean a4v) {
        this.a4v = a4v;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Question isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<Answer> getAnswers() {
        return this.answers;
    }

    public void setAnswers(Set<Answer> answers) {
        if (this.answers != null) {
            this.answers.forEach(i -> i.setQuestion(null));
        }
        if (answers != null) {
            answers.forEach(i -> i.setQuestion(this));
        }
        this.answers = answers;
    }

    public Question answers(Set<Answer> answers) {
        this.setAnswers(answers);
        return this;
    }

    public Question addAnswer(Answer answer) {
        this.answers.add(answer);
        answer.setQuestion(this);
        return this;
    }

    public Question removeAnswer(Answer answer) {
        this.answers.remove(answer);
        answer.setQuestion(null);
        return this;
    }

    public Set<Quiz> getQuizzes() {
        return this.quizzes;
    }

    public void setQuizzes(Set<Quiz> quizzes) {
        if (this.quizzes != null) {
            this.quizzes.forEach(i -> i.removeQuestions(this));
        }
        if (quizzes != null) {
            quizzes.forEach(i -> i.addQuestions(this));
        }
        this.quizzes = quizzes;
    }

    public Question quizzes(Set<Quiz> quizzes) {
        this.setQuizzes(quizzes);
        return this;
    }

    public Question addQuiz(Quiz quiz) {
        this.quizzes.add(quiz);
        quiz.getQuestions().add(this);
        return this;
    }

    public Question removeQuiz(Quiz quiz) {
        this.quizzes.remove(quiz);
        quiz.getQuestions().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Question)) {
            return false;
        }
        return getId() != null && getId().equals(((Question) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Question{" +
            "id=" + getId() +
            ", question='" + getQuestion() + "'" +
            ", details='" + getDetails() + "'" +
            ", a1='" + geta1() + "'" +
            ", a1v='" + geta1v() + "'" +
            ", a2='" + geta2() + "'" +
            ", a2v='" + geta2v() + "'" +
            ", a3='" + geta3() + "'" +
            ", a3v='" + geta3v() + "'" +
            ", a4='" + geta4() + "'" +
            ", a4v='" + geta4v() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
