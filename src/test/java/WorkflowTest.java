

import com.notezkanban.Workflow;
import com.notezkanban.card.Card;
import com.notezkanban.card.CardType;
import com.notezkanban.lane.Lane;
import com.notezkanban.lane.LaneBuilder;
import com.notezkanban.lane.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

public class WorkflowTest {
    private Workflow workflow;
    
    @BeforeEach
    void setUp() {
        workflow = new Workflow("board1", "workflow1", "Development Flow");
    }

    @Nested
    class WorkflowBasicOperations {
        @Test
        void createWorkflow() {
            assertEquals("workflow1", workflow.getWorkflowId());
            assertEquals("Development Flow", workflow.getWorkflowName());
            assertTrue(workflow.getLanes().isEmpty());
        }

        @Test
        void renameWorkflow() {
            workflow.rename("New Flow Name");
            assertEquals("New Flow Name", workflow.getWorkflowName());
        }

        @Test
        void renameWorkflowWithInvalidName() {
            assertThrows(IllegalArgumentException.class, () -> workflow.rename(""));
            assertThrows(IllegalArgumentException.class, () -> workflow.rename("   "));
        }
    }

    @Nested
    class StageOperations {
        @Test
        void addStageToWorkflow() {
            Stage stage = (Stage) LaneBuilder.newInstance()
                .laneId("stage1")
                .laneName("Development")
                .stage()
                .build();

            workflow.addRootStage(stage);

            assertTrue(workflow.getLane("stage1").isPresent());
            assertEquals("Development", workflow.getLane("stage1").get().getLaneName());
        }

        @Test
        void deleteStageFromWorkflow() {
            Stage stage = (Stage) LaneBuilder.newInstance()
                .laneId("stage1")
                .laneName("Development")
                .stage()
                .build();

            workflow.addRootStage(stage);
            workflow.deleteLane("stage1");

            assertFalse(workflow.getLane("stage1").isPresent());
        }

        @Test
        void addMultipleStages() {
            Stage dev = (Stage) LaneBuilder.newInstance()
                .laneId("dev")
                .laneName("Development")
                .stage()
                .build();

            Stage test = (Stage) LaneBuilder.newInstance()
                .laneId("test")
                .laneName("Testing")
                .stage()
                .build();

            workflow.addRootStage(dev);
            workflow.addRootStage(test);

            assertEquals(2, workflow.getLanes().size());
            assertTrue(workflow.getLane("dev").isPresent());
            assertTrue(workflow.getLane("test").isPresent());
        }
    }

    @Nested
    class CardOperations {
        private Stage sourceStage;
        private Stage targetStage;
        private Card card;

        @BeforeEach
        void setUp() {
            sourceStage = (Stage) LaneBuilder.newInstance()
                .laneId("source")
                .laneName("Source")
                .stage()
                .build();

            targetStage = (Stage) LaneBuilder.newInstance()
                .laneId("target")
                .laneName("Target")
                .stage()
                .build();

            workflow.addRootStage(sourceStage);
            workflow.addRootStage(targetStage);
            
            // 創建並添加卡片
            sourceStage.createCard("Test Task", CardType.Standard, "board1");
            card = sourceStage.getCards().get(0);
        }

        @Test
        void moveCardBetweenStages() {
            workflow.moveCard(sourceStage, targetStage, card);

            assertTrue(sourceStage.getCards().isEmpty());
            assertFalse(targetStage.getCards().isEmpty());
            assertEquals(1, targetStage.getCards().size());
            assertEquals("Test Task", targetStage.getCards().get(0).getDescription());
        }

        @Test
        void moveCardWithinSameStage() {
            workflow.moveCard(sourceStage, sourceStage, card);

            assertEquals(1, sourceStage.getCards().size());
            assertEquals("Test Task", sourceStage.getCards().get(0).getDescription());
        }
    }

    @Nested
    class LaneOperations {
        private Stage rootStage;

        @BeforeEach
        void setUp() {
            rootStage = (Stage) LaneBuilder.newInstance()
                .laneId("root")
                .laneName("Root Stage")
                .stage()
                .build();
            workflow.addRootStage(rootStage);
        }

        @Test
        void createNestedStructure() {
            rootStage.createSwimLane("swim1", "Swim Lane 1", 5);
            Lane swimLane = rootStage.getLaneById("swim1");
            swimLane.createStage("nested", "Nested Stage", 3);

            assertNotNull(rootStage.getLaneById("swim1"));
            assertNotNull(swimLane.getLaneById("nested"));
        }

        @Test
        void createExpediteLane() {
            rootStage.createExpediteLane("exp1", "Expedite Lane");
            
            Lane expediteLane = rootStage.getLaneById("exp1");
            assertNotNull(expediteLane);
            assertEquals("Expedite Lane", expediteLane.getLaneName());
            assertEquals(1, expediteLane.getWipLimit());
        }
    }
}
