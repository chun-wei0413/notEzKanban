package com.notezkanban;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Board {
    String boardId;
    String boardName;
    boolean isDeleted;
    List<BoardMember> boardMembers;
    List<Workflow> workflows;

    public Board(String boardId, String boardName) {
        checkBoardName(boardName);
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
        checkBoardName(newBoardName);
        boardName = newBoardName;
    }

    public String getBoardName() {
        return boardName;
    }

    public void addWorkflow(Workflow workflow) {
        workflows.add(workflow);
    }

    public void deleteWorkflow(String workflowId) {
        Workflow workflow = getWorkflow(workflowId).get();
        workflows.remove(workflow);
    }

    public Optional<Workflow> getWorkflow(String workflowId) {
        for (Workflow workflow : workflows) {
            if (workflow.workflowId.equals(workflowId)) {
                return Optional.of(workflow);
            }
        }
        return Optional.empty();
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

    private void checkBoardName(String boardName) {
        if(boardName.trim().isEmpty()){
            throw new IllegalArgumentException("Board name cannot be empty");
        }
    }

}
