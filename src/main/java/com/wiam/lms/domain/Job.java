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
 * A Job.
 */
@Entity
@Table(name = "job")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "job")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Job implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "title_ar", length = 100, nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String titleAr;

    @NotNull
    @Size(max = 100)
    @Column(name = "title_lat", length = 100, nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String titleLat;

    @Lob
    @Column(name = "description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "job")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
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
    private Set<UserCustom> userCustoms = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Job id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitleAr() {
        return this.titleAr;
    }

    public Job titleAr(String titleAr) {
        this.setTitleAr(titleAr);
        return this;
    }

    public void setTitleAr(String titleAr) {
        this.titleAr = titleAr;
    }

    public String getTitleLat() {
        return this.titleLat;
    }

    public Job titleLat(String titleLat) {
        this.setTitleLat(titleLat);
        return this;
    }

    public void setTitleLat(String titleLat) {
        this.titleLat = titleLat;
    }

    public String getDescription() {
        return this.description;
    }

    public Job description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<UserCustom> getUserCustoms() {
        return this.userCustoms;
    }

    public void setUserCustoms(Set<UserCustom> userCustoms) {
        if (this.userCustoms != null) {
            this.userCustoms.forEach(i -> i.setJob(null));
        }
        if (userCustoms != null) {
            userCustoms.forEach(i -> i.setJob(this));
        }
        this.userCustoms = userCustoms;
    }

    public Job userCustoms(Set<UserCustom> userCustoms) {
        this.setUserCustoms(userCustoms);
        return this;
    }

    public Job addUserCustom(UserCustom userCustom) {
        this.userCustoms.add(userCustom);
        userCustom.setJob(this);
        return this;
    }

    public Job removeUserCustom(UserCustom userCustom) {
        this.userCustoms.remove(userCustom);
        userCustom.setJob(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Job)) {
            return false;
        }
        return getId() != null && getId().equals(((Job) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Job{" +
            "id=" + getId() +
            ", titleAr='" + getTitleAr() + "'" +
            ", titleLat='" + getTitleLat() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
