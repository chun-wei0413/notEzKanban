package reportTest;

import com.notezkanban.Workflow;
import com.notezkanban.card.Card;
import com.notezkanban.card.CardType;
import com.notezkanban.lane.Lane;
import com.notezkanban.lane.LaneBuilder;
import com.notezkanban.lane.Stage;
import com.notezkanban.report.CycleTimeReport;
import com.notezkanban.report.CycleTimeReportGenerator;

import com.notezkanban.report.ReportGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class CycleTimeReportGeneratorTest {
    private Workflow workflow;
    private ReportGenerator<CycleTimeReport> generator;

    @BeforeEach
    void setUp() {
        workflow = new Workflow("board1", "workflow1", "Development Flow");
        generator = new CycleTimeReportGenerator(workflow);
    }

    @Nested
    class BasicReportTests {
        @Test
        void generateReportForEmptyWorkflow() {
            CycleTimeReport report = generator.generateReport();
            String expected = """
                Cycle Time Report - Development Flow
                ========================================
                """;
            assertEquals(expected, report.toString());
        }

        @Test
        void generateReportForSingleCard() throws InterruptedException {
            Stage stage = (Stage) LaneBuilder.newInstance()
                .laneId("stage1")
                .laneName("Development")
                .stage()
                .build();
                
            workflow.addRootStage(stage);
            
            Card card = new Card("Task 1", CardType.Standard, "board1");
            stage.createCard("Task 1", CardType.Standard, "board1");
            stage.deleteCard(card);

            CycleTimeReport report = generator.generateReport();

            String reportStr = report.toString();
            
            assertTrue(reportStr.contains("Cycle Time Report - Development Flow"));
            assertTrue(reportStr.contains("Lane: Development"));
            assertTrue(reportStr.contains("Card ID:"));
            assertTrue(reportStr.contains("Cycle Time: 0.0 days"));
        }
    }

    @Nested
    class MultiStageTests {
        @Test
        void generateReportForMultipleStages() {
            Stage devStage = (Stage) LaneBuilder.newInstance()
                .laneId("dev")
                .laneName("Development")
                .stage()
                .build();
            
            Stage testStage = (Stage) LaneBuilder.newInstance()
                .laneId("test")
                .laneName("Testing")
                .stage()
                .build();
                        
            devStage.createCard("Dev Task", CardType.Standard, "board1");
            testStage.createCard("Test Task", CardType.Standard, "board1");
            
            workflow.addRootStage(devStage);
            workflow.addRootStage(testStage);

            CycleTimeReport report = generator.generateReport();
            String reportStr = report.toString();
            
            assertTrue(reportStr.contains("Lane: Development"));
            assertTrue(reportStr.contains("Lane: Testing"));
            assertTrue(reportStr.contains("Cycle Time: 0.0 days"));
            assertTrue(reportStr.contains("Cycle Time: 0.0 days"));
        }
    }

    @Nested
    class NestedStructureTests {
        @Test
        void generateReportForNestedLanes() {
            Stage rootStage = (Stage) LaneBuilder.newInstance()
                .laneId("root")
                .laneName("Development")
                .stage()
                .build();
            
            rootStage.createSwimLane("swim1", "Team A", 5);
            Lane teamA = rootStage.getLaneById("swim1");

            Card card = new Card("Team Task", CardType.Standard, "board1");
            teamA.createCard("Team Task", CardType.Standard, "board1");
            teamA.deleteCard(card);

            workflow.addRootStage(rootStage);

            CycleTimeReport report = generator.generateReport();
            String reportStr = report.toString();
            
            assertTrue(reportStr.contains("Lane: Development"));
            assertTrue(reportStr.contains("Cycle Time: 0.0 days"));
        }

        @Test
        void generateReportForComplexStructure() {
            Stage rootStage = (Stage) LaneBuilder.newInstance()
                .laneId("root")
                .laneName("Development")
                .stage()
                .build();
            
            rootStage.createSwimLane("swim1", "Team A", 5);
            Lane teamA = rootStage.getLaneById("swim1");
            
            teamA.createStage("dev", "Dev Tasks", 3);
            teamA.createExpediteLane("urgent", "Urgent");
            
            Lane devTasks = teamA.getLaneById("dev");
            Lane urgentLane = teamA.getLaneById("urgent");
            
            devTasks.createCard("Regular Task", CardType.Standard, "board1");
            urgentLane.createCard("Urgent Fix", CardType.Expedite, "board1");
            
            workflow.addRootStage(rootStage);

            CycleTimeReport report = generator.generateReport();
            String reportStr = report.toString();
            
            assertTrue(reportStr.contains("Lane: Development"));
            assertTrue(reportStr.contains("Cycle Time: 0.0 days"));
            assertTrue(reportStr.contains("Cycle Time: 0.0 days"));
        }
    }
} 