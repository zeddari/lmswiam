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
 * A City.
 */
@Entity
@Table(name = "city")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "city", createIndex = false)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class City implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 10)
    @Column(name = "code", length = 10)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String code;

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

    @NotNull
    @Size(max = 100)
    @Column(name = "lat", length = 100, nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private Double lat;

    @NotNull
    @Size(max = 100)
    @Column(name = "lng", length = 100, nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private Double lng;

    // Add a ManyToOne relationship with the Country entity
    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false) // Foreign key to the Country table
    @JsonIgnoreProperties(value = { "cities" }, allowSetters = true) // Avoid circular references
    private Country country;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "city")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
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
    private Set<Site> sites = new HashSet<>();

    // Getters and setters for the fields, including the new country field

    public Long getId() {
        return this.id;
    }

    public City id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameAr() {
        return this.nameAr;
    }

    public City nameAr(String nameAr) {
        this.setNameAr(nameAr);
        return this;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getNameLat() {
        return this.nameLat;
    }

    public City nameLat(String nameLat) {
        this.setNameLat(nameLat);
        return this;
    }

    public void setNameLat(String nameLat) {
        this.nameLat = nameLat;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getLat() {
        return this.lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return this.lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Country getCountry() {
        return this.country;
    }

    public City country(Country country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Set<Site> getSites() {
        return this.sites;
    }

    public void setSites(Set<Site> sites) {
        if (this.sites != null) {
            this.sites.forEach(i -> i.setCity(null));
        }
        if (sites != null) {
            sites.forEach(i -> i.setCity(this));
        }
        this.sites = sites;
    }

    public City sites(Set<Site> sites) {
        this.setSites(sites);
        return this;
    }

    public City addSite(Site site) {
        this.sites.add(site);
        site.setCity(this);
        return this;
    }

    public City removeSite(Site site) {
        this.sites.remove(site);
        site.setCity(null);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof City)) {
            return false;
        }
        return getId() != null && getId().equals(((City) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "City{" +
            "id=" +
            getId() +
            ", nameAr='" +
            getNameAr() +
            "'" +
            ", nameLat='" +
            getNameLat() +
            "'" +
            ", country=" +
            (country != null ? country.getNameLat() : null) +
            "}"
        );
    }
}
