package com.wiam.lms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wiam.lms.domain.enumeration.EnrolementType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Enrolement.
 */
@Entity
@Table(name = "enrolement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "enrolement")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Enrolement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "ref", length = 100, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String ref;

    @Enumerated(EnumType.STRING)
    @Column(name = "enrolement_type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private EnrolementType enrolementType;

    @NotNull
    @Column(name = "enrolment_start_time", nullable = false)
    private ZonedDateTime enrolmentStartTime;

    @NotNull
    @Column(name = "enrolemnt_end_time", nullable = false)
    private ZonedDateTime enrolemntEndTime;

    @NotNull
    @Column(name = "is_active", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isActive;

    @Column(name = "activated_at")
    private ZonedDateTime activatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "enrolment")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "site9", "enrolment", "sponsoring", "session", "currency" }, allowSetters = true)
    private Set<Payment> payments = new HashSet<>();

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
    private Site site4;

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
    private UserCustom userCustom4;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "parts", "enrolements", "professors", "site1", "topic3" }, allowSetters = true)
    private Course course;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Enrolement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRef() {
        return this.ref;
    }

    public Enrolement ref(String ref) {
        this.setRef(ref);
        return this;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public EnrolementType getEnrolementType() {
        return this.enrolementType;
    }

    public Enrolement enrolementType(EnrolementType enrolementType) {
        this.setEnrolementType(enrolementType);
        return this;
    }

    public void setEnrolementType(EnrolementType enrolementType) {
        this.enrolementType = enrolementType;
    }

    public ZonedDateTime getEnrolmentStartTime() {
        return this.enrolmentStartTime;
    }

    public Enrolement enrolmentStartTime(ZonedDateTime enrolmentStartTime) {
        this.setEnrolmentStartTime(enrolmentStartTime);
        return this;
    }

    public void setEnrolmentStartTime(ZonedDateTime enrolmentStartTime) {
        this.enrolmentStartTime = enrolmentStartTime;
    }

    public ZonedDateTime getEnrolemntEndTime() {
        return this.enrolemntEndTime;
    }

    public Enrolement enrolemntEndTime(ZonedDateTime enrolemntEndTime) {
        this.setEnrolemntEndTime(enrolemntEndTime);
        return this;
    }

    public void setEnrolemntEndTime(ZonedDateTime enrolemntEndTime) {
        this.enrolemntEndTime = enrolemntEndTime;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Enrolement isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public ZonedDateTime getActivatedAt() {
        return this.activatedAt;
    }

    public Enrolement activatedAt(ZonedDateTime activatedAt) {
        this.setActivatedAt(activatedAt);
        return this;
    }

    public void setActivatedAt(ZonedDateTime activatedAt) {
        this.activatedAt = activatedAt;
    }

    public Set<Payment> getPayments() {
        return this.payments;
    }

    public void setPayments(Set<Payment> payments) {
        if (this.payments != null) {
            this.payments.forEach(i -> i.setEnrolment(null));
        }
        if (payments != null) {
            payments.forEach(i -> i.setEnrolment(this));
        }
        this.payments = payments;
    }

    public Enrolement payments(Set<Payment> payments) {
        this.setPayments(payments);
        return this;
    }

    public Enrolement addPayment(Payment payment) {
        this.payments.add(payment);
        payment.setEnrolment(this);
        return this;
    }

    public Enrolement removePayment(Payment payment) {
        this.payments.remove(payment);
        payment.setEnrolment(null);
        return this;
    }

    public Site getSite4() {
        return this.site4;
    }

    public void setSite4(Site site) {
        this.site4 = site;
    }

    public Enrolement site4(Site site) {
        this.setSite4(site);
        return this;
    }

    public UserCustom getUserCustom4() {
        return this.userCustom4;
    }

    public void setUserCustom4(UserCustom userCustom) {
        this.userCustom4 = userCustom;
    }

    public Enrolement userCustom4(UserCustom userCustom) {
        this.setUserCustom4(userCustom);
        return this;
    }

    public Course getCourse() {
        return this.course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Enrolement course(Course course) {
        this.setCourse(course);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Enrolement)) {
            return false;
        }
        return getId() != null && getId().equals(((Enrolement) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Enrolement{" +
            "id=" + getId() +
            ", ref='" + getRef() + "'" +
            ", enrolementType='" + getEnrolementType() + "'" +
            ", enrolmentStartTime='" + getEnrolmentStartTime() + "'" +
            ", enrolemntEndTime='" + getEnrolemntEndTime() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", activatedAt='" + getActivatedAt() + "'" +
            "}";
    }
}
