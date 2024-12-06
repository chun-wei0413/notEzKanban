import com.notezkanban.card.Card;
import com.notezkanban.card.CardType;
import com.notezkanban.lane.Stage;
import com.notezkanban.Workflow;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WorkflowTest {

    @Test
    public void createWorkflow() {
        Workflow workflow = createWorkflow("workflowId", "workflowName");

        assertEquals("workflowId", workflow.getWorkflowId());
        assertEquals("workflowName", workflow.getWorkflowName());
    }

    @Test
    public void renameWorkflow() {
        Workflow workflow = createWorkflow("workflowId", "workflowName");

        workflow.rename("newWorkflowName");

        assertEquals("newWorkflowName", workflow.getWorkflowName());
    }

    @Test
    public void renameWorkflowWithEmptyName() {
        Workflow workflow = createWorkflow("workflowId", "workflowName");

        assertThrows(IllegalArgumentException.class, () -> workflow.rename(""));
    }

    @Test
    public void addStageToWorkflow() {
        Workflow workflow = createWorkflow("workflowId", "workflowName");
        Stage rootStage = createRootStage("stageId", "stageName");

        workflow.addRootStage(rootStage);

        assertEquals("stageName", workflow.getLane("stageId").get().getLaneName());
    }

    @Test
    public void deleteStageFromWorkflow() {
        Workflow workflow = createWorkflow("workflowId", "workflowName");
        Stage rootStage = createRootStage("stageId", "stageName");

        workflow.addRootStage(rootStage);
        workflow.deleteLane("stageId");

        assertFalse(workflow.getLane("stageId").isPresent());
    }

    @Test
    public void moveCard() {
        Workflow workflow = createWorkflow("workflowId", "workflowName");
        Stage source = createRootStage("lane1","source");
        Stage destination = createRootStage("lane2","destination");
        Card card = new Card("card", CardType.Standard);

        workflow.addRootStage(source);
        workflow.addRootStage(destination);
        source.addCard(card);

        assertTrue(source.getCard(card.getId()).isPresent());
        workflow.moveCard(source, destination, card);
        assertFalse(source.getCard(card.getId()).isPresent());
        assertTrue(destination.getCard(card.getId()).isPresent());
    }

    private Workflow createWorkflow(String id, String name) {
        return new Workflow(id, name);
    }

    private Stage createRootStage(String id, String name) {
        return new Stage(id, name);
    }
}
