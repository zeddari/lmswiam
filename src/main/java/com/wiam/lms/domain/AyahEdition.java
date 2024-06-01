package com.wiam.lms.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AyahEdition.
 */
@Entity
@Table(name = "ayah_edition")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "ayahedition", createIndex = false)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AyahEdition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "ayah_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer ayahId;

    @Column(name = "edition_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer editionId;

    @Lob
    @Column(name = "data", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String data;

    @Column(name = "is_audio")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isAudio;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Integer getId() {
        return this.id;
    }

    public AyahEdition id(Integer id) {
        this.setId(id);
        return this;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAyahId() {
        return this.ayahId;
    }

    public AyahEdition ayahId(Integer ayahId) {
        this.setAyahId(ayahId);
        return this;
    }

    public void setAyahId(Integer ayahId) {
        this.ayahId = ayahId;
    }

    public Integer getEditionId() {
        return this.editionId;
    }

    public AyahEdition editionId(Integer editionId) {
        this.setEditionId(editionId);
        return this;
    }

    public void setEditionId(Integer editionId) {
        this.editionId = editionId;
    }

    public String getData() {
        return this.data;
    }

    public AyahEdition data(String data) {
        this.setData(data);
        return this;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Boolean getIsAudio() {
        return this.isAudio;
    }

    public AyahEdition isAudio(Boolean isAudio) {
        this.setIsAudio(isAudio);
        return this;
    }

    public void setIsAudio(Boolean isAudio) {
        this.isAudio = isAudio;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public AyahEdition createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public AyahEdition updatedAt(ZonedDateTime updatedAt) {
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
        if (!(o instanceof AyahEdition)) {
            return false;
        }
        return getId() != null && getId().equals(((AyahEdition) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AyahEdition{" +
            "id=" + getId() +
            ", ayahId=" + getAyahId() +
            ", editionId=" + getEditionId() +
            ", data='" + getData() + "'" +
            ", isAudio='" + getIsAudio() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
