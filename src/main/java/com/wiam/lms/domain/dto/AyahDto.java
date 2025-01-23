package com.wiam.lms.domain.dto;

import java.io.Serializable;

public class AyahDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer number;

    // Constructor
    public AyahDto(Integer id, Integer number) {
        this.id = id;
        this.number = number;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "AyahDto{" + "id=" + id + ", number=" + number + '}';
    }
}
