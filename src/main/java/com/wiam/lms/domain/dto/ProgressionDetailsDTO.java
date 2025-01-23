package com.wiam.lms.domain.dto;

import com.wiam.lms.domain.enumeration.Attendance;
import com.wiam.lms.domain.enumeration.Tilawa;

public class ProgressionDetailsDTO {

    private Long id;
    private Boolean isForAttendance;
    private Attendance attendance;
    private SurahDto fromSourate;
    private SurahDto toSourate;
    private AyahDto fromAyahDto;
    private AyahDto toAyahDto;
    private Tilawa tilawaType;

    // Default constructor
    public ProgressionDetailsDTO() {}

    // Constructor for JPA query
    public ProgressionDetailsDTO(
        Long id,
        Boolean isForAttendance,
        Attendance attendance,
        SurahDto fromSourate,
        SurahDto toSourate,
        AyahDto fromAyahDto,
        AyahDto toAyahDto,
        Tilawa tilawaType
    ) {
        this.id = id;
        this.isForAttendance = isForAttendance;
        this.attendance = attendance;
        this.fromSourate = fromSourate;
        this.toSourate = toSourate;
        this.fromAyahDto = fromAyahDto;
        this.toAyahDto = toAyahDto;
        this.tilawaType = tilawaType;
    }

    // Existing getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsForAttendance() {
        return isForAttendance;
    }

    public void setIsForAttendance(Boolean isForAttendance) {
        this.isForAttendance = isForAttendance;
    }

    public Attendance getAttendance() {
        return attendance;
    }

    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;
    }

    public SurahDto getFromSourate() {
        return fromSourate;
    }

    public void setFromSourate(SurahDto fromSourate) {
        this.fromSourate = fromSourate;
    }

    public SurahDto getToSourate() {
        return toSourate;
    }

    public void setToSourate(SurahDto toSourate) {
        this.toSourate = toSourate;
    }

    public AyahDto getFromAyahDto() {
        return fromAyahDto;
    }

    public void setFromAyahDto(AyahDto fromAyahDto) {
        this.fromAyahDto = fromAyahDto;
    }

    public AyahDto getToAyahDto() {
        return toAyahDto;
    }

    public void setToAyahDto(AyahDto toAyahDto) {
        this.toAyahDto = toAyahDto;
    }

    public Tilawa getTilawaType() {
        return tilawaType;
    }

    public void setTilawaType(Tilawa tilawaType) {
        this.tilawaType = tilawaType;
    }
}
