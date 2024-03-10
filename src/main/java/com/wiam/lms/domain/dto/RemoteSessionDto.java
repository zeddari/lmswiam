package com.wiam.lms.domain.dto;

import com.wiam.lms.domain.SessionInstance;
import com.wiam.lms.domain.SessionLink;
import com.wiam.lms.domain.UserCustom;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class RemoteSessionDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String title;
    private String sessionTime;
    private String gender;
    private Boolean monday;
    private Boolean tuesday;
    private Boolean wednesday;
    private Boolean thursday;
    private Boolean friday;
    private Boolean saturday;
    private Boolean sunday;
    private String groupName;
    private Set<UserCustom> professors = new HashSet<>();
    private Set<SessionLink> links = new HashSet<>();
    private String site;
    private Boolean isActive;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(String sessionTime) {
        this.sessionTime = sessionTime;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getGender() {
        return gender;
    }

    public Boolean getMonday() {
        return monday;
    }

    public Boolean getTuesday() {
        return tuesday;
    }

    public Boolean getWednesday() {
        return wednesday;
    }

    public Boolean getThursday() {
        return thursday;
    }

    public Boolean getFriday() {
        return friday;
    }

    public Boolean getSaturday() {
        return saturday;
    }

    public Boolean getSunday() {
        return sunday;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getSite() {
        return site;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setMonday(Boolean monday) {
        this.monday = monday;
    }

    public void setTuesday(Boolean tuesday) {
        this.tuesday = tuesday;
    }

    public void setWednesday(Boolean wednesday) {
        this.wednesday = wednesday;
    }

    public void setThursday(Boolean thursday) {
        this.thursday = thursday;
    }

    public void setFriday(Boolean friday) {
        this.friday = friday;
    }

    public void setSaturday(Boolean saturday) {
        this.saturday = saturday;
    }

    public void setSunday(Boolean sunday) {
        this.sunday = sunday;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setProfessors(Set<UserCustom> professors) {
        this.professors = professors;
    }

    public Set<UserCustom> getProfessors() {
        return professors;
    }

    public Set<SessionLink> getLinks() {
        return links;
    }

    public void setLinks(Set<SessionLink> links) {
        this.links = links;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
