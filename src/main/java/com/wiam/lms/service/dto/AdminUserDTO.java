package com.wiam.lms.service.dto;

import com.wiam.lms.config.Constants;
import com.wiam.lms.domain.Authority;
import com.wiam.lms.domain.City;
import com.wiam.lms.domain.Country;
import com.wiam.lms.domain.Job;
import com.wiam.lms.domain.Language;
import com.wiam.lms.domain.Nationality;
import com.wiam.lms.domain.UserCustom;
import com.wiam.lms.domain.enumeration.AccountStatus;
import com.wiam.lms.domain.enumeration.Role;
import com.wiam.lms.domain.enumeration.Sex;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO representing a user, with his authorities.
 */
public class AdminUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    private String email;

    @Size(max = 256)
    private String imageUrl;

    private boolean activated = false;

    @Size(min = 2, max = 10)
    private String langKey;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private Set<String> authorities;

    private Role role;
    private String phoneNumber1;
    private String phoneNumver2;
    private Sex sex;
    private LocalDate birthdate;
    private String address;
    private String biography;
    private String code;

    private Country country;
    private City city;
    private Nationality nationality;
    private Job job;
    private Set<Language> languages = new HashSet<>();
    private AccountStatus accountStatus;

    public void setRole(Role role) {
        this.role = role;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public String getCode() {
        return code;
    }

    public AdminUserDTO() {
        // Empty constructor needed for Jackson.
    }

    public AdminUserDTO(UserCustom user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.activated = user.isActivated();
        this.imageUrl = user.getImageUrl();
        this.langKey = user.getLangKey();
        this.createdBy = user.getCreatedBy();
        this.createdDate = user.getCreatedDate();
        this.lastModifiedBy = user.getLastModifiedBy();
        this.lastModifiedDate = user.getLastModifiedDate();
        this.authorities = user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet());

        this.role = user.getRole();
        this.phoneNumber1 = user.getPhoneNumber1();
        this.phoneNumver2 = user.getPhoneNumver2();
        this.sex = user.getSex();
        this.birthdate = user.getBirthdate();
        this.address = user.getAddress();
        this.biography = user.getBiography();
        this.country = user.getCountry();
        this.city = user.getCity();
        this.nationality = user.getNationality();
        this.job = user.getJob();
        this.languages = user.getLanguages();
        this.accountStatus = user.getAccountStatus();
        this.code = user.getCode();
    }

    public Role getRole() {
        return role;
    }

    public String getPhoneNumber1() {
        return phoneNumber1;
    }

    public String getPhoneNumver2() {
        return phoneNumver2;
    }

    public Sex getSex() {
        return sex;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public String getAddress() {
        return address;
    }

    public String getBiography() {
        return biography;
    }

    public Country getCountry() {
        return country;
    }

    public City getCity() {
        return city;
    }

    public Nationality getNationality() {
        return nationality;
    }

    public Job getJob() {
        return job;
    }

    public Set<Language> getLanguages() {
        return languages;
    }

    public void setPhoneNumber1(String phoneNumber1) {
        this.phoneNumber1 = phoneNumber1;
    }

    public void setPhoneNumver2(String phoneNumver2) {
        this.phoneNumver2 = phoneNumver2;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public void setNationality(Nationality nationality) {
        this.nationality = nationality;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public void setLanguages(Set<Language> languages) {
        this.languages = languages;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdminUserDTO{" +
            "login='" + login + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", imageUrl='" + imageUrl + '\'' +
            ", activated=" + activated +
            ", langKey='" + langKey + '\'' +
            ", createdBy=" + createdBy +
            ", createdDate=" + createdDate +
            ", lastModifiedBy='" + lastModifiedBy + '\'' +
            ", lastModifiedDate=" + lastModifiedDate +
            ", authorities=" + authorities +
            "}";
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
