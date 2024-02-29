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

    private Long id;

    private ExamType examType;

    private Riwayats riwaya;

    private Sourate fromSourate;

    private Sourate toSourate;

    private Integer fromAyaNum;

    private Integer toAyaNum;

    private String fromAyaVerset;

    private String toAyaVerset;

    private Tilawa tilawaType;

    private Integer tajweedScore;

    private Integer hifdScore;

    private Integer adaeScore;

    private String observation;

    private String siteName;

    private String sessionName;

    private ZonedDateTime startTime;

    private String studentCode;

    private String studentFullName;

    private Attendance attendance;

    public Attendance getAttendance() {
        return attendance;
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

    public String getFromAyaVerset() {
        return fromAyaVerset;
    }

    public String getToAyaVerset() {
        return toAyaVerset;
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

    public String getSiteName() {
        return siteName;
    }

    public String getSessionName() {
        return sessionName;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public String getStudentFullName() {
        return studentFullName;
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

    public void setFromAyaVerset(String fromAyaVerset) {
        this.fromAyaVerset = fromAyaVerset;
    }

    public void setToAyaVerset(String toAyaVerset) {
        this.toAyaVerset = toAyaVerset;
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

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public void setStudentFullName(String studentFullName) {
        this.studentFullName = studentFullName;
    }

    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
