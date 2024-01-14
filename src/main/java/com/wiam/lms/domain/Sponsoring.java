package com.wiam.lms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Sponsoring.
 */
@Entity
@Table(name = "sponsoring")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "sponsoring")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Sponsoring implements Serializable {

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

    @Lob
    @Column(name = "message")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String message;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_always")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isAlways;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sponsoring")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "enrolment", "sponsoring", "session", "currency" }, allowSetters = true)
    private Set<Payment> payments = new HashSet<>();

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
    private UserCustom sponsor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sponsorings", "typeProject" }, allowSetters = true)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sponsorings", "payments" }, allowSetters = true)
    private Currency currency;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Sponsoring id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRef() {
        return this.ref;
    }

    public Sponsoring ref(String ref) {
        this.setRef(ref);
        return this;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getMessage() {
        return this.message;
    }

    public Sponsoring message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Double getAmount() {
        return this.amount;
    }

    public Sponsoring amount(Double amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public Sponsoring startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public Sponsoring endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getIsAlways() {
        return this.isAlways;
    }

    public Sponsoring isAlways(Boolean isAlways) {
        this.setIsAlways(isAlways);
        return this;
    }

    public void setIsAlways(Boolean isAlways) {
        this.isAlways = isAlways;
    }

    public Set<Payment> getPayments() {
        return this.payments;
    }

    public void setPayments(Set<Payment> payments) {
        if (this.payments != null) {
            this.payments.forEach(i -> i.setSponsoring(null));
        }
        if (payments != null) {
            payments.forEach(i -> i.setSponsoring(this));
        }
        this.payments = payments;
    }

    public Sponsoring payments(Set<Payment> payments) {
        this.setPayments(payments);
        return this;
    }

    public Sponsoring addPayment(Payment payment) {
        this.payments.add(payment);
        payment.setSponsoring(this);
        return this;
    }

    public Sponsoring removePayment(Payment payment) {
        this.payments.remove(payment);
        payment.setSponsoring(null);
        return this;
    }

    public UserCustom getSponsor() {
        return this.sponsor;
    }

    public void setSponsor(UserCustom userCustom) {
        this.sponsor = userCustom;
    }

    public Sponsoring sponsor(UserCustom userCustom) {
        this.setSponsor(userCustom);
        return this;
    }

    public Project getProject() {
        return this.project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Sponsoring project(Project project) {
        this.setProject(project);
        return this;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Sponsoring currency(Currency currency) {
        this.setCurrency(currency);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sponsoring)) {
            return false;
        }
        return getId() != null && getId().equals(((Sponsoring) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sponsoring{" +
            "id=" + getId() +
            ", ref='" + getRef() + "'" +
            ", message='" + getMessage() + "'" +
            ", amount=" + getAmount() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", isAlways='" + getIsAlways() + "'" +
            "}";
    }
}
