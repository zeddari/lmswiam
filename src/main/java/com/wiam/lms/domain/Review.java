package com.wiam.lms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Review.
 */
@Entity
@Table(name = "review")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "review")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Review implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "body", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String body;

    @Min(value = 1)
    @Max(value = 5)
    @Column(name = "rating")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer rating;

    @NotNull
    @Column(name = "review_date", nullable = false)
    private ZonedDateTime reviewDate;

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
    private Site site3;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "parts", "reviews", "site2", "course", "part1" }, allowSetters = true)
    private Part part2;

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
    private UserCustom userCustom3;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Review id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return this.body;
    }

    public Review body(String body) {
        this.setBody(body);
        return this;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getRating() {
        return this.rating;
    }

    public Review rating(Integer rating) {
        this.setRating(rating);
        return this;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public ZonedDateTime getReviewDate() {
        return this.reviewDate;
    }

    public Review reviewDate(ZonedDateTime reviewDate) {
        this.setReviewDate(reviewDate);
        return this;
    }

    public void setReviewDate(ZonedDateTime reviewDate) {
        this.reviewDate = reviewDate;
    }

    public Site getSite3() {
        return this.site3;
    }

    public void setSite3(Site site) {
        this.site3 = site;
    }

    public Review site3(Site site) {
        this.setSite3(site);
        return this;
    }

    public Part getPart2() {
        return this.part2;
    }

    public void setPart2(Part part) {
        this.part2 = part;
    }

    public Review part2(Part part) {
        this.setPart2(part);
        return this;
    }

    public UserCustom getUserCustom3() {
        return this.userCustom3;
    }

    public void setUserCustom3(UserCustom userCustom) {
        this.userCustom3 = userCustom;
    }

    public Review userCustom3(UserCustom userCustom) {
        this.setUserCustom3(userCustom);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Review)) {
            return false;
        }
        return getId() != null && getId().equals(((Review) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Review{" +
            "id=" + getId() +
            ", body='" + getBody() + "'" +
            ", rating=" + getRating() +
            ", reviewDate='" + getReviewDate() + "'" +
            "}";
    }
}
