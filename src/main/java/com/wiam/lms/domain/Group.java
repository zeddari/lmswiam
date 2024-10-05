package com.wiam.lms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wiam.lms.domain.enumeration.GroupType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Group.
 */
@Entity
@Table(name = "jhi_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "group", createIndex = false)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Group implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "group_type", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private GroupType groupType;

    @NotNull
    @Size(max = 100)
    @Column(name = "name_ar", length = 100, nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nameAr;

    @NotNull
    @Size(max = 100)
    @Column(name = "name_lat", length = 100, nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nameLat;

    @Lob
    @Column(name = "description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "comitte")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "site19", "userCustom6", "comitte", "topic4" }, allowSetters = true)
    private Set<Certificate> certificates = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group1")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = { "certificates", "groups", "elements", "site11", "group1", "quizzes", "sessions5s" },
        allowSetters = true
    )
    private Set<Group> groups = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "rel_jhi_group__elements",
        joinColumns = @JoinColumn(name = "jhi_group_id"),
        inverseJoinColumns = @JoinColumn(name = "elements_id")
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
    private Set<UserCustom> elements = new HashSet<>();

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
    private Site site11;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "certificates", "groups", "elements", "site11", "group1", "quizzes", "sessions5s" },
        allowSetters = true
    )
    private Group group1;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "groups")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "quizResults", "groups", "questions", "site7", "topic1" }, allowSetters = true)
    private Set<Quiz> quizzes = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "groups")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = { "sessionInstances", "payments", "classrooms", "groups", "professors", "employees", "links", "site14", "comments" },
        allowSetters = true
    )
    private Set<Session> sessions5s = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Group id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GroupType getGroupType() {
        return this.groupType;
    }

    public Group groupType(GroupType groupType) {
        this.setGroupType(groupType);
        return this;
    }

    public void setGroupType(GroupType groupType) {
        this.groupType = groupType;
    }

    public String getNameAr() {
        return this.nameAr;
    }

    public Group nameAr(String nameAr) {
        this.setNameAr(nameAr);
        return this;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getNameLat() {
        return this.nameLat;
    }

    public Group nameLat(String nameLat) {
        this.setNameLat(nameLat);
        return this;
    }

    public void setNameLat(String nameLat) {
        this.nameLat = nameLat;
    }

    public String getDescription() {
        return this.description;
    }

    public Group description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Certificate> getCertificates() {
        return this.certificates;
    }

    public void setCertificates(Set<Certificate> certificates) {
        if (this.certificates != null) {
            this.certificates.forEach(i -> i.setComitte(null));
        }
        if (certificates != null) {
            certificates.forEach(i -> i.setComitte(this));
        }
        this.certificates = certificates;
    }

    public Group certificates(Set<Certificate> certificates) {
        this.setCertificates(certificates);
        return this;
    }

    public Group addCertificate(Certificate certificate) {
        this.certificates.add(certificate);
        certificate.setComitte(this);
        return this;
    }

    public Group removeCertificate(Certificate certificate) {
        this.certificates.remove(certificate);
        certificate.setComitte(null);
        return this;
    }

    public Set<Group> getGroups() {
        return this.groups;
    }

    public void setGroups(Set<Group> groups) {
        if (this.groups != null) {
            this.groups.forEach(i -> i.setGroup1(null));
        }
        if (groups != null) {
            groups.forEach(i -> i.setGroup1(this));
        }
        this.groups = groups;
    }

    public Group groups(Set<Group> groups) {
        this.setGroups(groups);
        return this;
    }

    public Group addGroup(Group group) {
        this.groups.add(group);
        group.setGroup1(this);
        return this;
    }

    public Group removeGroup(Group group) {
        this.groups.remove(group);
        group.setGroup1(null);
        return this;
    }

    public Set<UserCustom> getElements() {
        return this.elements;
    }

    public void setElements(Set<UserCustom> userCustoms) {
        this.elements = userCustoms;
    }

    public Group elements(Set<UserCustom> userCustoms) {
        this.setElements(userCustoms);
        return this;
    }

    public Group addElements(UserCustom userCustom) {
        this.elements.add(userCustom);
        return this;
    }

    public Group removeElements(UserCustom userCustom) {
        this.elements.remove(userCustom);
        return this;
    }

    public Site getSite11() {
        return this.site11;
    }

    public void setSite11(Site site) {
        this.site11 = site;
    }

    public Group site11(Site site) {
        this.setSite11(site);
        return this;
    }

    public Group getGroup1() {
        return this.group1;
    }

    public void setGroup1(Group group) {
        this.group1 = group;
    }

    public Group group1(Group group) {
        this.setGroup1(group);
        return this;
    }

    public Set<Quiz> getQuizzes() {
        return this.quizzes;
    }

    public void setQuizzes(Set<Quiz> quizzes) {
        if (this.quizzes != null) {
            this.quizzes.forEach(i -> i.removeGroups(this));
        }
        if (quizzes != null) {
            quizzes.forEach(i -> i.addGroups(this));
        }
        this.quizzes = quizzes;
    }

    public Group quizzes(Set<Quiz> quizzes) {
        this.setQuizzes(quizzes);
        return this;
    }

    public Group addQuiz(Quiz quiz) {
        this.quizzes.add(quiz);
        quiz.getGroups().add(this);
        return this;
    }

    public Group removeQuiz(Quiz quiz) {
        this.quizzes.remove(quiz);
        quiz.getGroups().remove(this);
        return this;
    }

    public Set<Session> getSessions5s() {
        return this.sessions5s;
    }

    public void setSessions5s(Set<Session> sessions) {
        if (this.sessions5s != null) {
            this.sessions5s.forEach(i -> i.removeGroups(this));
        }
        if (sessions != null) {
            sessions.forEach(i -> i.addGroups(this));
        }
        this.sessions5s = sessions;
    }

    public Group sessions5s(Set<Session> sessions) {
        this.setSessions5s(sessions);
        return this;
    }

    public Group addSessions5(Session session) {
        this.sessions5s.add(session);
        session.getGroups().add(this);
        return this;
    }

    public Group removeSessions5(Session session) {
        this.sessions5s.remove(session);
        session.getGroups().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Group)) {
            return false;
        }
        return getId() != null && getId().equals(((Group) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Group{" +
            "id=" + getId() +
            ", groupType='" + getGroupType() + "'" +
            ", nameAr='" + getNameAr() + "'" +
            ", nameLat='" + getNameLat() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
