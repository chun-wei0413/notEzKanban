package visitorTest;

import com.notezkanban.card.CardType;
import com.notezkanban.lane.Lane;
import com.notezkanban.lane.LaneBuilder;
import com.notezkanban.visitor.visitorImpl.TotalCardVisitor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TotalCardVisitorTest {

    @Nested
    class SingleLaneTests {
        @Test
        void emptyStage() {
            Lane stage = LaneBuilder.newInstance()
                .laneId("stage1")
                .laneName("Empty Stage")
                .stage()
                .build();

            TotalCardVisitor visitor = new TotalCardVisitor();
            stage.accept(visitor);
            assertEquals(0, visitor.getResult());
        }

        @Test
        void stageWithCards() {
            Lane stage = LaneBuilder.newInstance()
                .laneId("stage1")
                .laneName("Development")
                .stage()
                .build();

            stage.createCard("Task 1", CardType.Standard, "board1");
            stage.createCard("Task 2", CardType.Standard, "board1");

            TotalCardVisitor visitor = new TotalCardVisitor();
            stage.accept(visitor);
            assertEquals(2, visitor.getResult());
        }

        @Test
        void swimLaneWithCards() {
            Lane swimLane = LaneBuilder.newInstance()
                .laneId("swim1")
                .laneName("Team A")
                .swimLane()
                .build();

            swimLane.createCard("Task 1", CardType.Standard, "board1");
            swimLane.createCard("Task 2", CardType.Expedite, "board1");

            TotalCardVisitor visitor = new TotalCardVisitor();
            swimLane.accept(visitor);
            assertEquals(2, visitor.getResult());
        }

        @Test
        void expediteLaneWithCards() {
            Lane expediteLane = LaneBuilder.newInstance()
                .laneId("exp1")
                .laneName("Urgent Lane")
                .expediteLane()
                .build();

            expediteLane.createCard("Critical Fix", CardType.Expedite, "board1");

            TotalCardVisitor visitor = new TotalCardVisitor();
            expediteLane.accept(visitor);
            assertEquals(1, visitor.getResult());
        }
    }

    @Nested
    class NestedStructureTests {
        @Test
        void stageWithNestedSwimLanes() {
            Lane rootStage = LaneBuilder.newInstance()
                .laneId("root")
                .laneName("Development")
                .stage()
                .build();

            rootStage.createSwimLane("team1", "Team A", 5);
            rootStage.createSwimLane("team2", "Team B", 5);
            
            Lane teamA = rootStage.getLaneById("team1");
            Lane teamB = rootStage.getLaneById("team2");
            
            teamA.createCard("Task A", CardType.Standard, "board1");
            teamB.createCard("Task B", CardType.Standard, "board1");

            TotalCardVisitor visitor = new TotalCardVisitor();
            rootStage.accept(visitor);
            assertEquals(2, visitor.getResult());
        }

        @Test
        void complexNestedStructure() {
            Lane rootStage = LaneBuilder.newInstance()
                .laneId("root")
                .laneName("Project")
                .stage()
                .build();

            rootStage.createSwimLane("dev", "Development", 5);
            Lane devLane = rootStage.getLaneById("dev");
            
            devLane.createStage("frontend", "Frontend", 3);
            devLane.createStage("backend", "Backend", 3);
            devLane.createExpediteLane("hotfix", "Hotfix");

            Lane frontendLane = devLane.getLaneById("frontend");
            Lane backendLane = devLane.getLaneById("backend");
            Lane hotfixLane = devLane.getLaneById("hotfix");

            frontendLane.createCard("UI Fix", CardType.Standard, "board1");
            backendLane.createCard("API Task", CardType.Standard, "board1");
            hotfixLane.createCard("Critical Bug", CardType.Expedite, "board1");

            TotalCardVisitor visitor = new TotalCardVisitor();
            rootStage.accept(visitor);
            assertEquals(3, visitor.getResult());
        }

        @Test
        void deepNestedStructure() {
            Lane rootStage = LaneBuilder.newInstance()
                .laneId("root")
                .laneName("Root")
                .stage()
                .build();

            rootStage.createSwimLane("level1", "Level 1", 5);
            Lane level1 = rootStage.getLaneById("level1");
            level1.createStage("level2", "Level 2", 3);
            Lane level2 = level1.getLaneById("level2");
            level2.createSwimLane("level3", "Level 3", 3);
            Lane level3 = level2.getLaneById("level3");

            level3.createCard("Deep Task 1", CardType.Standard, "board1");
            level3.createCard("Deep Task 2", CardType.Standard, "board1");

            TotalCardVisitor visitor = new TotalCardVisitor();
            rootStage.accept(visitor);
            assertEquals(2, visitor.getResult());
        }
    }

    @Nested
    class MixedCardTypesTests {
        @Test
        void countMixedCardTypes() {
            Lane stage = LaneBuilder.newInstance()
                .laneId("stage1")
                .laneName("Mixed Stage")
                .stage()
                .build();

            stage.createCard("Standard Task", CardType.Standard, "board1");
            stage.createCard("Expedite Task", CardType.Expedite, "board1");
            stage.createCard("Another Standard", CardType.Standard, "board1");

            TotalCardVisitor visitor = new TotalCardVisitor();
            stage.accept(visitor);
            assertEquals(3, visitor.getResult());
        }
    }
} 