package com.wiam.lms.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wiam.lms.domain.enumeration.SessionType;
import com.wiam.lms.domain.enumeration.TargetedGender;
import java.time.LocalDate;

public class SessionDTO {

    private Long id;
    private String title;
    private Long siteId;
    private String nameAr;
    private String nameLat;
    private SessionType sessionType;
    private TargetedGender targetedGender;
    private LocalDate periodStartDate;
    private LocalDate periodeEndDate;
    private Boolean sunday;
    private Boolean monday;
    private Boolean tuesday;
    private Boolean wednesday;
    private Boolean thursday;
    private Boolean friday;
    private Boolean saturday;

    public SessionDTO() {}

    public SessionDTO(
        Long id,
        String title,
        Long siteId,
        String nameAr,
        String nameLat,
        SessionType sessionType,
        TargetedGender targetedGender,
        LocalDate periodStartDate,
        LocalDate periodeEndDate,
        Boolean sunday,
        Boolean monday,
        Boolean tuesday,
        Boolean wednesday,
        Boolean thursday,
        Boolean friday,
        Boolean saturday
    ) {
        this.id = id;
        this.title = title;
        this.siteId = siteId;
        this.nameAr = nameAr;
        this.nameLat = nameLat;
        this.sessionType = sessionType;
        this.targetedGender = targetedGender;
        this.periodStartDate = periodStartDate;
        this.periodeEndDate = periodeEndDate;
        this.sunday = sunday != null ? sunday : false;
        this.monday = monday != null ? monday : false;
        this.tuesday = tuesday != null ? tuesday : false;
        this.wednesday = wednesday != null ? wednesday : false;
        this.thursday = thursday != null ? thursday : false;
        this.friday = friday != null ? friday : false;
        this.saturday = saturday != null ? saturday : false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getNameLat() {
        return nameLat;
    }

    public void setNameLat(String nameLat) {
        this.nameLat = nameLat;
    }

    public SessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(SessionType sessionType) {
        this.sessionType = sessionType;
    }

    public TargetedGender getTargetedGender() {
        return targetedGender;
    }

    public void setTargetedGender(TargetedGender targetedGender) {
        this.targetedGender = targetedGender;
    }

    public LocalDate getPeriodStartDate() {
        return periodStartDate;
    }

    public void setPeriodStartDate(LocalDate periodStartDate) {
        this.periodStartDate = periodStartDate;
    }

    public LocalDate getPeriodeEndDate() {
        return periodeEndDate;
    }

    public void setPeriodeEndDate(LocalDate periodeEndDate) {
        this.periodeEndDate = periodeEndDate;
    }

    public Boolean getSunday() {
        return sunday != null ? sunday : false;
    }

    public void setSunday(Boolean sunday) {
        this.sunday = sunday;
    }

    public Boolean getMonday() {
        return monday != null ? monday : false;
    }

    public void setMonday(Boolean monday) {
        this.monday = monday;
    }

    public Boolean getTuesday() {
        return tuesday != null ? tuesday : false;
    }

    public void setTuesday(Boolean tuesday) {
        this.tuesday = tuesday;
    }

    public Boolean getWednesday() {
        return wednesday != null ? wednesday : false;
    }

    public void setWednesday(Boolean wednesday) {
        this.wednesday = wednesday;
    }

    public Boolean getThursday() {
        return thursday != null ? thursday : false;
    }

    public void setThursday(Boolean thursday) {
        this.thursday = thursday;
    }

    public Boolean getFriday() {
        return friday != null ? friday : false;
    }

    public void setFriday(Boolean friday) {
        this.friday = friday;
    }

    public Boolean getSaturday() {
        return saturday != null ? saturday : false;
    }

    public void setSaturday(Boolean saturday) {
        this.saturday = saturday;
    }

    @Override
    public String toString() {
        return (
            "SessionDTO{" +
            "id=" +
            id +
            ", title='" +
            title +
            '\'' +
            ", siteId=" +
            siteId +
            ", nameAr='" +
            nameAr +
            '\'' +
            ", nameLat='" +
            nameLat +
            '\'' +
            ", sessionType=" +
            sessionType +
            ", targetedGender=" +
            targetedGender +
            ", periodStartDate=" +
            periodStartDate +
            ", periodeEndDate=" +
            periodeEndDate +
            ", sunday=" +
            sunday +
            ", monday=" +
            monday +
            ", tuesday=" +
            tuesday +
            ", wednesday=" +
            wednesday +
            ", thursday=" +
            thursday +
            ", friday=" +
            friday +
            ", saturday=" +
            saturday +
            '}'
        );
    }
}
