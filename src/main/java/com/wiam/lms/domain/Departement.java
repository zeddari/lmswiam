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
 * A Departement.
 */
@Entity
@Table(name = "departement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "departement", createIndex = false)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Departement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name_ar", length = 100, nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nameAr;

    @NotNull
    @Size(max = 100)
    @Column(name = "name_lat", length = 100, nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nameLat;

    @Lob
    @Column(name = "description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "departement1")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "departements", "userCustoms", "departement1" }, allowSetters = true)
    private Set<Departement> departements = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "departement2")
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
    private Set<UserCustom> userCustoms = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "departements", "userCustoms", "departement1" }, allowSetters = true)
    private Departement departement1;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Departement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameAr() {
        return this.nameAr;
    }

    public Departement nameAr(String nameAr) {
        this.setNameAr(nameAr);
        return this;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getNameLat() {
        return this.nameLat;
    }

    public Departement nameLat(String nameLat) {
        this.setNameLat(nameLat);
        return this;
    }

    public void setNameLat(String nameLat) {
        this.nameLat = nameLat;
    }

    public String getDescription() {
        return this.description;
    }

    public Departement description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Departement> getDepartements() {
        return this.departements;
    }

    public void setDepartements(Set<Departement> departements) {
        if (this.departements != null) {
            this.departements.forEach(i -> i.setDepartement1(null));
        }
        if (departements != null) {
            departements.forEach(i -> i.setDepartement1(this));
        }
        this.departements = departements;
    }

    public Departement departements(Set<Departement> departements) {
        this.setDepartements(departements);
        return this;
    }

    public Departement addDepartement(Departement departement) {
        this.departements.add(departement);
        departement.setDepartement1(this);
        return this;
    }

    public Departement removeDepartement(Departement departement) {
        this.departements.remove(departement);
        departement.setDepartement1(null);
        return this;
    }

    public Set<UserCustom> getUserCustoms() {
        return this.userCustoms;
    }

    public void setUserCustoms(Set<UserCustom> userCustoms) {
        if (this.userCustoms != null) {
            this.userCustoms.forEach(i -> i.setDepartement2(null));
        }
        if (userCustoms != null) {
            userCustoms.forEach(i -> i.setDepartement2(this));
        }
        this.userCustoms = userCustoms;
    }

    public Departement userCustoms(Set<UserCustom> userCustoms) {
        this.setUserCustoms(userCustoms);
        return this;
    }

    public Departement addUserCustom(UserCustom userCustom) {
        this.userCustoms.add(userCustom);
        userCustom.setDepartement2(this);
        return this;
    }

    public Departement removeUserCustom(UserCustom userCustom) {
        this.userCustoms.remove(userCustom);
        userCustom.setDepartement2(null);
        return this;
    }

    public Departement getDepartement1() {
        return this.departement1;
    }

    public void setDepartement1(Departement departement) {
        this.departement1 = departement;
    }

    public Departement departement1(Departement departement) {
        this.setDepartement1(departement);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Departement)) {
            return false;
        }
        return getId() != null && getId().equals(((Departement) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Departement{" +
            "id=" + getId() +
            ", nameAr='" + getNameAr() + "'" +
            ", nameLat='" + getNameLat() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
