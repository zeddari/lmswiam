package com.wiam.lms.domain.dto.custom;

public class ElementDto {

    private Long id;
    private String elementFullName;

    public Long getId() {
        return id;
    }

    public ElementDto(Long id, String elementFullName) {
        this.id = id;
        this.elementFullName = elementFullName;
    }

    public String getElementFullName() {
        return elementFullName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setElementFullName(String elementFullName) {
        this.elementFullName = elementFullName;
    }
}
