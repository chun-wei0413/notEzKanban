package reportTest;

import com.notezkanban.Workflow;
import com.notezkanban.card.CardType;
import com.notezkanban.report.DistributionReport;
import com.notezkanban.report.DistributionReportGenerator;
import com.notezkanban.lane.Lane;
import com.notezkanban.lane.LaneBuilder;
import com.notezkanban.lane.Stage;
import com.notezkanban.report.ReportGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DistributionReportGeneratorTest {
    private Workflow workflow;
    private ReportGenerator<DistributionReport> generator;

    @BeforeEach
    void setUp() {
        workflow = new Workflow("board1", "workflow1", "Development Flow");
        generator = new DistributionReportGenerator(workflow);
    }

    @Nested
    class BasicChartGenerationTests {
        @Test
        void generateChartForEmptyWorkflow() {
            DistributionReport chart = generator.generateReport();
            String expected = """
                Distribution Chart - Development Flow
                =====================================


                Total cards: 0""";
            assertEquals(expected, chart.toString());
        }

        @Test
        void generateChartForSingleStage() {
            Stage stage = (Stage) LaneBuilder.newInstance()
                .laneId("stage1")
                .laneName("Development")
                .stage()
                .build();
            
            stage.createCard("Task 1", CardType.Standard, "board1");
            workflow.addRootStage(stage);

            DistributionReport chart = generator.generateReport();
            String expected = """
                Distribution Chart - Development Flow
                =====================================

                Development: 1 cards

                Total cards: 1""";
            assertEquals(expected, chart.toString());
        }
    }

    @Nested
    class MultiStageTests {
        @Test
        void generateChartForMultipleStages() {
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

            DistributionReport chart = generator.generateReport();
            String expected = """
                Distribution Chart - Development Flow
                =====================================

                Development: 1 cards
                Testing: 1 cards

                Total cards: 2""";
            assertEquals(expected, chart.toString());
        }
    }

    @Nested
    class NestedStructureTests {
        @Test
        void generateChartForNestedLanes() {
            Stage rootStage = (Stage) LaneBuilder.newInstance()
                .laneId("root")
                .laneName("Development")
                .stage()
                .build();
            
            rootStage.createSwimLane("swim1", "Team A", 5);
            Lane teamA = rootStage.getLaneById("swim1");
            teamA.createCard("Team Task", CardType.Standard, "board1");
            
            workflow.addRootStage(rootStage);

            DistributionReport chart = generator.generateReport();
            String expected = """
                Distribution Chart - Development Flow
                =====================================

                Development: 1 cards

                Total cards: 1""";
            assertEquals(expected, chart.toString());
        }

        @Test
        void generateChartForComplexStructure() {
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

            DistributionReport chart = generator.generateReport();
            String expected = """
                Distribution Chart - Development Flow
                =====================================

                Development: 2 cards

                Total cards: 2""";
            assertEquals(expected, chart.toString());
        }
    }
} 