package com.notezkanban;

import java.util.ArrayList;
import java.util.List;

public class Board {
    String boardId;
    String boardName;
    List<Workflow> workflows;

    public Board(String boardId, String boardName) {
        this.boardId = boardId;
        this.boardName = boardName;
        workflows = new ArrayList<>();
    }

    public String getBoardId() {
        return boardId;
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
}
