package com.notezkanban;

public class BoardMember {
    private String userId;
    private BoardRole role;

    public BoardMember(String userId, BoardRole role) {
        this.userId = userId;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public BoardRole getRole() {
        return role;
    }
}
