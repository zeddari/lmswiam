package com.wiam.lms.domain.custom.projection.interfaces;

import java.util.Optional;

public interface PeriodicReportDetailInterface {
    public String getStartDate();

    public String getEndDate();

    public String getSchoolName();

    public String getSchoolNameLat();

    public String getTeacherName();

    public String getSessionName();

    public String getGroupName();

    public Integer getNbStudentTotal();

    public Integer getNbStudentTotal2();

    public Integer getNbDaysSchoolingTotal();

    public Integer getOptionalLessons();

    public Integer getTajweedLessons();

    public Integer getNbAjzaeHifdTotal();

    public Integer getNbAjzaeRevTotal();

    public Integer getNbAjzaeHomeExamTotal();

    public Integer getNbPageGroupTalqeen();

    public String getStudentName();

    public Integer getNbAjzaeHifz();

    public Integer getNbPageHifz();

    public Integer getHifzScore();

    public Integer getNbAjzaeRev();

    public Integer getNbPageRev();

    public Integer getScoreRev();

    public Integer getNbAjzaeHomeExam();

    public Integer getNbPageHomeExam();

    public Integer getHifzScoreHomeExam();

    public Integer getNbDaysSchoolingStudent();

    public Long getPercentDaysSchoolingTotal();
}
