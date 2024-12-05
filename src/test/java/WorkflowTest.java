import com.notezkanban.card.Card;
import com.notezkanban.card.CardFactory;
import com.notezkanban.card.CardType;
import com.notezkanban.lane.Lane;
import com.notezkanban.lane.Stage;
import com.notezkanban.lane.SwimLane;
import com.notezkanban.Workflow;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WorkflowTest {

    @Test
    public void createWorkflow() {
        String workflowId = "workflowId";
        String workflowName = "workflowName";
        Workflow workflow = new Workflow(workflowId, workflowName);

        assertEquals(workflowId, workflow.getWorkflowId());
        assertEquals(workflowName, workflow.getWorkflowName());
    }

    @Test
    public void addStageToWorkflow() {
        String workflowId = "workflowId";
        String workflowName = "workflowName";
        Workflow workflow = new Workflow(workflowId, workflowName);

        String laneId = "stageId";
        String laneName = "stageName";

        Lane rootStage = new Stage(laneId, laneName);
        workflow.addLane(rootStage);

        assertEquals(rootStage, workflow.getLane(laneId));
    }

    @Test
    public void rootStageTypeMustBeStage() {
        String workflowId = "wordflowId";
        String workflowName = "workflowName";
        Workflow workflow = new Workflow(workflowId, workflowName);

        String laneId = "stageId";
        String laneName = "stageName";

        //TODO: only use Lane to create stage or swimlane.
        Lane rootStage = new SwimLane(laneId, laneName);
        assertEquals(0, workflow.getLanes().size());
        assertThrows(RuntimeException.class, () -> {
            workflow.addLane(rootStage);
        });
    }

    @Test
    public void moveCard() {
        String workflowId = "wordflowId";
        String workflowName = "workflowName";
        Workflow workflow = new Workflow(workflowId, workflowName);
        Lane source = createRootStage("lane1","source");
        Lane destination = createRootStage("lane2","destination");
        Card card = CardFactory.createCard("card", CardType.Standard);

        workflow.addLane(source);
        workflow.addLane(destination);
        source.addCard(card);

        assertTrue(source.getCard(card.getId()).isPresent());
        workflow.moveCard(source, destination, card);
        assertFalse(source.getCard(card.getId()).isPresent());
        assertTrue(destination.getCard(card.getId()).isPresent());
    }

    private Lane createRootStage(String id, String name) {
        String stageId = id;
        String stageName = name;
        return new Stage(stageId, stageName);
    }
}
