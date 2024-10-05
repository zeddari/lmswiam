//package com.wiam.lms.domain;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Size;
//import org.hibernate.annotations.Cache;
//import org.hibernate.annotations.CacheConcurrencyStrategy;
//
//import java.io.Serializable;
//import java.util.HashSet;
//import java.util.Set;
//
///**
// * A Country.
// */
//@Entity
//@Table(name = "country")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@org.springframework.data.elasticsearch.annotations.Document(indexName = "country", createIndex = false)
//@SuppressWarnings("common-java:DuplicatedBlocks")
//public class CountryOrig implements Serializable {
//
//    private static final long serialVersionUID = 1L;
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private Long id;
//
//    @NotNull
//    @Size(max = 100)
//    @Column(name = "name_ar", length = 100, nullable = false, unique = true)
//    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
//    private String nameAr;
//
//    @NotNull
//    @Size(max = 100)
//    @Column(name = "name_lat", length = 100, nullable = false, unique = true)
//    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
//    private String nameLat;
//
//    @Size(max = 10)
//    @Column(name = "code", length = 10)
//    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
//    private String code;
//
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "country")
//    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//    @org.springframework.data.annotation.Transient
//    @JsonIgnoreProperties(
//        value = {
//            "user",
//            "certificates",
//            "answers",
//            "quizResults",
//            "reviews",
//            "enrolements",
//            "progressions",
//            "tickets",
//            "sponsorings",
//            "depenses",
//            "diplomas",
//            "languages",
//            "site13",
//            "country",
//            "nationality",
//            "job",
//            "departement2",
//            "groups",
//            "courses",
//            "sessions2s",
//            "sessions3s",
//        },
//        allowSetters = true
//    )
//    private Set<UserCustom> userCustoms = new HashSet<>();
//
//    // jhipster-needle-entity-add-field - JHipster will add fields here
//
//    public Long getId() {
//        return this.id;
//    }
//
//    public CountryOrig id(Long id) {
//        this.setId(id);
//        return this;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getNameAr() {
//        return this.nameAr;
//    }
//
//    public CountryOrig nameAr(String nameAr) {
//        this.setNameAr(nameAr);
//        return this;
//    }
//
//    public void setNameAr(String nameAr) {
//        this.nameAr = nameAr;
//    }
//
//    public String getNameLat() {
//        return this.nameLat;
//    }
//
//    public CountryOrig nameLat(String nameLat) {
//        this.setNameLat(nameLat);
//        return this;
//    }
//
//    public void setNameLat(String nameLat) {
//        this.nameLat = nameLat;
//    }
//
//    public String getCode() {
//        return this.code;
//    }
//
//    public CountryOrig code(String code) {
//        this.setCode(code);
//        return this;
//    }
//
//    public void setCode(String code) {
//        this.code = code;
//    }
//
//    public Set<UserCustom> getUserCustoms() {
//        return this.userCustoms;
//    }
//
//    public void setUserCustoms(Set<UserCustom> userCustoms) {
//        if (this.userCustoms != null) {
//            this.userCustoms.forEach(i -> i.setCountry(null));
//        }
//        if (userCustoms != null) {
//            userCustoms.forEach(i -> i.setCountry(this));
//        }
//        this.userCustoms = userCustoms;
//    }
//
//    public CountryOrig userCustoms(Set<UserCustom> userCustoms) {
//        this.setUserCustoms(userCustoms);
//        return this;
//    }
//
//    public CountryOrig addUserCustom(UserCustom userCustom) {
//        this.userCustoms.add(userCustom);
//        userCustom.setCountry(this);
//        return this;
//    }
//
//    public CountryOrig removeUserCustom(UserCustom userCustom) {
//        this.userCustoms.remove(userCustom);
//        userCustom.setCountry(null);
//        return this;
//    }
//
//    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//        if (!(o instanceof CountryOrig)) {
//            return false;
//        }
//        return getId() != null && getId().equals(((CountryOrig) o).getId());
//    }
//
//    @Override
//    public int hashCode() {
//        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
//        return getClass().hashCode();
//    }
//
//    // prettier-ignore
//    @Override
//    public String toString() {
//        return "Country{" +
//            "id=" + getId() +
//            ", nameAr='" + getNameAr() + "'" +
//            ", nameLat='" + getNameLat() + "'" +
//            ", code='" + getCode() + "'" +
//            "}";
//    }
//}
