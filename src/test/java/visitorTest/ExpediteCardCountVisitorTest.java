package visitorTest;

import com.notezkanban.card.CardType;
import com.notezkanban.lane.Lane;
import com.notezkanban.lane.LaneBuilder;
import com.notezkanban.lane.exception.LaneException;
import com.notezkanban.visitor.visitorImpl.ExpediteCardCountVisitor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ExpediteCardCountVisitorTest {

    @Nested
    class SingleLaneTests {
        @Test
        void emptyStage_ShouldReturnZero() {
            Lane stage = LaneBuilder.newInstance()
                .laneId("empty")
                .laneName("Empty Stage")
                .stage()
                .build();

            ExpediteCardCountVisitor visitor = new ExpediteCardCountVisitor();
            stage.accept(visitor);
            
            assertEquals(0, visitor.getResult());
        }

        @Test
        void stageWithNoExpediteCards_ShouldReturnZero() {
            Lane stage = LaneBuilder.newInstance()
                .laneId("stage1")
                .laneName("Stage 1")
                .stage()
                .build();

            stage.createCard("Normal Task 1", CardType.Standard, "board1");
            stage.createCard("Normal Task 2", CardType.Standard, "board1");

            ExpediteCardCountVisitor visitor = new ExpediteCardCountVisitor();
            stage.accept(visitor);
            
            assertEquals(0, visitor.getResult());
        }

        @Test
        void stageWithMixedCards_ShouldCountOnlyExpedite() {
            Lane stage = LaneBuilder.newInstance()
                .laneId("stage1")
                .laneName("Stage 1")
                .stage()
                .build();

            stage.createCard("Normal Task", CardType.Standard, "board1");
            stage.createCard("Urgent Task", CardType.Expedite, "board1");
            stage.createCard("Another Normal", CardType.Standard, "board1");
            stage.createCard("Another Urgent", CardType.Expedite, "board1");

            ExpediteCardCountVisitor visitor = new ExpediteCardCountVisitor();
            stage.accept(visitor);
            
            assertEquals(2, visitor.getResult());
        }
    }

    @Nested
    class NestedStructureTests {
        @Test
        void nestedStagesWithExpediteCards() {
            Lane rootStage = LaneBuilder.newInstance()
                .laneId("root")
                .laneName("Root")
                .stage()
                .build();

            rootStage.createStage("child", "Child Stage", 5);
            Lane childStage = rootStage.getLaneById("child");

            childStage.createCard("Root Expedite", CardType.Expedite, "board1");
            childStage.createCard("Child Expedite", CardType.Expedite, "board1");

            ExpediteCardCountVisitor visitor = new ExpediteCardCountVisitor();
            rootStage.accept(visitor);
            
            assertEquals(2, visitor.getResult());
        }

        @Test
        void complexStructureWithAllLaneTypes() {
            Lane rootStage = LaneBuilder.newInstance()
                .laneId("root")
                .laneName("Root")
                .stage()
                .build();

            rootStage.createSwimLane("swim1", "Swim Lane", 5);
            rootStage.createExpediteLane("exp1", "Expedite Lane");
            Lane swimLane = rootStage.getLaneById("swim1");
            Lane expediteLane = rootStage.getLaneById("exp1");

            swimLane.createStage("substage", "Sub Stage", 3);
            Lane subStage = swimLane.getLaneById("substage");

            subStage.createCard("Root Expedite", CardType.Expedite, "board1");
            subStage.createCard("Swim Expedite", CardType.Expedite, "board1");
            subStage.createCard("Sub Expedite", CardType.Expedite, "board1");
            expediteLane.createCard("Expedite Lane Card", CardType.Expedite, "board1");

            ExpediteCardCountVisitor visitor = new ExpediteCardCountVisitor();
            rootStage.accept(visitor);
            
            assertEquals(4, visitor.getResult());
        }
    }

    @Nested
    class ExpediteLaneTests {
        @Test
        void expediteLaneOnly() {
            Lane expediteLane = LaneBuilder.newInstance()
                .laneId("exp1")
                .laneName("Expedite Lane")
                .expediteLane()
                .build();

            expediteLane.createCard("Urgent Task", CardType.Expedite, "board1");

            ExpediteCardCountVisitor visitor = new ExpediteCardCountVisitor();
            expediteLane.accept(visitor);
            
            assertEquals(1, visitor.getResult());
        }

        @Test
        void stageWithExpediteLane() {
            Lane stage = LaneBuilder.newInstance()
                .laneId("stage1")
                .laneName("Stage 1")
                .stage()
                .build();

            stage.createExpediteLane("exp1", "Expedite Lane");
            Lane expediteLane = stage.getLaneById("exp1");
            
            expediteLane.createCard("Stage Expedite", CardType.Expedite, "board1");
            
            ExpediteCardCountVisitor visitor = new ExpediteCardCountVisitor();
            stage.accept(visitor);
            
            assertEquals(1, visitor.getResult());

            //test if card in expediteLane is over WIP limit
            assertThrows(LaneException.class, () -> expediteLane.createCard("Lane Expedite", CardType.Expedite, "board1"));
        }
    }
} 