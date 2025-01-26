package com.wiam.lms.domain.dto;

import java.io.Serializable;

public class SurahDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String nameAr;
    private String nameEn;
    private Integer ayahsCount;

    public Integer getAyahsCount() {
        return ayahsCount;
    }

    // Constructor
    public SurahDto(Integer id, String nameAr, String nameEn, Integer ayahsCount) {
        this.id = id;
        this.nameAr = nameAr;
        this.nameEn = nameEn;
        this.ayahsCount = ayahsCount;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    @Override
    public String toString() {
        return "SurahDto{" + "id=" + id + ", nameAr='" + nameAr + "'" + ", nameEn='" + nameEn + "'" + '}';
    }

    public void setAyahsCount(Integer ayahsCount) {
        this.ayahsCount = ayahsCount;
    }
}
