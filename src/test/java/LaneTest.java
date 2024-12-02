import com.notezkanban.lane.Lane;
import com.notezkanban.lane.Stage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LaneTest {

    @Test
    public void createStage() {
        String stageId1 = "2";
        String stageName1 = "childStage";
        String stageId2 = "3";
        String stageName2 = "childStage2";
        Lane stage = createRootStage();

        stage.createStage(stageId1, stageName1);
        Lane stage1 = stage.getLaneById(stageId1);
        stage1.createStage(stageId2, stageName2);

        assertEquals(stageId1, stage.getLaneById(stageId1).getLaneId());
        assertEquals(stageId2, stage1.getLaneById(stageId2).getLaneId());
    }

    private Lane createRootStage() {
        String stageId = "1";
        String stageName = "rootStage";
        return new Stage(stageId, stageName);
    }
}
