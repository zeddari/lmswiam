package com.wiam.lms.domain.custom.projection.interfaces;

import lombok.Builder;
import lombok.Data;

public interface PeriodicReportPdfDetailInterface {
    public String getSchoolName();

    public String getSchoolNameLat();

    public String getTeacherName();

    public String getSessionName();

    public String getGroupName();

    public int getNbStudentSession();

    public int getNbStudentSession2();

    public int getNbDaysSchoolingTotal();

    public int getOptionalLessons();

    public int getTajweedLessons();

    public int getNbAjzaeHifdTotal();

    public int getNbAjzaeRevTotal();

    public int getNbAjzaeHomeExamTotal();

    public int getNbPageGroupTalqeen();

    public String getStudentName();

    public int getNbAjzaeHifz();

    public int getNbPageHifz();

    public int getHifzScore();

    public int getNbAjzaeRev();

    public int getNbPageRev();

    public int getScoreRev();

    public int getNbAjzaeHomeExam();

    public int getNbPageHomeExam();

    public int getHifzScoreHomeExam();

    public int getNbDaysSchoolingStudent();

    public int getPercentDaysSchoolingTotal();
}
