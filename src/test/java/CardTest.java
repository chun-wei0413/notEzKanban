import com.notezkanban.card.Card;
import com.notezkanban.card.CardType;
import com.notezkanban.lane.Lane;
import com.notezkanban.lane.LaneBuilder;
import com.notezkanban.card.exception.CardException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CardTest {
    @Test
    public void createCard() {
        Card card = new Card("card", CardType.Standard);
        assertEquals("card", card.getDescription());
        assertEquals(CardType.Standard, card.getType());
    }

    @Test
    public void createCardWithEmptyDescription() {
        assertThrows(CardException.class, () -> new Card("", CardType.Standard));
    }

    @Test
    public void changeCardDescriptionToEmpty() {
        Card card = new Card("card", CardType.Standard);

        assertThrows(CardException.class, () -> card.changeDescription(""));
    }

    @Test
    public void changeCardDescription() {
        Card card = new Card("card", CardType.Standard);

        assertEquals("card", card.getDescription());

        card.changeDescription("new description");

        assertEquals("new description", card.getDescription());
    }

    private Lane createRootStage(String stageId, String stageName) {
        return LaneBuilder.newInstance()
                .laneId(stageId)
                .laneName(stageName)
                .stage()
                .build();
    }
}
