package com.wiam.lms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wiam.lms.domain.enumeration.PaymentMode;
import com.wiam.lms.domain.enumeration.PaymentSide;
import com.wiam.lms.domain.enumeration.PaymentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "payment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "amount", nullable = false)
    private Double amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private PaymentMode paymentMethod;

    @NotNull
    @Column(name = "paied_by", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String paiedBy;

    @Lob
    @Column(name = "proof")
    private byte[] proof;

    @Column(name = "proof_content_type")
    private String proofContentType;

    @NotNull
    @Column(name = "paid_at", nullable = false)
    private ZonedDateTime paidAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private PaymentType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "side", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private PaymentSide side;

    @NotNull
    @Column(name = "validity_start_time", nullable = false)
    private ZonedDateTime validityStartTime;

    @NotNull
    @Column(name = "validity_end_time", nullable = false)
    private ZonedDateTime validityEndTime;

    @Lob
    @Column(name = "details")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String details;

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
    private Site site9;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "payments", "site4", "userCustom4", "course" }, allowSetters = true)
    private Enrolement enrolment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "payments", "site10", "sponsor", "project", "currency" }, allowSetters = true)
    private Sponsoring sponsoring;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "sessionInstances", "payments", "classrooms", "groups", "professors", "employees", "links", "site14" },
        allowSetters = true
    )
    private Session session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sponsorings", "payments" }, allowSetters = true)
    private Currency currency;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Payment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return this.amount;
    }

    public Payment amount(Double amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public PaymentMode getPaymentMethod() {
        return this.paymentMethod;
    }

    public Payment paymentMethod(PaymentMode paymentMethod) {
        this.setPaymentMethod(paymentMethod);
        return this;
    }

    public void setPaymentMethod(PaymentMode paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaiedBy() {
        return this.paiedBy;
    }

    public Payment paiedBy(String paiedBy) {
        this.setPaiedBy(paiedBy);
        return this;
    }

    public void setPaiedBy(String paiedBy) {
        this.paiedBy = paiedBy;
    }

    public byte[] getProof() {
        return this.proof;
    }

    public Payment proof(byte[] proof) {
        this.setProof(proof);
        return this;
    }

    public void setProof(byte[] proof) {
        this.proof = proof;
    }

    public String getProofContentType() {
        return this.proofContentType;
    }

    public Payment proofContentType(String proofContentType) {
        this.proofContentType = proofContentType;
        return this;
    }

    public void setProofContentType(String proofContentType) {
        this.proofContentType = proofContentType;
    }

    public ZonedDateTime getPaidAt() {
        return this.paidAt;
    }

    public Payment paidAt(ZonedDateTime paidAt) {
        this.setPaidAt(paidAt);
        return this;
    }

    public void setPaidAt(ZonedDateTime paidAt) {
        this.paidAt = paidAt;
    }

    public PaymentType getType() {
        return this.type;
    }

    public Payment type(PaymentType type) {
        this.setType(type);
        return this;
    }

    public void setType(PaymentType type) {
        this.type = type;
    }

    public PaymentSide getSide() {
        return this.side;
    }

    public Payment side(PaymentSide side) {
        this.setSide(side);
        return this;
    }

    public void setSide(PaymentSide side) {
        this.side = side;
    }

    public ZonedDateTime getValidityStartTime() {
        return this.validityStartTime;
    }

    public Payment validityStartTime(ZonedDateTime validityStartTime) {
        this.setValidityStartTime(validityStartTime);
        return this;
    }

    public void setValidityStartTime(ZonedDateTime validityStartTime) {
        this.validityStartTime = validityStartTime;
    }

    public ZonedDateTime getValidityEndTime() {
        return this.validityEndTime;
    }

    public Payment validityEndTime(ZonedDateTime validityEndTime) {
        this.setValidityEndTime(validityEndTime);
        return this;
    }

    public void setValidityEndTime(ZonedDateTime validityEndTime) {
        this.validityEndTime = validityEndTime;
    }

    public String getDetails() {
        return this.details;
    }

    public Payment details(String details) {
        this.setDetails(details);
        return this;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Site getSite9() {
        return this.site9;
    }

    public void setSite9(Site site) {
        this.site9 = site;
    }

    public Payment site9(Site site) {
        this.setSite9(site);
        return this;
    }

    public Enrolement getEnrolment() {
        return this.enrolment;
    }

    public void setEnrolment(Enrolement enrolement) {
        this.enrolment = enrolement;
    }

    public Payment enrolment(Enrolement enrolement) {
        this.setEnrolment(enrolement);
        return this;
    }

    public Sponsoring getSponsoring() {
        return this.sponsoring;
    }

    public void setSponsoring(Sponsoring sponsoring) {
        this.sponsoring = sponsoring;
    }

    public Payment sponsoring(Sponsoring sponsoring) {
        this.setSponsoring(sponsoring);
        return this;
    }

    public Session getSession() {
        return this.session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Payment session(Session session) {
        this.setSession(session);
        return this;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Payment currency(Currency currency) {
        this.setCurrency(currency);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        return getId() != null && getId().equals(((Payment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", paiedBy='" + getPaiedBy() + "'" +
            ", proof='" + getProof() + "'" +
            ", proofContentType='" + getProofContentType() + "'" +
            ", paidAt='" + getPaidAt() + "'" +
            ", type='" + getType() + "'" +
            ", side='" + getSide() + "'" +
            ", validityStartTime='" + getValidityStartTime() + "'" +
            ", validityEndTime='" + getValidityEndTime() + "'" +
            ", details='" + getDetails() + "'" +
            "}";
    }
}
