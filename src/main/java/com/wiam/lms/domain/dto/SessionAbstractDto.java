package com.wiam.lms.domain.dto;

import com.wiam.lms.domain.enumeration.SessionMode;
import com.wiam.lms.domain.enumeration.SessionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Session.
 */
@Entity
@Table(name = "session")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "session", createIndex = false)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SessionAbstractDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "session_mode", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private SessionMode sessionMode;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "session_type", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private SessionType sessionType;

    @NotNull
    @Size(max = 100)
    @Column(name = "title", length = 100, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String title;

    @Lob
    @Column(name = "description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    public Long getId() {
        return id;
    }

    public SessionMode getSessionMode() {
        return sessionMode;
    }

    public SessionType getSessionType() {
        return sessionType;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSessionMode(SessionMode sessionMode) {
        this.sessionMode = sessionMode;
    }

    public void setSessionType(SessionType sessionType) {
        this.sessionType = sessionType;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
