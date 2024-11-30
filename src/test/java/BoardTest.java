import com.notezkanban.Board;
import com.notezkanban.BoardMember;
import com.notezkanban.BoardRole;
import com.notezkanban.Workflow;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BoardTest {

    @Test
    public void createBoard() {
        String boardId = "boardId";
        String boardName = "boardName";
        Board board = new Board(boardId, boardName);

        String adminUserId = "adminUserId";
        board.addBoardMember(new BoardMember(adminUserId, BoardRole.Admin));

        assertEquals(boardId, board.getBoardId());
        assertEquals(boardName, board.getBoardName());
        assertEquals(1, board.getMembers().size());
        assertEquals(adminUserId, board.getMembers().get(0).getUserId());
    }

    @Test
    public void deletedABoard() {
        String boardId = "boardId";
        String boardName = "boardName";
        Board board = new Board(boardId, boardName);

        board.markAsDeleted();

        assertEquals(true, board.isDeleted());
    }

    @Test
    public void renameABoard() {
        String boardId = "boardId";
        String boardName = "boardName";
        Board board = new Board(boardId, boardName);

        String newBoardName = "newBoardName";
        board.rename(newBoardName);

        assertEquals(newBoardName, board.getBoardName());
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

    @Test
    public void renameABoardWhichIsDeleted() {
        String boardId = "boardId";
        String boardName = "boardName";
        Board board = new Board(boardId, boardName);

        board.markAsDeleted();

        assertThrows(IllegalStateException.class, () -> {
            board.rename("newBoardName");
        });
    }

    @Test
    public void addABoardMember() {
        String boardId = "boardId";
        String boardName = "boardName";
        Board board = new Board(boardId, boardName);

        String userId = "userId";
        board.addBoardMember(new BoardMember(userId, BoardRole.Member));

        assertEquals(1, board.getMembers().size());
        assertEquals(userId, board.getMembers().get(0).getUserId());
    }
}
