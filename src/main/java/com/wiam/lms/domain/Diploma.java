package com.wiam.lms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wiam.lms.domain.enumeration.DiplomaType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Diploma.
 */
@Entity
@Table(name = "diploma")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "diploma", createIndex = false)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Diploma implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private DiplomaType type;

    @NotNull
    @Size(max = 100)
    @Column(name = "title", length = 100, nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String title;

    @Lob
    @Column(name = "subject")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String subject;

    @Lob
    @Column(name = "detail")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String detail;

    @Lob
    @Column(name = "supervisor")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String supervisor;

    @Column(name = "grade")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String grade;

    @Column(name = "graduation_date")
    private LocalDate graduationDate;

    @Lob
    @Column(name = "school")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String school;

    @Lob
    @Column(name = "attachment")
    private byte[] attachment;

    @Column(name = "attachment_content_type")
    private String attachmentContentType;

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
    private Site site20;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "diplomas")
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
    private Set<UserCustom> userCustom7s = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Diploma id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DiplomaType getType() {
        return this.type;
    }

    public Diploma type(DiplomaType type) {
        this.setType(type);
        return this;
    }

    public void setType(DiplomaType type) {
        this.type = type;
    }

    public String getTitle() {
        return this.title;
    }

    public Diploma title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return this.subject;
    }

    public Diploma subject(String subject) {
        this.setSubject(subject);
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDetail() {
        return this.detail;
    }

    public Diploma detail(String detail) {
        this.setDetail(detail);
        return this;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getSupervisor() {
        return this.supervisor;
    }

    public Diploma supervisor(String supervisor) {
        this.setSupervisor(supervisor);
        return this;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getGrade() {
        return this.grade;
    }

    public Diploma grade(String grade) {
        this.setGrade(grade);
        return this;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public LocalDate getGraduationDate() {
        return this.graduationDate;
    }

    public Diploma graduationDate(LocalDate graduationDate) {
        this.setGraduationDate(graduationDate);
        return this;
    }

    public void setGraduationDate(LocalDate graduationDate) {
        this.graduationDate = graduationDate;
    }

    public String getSchool() {
        return this.school;
    }

    public Diploma school(String school) {
        this.setSchool(school);
        return this;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public byte[] getAttachment() {
        return this.attachment;
    }

    public Diploma attachment(byte[] attachment) {
        this.setAttachment(attachment);
        return this;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    public String getAttachmentContentType() {
        return this.attachmentContentType;
    }

    public Diploma attachmentContentType(String attachmentContentType) {
        this.attachmentContentType = attachmentContentType;
        return this;
    }

    public void setAttachmentContentType(String attachmentContentType) {
        this.attachmentContentType = attachmentContentType;
    }

    public Site getSite20() {
        return this.site20;
    }

    public void setSite20(Site site) {
        this.site20 = site;
    }

    public Diploma site20(Site site) {
        this.setSite20(site);
        return this;
    }

    public Set<UserCustom> getUserCustom7s() {
        return this.userCustom7s;
    }

    public void setUserCustom7s(Set<UserCustom> userCustoms) {
        if (this.userCustom7s != null) {
            this.userCustom7s.forEach(i -> i.removeDiplomas(this));
        }
        if (userCustoms != null) {
            userCustoms.forEach(i -> i.addDiplomas(this));
        }
        this.userCustom7s = userCustoms;
    }

    public Diploma userCustom7s(Set<UserCustom> userCustoms) {
        this.setUserCustom7s(userCustoms);
        return this;
    }

    public Diploma addUserCustom7(UserCustom userCustom) {
        this.userCustom7s.add(userCustom);
        userCustom.getDiplomas().add(this);
        return this;
    }

    public Diploma removeUserCustom7(UserCustom userCustom) {
        this.userCustom7s.remove(userCustom);
        userCustom.getDiplomas().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Diploma)) {
            return false;
        }
        return getId() != null && getId().equals(((Diploma) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Diploma{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", title='" + getTitle() + "'" +
            ", subject='" + getSubject() + "'" +
            ", detail='" + getDetail() + "'" +
            ", supervisor='" + getSupervisor() + "'" +
            ", grade='" + getGrade() + "'" +
            ", graduationDate='" + getGraduationDate() + "'" +
            ", school='" + getSchool() + "'" +
            ", attachment='" + getAttachment() + "'" +
            ", attachmentContentType='" + getAttachmentContentType() + "'" +
            "}";
    }
}
