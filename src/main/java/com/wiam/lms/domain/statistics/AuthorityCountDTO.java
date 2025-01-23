package com.wiam.lms.domain.statistics;

import com.wiam.lms.domain.enumeration.Role;

public class AuthorityCountDTO {

    private Role role;
    private Long userCount;

    public AuthorityCountDTO(String authorityName, Long userCount) {
        this.role = authorityNameToRole(authorityName);
        this.userCount = userCount;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getUserCount() {
        return userCount;
    }

    public void setUserCount(Long userCount) {
        this.userCount = userCount;
    }

    private Role authorityNameToRole(String authorityName) {
        switch (authorityName) {
            case "ROLE_INSTRUCTOR":
                return Role.INSTRUCTOR;
            case "ROLE_STUDENT":
                return Role.STUDENT;
            case "ROLE_PARENT":
                return Role.PARENT;
            case "ROLE_MANAGEMENT":
                return Role.MANAGEMENT;
            case "ROLE_SUPERVISOR":
                return Role.SUPERVISOR;
            case "ROLE_MANAGER":
                return Role.MANAGER;
            case "ROLE_SUPER_MANAGER":
                return Role.SUPER_MANAGER;
            case "ROLE_SPONSOR":
                return Role.SPONSOR;
            default:
                return null;
        }
    }
}
