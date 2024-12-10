package com.notezkanban.boardMember;

public class BoardMember {
    private final String userId;
    private final BoardRole role;

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
