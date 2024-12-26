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

public class ExpediteLaneTest {
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
    @DisplayName("Test getLaneId method")
    void testGetLaneId() {
        assertEquals("exp1", expediteLane.getLaneId());
    }

    @Test
    @DisplayName("Test getLaneName method")
    void testGetLaneName() {
        assertEquals("Expedite", expediteLane.getLaneName());
    }

    @Test
    @DisplayName("Test getChildren method returns empty list")
    void testGetChildren() {
        assertTrue(expediteLane.getChildren().isEmpty());
    }

    @Test
    @DisplayName("Test getWipLimit method always returns 1")
    void testGetWipLimit() {
        assertEquals(1, expediteLane.getWipLimit());
    }

    @Test
    @DisplayName("Test setWipLimit method throws UnsupportedOperationException")
    void testSetWipLimit() {
        assertThrows(UnsupportedOperationException.class, 
            () -> expediteLane.setWipLimit(2),
            "Should not be able to change WIP limit for Expedite Lane"
        );
    }

    @Test
    @DisplayName("Test iterator method returns empty iterator")
    void testIterator() {
        assertFalse(expediteLane.iterator().hasNext());
    }

    @Test
    @DisplayName("Test getCards method")
    void testGetCards() {
        assertTrue(expediteLane.getCards().isEmpty());
        
        expediteLane.createCard("Expedite Card", CardType.Expedite, "board1");
        assertEquals(1, expediteLane.getCards().size());
        assertEquals("Expedite Card", expediteLane.getCards().get(0).getDescription());
    }

    @Test
    @DisplayName("Test createStage method throws LaneException")
    void testCreateStage() {
        assertThrows(LaneException.class, 
            () -> expediteLane.createStage("stage1", "Development", 2),
            "Should not be able to create stage in Expedite Lane"
        );
    }

    @Test
    @DisplayName("Test createSwimLane method throws LaneException")
    void testCreateSwimLane() {
        assertThrows(LaneException.class, 
            () -> expediteLane.createSwimLane("swim1", "Team A", 2),
            "Should not be able to create swim lane in Expedite Lane"
        );
    }

    @Test
    @DisplayName("Test createCard method with valid and invalid cards")
    void testCreateCard() {
        // Test with non-Expedite card
        assertThrows(LaneException.class, 
            () -> expediteLane.createCard("Standard Card", CardType.Standard, "board1"),
            "Should not accept non-Expedite cards"
        );

        // Test with Expedite card
        expediteLane.createCard("Expedite Card", CardType.Expedite, "board1");
        assertEquals(1, expediteLane.getCards().size());

        // Test WIP limit
        assertThrows(LaneException.class, 
            () -> expediteLane.createCard("Second Expedite", CardType.Expedite, "board1"),
            "Should not exceed WIP limit"
        );
    }

    @Test
    @DisplayName("Test addCard method with valid and invalid cards")
    void testAddCard() {
        Card standardCard = new Card("Standard Card", CardType.Standard, "board1");
        Card expediteCard = new Card("Expedite Card", CardType.Expedite, "board1");
        
        // Test with non-Expedite card
        assertThrows(LaneException.class, 
            () -> expediteLane.createCard(standardCard.getDescription(), standardCard.getType(), standardCard.getBoardId()),
            "Should not accept non-Expedite cards"
        );
        
        // Test with Expedite card
        expediteLane.createCard(expediteCard.getDescription(), expediteCard.getType(), expediteCard.getBoardId());
        assertEquals(1, expediteLane.getCards().size());
        
        // Test WIP limit
        Card secondExpediteCard = new Card("Second Expedite", CardType.Expedite, "board1");
        assertThrows(LaneException.class, 
            () -> expediteLane.createCard(secondExpediteCard.getDescription(), secondExpediteCard.getType(), secondExpediteCard.getBoardId()),
            "Should not exceed WIP limit"
        );
    }

    @Test
    @DisplayName("Test deleteCard method")
    void testDeleteCard() {
        expediteLane.createCard("Expedite Card", CardType.Expedite, "board1");
        Card card = expediteLane.getCards().get(0);
        
        expediteLane.deleteCard(card);
        assertTrue(expediteLane.getCards().isEmpty());
        
        // Verify can add new card after deletion
        expediteLane.createCard("New Expedite Card", CardType.Expedite, "board1");
        assertEquals(1, expediteLane.getCards().size());
    }
}
