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

public class SwimLaneTest {
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
    @DisplayName("Test getLaneId method")
    void testGetLaneId() {
        assertEquals("swim1", swimLane.getLaneId());
    }

    @Test
    @DisplayName("Test getLaneName method")
    void testGetLaneName() {
        assertEquals("Team A", swimLane.getLaneName());
    }

    @Test
    @DisplayName("Test getChildren method")
    void testGetChildren() {
        assertTrue(swimLane.getChildren().isEmpty());
        
        swimLane.createSwimLane("subswim", "Sub Swim Lane", 2);
        assertEquals(1, swimLane.getChildren().size());
        assertEquals("subswim", swimLane.getChildren().get(0).getLaneId());
    }

    @Test
    @DisplayName("Test getWipLimit and setWipLimit methods")
    void testWipLimit() {
        assertEquals(3, swimLane.getWipLimit());
        
        swimLane.setWipLimit(5);
        assertEquals(5, swimLane.getWipLimit());
    }

    @Test
    @DisplayName("Test iterator method")
    void testIterator() {
        assertFalse(swimLane.iterator().hasNext());
        
        swimLane.createSwimLane("subswim", "Sub Swim Lane", 2);
        assertTrue(swimLane.iterator().hasNext());
        assertEquals("subswim", swimLane.iterator().next().getLaneId());
    }

    @Test
    @DisplayName("Test getCards method")
    void testGetCards() {
        assertTrue(swimLane.getCards().isEmpty());
        
        swimLane.createCard("Test Card", CardType.Standard, "board1");
        assertEquals(1, swimLane.getCards().size());
        assertEquals("Test Card", swimLane.getCards().get(0).getDescription());
    }

    @Test
    @DisplayName("Test createStage method")
    void testCreateStage() {
        swimLane.createStage("stage1", "Development Stage", 2);
        assertEquals(1, swimLane.getChildren().size());
        
        // Cannot create stage if swimlane exists
        swimLane = LaneBuilder.newInstance()
                .laneId("swim2")
                .laneName("Team B")
                .swimLane()
                .build();
        swimLane.createSwimLane("subswim", "Sub Swim Lane", 2);
        
        assertThrows(LaneException.class, 
            () -> swimLane.createStage("stage1", "Development Stage", 2),
            "Should not be able to create stage when swimlane exists"
        );
    }

    @Test
    @DisplayName("Test createSwimLane method")
    void testCreateSwimLane() {
        swimLane.createSwimLane("subswim", "Sub Swim Lane", 2);
        assertEquals(1, swimLane.getChildren().size());
        
        // Cannot create swimlane if stage exists
        swimLane = LaneBuilder.newInstance()
                .laneId("swim2")
                .laneName("Team B")
                .swimLane()
                .build();
        swimLane.createStage("stage1", "Development Stage", 2);
        
        assertThrows(LaneException.class, 
            () -> swimLane.createSwimLane("subswim", "Sub Swim Lane", 2),
            "Should not be able to create swimlane when stage exists"
        );
    }

    @Test
    @DisplayName("Test createCard method")
    void testCreateCard() {
        // Can create card in empty swimlane
        swimLane.createCard("Test Card", CardType.Standard, "board1");
        assertEquals(1, swimLane.getCards().size());
        
        // Cannot create card if swimlane has children
        swimLane = LaneBuilder.newInstance()
                .laneId("swim2")
                .laneName("Team B")
                .swimLane()
                .build();
        swimLane.createSwimLane("subswim", "Sub Swim Lane", 2);
        
        assertThrows(LaneException.class, 
            () -> swimLane.createCard("Test Card", CardType.Standard, "board1"),
            "Should not be able to create card when swimlane has children"
        );
        
        // Test WIP limit
        swimLane = LaneBuilder.newInstance()
                .laneId("swim3")
                .laneName("Team C")
                .wipLimit(1)
                .swimLane()
                .build();
        
        swimLane.createCard("Card 1", CardType.Standard, "board1");
        assertThrows(LaneException.class, 
            () -> swimLane.createCard("Card 2", CardType.Standard, "board1"),
            "Should not exceed WIP limit"
        );
    }

    @Test
    @DisplayName("Test addCard method")
    void testAddCard() {
        Card card = new Card("Test Card", CardType.Standard, "board1");
        swimLane.createCard(card.getDescription(), card.getType(), card.getBoardId());
        assertEquals(1, swimLane.getCards().size());
        
        // Test WIP limit
        swimLane.setWipLimit(1);
        Card secondCard = new Card("Second Card", CardType.Standard, "board1");
        assertThrows(LaneException.class, 
            () -> swimLane.createCard(secondCard.getDescription(), secondCard.getType(), secondCard.getBoardId()),
            "Should not exceed WIP limit"
        );
    }

    @Test
    @DisplayName("Test deleteCard method")
    void testDeleteCard() {
        swimLane.createCard("Test Card", CardType.Standard, "board1");
        Card card = swimLane.getCards().get(0);
        
        swimLane.deleteCard(card);
        assertTrue(swimLane.getCards().isEmpty());
    }
    
    @Test
    @DisplayName("Test mixing different card types")
    void testMixingCardTypes() {
        // SwimLane can accept both Standard and Expedite cards
        swimLane.createCard("Standard Card", CardType.Standard, "board1");
        swimLane.createCard("Expedite Card", CardType.Expedite, "board1");
        
        assertEquals(2, swimLane.getCards().size());
        assertEquals(CardType.Standard, swimLane.getCards().get(0).getType());
        assertEquals(CardType.Expedite, swimLane.getCards().get(1).getType());
    }

    @Test
    @DisplayName("Test nested structure with stages")
    void testNestedStructureWithStages() {
        swimLane.createStage("stage1", "Development Stage", 2);
        Lane stage = swimLane.getLaneById("stage1");
        stage.createCard("Stage Card", CardType.Standard, "board1");
        
        assertEquals(1, swimLane.getChildren().size());
        assertEquals(1, stage.getCards().size());
        assertEquals(0, swimLane.getCards().size());
    }

    @Test
    @DisplayName("Test nested structure with swimlanes")
    void testNestedStructureWithSwimlanes() {
        swimLane.createSwimLane("subswim", "Sub Swim Lane", 2);
        Lane subSwimLane = swimLane.getLaneById("subswim");
        subSwimLane.createCard("Swim Card", CardType.Standard, "board1");
        
        assertEquals(1, swimLane.getChildren().size());
        assertEquals(1, subSwimLane.getCards().size());
        assertEquals(0, swimLane.getCards().size());
    }
}
