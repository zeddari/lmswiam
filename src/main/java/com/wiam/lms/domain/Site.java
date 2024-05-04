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
 * A Site.
 */
@Entity
@Table(name = "site")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "site")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Site implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

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

    @Column(name = "localisation")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String localisation;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "site")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "site", "sessions6s" }, allowSetters = true)
    private Set<Classroom> classrooms = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "site1")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "parts", "enrolements", "professors", "site1", "topic3" }, allowSetters = true)
    private Set<Course> courses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "site2")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "parts", "reviews", "site2", "course", "part1" }, allowSetters = true)
    private Set<Part> parts = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "site3")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "site3", "part2", "userCustom3" }, allowSetters = true)
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "site4")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "payments", "site4", "userCustom4", "course" }, allowSetters = true)
    private Set<Enrolement> enrolements = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "site5")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "answers", "site5", "quizzes" }, allowSetters = true)
    private Set<Question> questions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "site6")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "site6", "question", "userCustom1" }, allowSetters = true)
    private Set<Answer> answers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "site7")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "quizResults", "groups", "questions", "site7", "topic1" }, allowSetters = true)
    private Set<Quiz> quizzes = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "site8")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "site8", "quiz", "userCustom2" }, allowSetters = true)
    private Set<QuizResult> quizResults = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "site9")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "site9", "enrolment", "sponsoring", "session", "currency" }, allowSetters = true)
    private Set<Payment> payments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "site10")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "payments", "site10", "sponsor", "project", "currency" }, allowSetters = true)
    private Set<Sponsoring> sponsorings = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "site11")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = { "certificates", "groups", "elements", "site11", "group1", "quizzes", "sessions5s" },
        allowSetters = true
    )
    private Set<Group> groups = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "site12")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "sponsorings", "site12", "typeProject" }, allowSetters = true)
    private Set<Project> projects = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "site13")
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
    private Set<UserCustom> userCustoms = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "site14")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = { "sessionInstances", "payments", "classrooms", "groups", "professors", "employees", "links", "site14", "comments" },
        allowSetters = true
    )
    private Set<Session> sessions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "site15")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "site15", "sessions4s", "sessions7s" }, allowSetters = true)
    private Set<SessionLink> sessionLinks = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "site16")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "progressions", "links", "courses", "site16", "session1" }, allowSetters = true)
    private Set<SessionInstance> sessionInstances = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "site17")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "site17", "sessionInstance", "student" }, allowSetters = true)
    private Set<Progression> progressions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "site18")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "site18", "userCustom5" }, allowSetters = true)
    private Set<Tickets> tickets = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "site19")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "site19", "userCustom6", "comitte", "topic4" }, allowSetters = true)
    private Set<Certificate> certificates = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "site20")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "site20", "userCustom7s" }, allowSetters = true)
    private Set<Diploma> diplomas = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sites" }, allowSetters = true)
    private City city;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Site id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameAr() {
        return this.nameAr;
    }

    public Site nameAr(String nameAr) {
        this.setNameAr(nameAr);
        return this;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getNameLat() {
        return this.nameLat;
    }

    public Site nameLat(String nameLat) {
        this.setNameLat(nameLat);
        return this;
    }

    public void setNameLat(String nameLat) {
        this.nameLat = nameLat;
    }

    public String getDescription() {
        return this.description;
    }

    public Site description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocalisation() {
        return this.localisation;
    }

    public Site localisation(String localisation) {
        this.setLocalisation(localisation);
        return this;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public Set<Classroom> getClassrooms() {
        return this.classrooms;
    }

    public void setClassrooms(Set<Classroom> classrooms) {
        if (this.classrooms != null) {
            this.classrooms.forEach(i -> i.setSite(null));
        }
        if (classrooms != null) {
            classrooms.forEach(i -> i.setSite(this));
        }
        this.classrooms = classrooms;
    }

    public Site classrooms(Set<Classroom> classrooms) {
        this.setClassrooms(classrooms);
        return this;
    }

    public Site addClassroom(Classroom classroom) {
        this.classrooms.add(classroom);
        classroom.setSite(this);
        return this;
    }

    public Site removeClassroom(Classroom classroom) {
        this.classrooms.remove(classroom);
        classroom.setSite(null);
        return this;
    }

    public Set<Course> getCourses() {
        return this.courses;
    }

    public void setCourses(Set<Course> courses) {
        if (this.courses != null) {
            this.courses.forEach(i -> i.setSite1(null));
        }
        if (courses != null) {
            courses.forEach(i -> i.setSite1(this));
        }
        this.courses = courses;
    }

    public Site courses(Set<Course> courses) {
        this.setCourses(courses);
        return this;
    }

    public Site addCourse(Course course) {
        this.courses.add(course);
        course.setSite1(this);
        return this;
    }

    public Site removeCourse(Course course) {
        this.courses.remove(course);
        course.setSite1(null);
        return this;
    }

    public Set<Part> getParts() {
        return this.parts;
    }

    public void setParts(Set<Part> parts) {
        if (this.parts != null) {
            this.parts.forEach(i -> i.setSite2(null));
        }
        if (parts != null) {
            parts.forEach(i -> i.setSite2(this));
        }
        this.parts = parts;
    }

    public Site parts(Set<Part> parts) {
        this.setParts(parts);
        return this;
    }

    public Site addPart(Part part) {
        this.parts.add(part);
        part.setSite2(this);
        return this;
    }

    public Site removePart(Part part) {
        this.parts.remove(part);
        part.setSite2(null);
        return this;
    }

    public Set<Review> getReviews() {
        return this.reviews;
    }

    public void setReviews(Set<Review> reviews) {
        if (this.reviews != null) {
            this.reviews.forEach(i -> i.setSite3(null));
        }
        if (reviews != null) {
            reviews.forEach(i -> i.setSite3(this));
        }
        this.reviews = reviews;
    }

    public Site reviews(Set<Review> reviews) {
        this.setReviews(reviews);
        return this;
    }

    public Site addReview(Review review) {
        this.reviews.add(review);
        review.setSite3(this);
        return this;
    }

    public Site removeReview(Review review) {
        this.reviews.remove(review);
        review.setSite3(null);
        return this;
    }

    public Set<Enrolement> getEnrolements() {
        return this.enrolements;
    }

    public void setEnrolements(Set<Enrolement> enrolements) {
        if (this.enrolements != null) {
            this.enrolements.forEach(i -> i.setSite4(null));
        }
        if (enrolements != null) {
            enrolements.forEach(i -> i.setSite4(this));
        }
        this.enrolements = enrolements;
    }

    public Site enrolements(Set<Enrolement> enrolements) {
        this.setEnrolements(enrolements);
        return this;
    }

    public Site addEnrolement(Enrolement enrolement) {
        this.enrolements.add(enrolement);
        enrolement.setSite4(this);
        return this;
    }

    public Site removeEnrolement(Enrolement enrolement) {
        this.enrolements.remove(enrolement);
        enrolement.setSite4(null);
        return this;
    }

    public Set<Question> getQuestions() {
        return this.questions;
    }

    public void setQuestions(Set<Question> questions) {
        if (this.questions != null) {
            this.questions.forEach(i -> i.setSite5(null));
        }
        if (questions != null) {
            questions.forEach(i -> i.setSite5(this));
        }
        this.questions = questions;
    }

    public Site questions(Set<Question> questions) {
        this.setQuestions(questions);
        return this;
    }

    public Site addQuestion(Question question) {
        this.questions.add(question);
        question.setSite5(this);
        return this;
    }

    public Site removeQuestion(Question question) {
        this.questions.remove(question);
        question.setSite5(null);
        return this;
    }

    public Set<Answer> getAnswers() {
        return this.answers;
    }

    public void setAnswers(Set<Answer> answers) {
        if (this.answers != null) {
            this.answers.forEach(i -> i.setSite6(null));
        }
        if (answers != null) {
            answers.forEach(i -> i.setSite6(this));
        }
        this.answers = answers;
    }

    public Site answers(Set<Answer> answers) {
        this.setAnswers(answers);
        return this;
    }

    public Site addAnswer(Answer answer) {
        this.answers.add(answer);
        answer.setSite6(this);
        return this;
    }

    public Site removeAnswer(Answer answer) {
        this.answers.remove(answer);
        answer.setSite6(null);
        return this;
    }

    public Set<Quiz> getQuizzes() {
        return this.quizzes;
    }

    public void setQuizzes(Set<Quiz> quizzes) {
        if (this.quizzes != null) {
            this.quizzes.forEach(i -> i.setSite7(null));
        }
        if (quizzes != null) {
            quizzes.forEach(i -> i.setSite7(this));
        }
        this.quizzes = quizzes;
    }

    public Site quizzes(Set<Quiz> quizzes) {
        this.setQuizzes(quizzes);
        return this;
    }

    public Site addQuiz(Quiz quiz) {
        this.quizzes.add(quiz);
        quiz.setSite7(this);
        return this;
    }

    public Site removeQuiz(Quiz quiz) {
        this.quizzes.remove(quiz);
        quiz.setSite7(null);
        return this;
    }

    public Set<QuizResult> getQuizResults() {
        return this.quizResults;
    }

    public void setQuizResults(Set<QuizResult> quizResults) {
        if (this.quizResults != null) {
            this.quizResults.forEach(i -> i.setSite8(null));
        }
        if (quizResults != null) {
            quizResults.forEach(i -> i.setSite8(this));
        }
        this.quizResults = quizResults;
    }

    public Site quizResults(Set<QuizResult> quizResults) {
        this.setQuizResults(quizResults);
        return this;
    }

    public Site addQuizResult(QuizResult quizResult) {
        this.quizResults.add(quizResult);
        quizResult.setSite8(this);
        return this;
    }

    public Site removeQuizResult(QuizResult quizResult) {
        this.quizResults.remove(quizResult);
        quizResult.setSite8(null);
        return this;
    }

    public Set<Payment> getPayments() {
        return this.payments;
    }

    public void setPayments(Set<Payment> payments) {
        if (this.payments != null) {
            this.payments.forEach(i -> i.setSite9(null));
        }
        if (payments != null) {
            payments.forEach(i -> i.setSite9(this));
        }
        this.payments = payments;
    }

    public Site payments(Set<Payment> payments) {
        this.setPayments(payments);
        return this;
    }

    public Site addPayment(Payment payment) {
        this.payments.add(payment);
        payment.setSite9(this);
        return this;
    }

    public Site removePayment(Payment payment) {
        this.payments.remove(payment);
        payment.setSite9(null);
        return this;
    }

    public Set<Sponsoring> getSponsorings() {
        return this.sponsorings;
    }

    public void setSponsorings(Set<Sponsoring> sponsorings) {
        if (this.sponsorings != null) {
            this.sponsorings.forEach(i -> i.setSite10(null));
        }
        if (sponsorings != null) {
            sponsorings.forEach(i -> i.setSite10(this));
        }
        this.sponsorings = sponsorings;
    }

    public Site sponsorings(Set<Sponsoring> sponsorings) {
        this.setSponsorings(sponsorings);
        return this;
    }

    public Site addSponsoring(Sponsoring sponsoring) {
        this.sponsorings.add(sponsoring);
        sponsoring.setSite10(this);
        return this;
    }

    public Site removeSponsoring(Sponsoring sponsoring) {
        this.sponsorings.remove(sponsoring);
        sponsoring.setSite10(null);
        return this;
    }

    public Set<Group> getGroups() {
        return this.groups;
    }

    public void setGroups(Set<Group> groups) {
        if (this.groups != null) {
            this.groups.forEach(i -> i.setSite11(null));
        }
        if (groups != null) {
            groups.forEach(i -> i.setSite11(this));
        }
        this.groups = groups;
    }

    public Site groups(Set<Group> groups) {
        this.setGroups(groups);
        return this;
    }

    public Site addGroup(Group group) {
        this.groups.add(group);
        group.setSite11(this);
        return this;
    }

    public Site removeGroup(Group group) {
        this.groups.remove(group);
        group.setSite11(null);
        return this;
    }

    public Set<Project> getProjects() {
        return this.projects;
    }

    public void setProjects(Set<Project> projects) {
        if (this.projects != null) {
            this.projects.forEach(i -> i.setSite12(null));
        }
        if (projects != null) {
            projects.forEach(i -> i.setSite12(this));
        }
        this.projects = projects;
    }

    public Site projects(Set<Project> projects) {
        this.setProjects(projects);
        return this;
    }

    public Site addProject(Project project) {
        this.projects.add(project);
        project.setSite12(this);
        return this;
    }

    public Site removeProject(Project project) {
        this.projects.remove(project);
        project.setSite12(null);
        return this;
    }

    public Set<UserCustom> getUserCustoms() {
        return this.userCustoms;
    }

    public void setUserCustoms(Set<UserCustom> userCustoms) {
        if (this.userCustoms != null) {
            this.userCustoms.forEach(i -> i.setSite13(null));
        }
        if (userCustoms != null) {
            userCustoms.forEach(i -> i.setSite13(this));
        }
        this.userCustoms = userCustoms;
    }

    public Site userCustoms(Set<UserCustom> userCustoms) {
        this.setUserCustoms(userCustoms);
        return this;
    }

    public Site addUserCustom(UserCustom userCustom) {
        this.userCustoms.add(userCustom);
        userCustom.setSite13(this);
        return this;
    }

    public Site removeUserCustom(UserCustom userCustom) {
        this.userCustoms.remove(userCustom);
        userCustom.setSite13(null);
        return this;
    }

    public Set<Session> getSessions() {
        return this.sessions;
    }

    public void setSessions(Set<Session> sessions) {
        if (this.sessions != null) {
            this.sessions.forEach(i -> i.setSite14(null));
        }
        if (sessions != null) {
            sessions.forEach(i -> i.setSite14(this));
        }
        this.sessions = sessions;
    }

    public Site sessions(Set<Session> sessions) {
        this.setSessions(sessions);
        return this;
    }

    public Site addSession(Session session) {
        this.sessions.add(session);
        session.setSite14(this);
        return this;
    }

    public Site removeSession(Session session) {
        this.sessions.remove(session);
        session.setSite14(null);
        return this;
    }

    public Set<SessionLink> getSessionLinks() {
        return this.sessionLinks;
    }

    public void setSessionLinks(Set<SessionLink> sessionLinks) {
        if (this.sessionLinks != null) {
            this.sessionLinks.forEach(i -> i.setSite15(null));
        }
        if (sessionLinks != null) {
            sessionLinks.forEach(i -> i.setSite15(this));
        }
        this.sessionLinks = sessionLinks;
    }

    public Site sessionLinks(Set<SessionLink> sessionLinks) {
        this.setSessionLinks(sessionLinks);
        return this;
    }

    public Site addSessionLink(SessionLink sessionLink) {
        this.sessionLinks.add(sessionLink);
        sessionLink.setSite15(this);
        return this;
    }

    public Site removeSessionLink(SessionLink sessionLink) {
        this.sessionLinks.remove(sessionLink);
        sessionLink.setSite15(null);
        return this;
    }

    public Set<SessionInstance> getSessionInstances() {
        return this.sessionInstances;
    }

    public void setSessionInstances(Set<SessionInstance> sessionInstances) {
        if (this.sessionInstances != null) {
            this.sessionInstances.forEach(i -> i.setSite16(null));
        }
        if (sessionInstances != null) {
            sessionInstances.forEach(i -> i.setSite16(this));
        }
        this.sessionInstances = sessionInstances;
    }

    public Site sessionInstances(Set<SessionInstance> sessionInstances) {
        this.setSessionInstances(sessionInstances);
        return this;
    }

    public Site addSessionInstance(SessionInstance sessionInstance) {
        this.sessionInstances.add(sessionInstance);
        sessionInstance.setSite16(this);
        return this;
    }

    public Site removeSessionInstance(SessionInstance sessionInstance) {
        this.sessionInstances.remove(sessionInstance);
        sessionInstance.setSite16(null);
        return this;
    }

    public Set<Progression> getProgressions() {
        return this.progressions;
    }

    public void setProgressions(Set<Progression> progressions) {
        if (this.progressions != null) {
            this.progressions.forEach(i -> i.setSite17(null));
        }
        if (progressions != null) {
            progressions.forEach(i -> i.setSite17(this));
        }
        this.progressions = progressions;
    }

    public Site progressions(Set<Progression> progressions) {
        this.setProgressions(progressions);
        return this;
    }

    public Site addProgression(Progression progression) {
        this.progressions.add(progression);
        progression.setSite17(this);
        return this;
    }

    public Site removeProgression(Progression progression) {
        this.progressions.remove(progression);
        progression.setSite17(null);
        return this;
    }

    public Set<Tickets> getTickets() {
        return this.tickets;
    }

    public void setTickets(Set<Tickets> tickets) {
        if (this.tickets != null) {
            this.tickets.forEach(i -> i.setSite18(null));
        }
        if (tickets != null) {
            tickets.forEach(i -> i.setSite18(this));
        }
        this.tickets = tickets;
    }

    public Site tickets(Set<Tickets> tickets) {
        this.setTickets(tickets);
        return this;
    }

    public Site addTickets(Tickets tickets) {
        this.tickets.add(tickets);
        tickets.setSite18(this);
        return this;
    }

    public Site removeTickets(Tickets tickets) {
        this.tickets.remove(tickets);
        tickets.setSite18(null);
        return this;
    }

    public Set<Certificate> getCertificates() {
        return this.certificates;
    }

    public void setCertificates(Set<Certificate> certificates) {
        if (this.certificates != null) {
            this.certificates.forEach(i -> i.setSite19(null));
        }
        if (certificates != null) {
            certificates.forEach(i -> i.setSite19(this));
        }
        this.certificates = certificates;
    }

    public Site certificates(Set<Certificate> certificates) {
        this.setCertificates(certificates);
        return this;
    }

    public Site addCertificate(Certificate certificate) {
        this.certificates.add(certificate);
        certificate.setSite19(this);
        return this;
    }

    public Site removeCertificate(Certificate certificate) {
        this.certificates.remove(certificate);
        certificate.setSite19(null);
        return this;
    }

    public Set<Diploma> getDiplomas() {
        return this.diplomas;
    }

    public void setDiplomas(Set<Diploma> diplomas) {
        if (this.diplomas != null) {
            this.diplomas.forEach(i -> i.setSite20(null));
        }
        if (diplomas != null) {
            diplomas.forEach(i -> i.setSite20(this));
        }
        this.diplomas = diplomas;
    }

    public Site diplomas(Set<Diploma> diplomas) {
        this.setDiplomas(diplomas);
        return this;
    }

    public Site addDiploma(Diploma diploma) {
        this.diplomas.add(diploma);
        diploma.setSite20(this);
        return this;
    }

    public Site removeDiploma(Diploma diploma) {
        this.diplomas.remove(diploma);
        diploma.setSite20(null);
        return this;
    }

    public City getCity() {
        return this.city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Site city(City city) {
        this.setCity(city);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Site)) {
            return false;
        }
        return getId() != null && getId().equals(((Site) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Site{" +
            "id=" + getId() +
            ", nameAr='" + getNameAr() + "'" +
            ", nameLat='" + getNameLat() + "'" +
            ", description='" + getDescription() + "'" +
            ", localisation='" + getLocalisation() + "'" +
            "}";
    }
}
