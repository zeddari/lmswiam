package com.wiam.lms.domain.dto.custom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wiam.lms.domain.UserCustom;
import com.wiam.lms.domain.enumeration.Role;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;

public class ChatMemberDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String login;
    private String firstName;
    private String lastName;

    public String getLogin() {
        return login;
    }

    public void setFather(UserCustom father) {
        this.father = father;
    }

    public void setMother(UserCustom mother) {
        this.mother = mother;
    }

    public void setWali(UserCustom wali) {
        this.wali = wali;
    }

    public UserCustom getFather() {
        return father;
    }

    public UserCustom getMother() {
        return mother;
    }

    public UserCustom getWali() {
        return wali;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "father",
            "mother",
            "wali",
            "photo",
            "createdBy",
            "createdDate",
            "lastModifiedBy",
            "lastModifiedDate",
            "accountStatus",
            "photoContentType",
            "facebook",
            "telegramUserCustomId",
            "telegramUserCustomName",
            "bankAccountDetails",
            "email",
            "activated",
            "langKey",
            "imageUrl",
            "resetDate",
            "phoneNumber1",
            "phoneNumver2",
            "birthdate",
            "biography",
            "city",
            "code",
            "address",
            "sex",
            "role",
            "user",
            "certificates",
            "answers",
            "quizResults",
            "reviews",
            "enrolements",
            "progressions",
            "tickets",
            "sponsorings",
            "depenses",
            "diplomas",
            "languages",
            "site13",
            "country",
            "nationality",
            "job",
            "departement2",
            "groups",
            "courses",
            "sessions2s",
            "sessions3s",
        },
        allowSetters = true
    )
    private UserCustom father;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "father",
            "mother",
            "wali",
            "photo",
            "createdBy",
            "createdDate",
            "lastModifiedBy",
            "lastModifiedDate",
            "accountStatus",
            "photoContentType",
            "facebook",
            "telegramUserCustomId",
            "telegramUserCustomName",
            "bankAccountDetails",
            "email",
            "activated",
            "langKey",
            "imageUrl",
            "resetDate",
            "phoneNumber1",
            "phoneNumver2",
            "birthdate",
            "biography",
            "city",
            "code",
            "address",
            "sex",
            "role",
            "user",
            "certificates",
            "answers",
            "quizResults",
            "reviews",
            "enrolements",
            "progressions",
            "tickets",
            "sponsorings",
            "depenses",
            "diplomas",
            "languages",
            "site13",
            "country",
            "nationality",
            "job",
            "departement2",
            "groups",
            "courses",
            "sessions2s",
            "sessions3s",
        },
        allowSetters = true
    )
    private UserCustom mother;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "father",
            "mother",
            "wali",
            "photo",
            "createdBy",
            "createdDate",
            "lastModifiedBy",
            "lastModifiedDate",
            "accountStatus",
            "photoContentType",
            "facebook",
            "telegramUserCustomId",
            "telegramUserCustomName",
            "bankAccountDetails",
            "email",
            "activated",
            "langKey",
            "imageUrl",
            "resetDate",
            "phoneNumber1",
            "phoneNumver2",
            "birthdate",
            "biography",
            "city",
            "code",
            "address",
            "sex",
            "role",
            "user",
            "certificates",
            "answers",
            "quizResults",
            "reviews",
            "enrolements",
            "progressions",
            "tickets",
            "sponsorings",
            "depenses",
            "diplomas",
            "languages",
            "site13",
            "country",
            "nationality",
            "job",
            "departement2",
            "groups",
            "courses",
            "sessions2s",
            "sessions3s",
        },
        allowSetters = true
    )
    private UserCustom wali;

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
