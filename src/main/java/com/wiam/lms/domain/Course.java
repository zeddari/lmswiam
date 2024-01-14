package com.wiam.lms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wiam.lms.domain.enumeration.Level;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Course.
 */
@Entity
@Table(name = "course")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "course")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private Level level;

    @NotNull
    @Size(max = 100)
    @Column(name = "title_ar", length = 100, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String titleAr;

    @NotNull
    @Size(max = 100)
    @Column(name = "title_lat", length = 100, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String titleLat;

    @Lob
    @Column(name = "description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @Lob
    @Column(name = "sub_titles")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String subTitles;

    @Lob
    @Column(name = "requirements")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String requirements;

    @Lob
    @Column(name = "options")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String options;

    @Column(name = "duration")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer duration;

    @Lob
    @Column(name = "image_link")
    private byte[] imageLink;

    @Column(name = "image_link_content_type")
    private String imageLinkContentType;

    @Column(name = "video_link")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String videoLink;

    @DecimalMin(value = "0")
    @Column(name = "price")
    private Double price;

    @Column(name = "is_active")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isActive;

    @Column(name = "activate_at")
    private ZonedDateTime activateAt;

    @Column(name = "is_confirmed")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isConfirmed;

    @Column(name = "confirmed_at")
    private ZonedDateTime confirmedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "course")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "parts", "reviews", "course", "part1" }, allowSetters = true)
    private Set<Part> parts = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "course")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "payments", "userCustom4", "course" }, allowSetters = true)
    private Set<Enrolement> enrolements = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_course__professors",
        joinColumns = @JoinColumn(name = "course_id"),
        inverseJoinColumns = @JoinColumn(name = "professors_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    private Set<UserCustom> professors = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "certificates", "quizzes", "topics", "courses", "topic2" }, allowSetters = true)
    private Topic topic3;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Course id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Level getLevel() {
        return this.level;
    }

    public Course level(Level level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getTitleAr() {
        return this.titleAr;
    }

    public Course titleAr(String titleAr) {
        this.setTitleAr(titleAr);
        return this;
    }

    public void setTitleAr(String titleAr) {
        this.titleAr = titleAr;
    }

    public String getTitleLat() {
        return this.titleLat;
    }

    public Course titleLat(String titleLat) {
        this.setTitleLat(titleLat);
        return this;
    }

    public void setTitleLat(String titleLat) {
        this.titleLat = titleLat;
    }

    public String getDescription() {
        return this.description;
    }

    public Course description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubTitles() {
        return this.subTitles;
    }

    public Course subTitles(String subTitles) {
        this.setSubTitles(subTitles);
        return this;
    }

    public void setSubTitles(String subTitles) {
        this.subTitles = subTitles;
    }

    public String getRequirements() {
        return this.requirements;
    }

    public Course requirements(String requirements) {
        this.setRequirements(requirements);
        return this;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getOptions() {
        return this.options;
    }

    public Course options(String options) {
        this.setOptions(options);
        return this;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public Course duration(Integer duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public byte[] getImageLink() {
        return this.imageLink;
    }

    public Course imageLink(byte[] imageLink) {
        this.setImageLink(imageLink);
        return this;
    }

    public void setImageLink(byte[] imageLink) {
        this.imageLink = imageLink;
    }

    public String getImageLinkContentType() {
        return this.imageLinkContentType;
    }

    public Course imageLinkContentType(String imageLinkContentType) {
        this.imageLinkContentType = imageLinkContentType;
        return this;
    }

    public void setImageLinkContentType(String imageLinkContentType) {
        this.imageLinkContentType = imageLinkContentType;
    }

    public String getVideoLink() {
        return this.videoLink;
    }

    public Course videoLink(String videoLink) {
        this.setVideoLink(videoLink);
        return this;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public Double getPrice() {
        return this.price;
    }

    public Course price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Course isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public ZonedDateTime getActivateAt() {
        return this.activateAt;
    }

    public Course activateAt(ZonedDateTime activateAt) {
        this.setActivateAt(activateAt);
        return this;
    }

    public void setActivateAt(ZonedDateTime activateAt) {
        this.activateAt = activateAt;
    }

    public Boolean getIsConfirmed() {
        return this.isConfirmed;
    }

    public Course isConfirmed(Boolean isConfirmed) {
        this.setIsConfirmed(isConfirmed);
        return this;
    }

    public void setIsConfirmed(Boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public ZonedDateTime getConfirmedAt() {
        return this.confirmedAt;
    }

    public Course confirmedAt(ZonedDateTime confirmedAt) {
        this.setConfirmedAt(confirmedAt);
        return this;
    }

    public void setConfirmedAt(ZonedDateTime confirmedAt) {
        this.confirmedAt = confirmedAt;
    }

    public Set<Part> getParts() {
        return this.parts;
    }

    public void setParts(Set<Part> parts) {
        if (this.parts != null) {
            this.parts.forEach(i -> i.setCourse(null));
        }
        if (parts != null) {
            parts.forEach(i -> i.setCourse(this));
        }
        this.parts = parts;
    }

    public Course parts(Set<Part> parts) {
        this.setParts(parts);
        return this;
    }

    public Course addPart(Part part) {
        this.parts.add(part);
        part.setCourse(this);
        return this;
    }

    public Course removePart(Part part) {
        this.parts.remove(part);
        part.setCourse(null);
        return this;
    }

    public Set<Enrolement> getEnrolements() {
        return this.enrolements;
    }

    public void setEnrolements(Set<Enrolement> enrolements) {
        if (this.enrolements != null) {
            this.enrolements.forEach(i -> i.setCourse(null));
        }
        if (enrolements != null) {
            enrolements.forEach(i -> i.setCourse(this));
        }
        this.enrolements = enrolements;
    }

    public Course enrolements(Set<Enrolement> enrolements) {
        this.setEnrolements(enrolements);
        return this;
    }

    public Course addEnrolement(Enrolement enrolement) {
        this.enrolements.add(enrolement);
        enrolement.setCourse(this);
        return this;
    }

    public Course removeEnrolement(Enrolement enrolement) {
        this.enrolements.remove(enrolement);
        enrolement.setCourse(null);
        return this;
    }

    public Set<UserCustom> getProfessors() {
        return this.professors;
    }

    public void setProfessors(Set<UserCustom> userCustoms) {
        this.professors = userCustoms;
    }

    public Course professors(Set<UserCustom> userCustoms) {
        this.setProfessors(userCustoms);
        return this;
    }

    public Course addProfessors(UserCustom userCustom) {
        this.professors.add(userCustom);
        return this;
    }

    public Course removeProfessors(UserCustom userCustom) {
        this.professors.remove(userCustom);
        return this;
    }

    public Topic getTopic3() {
        return this.topic3;
    }

    public void setTopic3(Topic topic) {
        this.topic3 = topic;
    }

    public Course topic3(Topic topic) {
        this.setTopic3(topic);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Course)) {
            return false;
        }
        return getId() != null && getId().equals(((Course) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Course{" +
            "id=" + getId() +
            ", level='" + getLevel() + "'" +
            ", titleAr='" + getTitleAr() + "'" +
            ", titleLat='" + getTitleLat() + "'" +
            ", description='" + getDescription() + "'" +
            ", subTitles='" + getSubTitles() + "'" +
            ", requirements='" + getRequirements() + "'" +
            ", options='" + getOptions() + "'" +
            ", duration=" + getDuration() +
            ", imageLink='" + getImageLink() + "'" +
            ", imageLinkContentType='" + getImageLinkContentType() + "'" +
            ", videoLink='" + getVideoLink() + "'" +
            ", price=" + getPrice() +
            ", isActive='" + getIsActive() + "'" +
            ", activateAt='" + getActivateAt() + "'" +
            ", isConfirmed='" + getIsConfirmed() + "'" +
            ", confirmedAt='" + getConfirmedAt() + "'" +
            "}";
    }
}
