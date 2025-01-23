package com.wiam.lms.domain.statistics;

public class AttendanceCounts {

    private Long userId;
    private Long siteId;
    private String sessionDate;
    private String gender;
    private String sessionType;
    private Long sessionId;

    private long presenceCount;
    private long absenceCount;
    private long lateArrivalCount;
    private long earlyDepartureCount;
    private long absenceAuthorizedCount;
    private long noneCount;

    public long getAbsenceAuthorizedCount() {
        return absenceAuthorizedCount;
    }

    public long getNoneCount() {
        return noneCount;
    }

    public void setAbsenceAuthorizedCount(long absenceAuthorizedCount) {
        this.absenceAuthorizedCount = absenceAuthorizedCount;
    }

    public void setNoneCount(long noneCount) {
        this.noneCount = noneCount;
    }

    // Getters and setters for the new fields
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(String sessionDate) {
        this.sessionDate = sessionDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    // Getters and setters for counts
    public long getPresenceCount() {
        return presenceCount;
    }

    public void setPresenceCount(long presenceCount) {
        this.presenceCount = presenceCount;
    }

    public long getAbsenceCount() {
        return absenceCount;
    }

    public void setAbsenceCount(long absenceCount) {
        this.absenceCount = absenceCount;
    }

    public long getLateArrivalCount() {
        return lateArrivalCount;
    }

    public void setLateArrivalCount(long lateArrivalCount) {
        this.lateArrivalCount = lateArrivalCount;
    }

    public long getEarlyDepartureCount() {
        return earlyDepartureCount;
    }

    public void setEarlyDepartureCount(long earlyDepartureCount) {
        this.earlyDepartureCount = earlyDepartureCount;
    }

    public void incrementPresenceCount() {
        this.presenceCount++;
    }

    public void incrementAbsenceCount() {
        this.absenceCount++;
    }

    public void incrementLateArrivalCount() {
        this.lateArrivalCount++;
    }

    public void incrementEarlyDepartureCount() {
        this.earlyDepartureCount++;
    }

    public void incrementAbsentAuthorizedCount() {
        this.absenceAuthorizedCount++;
    }

    public void incrementNoneCount() {
        this.noneCount++;
    }
}
