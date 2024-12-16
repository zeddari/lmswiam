package com.wiam.lms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wiam.lms.domain.enumeration.Periodicity;
import com.wiam.lms.domain.enumeration.SessionJoinMode;
import com.wiam.lms.domain.enumeration.SessionMode;
import com.wiam.lms.domain.enumeration.SessionType;
import com.wiam.lms.domain.enumeration.TargetedGender;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
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
public class Session implements Serializable {

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

    @Enumerated(EnumType.STRING)
    @Column(name = "session_periodicity", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private Periodicity sessionPeriodicity;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "session_type", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private SessionType sessionType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "session_join_mode", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private SessionJoinMode sessionJoinMode;

    @NotNull
    @Size(max = 100)
    @Column(name = "title", length = 100, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String title;

    @Lob
    @Column(name = "description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @Column(name = "period_start_date")
    private LocalDate periodStartDate;

    @Column(name = "periode_end_date")
    private LocalDate periodeEndDate;

    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "session_size", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer sessionSize;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "targeted_gender", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private TargetedGender targetedGender;

    @DecimalMin(value = "0")
    @Column(name = "price")
    private Double price;

    @Lob
    @Column(name = "thumbnail")
    private byte[] thumbnail;

    @Column(name = "thumbnail_content_type")
    private String thumbnailContentType;

    @Column(name = "monday")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean monday;

    @Column(name = "tuesday")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean tuesday;

    @Column(name = "wednesday")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean wednesday;

    @Column(name = "thursday")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean thursday;

    @Column(name = "friday")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean friday;

    @Column(name = "saturday")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean saturday;

    @Column(name = "sunday")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean sunday;

    // start times

    @Column(name = "mondayTime")
    private LocalTime mondayTime;

    @Column(name = "tuesdayTime")
    private LocalTime tuesdayTime;

    @Column(name = "wednesdayTime")
    private LocalTime wednesdayTime;

    @Column(name = "thursdayTime")
    private LocalTime thursdayTime;

    @Column(name = "fridayTime")
    private LocalTime fridayTime;

    @Column(name = "saturdayTime")
    private LocalTime saturdayTime;

    @Column(name = "sundayTime")
    private LocalTime sundayTime;

    // end times

    @Column(name = "mondayEndTime")
    private LocalTime mondayEndTime;

    @Column(name = "tuesdayEndTime")
    private LocalTime tuesdayEndTime;

    @Column(name = "wednesdayEndTime")
    private LocalTime wednesdayEndTime;

    @Column(name = "thursdayEndTime")
    private LocalTime thursdayEndTime;

    @Column(name = "fridayEndTime")
    private LocalTime fridayEndTime;

    @Column(name = "saturdayEndTime")
    private LocalTime saturdayEndTime;

    @Column(name = "sundayEndTime")
    private LocalTime sundayEndTime;

    public void setMondayEndTime(LocalTime mondayEndTime) {
        this.mondayEndTime = mondayEndTime;
    }

    public void setTuesdayEndTime(LocalTime tuesdayEndTime) {
        this.tuesdayEndTime = tuesdayEndTime;
    }

    public void setWednesdayEndTime(LocalTime wednesdayEndTime) {
        this.wednesdayEndTime = wednesdayEndTime;
    }

    public void setThursdayEndTime(LocalTime thursdayEndTime) {
        this.thursdayEndTime = thursdayEndTime;
    }

    public void setFridayEndTime(LocalTime fridayEndTime) {
        this.fridayEndTime = fridayEndTime;
    }

    public void setSaturdayEndTime(LocalTime saturdayEndTime) {
        this.saturdayEndTime = saturdayEndTime;
    }

    public void setSundayEndTime(LocalTime sundayEndTime) {
        this.sundayEndTime = sundayEndTime;
    }

    public LocalTime getMondayEndTime() {
        return mondayEndTime;
    }

    public LocalTime getTuesdayEndTime() {
        return tuesdayEndTime;
    }

    public LocalTime getWednesdayEndTime() {
        return wednesdayEndTime;
    }

    public LocalTime getThursdayEndTime() {
        return thursdayEndTime;
    }

    public LocalTime getFridayEndTime() {
        return fridayEndTime;
    }

    public LocalTime getSaturdayEndTime() {
        return saturdayEndTime;
    }

    public LocalTime getSundayEndTime() {
        return sundayEndTime;
    }

    @NotNull
    @Column(name = "is_periodic", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isPeriodic;

    @NotNull
    @Column(name = "is_minor_allowed", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isMinorAllowed;

    @NotNull
    @Column(name = "is_active", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isActive;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "session1")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "progressions", "links", "site16", "session1" }, allowSetters = true)
    private Set<SessionInstance> sessionInstances = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "session")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "site9", "enrolment", "sponsoring", "session", "currency" }, allowSetters = true)
    private Set<Payment> payments = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_session__classrooms",
        joinColumns = @JoinColumn(name = "session_id"),
        inverseJoinColumns = @JoinColumn(name = "classrooms_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "site", "sessions6s" }, allowSetters = true)
    private Set<Classroom> classrooms = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "rel_session__groups",
        joinColumns = @JoinColumn(name = "session_id"),
        inverseJoinColumns = @JoinColumn(name = "groups_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "certificates", "groups"/*, "elements"*/, "site11", "group1", "quizzes", "sessions5s" },
        allowSetters = true
    )
    private Set<Group> groups = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_session__professors",
        joinColumns = @JoinColumn(name = "session_id"),
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
    private Set<UserCustom> professors = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_session__employees",
        joinColumns = @JoinColumn(name = "session_id"),
        inverseJoinColumns = @JoinColumn(name = "employees_id")
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
    private Set<UserCustom> employees = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_session__links",
        joinColumns = @JoinColumn(name = "session_id"),
        inverseJoinColumns = @JoinColumn(name = "links_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "site15", "sessions4s", "sessions7s" }, allowSetters = true)
    private Set<SessionLink> links = new HashSet<>();

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
    private Site site14;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LocalTime getMondayTime() {
        return mondayTime;
    }

    public LocalTime getTuesdayTime() {
        return tuesdayTime;
    }

    public LocalTime getWednesdayTime() {
        return wednesdayTime;
    }

    public LocalTime getThursdayTime() {
        return thursdayTime;
    }

    public LocalTime getFridayTime() {
        return fridayTime;
    }

    public LocalTime getSaturdayTime() {
        return saturdayTime;
    }

    public LocalTime getSundayTime() {
        return sundayTime;
    }

    public void setMondayTime(LocalTime mondayTime) {
        this.mondayTime = mondayTime;
    }

    public void setTuesdayTime(LocalTime tuesdayTime) {
        this.tuesdayTime = tuesdayTime;
    }

    public void setWednesdayTime(LocalTime wednesdayTime) {
        this.wednesdayTime = wednesdayTime;
    }

    public void setThursdayTime(LocalTime thursdayTime) {
        this.thursdayTime = thursdayTime;
    }

    public void setFridayTime(LocalTime fridayTime) {
        this.fridayTime = fridayTime;
    }

    public void setSaturdayTime(LocalTime saturdayTime) {
        this.saturdayTime = saturdayTime;
    }

    public void setSundayTime(LocalTime sundayTime) {
        this.sundayTime = sundayTime;
    }

    public Session id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SessionMode getSessionMode() {
        return this.sessionMode;
    }

    public Session sessionMode(SessionMode sessionMode) {
        this.setSessionMode(sessionMode);
        return this;
    }

    public void setSessionMode(SessionMode sessionMode) {
        this.sessionMode = sessionMode;
    }

    public SessionType getSessionType() {
        return this.sessionType;
    }

    public Session sessionType(SessionType sessionType) {
        this.setSessionType(sessionType);
        return this;
    }

    public void setSessionType(SessionType sessionType) {
        this.sessionType = sessionType;
    }

    public SessionJoinMode getSessionJoinMode() {
        return this.sessionJoinMode;
    }

    public Session sessionJoinMode(SessionJoinMode sessionJoinMode) {
        this.setSessionJoinMode(sessionJoinMode);
        return this;
    }

    public void setSessionJoinMode(SessionJoinMode sessionJoinMode) {
        this.sessionJoinMode = sessionJoinMode;
    }

    public String getTitle() {
        return this.title;
    }

    public Session title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Session description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getPeriodStartDate() {
        return this.periodStartDate;
    }

    public Session periodStartDate(LocalDate periodStartDate) {
        this.setPeriodStartDate(periodStartDate);
        return this;
    }

    public void setPeriodStartDate(LocalDate periodStartDate) {
        this.periodStartDate = periodStartDate;
    }

    public LocalDate getPeriodeEndDate() {
        return this.periodeEndDate;
    }

    public Session periodeEndDate(LocalDate periodeEndDate) {
        this.setPeriodeEndDate(periodeEndDate);
        return this;
    }

    public void setPeriodeEndDate(LocalDate periodeEndDate) {
        this.periodeEndDate = periodeEndDate;
    }

    public Integer getSessionSize() {
        return this.sessionSize;
    }

    public Session sessionSize(Integer sessionSize) {
        this.setSessionSize(sessionSize);
        return this;
    }

    public void setSessionSize(Integer sessionSize) {
        this.sessionSize = sessionSize;
    }

    public TargetedGender getTargetedGender() {
        return this.targetedGender;
    }

    public Session targetedGender(TargetedGender targetedGender) {
        this.setTargetedGender(targetedGender);
        return this;
    }

    public void setTargetedGender(TargetedGender targetedGender) {
        this.targetedGender = targetedGender;
    }

    public Double getPrice() {
        return this.price;
    }

    public Session price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public byte[] getThumbnail() {
        return this.thumbnail;
    }

    public Session thumbnail(byte[] thumbnail) {
        this.setThumbnail(thumbnail);
        return this;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnailContentType() {
        return this.thumbnailContentType;
    }

    public Session thumbnailContentType(String thumbnailContentType) {
        this.thumbnailContentType = thumbnailContentType;
        return this;
    }

    public void setThumbnailContentType(String thumbnailContentType) {
        this.thumbnailContentType = thumbnailContentType;
    }

    public Boolean getMonday() {
        return this.monday;
    }

    public Session monday(Boolean monday) {
        this.setMonday(monday);
        return this;
    }

    public void setMonday(Boolean monday) {
        this.monday = monday;
    }

    public Boolean getTuesday() {
        return this.tuesday;
    }

    public Session tuesday(Boolean tuesday) {
        this.setTuesday(tuesday);
        return this;
    }

    public void setTuesday(Boolean tuesday) {
        this.tuesday = tuesday;
    }

    public Boolean getWednesday() {
        return this.wednesday;
    }

    public Session wednesday(Boolean wednesday) {
        this.setWednesday(wednesday);
        return this;
    }

    public void setWednesday(Boolean wednesday) {
        this.wednesday = wednesday;
    }

    public Boolean getThursday() {
        return this.thursday;
    }

    public Session thursday(Boolean thursday) {
        this.setThursday(thursday);
        return this;
    }

    public void setThursday(Boolean thursday) {
        this.thursday = thursday;
    }

    public Boolean getFriday() {
        return this.friday;
    }

    public Session friday(Boolean friday) {
        this.setFriday(friday);
        return this;
    }

    public void setFriday(Boolean friday) {
        this.friday = friday;
    }

    public Boolean getSaturday() {
        return this.saturday;
    }

    public Session saturday(Boolean saturday) {
        this.setSaturday(saturday);
        return this;
    }

    public void setSaturday(Boolean saturday) {
        this.saturday = saturday;
    }

    public Boolean getSunday() {
        return this.sunday;
    }

    public Session sunday(Boolean sunday) {
        this.setSunday(sunday);
        return this;
    }

    public void setSunday(Boolean sunday) {
        this.sunday = sunday;
    }

    public Boolean getIsPeriodic() {
        return this.isPeriodic;
    }

    public Session isPeriodic(Boolean isPeriodic) {
        this.setIsPeriodic(isPeriodic);
        return this;
    }

    public void setIsPeriodic(Boolean isPeriodic) {
        this.isPeriodic = isPeriodic;
    }

    public Boolean getIsMinorAllowed() {
        return this.isMinorAllowed;
    }

    public Session isMinorAllowed(Boolean isMinorAllowed) {
        this.setIsMinorAllowed(isMinorAllowed);
        return this;
    }

    public void setIsMinorAllowed(Boolean isMinorAllowed) {
        this.isMinorAllowed = isMinorAllowed;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Session isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<SessionInstance> getSessionInstances() {
        return this.sessionInstances;
    }

    public void setSessionInstances(Set<SessionInstance> sessionInstances) {
        if (this.sessionInstances != null) {
            this.sessionInstances.forEach(i -> i.setSession1(null));
        }
        if (sessionInstances != null) {
            sessionInstances.forEach(i -> i.setSession1(this));
        }
        this.sessionInstances = sessionInstances;
    }

    public Session sessionInstances(Set<SessionInstance> sessionInstances) {
        this.setSessionInstances(sessionInstances);
        return this;
    }

    public Session addSessionInstance(SessionInstance sessionInstance) {
        this.sessionInstances.add(sessionInstance);
        sessionInstance.setSession1(this);
        return this;
    }

    public Session removeSessionInstance(SessionInstance sessionInstance) {
        this.sessionInstances.remove(sessionInstance);
        sessionInstance.setSession1(null);
        return this;
    }

    public Set<Payment> getPayments() {
        return this.payments;
    }

    public void setPayments(Set<Payment> payments) {
        if (this.payments != null) {
            this.payments.forEach(i -> i.setSession(null));
        }
        if (payments != null) {
            payments.forEach(i -> i.setSession(this));
        }
        this.payments = payments;
    }

    public Session payments(Set<Payment> payments) {
        this.setPayments(payments);
        return this;
    }

    public Session addPayment(Payment payment) {
        this.payments.add(payment);
        payment.setSession(this);
        return this;
    }

    public Session removePayment(Payment payment) {
        this.payments.remove(payment);
        payment.setSession(null);
        return this;
    }

    public Set<Classroom> getClassrooms() {
        return this.classrooms;
    }

    public void setClassrooms(Set<Classroom> classrooms) {
        this.classrooms = classrooms;
    }

    public Session classrooms(Set<Classroom> classrooms) {
        this.setClassrooms(classrooms);
        return this;
    }

    public Session addClassrooms(Classroom classroom) {
        this.classrooms.add(classroom);
        return this;
    }

    public Session removeClassrooms(Classroom classroom) {
        this.classrooms.remove(classroom);
        return this;
    }

    public Set<Group> getGroups() {
        return this.groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public Session groups(Set<Group> groups) {
        this.setGroups(groups);
        return this;
    }

    public Session addGroups(Group group) {
        this.groups.add(group);
        return this;
    }

    public Session removeGroups(Group group) {
        this.groups.remove(group);
        return this;
    }

    public Set<UserCustom> getProfessors() {
        return this.professors;
    }

    public void setProfessors(Set<UserCustom> userCustoms) {
        this.professors = userCustoms;
    }

    public Session professors(Set<UserCustom> userCustoms) {
        this.setProfessors(userCustoms);
        return this;
    }

    public Session addProfessors(UserCustom userCustom) {
        this.professors.add(userCustom);
        return this;
    }

    public Session removeProfessors(UserCustom userCustom) {
        this.professors.remove(userCustom);
        return this;
    }

    public Set<UserCustom> getEmployees() {
        return this.employees;
    }

    public void setEmployees(Set<UserCustom> userCustoms) {
        this.employees = userCustoms;
    }

    public Session employees(Set<UserCustom> userCustoms) {
        this.setEmployees(userCustoms);
        return this;
    }

    public Session addEmployees(UserCustom userCustom) {
        this.employees.add(userCustom);
        return this;
    }

    public Session removeEmployees(UserCustom userCustom) {
        this.employees.remove(userCustom);
        return this;
    }

    public Set<SessionLink> getLinks() {
        return this.links;
    }

    public void setLinks(Set<SessionLink> sessionLinks) {
        this.links = sessionLinks;
    }

    public Session links(Set<SessionLink> sessionLinks) {
        this.setLinks(sessionLinks);
        return this;
    }

    public Session addLinks(SessionLink sessionLink) {
        this.links.add(sessionLink);
        return this;
    }

    public Session removeLinks(SessionLink sessionLink) {
        this.links.remove(sessionLink);
        return this;
    }

    public Site getSite14() {
        return this.site14;
    }

    public void setSite14(Site site) {
        this.site14 = site;
    }

    public Session site14(Site site) {
        this.setSite14(site);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Session)) {
            return false;
        }
        return getId() != null && getId().equals(((Session) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Session{" +
            "id=" + getId() +
            ", sessionMode='" + getSessionMode() + "'" +
            ", sessionType='" + getSessionType() + "'" +
            ", sessionJoinMode='" + getSessionJoinMode() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", periodStartDate='" + getPeriodStartDate() + "'" +
            ", periodeEndDate='" + getPeriodeEndDate() + "'" +

            ", sessionSize=" + getSessionSize() +
            ", targetedGender='" + getTargetedGender() + "'" +
            ", price=" + getPrice() +
            ", thumbnail='" + getThumbnail() + "'" +
            ", thumbnailContentType='" + getThumbnailContentType() + "'" +
            ", monday='" + getMonday() + "'" +
            ", tuesday='" + getTuesday() + "'" +
            ", wednesday='" + getWednesday() + "'" +
            ", thursday='" + getThursday() + "'" +
            ", friday='" + getFriday() + "'" +
            ", saturday='" + getSaturday() + "'" +
            ", sunday='" + getSunday() + "'" +
            ", isPeriodic='" + getIsPeriodic() + "'" +
            ", isMinorAllowed='" + getIsMinorAllowed() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Periodicity getSessionPeriodicity() {
        return sessionPeriodicity;
    }

    public void setSessionPeriodicity(Periodicity sessionPeriodicity) {
        this.sessionPeriodicity = sessionPeriodicity;
    }
}
