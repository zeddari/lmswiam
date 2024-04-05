package com.wiam.lms.domain.custom.projection.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PeriodicReportPdfDetail {

    private String schoolName;
    private String schoolNameLat;
    private String teacherName;
    private String sessionName;
    private String groupName;
    private int nbStudentSession;
    private int nbStudentSession2;
    private int nbDaysSchoolingTotal;
    private int optionalLessons;
    private int tajweedLessons;
    private int nbAjzaeHifdTotal;
    private int nbAjzaeRevTotal;
    private int nbAjzaeHomeExamTotal;
    private int nbPageGroupTalqeen;
    private String studentName;
    private int nbAjzaeHifz;
    private int nbPageHifz;
    private int hifzScore;
    private int nbAjzaeRev;
    private int nbPageRev;
    private int scoreRev;
    private int nbAjzaeHomeExam;
    private int nbPageHomeExam;
    private int hifzScoreHomeExam;
    private int nbDaysSchoolingStudent;
    private int percentDaysSchoolingTotal;
}
