package laneTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import com.notezkanban.card.Card;
import com.notezkanban.card.CardType;
import com.notezkanban.lane.Lane;
import com.notezkanban.lane.LaneBuilder;
import com.notezkanban.lane.exception.LaneException;

public class LaneTest {
    
    @Nested
    class StageTests {
        private Lane stage;

        @BeforeEach
        void setUp() {
            stage = LaneBuilder.newInstance()
                    .laneId("stage1")
                    .laneName("Development")
                    .wipLimit(3)
                    .stage()
                    .build();
        }

        @Test
        void testBasicProperties() {
            assertEquals("stage1", stage.getLaneId());
            assertEquals("Development", stage.getLaneName());
            assertEquals(3, stage.getWipLimit());
            assertTrue(stage.getChildren().isEmpty());
            assertTrue(stage.getCards().isEmpty());
        }

        @Test
        void testSetWipLimit() {
            stage.setWipLimit(5);
            assertEquals(5, stage.getWipLimit());
        }

        @Test
        void testCreateCard() {
            stage.createCard("Test Card", CardType.Standard, "board1");
            assertEquals(1, stage.getCards().size());
            assertEquals("Test Card", stage.getCards().get(0).getDescription());
        }

        @Test
        void testCreateCardInNonLeafLane() {
            stage.createSwimLane("swim1", "Team A", 2);
            assertThrows(LaneException.class, () -> 
                stage.createCard("Test Card", CardType.Standard, "board1")
            );
        }

        @Test
        void testGetCardById() {
            stage.createCard("Test Card", CardType.Standard, "board1");
            UUID cardId = stage.getCards().get(0).getId();
            assertTrue(stage.getCard(cardId).isPresent());
            assertFalse(stage.getCard(UUID.randomUUID()).isPresent());
        }

        @Test
        void testDeleteCard() {
            stage.createCard("Test Card", CardType.Standard, "board1");
            Card card = stage.getCards().get(0);
            stage.deleteCard(card);
            assertTrue(stage.getCards().isEmpty());
        }

        @Test
        void testCreateStageAndSwimLane() {
            stage.createStage("stage2", "Testing", 2);
            assertThrows(LaneException.class, () -> 
                stage.createSwimLane("swim1", "Team A", 2)
            );
        }

        @Test
        void testCreateExpediteLane() {
            stage.createExpediteLane("exp1", "Expedite");
            assertEquals(1, stage.getChildren().size());
            assertEquals("exp1", stage.getChildren().get(0).getLaneId());
        }
    }

    @Nested
    class SwimLaneTests {
        private Lane swimLane;

        @BeforeEach
        void setUp() {
            swimLane = LaneBuilder.newInstance()
                    .laneId("swim1")
                    .laneName("Team A")
                    .wipLimit(3)
                    .swimLane()
                    .build();
        }

        @Test
        void testBasicProperties() {
            assertEquals("swim1", swimLane.getLaneId());
            assertEquals("Team A", swimLane.getLaneName());
            assertEquals(3, swimLane.getWipLimit());
        }

        @Test
        void testCreateStageInSwimLane() {
            swimLane.createSwimLane("swim2", "Team B", 2);
            assertThrows(LaneException.class, () -> 
                swimLane.createStage("stage1", "Development", 2)
            );
        }
    }

    @Nested
    class ExpediteLaneTests {
        private Lane expediteLane;

        @BeforeEach
        void setUp() {
            expediteLane = LaneBuilder.newInstance()
                    .laneId("exp1")
                    .laneName("Expedite")
                    .expediteLane()
                    .build();
        }

        @Test
        void testBasicProperties() {
            assertEquals("exp1", expediteLane.getLaneId());
            assertEquals("Expedite", expediteLane.getLaneName());
            assertEquals(1, expediteLane.getWipLimit());
            assertTrue(expediteLane.getChildren().isEmpty());
        }

        @Test
        void testWipLimitIsAlwaysOne() {
            assertThrows(UnsupportedOperationException.class, () -> 
                expediteLane.setWipLimit(2)
            );
            assertEquals(1, expediteLane.getWipLimit());
        }

        @Test
        void testCannotCreateChildren() {
            assertThrows(LaneException.class, () -> 
                expediteLane.createStage("stage1", "Development", 2)
            );
            assertThrows(LaneException.class, () -> 
                expediteLane.createSwimLane("swim1", "Team A", 2)
            );
        }
    }

    @Nested
    class LaneIteratorTests {
        private Lane rootLane;

        @BeforeEach
        void setUp() {
            rootLane = LaneBuilder.newInstance()
                    .laneId("root")
                    .laneName("Root")
                    .stage()
                    .build();
            
            rootLane.createStage("stage1", "Development", 3);
            rootLane.createStage("stage2", "Testing", 2);
        }

        @Test
        void testIterator() {
            Iterator<Lane> iterator = rootLane.iterator();
            assertTrue(iterator.hasNext());
            assertEquals("stage1", iterator.next().getLaneId());
            assertEquals("stage2", iterator.next().getLaneId());
            assertFalse(iterator.hasNext());
        }

        @Test
        void testDfsIterator() {
            Lane stage1 = rootLane.getLaneById("stage1");
            stage1.createStage("substage1", "SubStage", 2);

            Iterator<Lane> dfsIterator = rootLane.dfsIterator();
            assertTrue(dfsIterator.hasNext());
            assertEquals("root", dfsIterator.next().getLaneId());
            assertEquals("stage1", dfsIterator.next().getLaneId());
            assertEquals("substage1", dfsIterator.next().getLaneId());
            assertEquals("stage2", dfsIterator.next().getLaneId());
            assertFalse(dfsIterator.hasNext());
        }

        @Test
        void testNullIterator() {
            Lane stage1 = rootLane.getLaneById("stage1");
            stage1.createExpediteLane("exp1", "Expedite");

            Iterator<Lane> nullIterator = stage1.iterator();
            assertTrue(nullIterator.hasNext());
            assertEquals("exp1", nullIterator.next().getLaneId());
            assertFalse(nullIterator.hasNext());
            assertThrows(NoSuchElementException.class, () ->
                nullIterator.next()
            );
        }
    }

    @Test
    void testGetLaneById() {
        Lane rootLane = LaneBuilder.newInstance()
                .laneId("root")
                .laneName("Root")
                .stage()
                .build();
        
        rootLane.createStage("stage1", "Development", 3);
        
        Lane foundLane = rootLane.getLaneById("stage1");
        assertNotNull(foundLane);
        assertEquals("Development", foundLane.getLaneName());
        
        assertNull(rootLane.getLaneById("nonexistent"));
    }
}
