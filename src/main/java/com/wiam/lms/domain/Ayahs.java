package com.wiam.lms.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Ayahs.
 */
@Entity
@Table(name = "ayahs")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "ayahs")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ayahs implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "number")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer number;

    @NotNull
    @Size(max = 100)
    @Column(name = "textdesc", length = 100, nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String textdesc;

    @Column(name = "number_in_surah")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer numberInSurah;

    @Column(name = "page")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer page;

    @Column(name = "surah_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer surahId;

    @Column(name = "hizb_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer hizbId;

    @Column(name = "juz_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer juzId;

    @Column(name = "sajda")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean sajda;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Integer getId() {
        return this.id;
    }

    public Ayahs id(Integer id) {
        this.setId(id);
        return this;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumber() {
        return this.number;
    }

    public Ayahs number(Integer number) {
        this.setNumber(number);
        return this;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getTextdesc() {
        return this.textdesc;
    }

    public Ayahs textdesc(String textdesc) {
        this.setTextdesc(textdesc);
        return this;
    }

    public void setTextdesc(String textdesc) {
        this.textdesc = textdesc;
    }

    public Integer getNumberInSurah() {
        return this.numberInSurah;
    }

    public Ayahs numberInSurah(Integer numberInSurah) {
        this.setNumberInSurah(numberInSurah);
        return this;
    }

    public void setNumberInSurah(Integer numberInSurah) {
        this.numberInSurah = numberInSurah;
    }

    public Integer getPage() {
        return this.page;
    }

    public Ayahs page(Integer page) {
        this.setPage(page);
        return this;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSurahId() {
        return this.surahId;
    }

    public Ayahs surahId(Integer surahId) {
        this.setSurahId(surahId);
        return this;
    }

    public void setSurahId(Integer surahId) {
        this.surahId = surahId;
    }

    public Integer getHizbId() {
        return this.hizbId;
    }

    public Ayahs hizbId(Integer hizbId) {
        this.setHizbId(hizbId);
        return this;
    }

    public void setHizbId(Integer hizbId) {
        this.hizbId = hizbId;
    }

    public Integer getJuzId() {
        return this.juzId;
    }

    public Ayahs juzId(Integer juzId) {
        this.setJuzId(juzId);
        return this;
    }

    public void setJuzId(Integer juzId) {
        this.juzId = juzId;
    }

    public Boolean getSajda() {
        return this.sajda;
    }

    public Ayahs sajda(Boolean sajda) {
        this.setSajda(sajda);
        return this;
    }

    public void setSajda(Boolean sajda) {
        this.sajda = sajda;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public Ayahs createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public Ayahs updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ayahs)) {
            return false;
        }
        return getId() != null && getId().equals(((Ayahs) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ayahs{" +
            "id=" + getId() +
            ", number=" + getNumber() +
            ", textdesc='" + getTextdesc() + "'" +
            ", numberInSurah=" + getNumberInSurah() +
            ", page=" + getPage() +
            ", surahId=" + getSurahId() +
            ", hizbId=" + getHizbId() +
            ", juzId=" + getJuzId() +
            ", sajda='" + getSajda() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
