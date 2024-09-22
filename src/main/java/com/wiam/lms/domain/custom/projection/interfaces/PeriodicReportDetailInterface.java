package com.wiam.lms.domain.custom.projection.interfaces;

import java.util.Optional;

public interface PeriodicReportDetailInterface {
    public Optional<String> getStartDate();

    public Optional<String> getEndDate();

    public Optional<String> getSchoolName();

    public Optional<String> getSchoolNameLat();

    public Optional<String> getTeacherName();

    public Optional<String> getSessionName();

    public Optional<String> getGroupName();

    public Optional<Integer> getNbStudentTotal();

    public Optional<Integer> getNbStudentTotal2();

    public Optional<Integer> getNbDaysSchoolingTotal();

    public Optional<Integer> getOptionalLessons();

    public Optional<Integer> getTajweedLessons();

    public Optional<Integer> getNbAjzaeHifdTotal();

    public Optional<Integer> getNbAjzaeRevTotal();

    public Optional<Integer> getNbAjzaeHomeExamTotal();

    public Optional<Integer> getNbPageGroupTalqeen();

    public Optional<String> getStudentName();

    public Optional<Integer> getNbAjzaeHifz();

    public Optional<Integer> getNbPageHifz();

    public Optional<Integer> getHifzScore();

    public Optional<Integer> getNbAjzaeRev();

    public Optional<Integer> getNbPageRev();

    public Optional<Integer> getScoreRev();

    public Optional<Integer> getNbAjzaeHomeExam();

    public Optional<Integer> getNbPageHomeExam();

    public Optional<Integer> getHifzScoreHomeExam();

    public Optional<Integer> getNbDaysSchoolingStudent();

    public Optional<Integer> getPercentDaysSchoolingTotal();
}
