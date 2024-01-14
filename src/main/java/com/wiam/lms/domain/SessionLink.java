package com.wiam.lms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wiam.lms.domain.enumeration.SessionProvider;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SessionLink.
 */
@Entity
@Table(name = "session_link")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "sessionlink")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SessionLink implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private SessionProvider provider;

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
    @Column(name = "link", length = 500)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String link;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "links")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "progressions", "links", "session1" }, allowSetters = true)
    private Set<SessionInstance> sessions4s = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "links")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = { "sessionInstances", "payments", "classrooms", "groups", "professors", "employees", "links" },
        allowSetters = true
    )
    private Set<Session> sessions7s = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SessionLink id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SessionProvider getProvider() {
        return this.provider;
    }

    public SessionLink provider(SessionProvider provider) {
        this.setProvider(provider);
        return this;
    }

    public void setProvider(SessionProvider provider) {
        this.provider = provider;
    }

    public String getTitle() {
        return this.title;
    }

    public SessionLink title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public SessionLink description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return this.link;
    }

    public SessionLink link(String link) {
        this.setLink(link);
        return this;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Set<SessionInstance> getSessions4s() {
        return this.sessions4s;
    }

    public void setSessions4s(Set<SessionInstance> sessionInstances) {
        if (this.sessions4s != null) {
            this.sessions4s.forEach(i -> i.removeLinks(this));
        }
        if (sessionInstances != null) {
            sessionInstances.forEach(i -> i.addLinks(this));
        }
        this.sessions4s = sessionInstances;
    }

    public SessionLink sessions4s(Set<SessionInstance> sessionInstances) {
        this.setSessions4s(sessionInstances);
        return this;
    }

    public SessionLink addSessions4(SessionInstance sessionInstance) {
        this.sessions4s.add(sessionInstance);
        sessionInstance.getLinks().add(this);
        return this;
    }

    public SessionLink removeSessions4(SessionInstance sessionInstance) {
        this.sessions4s.remove(sessionInstance);
        sessionInstance.getLinks().remove(this);
        return this;
    }

    public Set<Session> getSessions7s() {
        return this.sessions7s;
    }

    public void setSessions7s(Set<Session> sessions) {
        if (this.sessions7s != null) {
            this.sessions7s.forEach(i -> i.removeLinks(this));
        }
        if (sessions != null) {
            sessions.forEach(i -> i.addLinks(this));
        }
        this.sessions7s = sessions;
    }

    public SessionLink sessions7s(Set<Session> sessions) {
        this.setSessions7s(sessions);
        return this;
    }

    public SessionLink addSessions7(Session session) {
        this.sessions7s.add(session);
        session.getLinks().add(this);
        return this;
    }

    public SessionLink removeSessions7(Session session) {
        this.sessions7s.remove(session);
        session.getLinks().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SessionLink)) {
            return false;
        }
        return getId() != null && getId().equals(((SessionLink) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SessionLink{" +
            "id=" + getId() +
            ", provider='" + getProvider() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", link='" + getLink() + "'" +
            "}";
    }
}
