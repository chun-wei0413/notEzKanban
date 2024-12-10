package com.notezkanban;

public class DomainEvent implements Event{
    private String boardId;
    private String message;

    public DomainEvent(String boardId, String message) {
        this.boardId = boardId;
        this.message = message;
    }

    public String getBoardId() {
        return boardId;
    }

    public String getMessage() {
        return message;
    }
}
