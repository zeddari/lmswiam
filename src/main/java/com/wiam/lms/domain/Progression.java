package com.wiam.lms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wiam.lms.domain.enumeration.Attendance;
import com.wiam.lms.domain.enumeration.ExamType;
import com.wiam.lms.domain.enumeration.ProgressionMode;
import com.wiam.lms.domain.enumeration.Riwayats;
import com.wiam.lms.domain.enumeration.Sourate;
import com.wiam.lms.domain.enumeration.Tilawa;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Progression.
 */
@Entity
@Table(name = "progression")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "progression", createIndex = false)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Progression implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "is_for_attendance", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isForAttendance;

    public Boolean getIsForAttendance() {
        return isForAttendance;
    }

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "attendance", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private Attendance attendance;

    @Column(name = "justif_ref")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String justifRef;

    @NotNull
    @Column(name = "late_arrival", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean lateArrival;

    @NotNull
    @Column(name = "early_departure", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean earlyDeparture;

    @Enumerated(EnumType.STRING)
    @Column(name = "progression_mode")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private ProgressionMode progressionMode;

    @Enumerated(EnumType.STRING)
    @Column(name = "exam_type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private ExamType examType;

    @Enumerated(EnumType.STRING)
    @Column(name = "riwaya")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private Riwayats riwaya;

    @ManyToOne(fetch = FetchType.LAZY)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private Surahs fromSourate;

    @ManyToOne(fetch = FetchType.LAZY)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private Surahs toSourate;

    @ManyToOne(fetch = FetchType.LAZY)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private Ayahs fromAyahs;

    @ManyToOne(fetch = FetchType.LAZY)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private Ayahs toAyahs;

    @Column(name = "from_aya_num")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer fromAyaNum;

    @Column(name = "to_aya_num")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer toAyaNum;

    @Lob
    @Column(name = "from_aya_verset")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String fromAyaVerset;

    @Lob
    @Column(name = "to_aya_verset")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String toAyaVerset;

    @Enumerated(EnumType.STRING)
    @Column(name = "tilawa_type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private Tilawa tilawaType;

    @NotNull
    @Column(name = "task_done", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean taskDone;

    @NotNull
    @Min(value = 0)
    @Max(value = 5)
    @Column(name = "tajweed_score", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer tajweedScore;

    @Lob
    @Column(name = "tajweed_course_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String tajweedCourseName;

    @Lob
    @Column(name = "activity_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String activityName;

    @Lob
    @Column(name = "activity_course_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String activityCourseName;

    @NotNull
    @Min(value = 0)
    @Max(value = 5)
    @Column(name = "hifd_score", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer hifdScore;

    @NotNull
    @Min(value = 0)
    @Max(value = 5)
    @Column(name = "adae_score", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer adaeScore;

    @Lob
    @Column(name = "observation")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String observation;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

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
    private Site site17;

    public void setFromSourate(Surahs fromSourate) {
        this.fromSourate = fromSourate;
    }

    public void setToSourate(Surahs toSourate) {
        this.toSourate = toSourate;
    }

    public Surahs getFromSourate() {
        return fromSourate;
    }

    public Surahs getToSourate() {
        return toSourate;
    }

    public void setFromAyahs(Ayahs fromAyahs) {
        this.fromAyahs = fromAyahs;
    }

    public void setToAyahs(Ayahs toAyahs) {
        this.toAyahs = toAyahs;
    }

    public Ayahs getFromAyahs() {
        return fromAyahs;
    }

    public Ayahs getToAyahs() {
        return toAyahs;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    //@JsonIgnoreProperties(value = { "progressions", "links", "courses", "site16", "session1" }, allowSetters = true)
    private SessionInstance sessionInstance;

    @ManyToOne(fetch = FetchType.EAGER)
    /*@JsonIgnoreProperties(
        value = {
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
    )*/
    private UserCustom student;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Progression id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Attendance getAttendance() {
        return this.attendance;
    }

    public Progression attendance(Attendance attendance) {
        this.setAttendance(attendance);
        return this;
    }

    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;
    }

    public String getJustifRef() {
        return this.justifRef;
    }

    public Progression justifRef(String justifRef) {
        this.setJustifRef(justifRef);
        return this;
    }

    public void setJustifRef(String justifRef) {
        this.justifRef = justifRef;
    }

    public Boolean getLateArrival() {
        return this.lateArrival;
    }

    public Progression lateArrival(Boolean lateArrival) {
        this.setLateArrival(lateArrival);
        return this;
    }

    public void setLateArrival(Boolean lateArrival) {
        this.lateArrival = lateArrival;
    }

    public Boolean getEarlyDeparture() {
        return this.earlyDeparture;
    }

    public Progression earlyDeparture(Boolean earlyDeparture) {
        this.setEarlyDeparture(earlyDeparture);
        return this;
    }

    public void setEarlyDeparture(Boolean earlyDeparture) {
        this.earlyDeparture = earlyDeparture;
    }

    public ProgressionMode getProgressionMode() {
        return this.progressionMode;
    }

    public Progression progressionMode(ProgressionMode progressionMode) {
        this.setProgressionMode(progressionMode);
        return this;
    }

    public void setProgressionMode(ProgressionMode progressionMode) {
        this.progressionMode = progressionMode;
    }

    public ExamType getExamType() {
        return this.examType;
    }

    public Progression examType(ExamType examType) {
        this.setExamType(examType);
        return this;
    }

    public void setExamType(ExamType examType) {
        this.examType = examType;
    }

    public Riwayats getRiwaya() {
        return this.riwaya;
    }

    public Progression riwaya(Riwayats riwaya) {
        this.setRiwaya(riwaya);
        return this;
    }

    public void setRiwaya(Riwayats riwaya) {
        this.riwaya = riwaya;
    }

    public Integer getFromAyaNum() {
        return this.fromAyaNum;
    }

    public Progression fromAyaNum(Integer fromAyaNum) {
        this.setFromAyaNum(fromAyaNum);
        return this;
    }

    public void setFromAyaNum(Integer fromAyaNum) {
        this.fromAyaNum = fromAyaNum;
    }

    public Integer getToAyaNum() {
        return this.toAyaNum;
    }

    public Progression toAyaNum(Integer toAyaNum) {
        this.setToAyaNum(toAyaNum);
        return this;
    }

    public void setToAyaNum(Integer toAyaNum) {
        this.toAyaNum = toAyaNum;
    }

    public String getFromAyaVerset() {
        return this.fromAyaVerset;
    }

    public Progression fromAyaVerset(String fromAyaVerset) {
        this.setFromAyaVerset(fromAyaVerset);
        return this;
    }

    public void setFromAyaVerset(String fromAyaVerset) {
        this.fromAyaVerset = fromAyaVerset;
    }

    public String getToAyaVerset() {
        return this.toAyaVerset;
    }

    public Progression toAyaVerset(String toAyaVerset) {
        this.setToAyaVerset(toAyaVerset);
        return this;
    }

    public void setToAyaVerset(String toAyaVerset) {
        this.toAyaVerset = toAyaVerset;
    }

    public Tilawa getTilawaType() {
        return this.tilawaType;
    }

    public Progression tilawaType(Tilawa tilawaType) {
        this.setTilawaType(tilawaType);
        return this;
    }

    public void setTilawaType(Tilawa tilawaType) {
        this.tilawaType = tilawaType;
    }

    public Boolean getTaskDone() {
        return this.taskDone;
    }

    public Progression taskDone(Boolean taskDone) {
        this.setTaskDone(taskDone);
        return this;
    }

    public void setTaskDone(Boolean taskDone) {
        this.taskDone = taskDone;
    }

    public Integer getTajweedScore() {
        return this.tajweedScore;
    }

    public Progression tajweedScore(Integer tajweedScore) {
        this.setTajweedScore(tajweedScore);
        return this;
    }

    public void setTajweedScore(Integer tajweedScore) {
        this.tajweedScore = tajweedScore;
    }

    public Integer getHifdScore() {
        return this.hifdScore;
    }

    public Progression hifdScore(Integer hifdScore) {
        this.setHifdScore(hifdScore);
        return this;
    }

    public void setHifdScore(Integer hifdScore) {
        this.hifdScore = hifdScore;
    }

    public Integer getAdaeScore() {
        return this.adaeScore;
    }

    public Progression adaeScore(Integer adaeScore) {
        this.setAdaeScore(adaeScore);
        return this;
    }

    public void setAdaeScore(Integer adaeScore) {
        this.adaeScore = adaeScore;
    }

    public String getObservation() {
        return this.observation;
    }

    public Progression observation(String observation) {
        this.setObservation(observation);
        return this;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Site getSite17() {
        return this.site17;
    }

    public void setSite17(Site site) {
        this.site17 = site;
    }

    public Progression site17(Site site) {
        this.setSite17(site);
        return this;
    }

    public SessionInstance getSessionInstance() {
        return this.sessionInstance;
    }

    public void setSessionInstance(SessionInstance sessionInstance) {
        this.sessionInstance = sessionInstance;
    }

    public Progression sessionInstance(SessionInstance sessionInstance) {
        this.setSessionInstance(sessionInstance);
        return this;
    }

    public UserCustom getStudent() {
        return this.student;
    }

    public void setStudent(UserCustom userCustom) {
        this.student = userCustom;
    }

    public Progression student(UserCustom userCustom) {
        this.setStudent(userCustom);
        return this;
    }

    public String getTajweedCourseName() {
        return tajweedCourseName;
    }

    public void setTajweedCourseName(String tajweedCourseName) {
        this.tajweedCourseName = tajweedCourseName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityCourseName() {
        return activityCourseName;
    }

    public void setActivityCourseName(String activityCourseName) {
        this.activityCourseName = activityCourseName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Progression)) {
            return false;
        }
        return getId() != null && getId().equals(((Progression) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Progression{" +
            "id=" + getId() +
            ", attendance='" + getAttendance() + "'" +
            ", justifRef='" + getJustifRef() + "'" +
            ", lateArrival='" + getLateArrival() + "'" +
            ", earlyDeparture='" + getEarlyDeparture() + "'" +
            ", progressionMode='" + getProgressionMode() + "'" +
            ", examType='" + getExamType() + "'" +
            ", riwaya='" + getRiwaya() + "'" +
            ", fromSourate='" + getFromSourate() + "'" +
            ", toSourate='" + getToSourate() + "'" +
            ", fromAyaNum=" + getFromAyaNum() +
            ", toAyaNum=" + getToAyaNum() +
            ", fromAyaVerset='" + getFromAyaVerset() + "'" +
            ", toAyaVerset='" + getToAyaVerset() + "'" +
            ", tilawaType='" + getTilawaType() + "'" +
            ", taskDone='" + getTaskDone() + "'" +
            ", tajweedScore=" + getTajweedScore() +
            ", hifdScore=" + getHifdScore() +
            ", adaeScore=" + getAdaeScore() +
            ", observation='" + getObservation() + "'" +
            "}";
    }

    public void setIsForAttendance(Boolean isForAttendance) {
        this.isForAttendance = isForAttendance;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Boolean getForAttendance() {
        return isForAttendance;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setForAttendance(Boolean forAttendance) {
        isForAttendance = forAttendance;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
