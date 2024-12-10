package com.notezkanban;

import com.notezkanban.boardMember.BoardMember;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Board {
    private final String boardId;
    private String boardName;
    private boolean isDeleted;
    private final List<BoardMember> boardMembers;
    private final List<Workflow> workflows;

    public Board(String boardId, String boardName) {
        this.boardId = Objects.requireNonNull(boardId, "Board ID cannot be null");
        validateBoardName(boardName);
        this.boardName = boardName;
        this.boardMembers = new ArrayList<>();
        this.workflows = new ArrayList<>();
        this.isDeleted = false;
    }

    // Getters
    public String getBoardId() {
        return boardId;
    }

    public String getBoardName() {
        return boardName;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public List<BoardMember> getMembers() {
        return Collections.unmodifiableList(boardMembers);
    }

    public List<Workflow> getWorkflows() {
        return Collections.unmodifiableList(workflows);
    }

    // Board Operations
    public void rename(String newBoardName) {
        validateBoardState();
        validateBoardName(newBoardName);
        this.boardName = newBoardName;
    }

    public void markAsDeleted() {
        this.isDeleted = true;
    }

    // Workflow Management
    public void addWorkflow(Workflow workflow) {
        validateBoardState();
        Objects.requireNonNull(workflow, "Workflow cannot be null");
        workflows.add(workflow);
    }

    public void deleteWorkflow(String workflowId) {
        validateBoardState();
        findWorkflowById(workflowId)
                .ifPresent(workflows::remove);
    }

    public Optional<Workflow> findWorkflowById(String workflowId) {
        return workflows.stream()
                .filter(workflow -> workflow.workflowId.equals(workflowId))
                .findFirst();
    }

    // Member Management
    public void addBoardMember(BoardMember boardMember) {
        validateBoardState();
        Objects.requireNonNull(boardMember, "Board member cannot be null");
        boardMembers.add(boardMember);
    }

    public void removeBoardMember(BoardMember boardMember) {
        validateBoardState();
        Objects.requireNonNull(boardMember, "Board member cannot be null");
        boardMembers.remove(boardMember);
    }

    // Validation
    private void validateBoardState() {
        if (isDeleted) {
            throw new IllegalStateException("Cannot perform operations on a deleted board");
        }
    }

    private void validateBoardName(String boardName) {
        if (boardName == null || boardName.trim().isEmpty()) {
            throw new IllegalArgumentException("Board name cannot be empty");
        }
    }
}
