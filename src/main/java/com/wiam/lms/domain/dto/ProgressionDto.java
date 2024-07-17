package com.wiam.lms.domain.dto;

import com.wiam.lms.domain.enumeration.Attendance;
import com.wiam.lms.domain.enumeration.ExamType;
import com.wiam.lms.domain.enumeration.Riwayats;
import com.wiam.lms.domain.enumeration.Tilawa;
import java.time.LocalDate;

public class ProgressionDto {

    private Long id;
    private ExamType examType;
    private Riwayats riwaya;
    private Integer ayahsCount;
    private Tilawa tilawaType;
    private Integer tajweedScore;
    private Integer hifdScore;
    private Integer adaeScore;
    private LocalDate sessionDate;
    private boolean isForAttendance;
    private Attendance attendance;
    private Long studentId;
    private boolean lateArrival;
    private boolean earlyDeparture;

    public void setLateArrival(boolean lateArrival) {
        this.lateArrival = lateArrival;
    }

    public void setEarlyDeparture(boolean earlyDeparture) {
        this.earlyDeparture = earlyDeparture;
    }

    public boolean isLateArrival() {
        return lateArrival;
    }

    public boolean isEarlyDeparture() {
        return earlyDeparture;
    }

    public void setForAttendance(boolean isForAttendance) {
        this.isForAttendance = isForAttendance;
    }

    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public boolean isForAttendance() {
        return isForAttendance;
    }

    public Attendance getAttendance() {
        return attendance;
    }

    public Long getStudentId() {
        return studentId;
    }

    public Long getId() {
        return id;
    }

    public ExamType getExamType() {
        return examType;
    }

    public Riwayats getRiwaya() {
        return riwaya;
    }

    public Integer getAyahsCount() {
        return ayahsCount;
    }

    public Tilawa getTilawaType() {
        return tilawaType;
    }

    public Integer getTajweedScore() {
        return tajweedScore;
    }

    public Integer getHifdScore() {
        return hifdScore;
    }

    public Integer getAdaeScore() {
        return adaeScore;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setExamType(ExamType examType) {
        this.examType = examType;
    }

    public void setRiwaya(Riwayats riwaya) {
        this.riwaya = riwaya;
    }

    public void setAyahsCount(Integer ayahsCount) {
        this.ayahsCount = ayahsCount;
    }

    public void setTilawaType(Tilawa tilawaType) {
        this.tilawaType = tilawaType;
    }

    public void setTajweedScore(Integer tajweedScore) {
        this.tajweedScore = tajweedScore;
    }

    public void setHifdScore(Integer hifdScore) {
        this.hifdScore = hifdScore;
    }

    public void setAdaeScore(Integer adaeScore) {
        this.adaeScore = adaeScore;
    }

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }
}
