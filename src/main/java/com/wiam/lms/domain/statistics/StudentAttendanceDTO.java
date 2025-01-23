package com.wiam.lms.domain.statistics;

import com.wiam.lms.domain.enumeration.Attendance;
import java.time.LocalDate;

public class StudentAttendanceDTO {

    private Long sessionId;
    private LocalDate sessionDate;
    private String sessionTitle;
    private Attendance attendance;
    private boolean lateArrival;
    private boolean earlyDeparture;

    public StudentAttendanceDTO(
        Long sessionId,
        LocalDate sessionDate,
        String sessionTitle,
        Attendance attendance,
        boolean lateArrival,
        boolean earlyDeparture
    ) {
        this.sessionId = sessionId;
        this.sessionDate = sessionDate;
        this.sessionTitle = sessionTitle;
        this.attendance = attendance;
        this.lateArrival = lateArrival;
        this.earlyDeparture = earlyDeparture;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public void setSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }

    public void setSessionTitle(String sessionTitle) {
        this.sessionTitle = sessionTitle;
    }

    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;
    }

    public void setLateArrival(boolean lateArrival) {
        this.lateArrival = lateArrival;
    }

    public void setEarlyDeparture(boolean earlyDeparture) {
        this.earlyDeparture = earlyDeparture;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public String getSessionTitle() {
        return sessionTitle;
    }

    public Attendance getAttendance() {
        return attendance;
    }

    public boolean isLateArrival() {
        return lateArrival;
    }

    public boolean isEarlyDeparture() {
        return earlyDeparture;
    }
}
