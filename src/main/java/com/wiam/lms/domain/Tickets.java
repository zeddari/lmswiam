package com.wiam.lms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wiam.lms.domain.enumeration.TicketStatus;
import com.wiam.lms.domain.enumeration.TicketSubjects;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Tickets.
 */
@Entity
@Table(name = "tickets")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "tickets")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Tickets implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "subject", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private TicketSubjects subject;

    @NotNull
    @Size(max = 100)
    @Column(name = "title", length = 100, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String title;

    @NotNull
    @Size(max = 100)
    @Column(name = "reference", length = 100, nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String reference;

    @Lob
    @Column(name = "description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @Lob
    @Column(name = "justif_doc")
    private byte[] justifDoc;

    @Column(name = "justif_doc_content_type")
    private String justifDocContentType;

    @NotNull
    @Column(name = "date_ticket", nullable = false)
    private ZonedDateTime dateTicket;

    @Column(name = "date_process")
    private ZonedDateTime dateProcess;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "processed", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private TicketStatus processed;

    @Column(name = "jhi_from")
    private ZonedDateTime from;

    @Column(name = "to_date")
    private ZonedDateTime toDate;

    @Lob
    @Column(name = "decision_detail")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String decisionDetail;

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
    private Site site18;

    @ManyToOne(fetch = FetchType.LAZY)
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
    private UserCustom userCustom5;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tickets id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TicketSubjects getSubject() {
        return this.subject;
    }

    public Tickets subject(TicketSubjects subject) {
        this.setSubject(subject);
        return this;
    }

    public void setSubject(TicketSubjects subject) {
        this.subject = subject;
    }

    public String getTitle() {
        return this.title;
    }

    public Tickets title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReference() {
        return this.reference;
    }

    public Tickets reference(String reference) {
        this.setReference(reference);
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDescription() {
        return this.description;
    }

    public Tickets description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getJustifDoc() {
        return this.justifDoc;
    }

    public Tickets justifDoc(byte[] justifDoc) {
        this.setJustifDoc(justifDoc);
        return this;
    }

    public void setJustifDoc(byte[] justifDoc) {
        this.justifDoc = justifDoc;
    }

    public String getJustifDocContentType() {
        return this.justifDocContentType;
    }

    public Tickets justifDocContentType(String justifDocContentType) {
        this.justifDocContentType = justifDocContentType;
        return this;
    }

    public void setJustifDocContentType(String justifDocContentType) {
        this.justifDocContentType = justifDocContentType;
    }

    public ZonedDateTime getDateTicket() {
        return this.dateTicket;
    }

    public Tickets dateTicket(ZonedDateTime dateTicket) {
        this.setDateTicket(dateTicket);
        return this;
    }

    public void setDateTicket(ZonedDateTime dateTicket) {
        this.dateTicket = dateTicket;
    }

    public ZonedDateTime getDateProcess() {
        return this.dateProcess;
    }

    public Tickets dateProcess(ZonedDateTime dateProcess) {
        this.setDateProcess(dateProcess);
        return this;
    }

    public void setDateProcess(ZonedDateTime dateProcess) {
        this.dateProcess = dateProcess;
    }

    public TicketStatus getProcessed() {
        return this.processed;
    }

    public Tickets processed(TicketStatus processed) {
        this.setProcessed(processed);
        return this;
    }

    public void setProcessed(TicketStatus processed) {
        this.processed = processed;
    }

    public ZonedDateTime getFrom() {
        return this.from;
    }

    public Tickets from(ZonedDateTime from) {
        this.setFrom(from);
        return this;
    }

    public void setFrom(ZonedDateTime from) {
        this.from = from;
    }

    public ZonedDateTime getToDate() {
        return this.toDate;
    }

    public Tickets toDate(ZonedDateTime toDate) {
        this.setToDate(toDate);
        return this;
    }

    public void setToDate(ZonedDateTime toDate) {
        this.toDate = toDate;
    }

    public String getDecisionDetail() {
        return this.decisionDetail;
    }

    public Tickets decisionDetail(String decisionDetail) {
        this.setDecisionDetail(decisionDetail);
        return this;
    }

    public void setDecisionDetail(String decisionDetail) {
        this.decisionDetail = decisionDetail;
    }

    public Site getSite18() {
        return this.site18;
    }

    public void setSite18(Site site) {
        this.site18 = site;
    }

    public Tickets site18(Site site) {
        this.setSite18(site);
        return this;
    }

    public UserCustom getUserCustom5() {
        return this.userCustom5;
    }

    public void setUserCustom5(UserCustom userCustom) {
        this.userCustom5 = userCustom;
    }

    public Tickets userCustom5(UserCustom userCustom) {
        this.setUserCustom5(userCustom);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tickets)) {
            return false;
        }
        return getId() != null && getId().equals(((Tickets) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tickets{" +
            "id=" + getId() +
            ", subject='" + getSubject() + "'" +
            ", title='" + getTitle() + "'" +
            ", reference='" + getReference() + "'" +
            ", description='" + getDescription() + "'" +
            ", justifDoc='" + getJustifDoc() + "'" +
            ", justifDocContentType='" + getJustifDocContentType() + "'" +
            ", dateTicket='" + getDateTicket() + "'" +
            ", dateProcess='" + getDateProcess() + "'" +
            ", processed='" + getProcessed() + "'" +
            ", from='" + getFrom() + "'" +
            ", toDate='" + getToDate() + "'" +
            ", decisionDetail='" + getDecisionDetail() + "'" +
            "}";
    }
}
