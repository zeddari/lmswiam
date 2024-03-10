package com.wiam.lms.domain.dto;

import com.wiam.lms.domain.enumeration.Attendance;
import com.wiam.lms.domain.enumeration.ExamType;
import com.wiam.lms.domain.enumeration.Riwayats;
import com.wiam.lms.domain.enumeration.Sourate;
import com.wiam.lms.domain.enumeration.Tilawa;
import java.io.Serializable;
import java.time.ZonedDateTime;

public class ExamDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private ExamType examType;
    private Riwayats riwaya;
    private Sourate fromSourate;
    private Sourate toSourate;
    private Integer fromAyaNum;
    private Integer toAyaNum;
    private Tilawa tilawaType;
    private Integer tajweedScore;
    private Integer hifdScore;
    private Integer adaeScore;
    private String observation;
    private String sessionName;
    private ZonedDateTime startTime;
    private Attendance attendance;

    public Attendance getAttendance() {
        return attendance;
    }

    public ExamType getExamType() {
        return examType;
    }

    public Riwayats getRiwaya() {
        return riwaya;
    }

    public Sourate getFromSourate() {
        return fromSourate;
    }

    public Sourate getToSourate() {
        return toSourate;
    }

    public Integer getFromAyaNum() {
        return fromAyaNum;
    }

    public Integer getToAyaNum() {
        return toAyaNum;
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

    public String getObservation() {
        return observation;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setExamType(ExamType examType) {
        this.examType = examType;
    }

    public void setRiwaya(Riwayats riwaya) {
        this.riwaya = riwaya;
    }

    public void setFromSourate(Sourate fromSourate) {
        this.fromSourate = fromSourate;
    }

    public void setToSourate(Sourate toSourate) {
        this.toSourate = toSourate;
    }

    public void setFromAyaNum(Integer fromAyaNum) {
        this.fromAyaNum = fromAyaNum;
    }

    public void setToAyaNum(Integer toAyaNum) {
        this.toAyaNum = toAyaNum;
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

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }
}
