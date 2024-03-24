package com.wiam.lms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wiam.lms.domain.enumeration.Attendance;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SessionInstance.
 */
@Entity
@Table(name = "session_instance")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "sessioninstance")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SessionInstance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // Linking with id group
    @ManyToOne(fetch = FetchType.LAZY)
    private Group group;

    // Linking with id professor
    @ManyToOne(fetch = FetchType.LAZY)
    private UserCustom professor;

    public UserCustom getProfessor() {
        return professor;
    }

    @NotNull
    @Size(max = 100)
    @Column(name = "title", length = 100, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String title;

    @NotNull
    @Column(name = "session_date", nullable = false)
    private LocalDate sessionDate;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private ZonedDateTime startTime;

    private String sessionStartTime;
    private String sessionEndTime;

    public String getSessionStartTime() {
        return sessionStartTime;
    }

    public String getSessionEndTime() {
        return sessionEndTime;
    }

    public void setSessionStartTime(String sessionStartTime) {
        this.sessionStartTime = sessionStartTime;
    }

    public void setSessionEndTime(String sessionEndTime) {
        this.sessionEndTime = sessionEndTime;
    }

    @NotNull
    @Column(name = "duration", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer duration;

    @Lob
    @Column(name = "info")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String info;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "attendance", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private Attendance attendance;

    @Column(name = "justif_ref")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String justifRef;

    @NotNull
    @Column(name = "is_active", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isActive;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sessionInstance")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "site17", "sessionInstance", "student" }, allowSetters = true)
    private Set<Progression> progressions = new HashSet<>();

    @Column(name = "session_link")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String sessionLink;

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
            "sessionInstances",
            "progressions",
            "tickets",
            "certificates",
            "diplomas",
            "city",
        },
        allowSetters = true
    )
    private Site site16;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getSessionLink() {
        return sessionLink;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "sessionInstances", "payments", "classrooms", "groups", "professors", "employees", "site14" },
        allowSetters = true
    )
    private Session session1;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Group getGroup() {
        return group;
    }

    public SessionInstance id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public SessionInstance title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getSessionDate() {
        return this.sessionDate;
    }

    public SessionInstance sessionDate(LocalDate sessionDate) {
        this.setSessionDate(sessionDate);
        return this;
    }

    public void setSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }

    public ZonedDateTime getStartTime() {
        return this.startTime;
    }

    public SessionInstance startTime(ZonedDateTime startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public SessionInstance duration(Integer duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getInfo() {
        return this.info;
    }

    public SessionInstance info(String info) {
        this.setInfo(info);
        return this;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Attendance getAttendance() {
        return this.attendance;
    }

    public SessionInstance attendance(Attendance attendance) {
        this.setAttendance(attendance);
        return this;
    }

    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;
    }

    public String getJustifRef() {
        return this.justifRef;
    }

    public SessionInstance justifRef(String justifRef) {
        this.setJustifRef(justifRef);
        return this;
    }

    public void setJustifRef(String justifRef) {
        this.justifRef = justifRef;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public SessionInstance isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<Progression> getProgressions() {
        return this.progressions;
    }

    public void setProgressions(Set<Progression> progressions) {
        if (this.progressions != null) {
            this.progressions.forEach(i -> i.setSessionInstance(null));
        }
        if (progressions != null) {
            progressions.forEach(i -> i.setSessionInstance(this));
        }
        this.progressions = progressions;
    }

    public SessionInstance progressions(Set<Progression> progressions) {
        this.setProgressions(progressions);
        return this;
    }

    public SessionInstance addProgression(Progression progression) {
        this.progressions.add(progression);
        progression.setSessionInstance(this);
        return this;
    }

    public SessionInstance removeProgression(Progression progression) {
        this.progressions.remove(progression);
        progression.setSessionInstance(null);
        return this;
    }

    public Site getSite16() {
        return this.site16;
    }

    public void setSite16(Site site) {
        this.site16 = site;
    }

    public SessionInstance site16(Site site) {
        this.setSite16(site);
        return this;
    }

    public Session getSession1() {
        return this.session1;
    }

    public void setSession1(Session session) {
        this.session1 = session;
    }

    public SessionInstance session1(Session session) {
        this.setSession1(session);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SessionInstance)) {
            return false;
        }
        return getId() != null && getId().equals(((SessionInstance) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SessionInstance{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", sessionDate='" + getSessionDate() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", duration=" + getDuration() +
            ", info='" + getInfo() + "'" +
            ", attendance='" + getAttendance() + "'" +
            ", justifRef='" + getJustifRef() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setProfessor(UserCustom professor) {
        this.professor = professor;
    }

    public void setSessionLink(String sessionLink) {
        this.sessionLink = sessionLink;
    }
}
