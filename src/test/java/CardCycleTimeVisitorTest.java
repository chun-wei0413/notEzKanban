import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;

import com.notezkanban.card.Card;
import com.notezkanban.lane.ExpediteLane;
import com.notezkanban.lane.Stage;
import com.notezkanban.lane.SwimLane;
import com.notezkanban.visitor.visitorImpl.CardCycleTimeVisitor;

import java.util.*;

class CardCycleTimeVisitorTest {

    private CardCycleTimeVisitor visitor;
    private Stage mockStage;
    private SwimLane mockSwimLane;
    private ExpediteLane mockExpediteLane;

    @BeforeEach
    void setUp() {
        visitor = new CardCycleTimeVisitor();
        mockStage = Mockito.mock(Stage.class);
        mockSwimLane = Mockito.mock(SwimLane.class);
        mockExpediteLane = Mockito.mock(ExpediteLane.class);
    }

    @Test
    void testVisitStage() {
        Card card1 = createMockCard("stage-1", 10, 20);
        Card card2 = createMockCard("stage-1", 30, 50);
        List<Card> cards = Arrays.asList(card1, card2);

        Mockito.when(mockStage.getCards()).thenReturn(cards);
        Mockito.when(mockStage.getLaneId()).thenReturn("stage-1");
        Mockito.when(mockStage.getLaneName()).thenReturn("Stage 1");
        Mockito.when(mockStage.iterator()).thenReturn(Collections.emptyIterator());

        visitor.visitStage(mockStage);

        Map<String, Map<UUID, Double>> result = visitor.getResult();
        assertEquals(1, result.size());
        assertTrue(result.containsKey("Stage 1"));

        Map<UUID, Double> stageData = result.get("Stage 1");
        assertEquals(2, stageData.size());
        assertEquals(10 / (1000.0 * 60 * 60 * 24), stageData.get(card1.getId()));
        assertEquals(20 / (1000.0 * 60 * 60 * 24), stageData.get(card2.getId()));
    }

    @Test
    void testVisitSwimLane() {
        Card card1 = createMockCard("swimlane-1", 15, 25);
        Card card2 = createMockCard("swimlane-1", 35, 55);
        List<Card> cards = Arrays.asList(card1, card2);

        Mockito.when(mockSwimLane.getCards()).thenReturn(cards);
        Mockito.when(mockSwimLane.getLaneId()).thenReturn("swimlane-1");
        Mockito.when(mockSwimLane.getLaneName()).thenReturn("SwimLane 1");
        Mockito.when(mockSwimLane.iterator()).thenReturn(Collections.emptyIterator());

        visitor.visitSwimLane(mockSwimLane);

        Map<String, Map<UUID, Double>> result = visitor.getResult();
        assertEquals(1, result.size());
        assertTrue(result.containsKey("SwimLane 1"));

        Map<UUID, Double> swimLaneData = result.get("SwimLane 1");
        assertEquals(2, swimLaneData.size());
        assertEquals(10 / (1000.0 * 60 * 60 * 24), swimLaneData.get(card1.getId()));
        assertEquals(20 / (1000.0 * 60 * 60 * 24), swimLaneData.get(card2.getId()));
    }

    @Test
    void testVisitExpediteLane() {
        Card card1 = createMockCard("expedite-1", 40, 60);
        Card card2 = createMockCard("expedite-1", 70, 90);
        List<Card> cards = Arrays.asList(card1, card2);

        Mockito.when(mockExpediteLane.getCards()).thenReturn(cards);
        Mockito.when(mockExpediteLane.getLaneId()).thenReturn("expedite-1");
        Mockito.when(mockExpediteLane.getLaneName()).thenReturn("Expedite Lane 1");
        Mockito.when(mockExpediteLane.iterator()).thenReturn(Collections.emptyIterator());

        visitor.visitExpediteLane(mockExpediteLane);

        Map<String, Map<UUID, Double>> result = visitor.getResult();
        assertEquals(1, result.size());
        assertTrue(result.containsKey("Expedite Lane 1"));

        Map<UUID, Double> expediteLaneData = result.get("Expedite Lane 1");
        assertEquals(2, expediteLaneData.size());
        assertEquals(20 / (1000.0 * 60 * 60 * 24), expediteLaneData.get(card1.getId()));
        assertEquals(20 / (1000.0 * 60 * 60 * 24), expediteLaneData.get(card2.getId()));
    }

    private Card createMockCard(String stageId, long entryTime, long exitTime) {
        Card card = Mockito.mock(Card.class);
        UUID cardId = UUID.randomUUID();

        Mockito.when(card.getId()).thenReturn(cardId);
        Mockito.when(card.getCycleTime(stageId)).thenReturn((exitTime - entryTime) / (1000.0 * 60 * 60 * 24));

        return card;
    }
}
