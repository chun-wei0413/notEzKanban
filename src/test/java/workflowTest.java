import com.notezkanban.Lane;
import com.notezkanban.Stage;
import com.notezkanban.SwimLane;
import com.notezkanban.Workflow;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class workflowTest {

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

        Lane rootStage = new SwimLane(laneId, laneName);
        assertEquals(0, workflow.getLanes().size());
        assertThrows(RuntimeException.class, () -> {
            workflow.addLane(rootStage);
        });
    }
}
