package com.notezkanban;

import java.util.ArrayList;
import java.util.List;

public class Board {
    String boardId;
    String boardName;
    boolean isDeleted;
    List<BoardMember> boardMembers;
    List<Workflow> workflows;

    public Board(String boardId, String boardName) {
        this.boardId = boardId;
        this.boardName = boardName;
        workflows = new ArrayList<>();
        boardMembers = new ArrayList<>();
    }

    public String getBoardId() {
        return boardId;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void rename(String newBoardName) {
        if (isDeleted) {
            throw new IllegalStateException("Cannot rename a deleted board");
        }
        boardName = newBoardName;
    }

    public String getBoardName() {
        return boardName;
    }

    public void addWorkflow(Workflow workflow) {
        workflows.add(workflow);
    }

    public Workflow getWorkflow(String workflowId) {
        for (Workflow workflow : workflows) {
            if (workflow.workflowId.equals(workflowId)) {
                return workflow;
            }
        }
        return null;
    }

    public void markAsDeleted() {
        isDeleted = true;
    }

    public void addBoardMember(BoardMember boardMember) {
        boardMembers.add(boardMember);
    }

    public List<BoardMember> getMembers() {
        return boardMembers;
    }


}
