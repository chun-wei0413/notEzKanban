package laneTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import com.notezkanban.card.Card;
import com.notezkanban.card.CardType;
import com.notezkanban.lane.Lane;
import com.notezkanban.lane.LaneBuilder;
import com.notezkanban.lane.exception.LaneException;

public class StageTest {
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
    @DisplayName("Test getLaneId method")
    void testGetLaneId() {
        assertEquals("stage1", stage.getLaneId());
    }

    @Test
    @DisplayName("Test getLaneName method")
    void testGetLaneName() {
        assertEquals("Development", stage.getLaneName());
    }

    @Test
    @DisplayName("Test getChildren method")
    void testGetChildren() {
        assertTrue(stage.getChildren().isEmpty());
        
        stage.createStage("substage", "Sub Stage", 2);
        assertEquals(1, stage.getChildren().size());
        assertEquals("substage", stage.getChildren().get(0).getLaneId());
    }

    @Test
    @DisplayName("Test getWipLimit and setWipLimit methods")
    void testWipLimit() {
        assertEquals(3, stage.getWipLimit());
        
        stage.setWipLimit(5);
        assertEquals(5, stage.getWipLimit());
    }

    @Test
    @DisplayName("Test iterator method")
    void testIterator() {
        assertFalse(stage.iterator().hasNext());
        
        stage.createStage("substage", "Sub Stage", 2);
        assertTrue(stage.iterator().hasNext());
        assertEquals("substage", stage.iterator().next().getLaneId());
    }

    @Test
    @DisplayName("Test getCards method")
    void testGetCards() {
        assertTrue(stage.getCards().isEmpty());
        
        stage.createCard("Test Card", CardType.Standard, "board1");
        assertEquals(1, stage.getCards().size());
        assertEquals("Test Card", stage.getCards().get(0).getDescription());
    }

    @Test
    @DisplayName("Test createStage method")
    void testCreateStage() {
        stage.createStage("substage", "Sub Stage", 2);
        assertEquals(1, stage.getChildren().size());
        
        // Cannot create stage if swimlane exists
        stage = LaneBuilder.newInstance()
                .laneId("stage2")
                .laneName("Stage 2")
                .stage()
                .build();
        stage.createSwimLane("swim1", "Swim Lane", 2);
        
        assertThrows(LaneException.class, 
            () -> stage.createStage("substage", "Sub Stage", 2),
            "Should not be able to create stage when swimlane exists"
        );
    }

    @Test
    @DisplayName("Test createSwimLane method")
    void testCreateSwimLane() {
        stage.createSwimLane("swim1", "Swim Lane", 2);
        assertEquals(1, stage.getChildren().size());
        
        // Cannot create swimlane if stage exists
        stage = LaneBuilder.newInstance()
                .laneId("stage2")
                .laneName("Stage 2")
                .stage()
                .build();
        stage.createStage("substage", "Sub Stage", 2);
        
        assertThrows(LaneException.class, 
            () -> stage.createSwimLane("swim1", "Swim Lane", 2),
            "Should not be able to create swimlane when stage exists"
        );
    }

    @Test
    @DisplayName("Test createCard method")
    void testCreateCard() {
        // Can create card in empty stage
        stage.createCard("Test Card", CardType.Standard, "board1");
        assertEquals(1, stage.getCards().size());
        
        // Cannot create card if stage has children
        stage = LaneBuilder.newInstance()
                .laneId("stage2")
                .laneName("Stage 2")
                .stage()
                .build();
        stage.createStage("substage", "Sub Stage", 2);
        
        assertThrows(LaneException.class, 
            () -> stage.createCard("Test Card", CardType.Standard, "board1"),
            "Should not be able to create card when stage has children"
        );
        
        // Test WIP limit
        stage = LaneBuilder.newInstance()
                .laneId("stage3")
                .laneName("Stage 3")
                .wipLimit(1)
                .stage()
                .build();
        
        stage.createCard("Card 1", CardType.Standard, "board1");
        assertThrows(LaneException.class, 
            () -> stage.createCard("Card 2", CardType.Standard, "board1"),
            "Should not exceed WIP limit"
        );
    }

    @Test
    @DisplayName("Test addCard method")
    void testAddCard() {
        Card card = new Card("Test Card", CardType.Standard, "board1");
        stage.createCard(card.getDescription(), card.getType(), card.getBoardId());
        assertEquals(1, stage.getCards().size());
        
        // Test WIP limit
        stage.setWipLimit(1);
        Card secondCard = new Card("Second Card", CardType.Standard, "board1");
        assertThrows(LaneException.class, 
            () -> stage.createCard(secondCard.getDescription(), secondCard.getType(), secondCard.getBoardId()),
            "Should not exceed WIP limit"
        );
    }

    @Test
    @DisplayName("Test deleteCard method")
    void testDeleteCard() {
        stage.createCard("Test Card", CardType.Standard, "board1");
        Card card = stage.getCards().get(0);
        
        stage.deleteCard(card);
        assertTrue(stage.getCards().isEmpty());
    }

    @Test
    @DisplayName("Test mixing different card types")
    void testMixingCardTypes() {
        // Stage can accept both Standard and Expedite cards
        stage.createCard("Standard Card", CardType.Standard, "board1");
        stage.createCard("Expedite Card", CardType.Expedite, "board1");
        
        assertEquals(2, stage.getCards().size());
        assertEquals(CardType.Standard, stage.getCards().get(0).getType());
        assertEquals(CardType.Expedite, stage.getCards().get(1).getType());
    }

    @Test
    @DisplayName("Test hierarchical structure")
    void testHierarchicalStructure() {
        // Create a nested structure
        stage.createStage("substage1", "Sub Stage 1", 2);
        Lane subStage = stage.getLaneById("substage1");
        subStage.createCard("Sub Card", CardType.Standard, "board1");
        
        assertEquals(1, stage.getChildren().size());
        assertEquals(1, subStage.getCards().size());
        assertEquals(0, stage.getCards().size());
    }
}
