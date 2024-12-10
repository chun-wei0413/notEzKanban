import com.notezkanban.card.Card;
import com.notezkanban.card.CardType;
import com.notezkanban.card.exception.CardException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CardTest {
    
    @Test
    public void createCardSuccessfully() {
        Card card = new Card("test card", CardType.Standard, "board-1");
        
        assertNotNull(card.getId());
        assertEquals("test card", card.getDescription());
        assertEquals(CardType.Standard, card.getType());
        assertEquals("board-1", card.getBoardId());
    }

    @Test
    public void createCardWithEmptyDescription() {
        assertThrows(CardException.class, () -> 
            new Card("", CardType.Standard, "board-1")
        );
    }

    @Test
    public void createCardWithBlankDescription() {
        assertThrows(CardException.class, () -> 
            new Card("   ", CardType.Standard, "board-1")
        );
    }

    @Test
    public void changeDescriptionSuccessfully() {
        Card card = new Card("old description", CardType.Standard, "board-1");
        card.changeDescription("new description");
        assertEquals("new description", card.getDescription());
    }

    @Test
    public void changeDescriptionToEmpty() {
        Card card = new Card("old description", CardType.Standard, "board-1");
        assertThrows(CardException.class, () -> 
            card.changeDescription("")
        );
    }

    @Test
    public void changeDescriptionToBlank() {
        Card card = new Card("old description", CardType.Standard, "board-1");
        assertThrows(CardException.class, () -> 
            card.changeDescription("   ")
        );
    }

    @Test
    public void cardIdShouldBeUnique() {
        Card card1 = new Card("card 1", CardType.Standard, "board-1");
        Card card2 = new Card("card 2", CardType.Standard, "board-1");
        
        assertNotEquals(card1.getId(), card2.getId());
    }

    @Test
    public void createCardWithNullDescription() {
        assertThrows(NullPointerException.class, () -> 
            new Card(null, CardType.Standard, "board-1")
        );
    }

    @Test
    public void changeDescriptionToNull() {
        Card card = new Card("old description", CardType.Standard, "board-1");
        assertThrows(NullPointerException.class, () -> 
            card.changeDescription(null)
        );
    }
}
