package com.wiam.lms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wiam.lms.domain.enumeration.CertificateType;
import com.wiam.lms.domain.enumeration.Riwayats;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Certificate.
 */
@Entity
@Table(name = "certificate")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "certificate")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Certificate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "certificate_type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private CertificateType certificateType;

    @NotNull
    @Size(max = 100)
    @Column(name = "title", length = 100, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "riwaya")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private Riwayats riwaya;

    @NotNull
    @Min(value = 1)
    @Max(value = 60)
    @Column(name = "miqdar", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer miqdar;

    @Lob
    @Column(name = "observation", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String observation;

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
    private UserCustom userCustom6;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "certificates", "groups", "elements", "group1", "quizzes", "sessions5s" }, allowSetters = true)
    private Group comitte;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "certificates", "quizzes", "topics", "courses", "topic2" }, allowSetters = true)
    private Topic topic4;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Certificate id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CertificateType getCertificateType() {
        return this.certificateType;
    }

    public Certificate certificateType(CertificateType certificateType) {
        this.setCertificateType(certificateType);
        return this;
    }

    public void setCertificateType(CertificateType certificateType) {
        this.certificateType = certificateType;
    }

    public String getTitle() {
        return this.title;
    }

    public Certificate title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Riwayats getRiwaya() {
        return this.riwaya;
    }

    public Certificate riwaya(Riwayats riwaya) {
        this.setRiwaya(riwaya);
        return this;
    }

    public void setRiwaya(Riwayats riwaya) {
        this.riwaya = riwaya;
    }

    public Integer getMiqdar() {
        return this.miqdar;
    }

    public Certificate miqdar(Integer miqdar) {
        this.setMiqdar(miqdar);
        return this;
    }

    public void setMiqdar(Integer miqdar) {
        this.miqdar = miqdar;
    }

    public String getObservation() {
        return this.observation;
    }

    public Certificate observation(String observation) {
        this.setObservation(observation);
        return this;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public UserCustom getUserCustom6() {
        return this.userCustom6;
    }

    public void setUserCustom6(UserCustom userCustom) {
        this.userCustom6 = userCustom;
    }

    public Certificate userCustom6(UserCustom userCustom) {
        this.setUserCustom6(userCustom);
        return this;
    }

    public Group getComitte() {
        return this.comitte;
    }

    public void setComitte(Group group) {
        this.comitte = group;
    }

    public Certificate comitte(Group group) {
        this.setComitte(group);
        return this;
    }

    public Topic getTopic4() {
        return this.topic4;
    }

    public void setTopic4(Topic topic) {
        this.topic4 = topic;
    }

    public Certificate topic4(Topic topic) {
        this.setTopic4(topic);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Certificate)) {
            return false;
        }
        return getId() != null && getId().equals(((Certificate) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Certificate{" +
            "id=" + getId() +
            ", certificateType='" + getCertificateType() + "'" +
            ", title='" + getTitle() + "'" +
            ", riwaya='" + getRiwaya() + "'" +
            ", miqdar=" + getMiqdar() +
            ", observation='" + getObservation() + "'" +
            "}";
    }
}
