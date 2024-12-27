package visitorTest;

import com.notezkanban.card.CardType;
import com.notezkanban.lane.Lane;
import com.notezkanban.lane.LaneBuilder;
import com.notezkanban.visitor.visitorImpl.PrettyPrintVisitor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PrettyPrintVisitorTest {

    @Nested
    class SingleLaneTests {
        @Test
        void emptyStage() {
            Lane stage = LaneBuilder.newInstance()
                .laneId("stage1")
                .laneName("Empty Stage")
                .stage()
                .build();

            PrettyPrintVisitor visitor = new PrettyPrintVisitor();
            stage.accept(visitor);
            String result = visitor.getResult();

            assertTrue(result.contains("Stage: Empty Stage"));
            assertFalse(result.contains("Card:"));
        }

        @Test
        void leafStageWithCards() {
            Lane stage = LaneBuilder.newInstance()
                .laneId("stage1")
                .laneName("Development")
                .stage()
                .build();

            stage.createCard("Task 1", CardType.Standard, "board1");
            stage.createCard("Urgent Task", CardType.Expedite, "board1");

            PrettyPrintVisitor visitor = new PrettyPrintVisitor();
            stage.accept(visitor);
            String result = visitor.getResult();

            assertTrue(result.contains("Stage: Development"));
            assertTrue(result.contains("Card: Task 1 (Standard)"));
            assertTrue(result.contains("Card: Urgent Task (Expedite)"));
        }

        @Test
        void leafSwimLaneWithCards() {
            Lane swimLane = LaneBuilder.newInstance()
                .laneId("swim1")
                .laneName("Team A")
                .swimLane()
                .build();

            swimLane.createCard("Team Task", CardType.Standard, "board1");

            PrettyPrintVisitor visitor = new PrettyPrintVisitor();
            swimLane.accept(visitor);
            String result = visitor.getResult();

            assertTrue(result.contains("SwimLane: Team A"));
            assertTrue(result.contains("Card: Team Task (Standard)"));
        }

        @Test
        void expediteLaneWithCards() {
            Lane expediteLane = LaneBuilder.newInstance()
                .laneId("exp1")
                .laneName("Urgent Lane")
                .expediteLane()
                .build();

            expediteLane.createCard("Critical Fix", CardType.Expedite, "board1");

            PrettyPrintVisitor visitor = new PrettyPrintVisitor();
            expediteLane.accept(visitor);
            String result = visitor.getResult();

            assertTrue(result.contains("ExpediteLane: Urgent Lane"));
            assertTrue(result.contains("Card: Critical Fix (Expedite)"));
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
            teamA.createCard("Team A Task", CardType.Standard, "board1");

            PrettyPrintVisitor visitor = new PrettyPrintVisitor();
            rootStage.accept(visitor);
            String result = visitor.getResult();

            assertTrue(result.contains("Stage: Development"));
            assertTrue(result.contains("  SwimLane: Team A"));
            assertTrue(result.contains("  SwimLane: Team B"));
            assertTrue(result.contains("    Card: Team A Task (Standard)"));
        }

        @Test
        void complexNestedStructure() {
            // 創建根階段
            Lane rootStage = LaneBuilder.newInstance()
                .laneId("root")
                .laneName("Project")
                .stage()
                .build();

            // 添加子泳道
            rootStage.createSwimLane("dev", "Development", 5);
            Lane devLane = rootStage.getLaneById("dev");
            
            // 在開發泳道中添加子階段和加急泳道（葉子節點）
            devLane.createStage("frontend", "Frontend", 3);
            devLane.createStage("backend", "Backend", 3);
            devLane.createExpediteLane("hotfix", "Hotfix");

            // 只在葉子節點添加卡片
            Lane frontendLane = devLane.getLaneById("frontend");
            Lane backendLane = devLane.getLaneById("backend");
            Lane hotfixLane = devLane.getLaneById("hotfix");

            frontendLane.createCard("UI Fix", CardType.Standard, "board1");
            backendLane.createCard("API Task", CardType.Standard, "board1");
            hotfixLane.createCard("Critical Bug", CardType.Expedite, "board1");

            PrettyPrintVisitor visitor = new PrettyPrintVisitor();
            rootStage.accept(visitor);
            String result = visitor.getResult();

            // 驗證結構
            assertTrue(result.contains("Stage: Project"));
            assertTrue(result.contains("  SwimLane: Development"));
            assertTrue(result.contains("    Stage: Frontend"));
            assertTrue(result.contains("      Card: UI Fix (Standard)"));
            assertTrue(result.contains("    Stage: Backend"));
            assertTrue(result.contains("      Card: API Task (Standard)"));
            assertTrue(result.contains("    ExpediteLane: Hotfix"));
            assertTrue(result.contains("      Card: Critical Bug (Expedite)"));
        }
    }

    @Nested
    class IndentationTests {
        @Test
        void verifyIndentationLevels() {
            // 創建測試結構
            Lane rootStage = LaneBuilder.newInstance()
                .laneId("root")
                .laneName("Root")
                .stage()
                .build();

            rootStage.createSwimLane("swim1", "Level 1", 5);
            Lane level1 = rootStage.getLaneById("swim1");
            level1.createStage("stage1", "Level 2", 3);
            Lane leafStage = level1.getLaneById("stage1");
            leafStage.createCard("Leaf Task", CardType.Standard, "board1");

            PrettyPrintVisitor visitor = new PrettyPrintVisitor();
            rootStage.accept(visitor);
            String result = visitor.getResult();

            // 由於使用 DFS，輸出順序應該是：
            // Root
            // Level 1
            // Level 2
            // Leaf Task
            String[] lines = result.split("\n");
            assertEquals("Stage: Root", lines[0].trim());
            assertEquals("SwimLane: Level 1", lines[1].trim());
            assertEquals("Stage: Level 2", lines[2].trim());
            assertTrue(lines[3].trim().startsWith("Card: Leaf Task"));

            // 驗證縮排
            assertTrue(lines[0].startsWith("Stage"));  // 無縮排
            assertTrue(lines[1].startsWith("  SwimLane"));  // 1層縮排
            assertTrue(lines[2].startsWith("    Stage"));   // 2層縮排
            assertTrue(lines[3].startsWith("      Card"));  // 3層縮排
        }

        @Test
        void verifyDFSOrderWithMultipleBranches() {
            Lane rootStage = LaneBuilder.newInstance()
                .laneId("root")
                .laneName("Root")
                .stage()
                .build();

            // 創建兩個平行的分支
            rootStage.createSwimLane("swim1", "Branch 1", 5);
            rootStage.createSwimLane("swim2", "Branch 2", 5);

            // 在每個分支添加葉子節點
            Lane branch1 = rootStage.getLaneById("swim1");
            Lane branch2 = rootStage.getLaneById("swim2");
            
            branch1.createStage("stage1", "Leaf 1", 3);
            branch2.createStage("stage2", "Leaf 2", 3);

            // 在葉子節點添加卡片
            branch1.getLaneById("stage1").createCard("Task 1", CardType.Standard, "board1");
            branch2.getLaneById("stage2").createCard("Task 2", CardType.Standard, "board1");

            PrettyPrintVisitor visitor = new PrettyPrintVisitor();
            rootStage.accept(visitor);
            String result = visitor.getResult();

            String[] lines = result.split("\n");
            
            // 驗證 DFS 順序和縮排
            assertEquals("Stage: Root", lines[0].trim());
            assertEquals("SwimLane: Branch 1", lines[1].trim());
            assertEquals("Stage: Leaf 1", lines[2].trim());
            assertTrue(lines[3].trim().startsWith("Card: Task 1"));
            assertEquals("SwimLane: Branch 2", lines[4].trim());
            assertEquals("Stage: Leaf 2", lines[5].trim());
            assertTrue(lines[6].trim().startsWith("Card: Task 2"));

            // 驗證縮排級別
            assertTrue(lines[0].startsWith("Stage"));          // 0 縮排
            assertTrue(lines[1].startsWith("  SwimLane"));    // 1 縮排
            assertTrue(lines[2].startsWith("    Stage"));     // 2 縮排
            assertTrue(lines[3].startsWith("      Card"));    // 3 縮排
            assertTrue(lines[4].startsWith("  SwimLane"));    // 1 縮排
            assertTrue(lines[5].startsWith("    Stage"));     // 2 縮排
            assertTrue(lines[6].startsWith("      Card"));    // 3 縮排
        }
    }
} 