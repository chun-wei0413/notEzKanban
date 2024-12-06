import com.notezkanban.Board;
import com.notezkanban.BoardMember;
import com.notezkanban.BoardRole;
import com.notezkanban.Workflow;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    public void createBoard() {
        Board board = createBoard("boardId", "boardName");

        String adminUserId = "adminUserId";
        board.addBoardMember(new BoardMember(adminUserId, BoardRole.Admin));

        assertEquals("boardId", board.getBoardId());
        assertEquals("boardName", board.getBoardName());
        assertEquals(1, board.getMembers().size());
        assertEquals(adminUserId, board.getMembers().get(0).getUserId());
    }

    @Test
    public void addWorkflowToBoard() {
        Board board = createBoard("boardId", "boardName");

        String workflowId = "workflowId";
        String workflowName = "workflowName";

        Workflow workflow = new Workflow(workflowId, workflowName);
        board.addWorkflow(workflow);

        assertEquals(workflowName, board.getWorkflow(workflowId).get().getWorkflowName());
    }

    @Test
    public void deletedBoard() {
        Board board = createBoard("boardId", "boardName");

        board.markAsDeleted();

        assertTrue(board.isDeleted());
    }

    @Test
    public void deletedWorkflowFromBoard() {
        Board board = createBoard("boardId", "boardName");

        String workflowId = "workflowId";
        String workflowName = "workflowName";

        Workflow workflow = new Workflow(workflowId, workflowName);
        board.addWorkflow(workflow);
        board.deleteWorkflow(workflowId);

        assertFalse(board.getWorkflow(workflowId).isPresent());
    }

    @Test
    public void renameBoard() {
        Board board = createBoard("boardId", "boardName");

        String newBoardName = "newBoardName";
        board.rename(newBoardName);

        assertEquals(newBoardName, board.getBoardName());
    }

    @Test
    public void renameABoardWithEmptyName() {
        Board board = createBoard("boardId", "boardName");

        assertThrows(IllegalArgumentException.class, () -> {
            board.rename("");
        });
    }

    @Test
    public void renameABoardWhichIsDeleted() {
        Board board = createBoard("boardId", "boardName");

        board.markAsDeleted();

        assertThrows(IllegalStateException.class, () -> {
            board.rename("newBoardName");
        });
    }

    @Test
    public void addABoardMember() {
        Board board = createBoard("boardId", "boardName");

        String userId = "userId";
        board.addBoardMember(new BoardMember(userId, BoardRole.Member));

        assertEquals(1, board.getMembers().size());
        assertEquals(userId, board.getMembers().get(0).getUserId());
    }

    private Board createBoard(String id, String name) {
        return new Board(id, name);
    }
}
