package com.wiam.lms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wiam.lms.domain.enumeration.DepenseFrequency;
import com.wiam.lms.domain.enumeration.DepenseTarget;
import com.wiam.lms.domain.enumeration.DepenseType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Depense.
 */
@Entity
@Table(name = "depense")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "depense")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Depense implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private DepenseType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "target", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private DepenseTarget target;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "frequency", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private DepenseFrequency frequency;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "amount", nullable = false)
    private Double amount;

    @NotNull
    @Column(name = "ref", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String ref;

    @Column(name = "message")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String message;

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
    private UserCustom resource;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Depense id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DepenseType getType() {
        return this.type;
    }

    public Depense type(DepenseType type) {
        this.setType(type);
        return this;
    }

    public void setType(DepenseType type) {
        this.type = type;
    }

    public DepenseTarget getTarget() {
        return this.target;
    }

    public Depense target(DepenseTarget target) {
        this.setTarget(target);
        return this;
    }

    public void setTarget(DepenseTarget target) {
        this.target = target;
    }

    public DepenseFrequency getFrequency() {
        return this.frequency;
    }

    public Depense frequency(DepenseFrequency frequency) {
        this.setFrequency(frequency);
        return this;
    }

    public void setFrequency(DepenseFrequency frequency) {
        this.frequency = frequency;
    }

    public Double getAmount() {
        return this.amount;
    }

    public Depense amount(Double amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getRef() {
        return this.ref;
    }

    public Depense ref(String ref) {
        this.setRef(ref);
        return this;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getMessage() {
        return this.message;
    }

    public Depense message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserCustom getResource() {
        return this.resource;
    }

    public void setResource(UserCustom userCustom) {
        this.resource = userCustom;
    }

    public Depense resource(UserCustom userCustom) {
        this.setResource(userCustom);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Depense)) {
            return false;
        }
        return getId() != null && getId().equals(((Depense) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Depense{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", target='" + getTarget() + "'" +
            ", frequency='" + getFrequency() + "'" +
            ", amount=" + getAmount() +
            ", ref='" + getRef() + "'" +
            ", message='" + getMessage() + "'" +
            "}";
    }
}
