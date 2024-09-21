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
@org.springframework.data.elasticsearch.annotations.Document(indexName = "sessioninstance", createIndex = false)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SessionInstance implements Serializable {

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

    @NotNull
    @Column(name = "session_date", nullable = false)
    private LocalDate sessionDate;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private ZonedDateTime startTime;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sessionInstance", cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "site17", "sessionInstance", "student" }, allowSetters = true)
    private Set<Progression> progressions = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "rel_session_instance__course",
        joinColumns = @JoinColumn(name = "session_instance_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sessionsInstance5s" }, allowSetters = true)
    private Set<SessionCourses> courses = new HashSet<>();

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
    private Site site16;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "sessionInstances", "payments", "classrooms", "groups", "professors", "employees", "links", "site14", "comments" },
        allowSetters = true
    )
    private Session session1;

    // Linking with id professor
    @ManyToOne(fetch = FetchType.EAGER)
    private UserCustom professor;

    @ManyToOne(fetch = FetchType.EAGER)
    private SessionLink sessionLink;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public void setProfessor(UserCustom professor) {
        this.professor = professor;
    }

    public void setSessionLink(SessionLink sessionLink) {
        this.sessionLink = sessionLink;
    }

    public UserCustom getProfessor() {
        return professor;
    }

    public SessionLink getSessionLink() {
        return sessionLink;
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

    @ManyToOne(fetch = FetchType.EAGER)
    private Group group;

    public Group getGroup() {
        return group;
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

    public Set<SessionCourses> getCourses() {
        return this.courses;
    }

    public void setCourses(Set<SessionCourses> sessionCourses) {
        this.courses = sessionCourses;
    }

    public SessionInstance courses(Set<SessionCourses> sessionCourses) {
        this.setCourses(sessionCourses);
        return this;
    }

    public SessionInstance addCourse(SessionCourses sessionCourses) {
        this.courses.add(sessionCourses);
        return this;
    }

    public SessionInstance removeCourse(SessionCourses sessionCourses) {
        this.courses.remove(sessionCourses);
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
}
