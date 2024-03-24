package com.wiam.lms.domain.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class RemoteSessionDto implements Serializable {

    private static final long serialVersionUID = 1L;

    // session info
    private String title;
    private String groupName;
    private String gender;
    // session time
    private LocalDate sessionDate;
    private int day;
    private String sessionStartTime;
    private String sessionEndTime;
    // link and session status
    private String link;
    private Boolean isActive;
    // session type
    //private String sessionType;
    // info will be used to display other info such as additional links
    private String info;

    /*public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }*/

    public void setInfo(String info) {
        this.info = info;
    }

    /*public String getSessionType() {
        return sessionType;
    }*/

    public String getInfo() {
        return info;
    }

    public void setSessionStartTime(String sessionStartTime) {
        this.sessionStartTime = sessionStartTime;
    }

    public void setSessionEndTime(String sessionEndTime) {
        this.sessionEndTime = sessionEndTime;
    }

    public String getSessionStartTime() {
        return sessionStartTime;
    }

    public String getSessionEndTime() {
        return sessionEndTime;
    }

    public void setSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public int getDay() {
        return day;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getTitle() {
        return title;
    }

    public String getGender() {
        return gender;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
