package com.wiam.lms.domain.dto.custom;

import java.util.List;

public class ChatRoomDto {

    private String sessionName;
    private String groupName;
    private List<ChatMemberDto> chatMembers;

    // Getters and Setters
    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<ChatMemberDto> getChatMembers() {
        return chatMembers;
    }

    public void setChatMembers(List<ChatMemberDto> chatMembers) {
        this.chatMembers = chatMembers;
    }
}
