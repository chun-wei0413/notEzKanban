import com.notezkanban.Board;
import com.notezkanban.EventBus;
import com.notezkanban.card.Card;
import com.notezkanban.card.CardType;
import com.notezkanban.lane.Stage;
import com.notezkanban.Workflow;
import com.notezkanban.notifier.Notifier;
import com.notezkanban.notifier.notifierImpl.EmailNotifier;
import com.notezkanban.notifier.notifierImpl.LineNotifier;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;


import static org.junit.jupiter.api.Assertions.*;

public class WorkflowTest {
    @Test
    public void createWorkflow() {
        Workflow workflow = createWorkflow("boardId", "workflowId", "workflowName");

        assertEquals("workflowId", workflow.getWorkflowId());
        assertEquals("workflowName", workflow.getWorkflowName());
    }

    @Test
    public void renameWorkflow() {
        Workflow workflow = createWorkflow("boardId", "workflowId", "workflowName");

        workflow.rename("newWorkflowName");

        assertEquals("newWorkflowName", workflow.getWorkflowName());
    }

    @Test
    public void renameWorkflowWithEmptyName() {
        Workflow workflow = createWorkflow("boardId", "workflowId", "workflowName");

        assertThrows(IllegalArgumentException.class, () -> workflow.rename(""));
    }

    @Test
    public void addStageToWorkflow() {
        Workflow workflow = createWorkflow("boardId", "workflowId", "workflowName");
        Stage rootStage = createRootStage("stageId", "stageName");

        workflow.addRootStage(rootStage);

        assertEquals("stageName", workflow.getLane("stageId").get().getLaneName());
    }

    @Test
    public void deleteStageFromWorkflow() {
        Workflow workflow = createWorkflow("boardId", "workflowId", "workflowName");
        Stage rootStage = createRootStage("stageId", "stageName");

        workflow.addRootStage(rootStage);
        workflow.deleteLane("stageId");

        assertFalse(workflow.getLane("stageId").isPresent());
    }

    @Test
    public void moveCard_ShouldMoveCardBetweenStages() {
        Workflow workflow = createWorkflow("boardId", "workflowId", "workflowName");
        Stage source = createRootStage("lane1", "source");
        Stage destination = createRootStage("lane2", "destination");
        Card card = new Card("cardId", CardType.Standard, "boardId");

        workflow.addRootStage(source);
        workflow.addRootStage(destination);
        source.addCard(card);

        assertTrue(source.getCard(card.getId()).isPresent());

        workflow.moveCard(source, destination, card);

        assertFalse(source.getCard(card.getId()).isPresent());
        assertTrue(destination.getCard(card.getId()).isPresent());
    }

    private Workflow createWorkflow(String boardId, String workflowId, String name) {
        return new Workflow(boardId, workflowId, name);
    }

    private Stage createRootStage(String id, String name) {
        return new Stage(id, name);
    }
}
