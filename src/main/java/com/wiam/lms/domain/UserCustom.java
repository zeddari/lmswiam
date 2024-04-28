package com.wiam.lms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wiam.lms.domain.enumeration.AccountStatus;
import com.wiam.lms.domain.enumeration.Role;
import com.wiam.lms.domain.enumeration.Sex;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserCustom.
 */
@Entity
@Table(name = "user_custom")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "usercustom")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserCustom implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "first_name", length = 50, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String firstName;

    @NotNull
    @Size(max = 50)
    @Column(name = "last_name", length = 50, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String lastName;

    @Size(max = 100)
    @Column(name = "code", length = 100, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String code;

    @NotNull
    @Size(max = 50)
    @Column(name = "account_name", length = 50, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String accountName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private Role role;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private AccountStatus accountStatus;

    @NotNull
    @Size(max = 50)
    @Column(name = "phone_number_1", length = 50, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String phoneNumber1;

    @Size(max = 50)
    @Column(name = "phone_numver_2", length = 50)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String phoneNumver2;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sex", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private Sex sex;

    @NotNull
    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "photo_content_type")
    private String photoContentType;

    @Lob
    @Column(name = "address")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String address;

    @Column(name = "facebook")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String facebook;

    @Column(name = "telegram_user_custom_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String telegramUserCustomId;

    @Column(name = "telegram_user_custom_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String telegramUserCustomName;

    @Lob
    @Column(name = "biography")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String biography;

    @Lob
    @Column(name = "bank_account_details")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String bankAccountDetails;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User user;

    /**
     * /
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userCustom6")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "site19", "userCustom6", "comitte", "topic4" }, allowSetters = true)
    private Set<Certificate> certificates = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userCustom1")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "site6", "question", "userCustom1" }, allowSetters = true)
    private Set<Answer> answers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userCustom2")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "site8", "quiz", "userCustom2" }, allowSetters = true)
    private Set<QuizResult> quizResults = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userCustom3")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "site3", "part2", "userCustom3" }, allowSetters = true)
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userCustom4")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "payments", "site4", "userCustom4", "course" }, allowSetters = true)
    private Set<Enrolement> enrolements = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "student")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "site17", "sessionInstance", "student" }, allowSetters = true)
    private Set<Progression> progressions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userCustom5")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "site18", "userCustom5" }, allowSetters = true)
    private Set<Tickets> tickets = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sponsor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "payments", "site10", "sponsor", "project", "currency" }, allowSetters = true)
    private Set<Sponsoring> sponsorings = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "resource")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "resource" }, allowSetters = true)
    private Set<Depense> depenses = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_user_custom__diplomas",
        joinColumns = @JoinColumn(name = "user_custom_id"),
        inverseJoinColumns = @JoinColumn(name = "diplomas_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "site20", "userCustom7s" }, allowSetters = true)
    private Set<Diploma> diplomas = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_user_custom__languages",
        joinColumns = @JoinColumn(name = "user_custom_id"),
        inverseJoinColumns = @JoinColumn(name = "languages_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "userCustom8s" }, allowSetters = true)
    private Set<Language> languages = new HashSet<>();

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
    private Site site13;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "userCustoms" }, allowSetters = true)
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "userCustoms" }, allowSetters = true)
    private Nationality nationality;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "userCustoms" }, allowSetters = true)
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "departements", "userCustoms", "departement1" }, allowSetters = true)
    private Departement departement2;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "elements")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = { "certificates", "groups", "elements", "site11", "group1", "quizzes", "sessions5s" },
        allowSetters = true
    )
    private Set<Group> groups = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "professors")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "parts", "enrolements", "professors", "site1", "topic3" }, allowSetters = true)
    private Set<Course> courses = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "professors")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = { "sessionInstances", "payments", "classrooms", "groups", "professors", "employees", "links", "site14" },
        allowSetters = true
    )
    private Set<Session> sessions2s = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "employees")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = { "sessionInstances", "payments", "classrooms", "groups", "professors", "employees", "links", "site14" },
        allowSetters = true
    )
    private Set<Session> sessions3s = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserCustom id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public UserCustom firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public UserCustom lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCode() {
        return this.code;
    }

    public UserCustom code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public UserCustom accountName(String accountName) {
        this.setAccountName(accountName);
        return this;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Role getRole() {
        return this.role;
    }

    public UserCustom role(Role role) {
        this.setRole(role);
        return this;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public AccountStatus getAccountStatus() {
        return this.accountStatus;
    }

    public UserCustom accountStatus(AccountStatus accountStatus) {
        this.setAccountStatus(accountStatus);
        return this;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getPhoneNumber1() {
        return this.phoneNumber1;
    }

    public UserCustom phoneNumber1(String phoneNumber1) {
        this.setPhoneNumber1(phoneNumber1);
        return this;
    }

    public void setPhoneNumber1(String phoneNumber1) {
        this.phoneNumber1 = phoneNumber1;
    }

    public String getPhoneNumver2() {
        return this.phoneNumver2;
    }

    public UserCustom phoneNumver2(String phoneNumver2) {
        this.setPhoneNumver2(phoneNumver2);
        return this;
    }

    public void setPhoneNumver2(String phoneNumver2) {
        this.phoneNumver2 = phoneNumver2;
    }

    public Sex getSex() {
        return this.sex;
    }

    public UserCustom sex(Sex sex) {
        this.setSex(sex);
        return this;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public LocalDate getBirthdate() {
        return this.birthdate;
    }

    public UserCustom birthdate(LocalDate birthdate) {
        this.setBirthdate(birthdate);
        return this;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public byte[] getPhoto() {
        return this.photo;
    }

    public UserCustom photo(byte[] photo) {
        this.setPhoto(photo);
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return this.photoContentType;
    }

    public UserCustom photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public String getAddress() {
        return this.address;
    }

    public UserCustom address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFacebook() {
        return this.facebook;
    }

    public UserCustom facebook(String facebook) {
        this.setFacebook(facebook);
        return this;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTelegramUserCustomId() {
        return this.telegramUserCustomId;
    }

    public UserCustom telegramUserCustomId(String telegramUserCustomId) {
        this.setTelegramUserCustomId(telegramUserCustomId);
        return this;
    }

    public void setTelegramUserCustomId(String telegramUserCustomId) {
        this.telegramUserCustomId = telegramUserCustomId;
    }

    public String getTelegramUserCustomName() {
        return this.telegramUserCustomName;
    }

    public UserCustom telegramUserCustomName(String telegramUserCustomName) {
        this.setTelegramUserCustomName(telegramUserCustomName);
        return this;
    }

    public void setTelegramUserCustomName(String telegramUserCustomName) {
        this.telegramUserCustomName = telegramUserCustomName;
    }

    public String getBiography() {
        return this.biography;
    }

    public UserCustom biography(String biography) {
        this.setBiography(biography);
        return this;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getBankAccountDetails() {
        return this.bankAccountDetails;
    }

    public UserCustom bankAccountDetails(String bankAccountDetails) {
        this.setBankAccountDetails(bankAccountDetails);
        return this;
    }

    public void setBankAccountDetails(String bankAccountDetails) {
        this.bankAccountDetails = bankAccountDetails;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserCustom user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Certificate> getCertificates() {
        return this.certificates;
    }

    public void setCertificates(Set<Certificate> certificates) {
        if (this.certificates != null) {
            this.certificates.forEach(i -> i.setUserCustom6(null));
        }
        if (certificates != null) {
            certificates.forEach(i -> i.setUserCustom6(this));
        }
        this.certificates = certificates;
    }

    public UserCustom certificates(Set<Certificate> certificates) {
        this.setCertificates(certificates);
        return this;
    }

    public UserCustom addCertificate(Certificate certificate) {
        this.certificates.add(certificate);
        certificate.setUserCustom6(this);
        return this;
    }

    public UserCustom removeCertificate(Certificate certificate) {
        this.certificates.remove(certificate);
        certificate.setUserCustom6(null);
        return this;
    }

    public Set<Answer> getAnswers() {
        return this.answers;
    }

    public void setAnswers(Set<Answer> answers) {
        if (this.answers != null) {
            this.answers.forEach(i -> i.setUserCustom1(null));
        }
        if (answers != null) {
            answers.forEach(i -> i.setUserCustom1(this));
        }
        this.answers = answers;
    }

    public UserCustom answers(Set<Answer> answers) {
        this.setAnswers(answers);
        return this;
    }

    public UserCustom addAnswer(Answer answer) {
        this.answers.add(answer);
        answer.setUserCustom1(this);
        return this;
    }

    public UserCustom removeAnswer(Answer answer) {
        this.answers.remove(answer);
        answer.setUserCustom1(null);
        return this;
    }

    public Set<QuizResult> getQuizResults() {
        return this.quizResults;
    }

    public void setQuizResults(Set<QuizResult> quizResults) {
        if (this.quizResults != null) {
            this.quizResults.forEach(i -> i.setUserCustom2(null));
        }
        if (quizResults != null) {
            quizResults.forEach(i -> i.setUserCustom2(this));
        }
        this.quizResults = quizResults;
    }

    public UserCustom quizResults(Set<QuizResult> quizResults) {
        this.setQuizResults(quizResults);
        return this;
    }

    public UserCustom addQuizResult(QuizResult quizResult) {
        this.quizResults.add(quizResult);
        quizResult.setUserCustom2(this);
        return this;
    }

    public UserCustom removeQuizResult(QuizResult quizResult) {
        this.quizResults.remove(quizResult);
        quizResult.setUserCustom2(null);
        return this;
    }

    public Set<Review> getReviews() {
        return this.reviews;
    }

    public void setReviews(Set<Review> reviews) {
        if (this.reviews != null) {
            this.reviews.forEach(i -> i.setUserCustom3(null));
        }
        if (reviews != null) {
            reviews.forEach(i -> i.setUserCustom3(this));
        }
        this.reviews = reviews;
    }

    public UserCustom reviews(Set<Review> reviews) {
        this.setReviews(reviews);
        return this;
    }

    public UserCustom addReview(Review review) {
        this.reviews.add(review);
        review.setUserCustom3(this);
        return this;
    }

    public UserCustom removeReview(Review review) {
        this.reviews.remove(review);
        review.setUserCustom3(null);
        return this;
    }

    public Set<Enrolement> getEnrolements() {
        return this.enrolements;
    }

    public void setEnrolements(Set<Enrolement> enrolements) {
        if (this.enrolements != null) {
            this.enrolements.forEach(i -> i.setUserCustom4(null));
        }
        if (enrolements != null) {
            enrolements.forEach(i -> i.setUserCustom4(this));
        }
        this.enrolements = enrolements;
    }

    public UserCustom enrolements(Set<Enrolement> enrolements) {
        this.setEnrolements(enrolements);
        return this;
    }

    public UserCustom addEnrolement(Enrolement enrolement) {
        this.enrolements.add(enrolement);
        enrolement.setUserCustom4(this);
        return this;
    }

    public UserCustom removeEnrolement(Enrolement enrolement) {
        this.enrolements.remove(enrolement);
        enrolement.setUserCustom4(null);
        return this;
    }

    public Set<Progression> getProgressions() {
        return this.progressions;
    }

    public void setProgressions(Set<Progression> progressions) {
        if (this.progressions != null) {
            this.progressions.forEach(i -> i.setStudent(null));
        }
        if (progressions != null) {
            progressions.forEach(i -> i.setStudent(this));
        }
        this.progressions = progressions;
    }

    public UserCustom progressions(Set<Progression> progressions) {
        this.setProgressions(progressions);
        return this;
    }

    public UserCustom addProgression(Progression progression) {
        this.progressions.add(progression);
        progression.setStudent(this);
        return this;
    }

    public UserCustom removeProgression(Progression progression) {
        this.progressions.remove(progression);
        progression.setStudent(null);
        return this;
    }

    public Set<Tickets> getTickets() {
        return this.tickets;
    }

    public void setTickets(Set<Tickets> tickets) {
        if (this.tickets != null) {
            this.tickets.forEach(i -> i.setUserCustom5(null));
        }
        if (tickets != null) {
            tickets.forEach(i -> i.setUserCustom5(this));
        }
        this.tickets = tickets;
    }

    public UserCustom tickets(Set<Tickets> tickets) {
        this.setTickets(tickets);
        return this;
    }

    public UserCustom addTickets(Tickets tickets) {
        this.tickets.add(tickets);
        tickets.setUserCustom5(this);
        return this;
    }

    public UserCustom removeTickets(Tickets tickets) {
        this.tickets.remove(tickets);
        tickets.setUserCustom5(null);
        return this;
    }

    public Set<Sponsoring> getSponsorings() {
        return this.sponsorings;
    }

    public void setSponsorings(Set<Sponsoring> sponsorings) {
        if (this.sponsorings != null) {
            this.sponsorings.forEach(i -> i.setSponsor(null));
        }
        if (sponsorings != null) {
            sponsorings.forEach(i -> i.setSponsor(this));
        }
        this.sponsorings = sponsorings;
    }

    public UserCustom sponsorings(Set<Sponsoring> sponsorings) {
        this.setSponsorings(sponsorings);
        return this;
    }

    public UserCustom addSponsoring(Sponsoring sponsoring) {
        this.sponsorings.add(sponsoring);
        sponsoring.setSponsor(this);
        return this;
    }

    public UserCustom removeSponsoring(Sponsoring sponsoring) {
        this.sponsorings.remove(sponsoring);
        sponsoring.setSponsor(null);
        return this;
    }

    public Set<Depense> getDepenses() {
        return this.depenses;
    }

    public void setDepenses(Set<Depense> depenses) {
        if (this.depenses != null) {
            this.depenses.forEach(i -> i.setResource(null));
        }
        if (depenses != null) {
            depenses.forEach(i -> i.setResource(this));
        }
        this.depenses = depenses;
    }

    public UserCustom depenses(Set<Depense> depenses) {
        this.setDepenses(depenses);
        return this;
    }

    public UserCustom addDepense(Depense depense) {
        this.depenses.add(depense);
        depense.setResource(this);
        return this;
    }

    public UserCustom removeDepense(Depense depense) {
        this.depenses.remove(depense);
        depense.setResource(null);
        return this;
    }

    public Set<Diploma> getDiplomas() {
        return this.diplomas;
    }

    public void setDiplomas(Set<Diploma> diplomas) {
        this.diplomas = diplomas;
    }

    public UserCustom diplomas(Set<Diploma> diplomas) {
        this.setDiplomas(diplomas);
        return this;
    }

    public UserCustom addDiplomas(Diploma diploma) {
        this.diplomas.add(diploma);
        return this;
    }

    public UserCustom removeDiplomas(Diploma diploma) {
        this.diplomas.remove(diploma);
        return this;
    }

    public Set<Language> getLanguages() {
        return this.languages;
    }

    public void setLanguages(Set<Language> languages) {
        this.languages = languages;
    }

    public UserCustom languages(Set<Language> languages) {
        this.setLanguages(languages);
        return this;
    }

    public UserCustom addLanguages(Language language) {
        this.languages.add(language);
        return this;
    }

    public UserCustom removeLanguages(Language language) {
        this.languages.remove(language);
        return this;
    }

    public Site getSite13() {
        return this.site13;
    }

    public void setSite13(Site site) {
        this.site13 = site;
    }

    public UserCustom site13(Site site) {
        this.setSite13(site);
        return this;
    }

    public Country getCountry() {
        return this.country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public UserCustom country(Country country) {
        this.setCountry(country);
        return this;
    }

    public Nationality getNationality() {
        return this.nationality;
    }

    public void setNationality(Nationality nationality) {
        this.nationality = nationality;
    }

    public UserCustom nationality(Nationality nationality) {
        this.setNationality(nationality);
        return this;
    }

    public Job getJob() {
        return this.job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public UserCustom job(Job job) {
        this.setJob(job);
        return this;
    }

    public Departement getDepartement2() {
        return this.departement2;
    }

    public void setDepartement2(Departement departement) {
        this.departement2 = departement;
    }

    public UserCustom departement2(Departement departement) {
        this.setDepartement2(departement);
        return this;
    }

    public Set<Group> getGroups() {
        return this.groups;
    }

    public void setGroups(Set<Group> groups) {
        if (this.groups != null) {
            this.groups.forEach(i -> i.removeElements(this));
        }
        if (groups != null) {
            groups.forEach(i -> i.addElements(this));
        }
        this.groups = groups;
    }

    public UserCustom groups(Set<Group> groups) {
        this.setGroups(groups);
        return this;
    }

    public UserCustom addGroups(Group group) {
        this.groups.add(group);
        group.getElements().add(this);
        return this;
    }

    public UserCustom removeGroups(Group group) {
        this.groups.remove(group);
        group.getElements().remove(this);
        return this;
    }

    public Set<Course> getCourses() {
        return this.courses;
    }

    public void setCourses(Set<Course> courses) {
        if (this.courses != null) {
            this.courses.forEach(i -> i.removeProfessors(this));
        }
        if (courses != null) {
            courses.forEach(i -> i.addProfessors(this));
        }
        this.courses = courses;
    }

    public UserCustom courses(Set<Course> courses) {
        this.setCourses(courses);
        return this;
    }

    public UserCustom addCourses(Course course) {
        this.courses.add(course);
        course.getProfessors().add(this);
        return this;
    }

    public UserCustom removeCourses(Course course) {
        this.courses.remove(course);
        course.getProfessors().remove(this);
        return this;
    }

    public Set<Session> getSessions2s() {
        return this.sessions2s;
    }

    public void setSessions2s(Set<Session> sessions) {
        if (this.sessions2s != null) {
            this.sessions2s.forEach(i -> i.removeProfessors(this));
        }
        if (sessions != null) {
            sessions.forEach(i -> i.addProfessors(this));
        }
        this.sessions2s = sessions;
    }

    public UserCustom sessions2s(Set<Session> sessions) {
        this.setSessions2s(sessions);
        return this;
    }

    public UserCustom addSessions2(Session session) {
        this.sessions2s.add(session);
        session.getProfessors().add(this);
        return this;
    }

    public UserCustom removeSessions2(Session session) {
        this.sessions2s.remove(session);
        session.getProfessors().remove(this);
        return this;
    }

    public Set<Session> getSessions3s() {
        return this.sessions3s;
    }

    public void setSessions3s(Set<Session> sessions) {
        if (this.sessions3s != null) {
            this.sessions3s.forEach(i -> i.removeEmployees(this));
        }
        if (sessions != null) {
            sessions.forEach(i -> i.addEmployees(this));
        }
        this.sessions3s = sessions;
    }

    public UserCustom sessions3s(Set<Session> sessions) {
        this.setSessions3s(sessions);
        return this;
    }

    public UserCustom addSessions3(Session session) {
        this.sessions3s.add(session);
        session.getEmployees().add(this);
        return this;
    }

    public UserCustom removeSessions3(Session session) {
        this.sessions3s.remove(session);
        session.getEmployees().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserCustom)) {
            return false;
        }
        return getId() != null && getId().equals(((UserCustom) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserCustom{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", code='" + getCode() + "'" +
            ", accountName='" + getAccountName() + "'" +
            ", role='" + getRole() + "'" +
            ", accountStatus='" + getAccountStatus() + "'" +
            ", phoneNumber1='" + getPhoneNumber1() + "'" +
            ", phoneNumver2='" + getPhoneNumver2() + "'" +
            ", sex='" + getSex() + "'" +
            ", birthdate='" + getBirthdate() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", photoContentType='" + getPhotoContentType() + "'" +
            ", address='" + getAddress() + "'" +
            ", facebook='" + getFacebook() + "'" +
            ", telegramUserCustomId='" + getTelegramUserCustomId() + "'" +
            ", telegramUserCustomName='" + getTelegramUserCustomName() + "'" +
            ", biography='" + getBiography() + "'" +
            ", bankAccountDetails='" + getBankAccountDetails() + "'" +
            "}";
    }
}
