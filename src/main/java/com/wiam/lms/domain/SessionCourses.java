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
 * A SessionCourses.
 */
@Entity
@Table(name = "session_courses")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "sessioncourses")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SessionCourses implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "title", length = 100, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String title;

    @Lob
    @Column(name = "description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @Size(max = 500)
    @Column(name = "resource_link", length = 500)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String resourceLink;

    @Lob
    @Column(name = "resource_file")
    private byte[] resourceFile;

    @Column(name = "resource_file_content_type")
    private String resourceFileContentType;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "courses")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "progressions", "links", "courses", "site16", "session1" }, allowSetters = true)
    private Set<SessionInstance> sessionsInstance5s = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SessionCourses id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public SessionCourses title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public SessionCourses description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResourceLink() {
        return this.resourceLink;
    }

    public SessionCourses resourceLink(String resourceLink) {
        this.setResourceLink(resourceLink);
        return this;
    }

    public void setResourceLink(String resourceLink) {
        this.resourceLink = resourceLink;
    }

    public byte[] getResourceFile() {
        return this.resourceFile;
    }

    public SessionCourses resourceFile(byte[] resourceFile) {
        this.setResourceFile(resourceFile);
        return this;
    }

    public void setResourceFile(byte[] resourceFile) {
        this.resourceFile = resourceFile;
    }

    public String getResourceFileContentType() {
        return this.resourceFileContentType;
    }

    public SessionCourses resourceFileContentType(String resourceFileContentType) {
        this.resourceFileContentType = resourceFileContentType;
        return this;
    }

    public void setResourceFileContentType(String resourceFileContentType) {
        this.resourceFileContentType = resourceFileContentType;
    }

    public Set<SessionInstance> getSessionsInstance5s() {
        return this.sessionsInstance5s;
    }

    public void setSessionsInstance5s(Set<SessionInstance> sessionInstances) {
        if (this.sessionsInstance5s != null) {
            this.sessionsInstance5s.forEach(i -> i.removeCourse(this));
        }
        if (sessionInstances != null) {
            sessionInstances.forEach(i -> i.addCourse(this));
        }
        this.sessionsInstance5s = sessionInstances;
    }

    public SessionCourses sessionsInstance5s(Set<SessionInstance> sessionInstances) {
        this.setSessionsInstance5s(sessionInstances);
        return this;
    }

    public SessionCourses addSessionsInstance5(SessionInstance sessionInstance) {
        this.sessionsInstance5s.add(sessionInstance);
        sessionInstance.getCourses().add(this);
        return this;
    }

    public SessionCourses removeSessionsInstance5(SessionInstance sessionInstance) {
        this.sessionsInstance5s.remove(sessionInstance);
        sessionInstance.getCourses().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SessionCourses)) {
            return false;
        }
        return getId() != null && getId().equals(((SessionCourses) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SessionCourses{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", resourceLink='" + getResourceLink() + "'" +
            ", resourceFile='" + getResourceFile() + "'" +
            ", resourceFileContentType='" + getResourceFileContentType() + "'" +
            "}";
    }
}
