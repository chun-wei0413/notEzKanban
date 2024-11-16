import com.notezkanban.Board;
import com.notezkanban.Workflow;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class boardTest {

    @Test
    public void createBoard() {
        String boardId = "boardId";
        String boardName = "boardName";
        Board board = new Board(boardId, boardName);

        assertEquals(boardId, board.getBoardId());
        assertEquals(boardName, board.getBoardName());
    }

    @Test
    public void addWorkflowToBoard() {
        String boardId = "boardId";
        String boardName = "boardName";
        Board board = new Board(boardId, boardName);

        String workflowId = "workflowId";
        String workflowName = "workflowName";

        Workflow workflow = new Workflow(workflowId, workflowName);
        board.addWorkflow(workflow);

        assertEquals(workflow, board.getWorkflow(workflowId));
    }
}
