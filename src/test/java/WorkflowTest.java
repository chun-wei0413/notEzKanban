import com.notezkanban.lane.Lane;
import com.notezkanban.lane.Stage;
import com.notezkanban.lane.SwimLane;
import com.notezkanban.Workflow;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
}
