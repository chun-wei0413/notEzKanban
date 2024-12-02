import com.notezkanban.*;
import com.notezkanban.exception.CardException;
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
        Card card = new Card("initial description", CardType.Standard);

        assertThrows(CardException.class, () -> card.changeDescription(""));
    }


    @Test
    public void changeCardDescription() {
        Card card = new Card("card", CardType.Standard);

        card.changeDescription("new description");

        assertEquals("new description", card.getDescription());
    }

    @Test
    public void moveCard() {
        String stageId1 = "1";
        String stageName1 = "rootStage";
        String stageId2 = "2";
        String stageName2 = "rootStage2";
        Lane stage = createRootStage(stageId1, stageName1);
        Lane stage2 = createRootStage(stageId2, stageName2);
        Card card = new Card("card", CardType.Standard);

        stage.addCard(card);
        assertTrue(stage.getCard(card.getId()).isPresent());

        Command moveCardCommand = new MoveCardCommand(stage, stage2, card);
        moveCardCommand.execute();

        assertTrue(stage2.getCard(card.getId()).isPresent());
        assertFalse(stage.getCard(card.getId()).isPresent());

    }

    @Test
    public void moveNonExistentCard() {
        String stageId1 = "1";
        String stageName1 = "rootStage";
        String stageId2 = "2";
        String stageName2 = "rootStage2";
        Lane sourceLane = createRootStage(stageId1, stageName1);
        Lane targetLane = createRootStage(stageId2, stageName2);
        Card card = new Card("card", CardType.Standard);

        Command moveCardCommand = new MoveCardCommand(sourceLane, targetLane, card);

        // sourceLane 沒有這張卡片，throw exception
        assertThrows(IllegalArgumentException.class, moveCardCommand::execute);
    }

    @Test
    public void deleteCardRemovesFromLane() {
        String stageId = "1";
        String stageName = "rootStage";
        Lane stage = createRootStage(stageId, stageName);
        Card card = new Card("card", CardType.Standard);

        stage.addCard(card);
        assertTrue(stage.getCard(card.getId()).isPresent());

        stage.deleteCard(card);

        assertFalse(stage.getCard(card.getId()).isPresent());
    }

    @Test
    public void batchMoveCards() {
        String stageId1 = "1";
        String stageName1 = "rootStage";
        String stageId2 = "2";
        String stageName2 = "targetStage";
        Lane sourceLane = createRootStage(stageId1, stageName1);
        Lane targetLane = createRootStage(stageId2, stageName2);

        Card card1 = new Card("card1", CardType.Standard);
        Card card2 = new Card("card2", CardType.Standard);

        sourceLane.addCard(card1);
        sourceLane.addCard(card2);

        Command moveCard1Command = new MoveCardCommand(sourceLane, targetLane, card1);
        Command moveCard2Command = new MoveCardCommand(sourceLane, targetLane, card2);

        moveCard1Command.execute();
        moveCard2Command.execute();

        assertTrue(targetLane.getCard(card1.getId()).isPresent());
        assertTrue(targetLane.getCard(card2.getId()).isPresent());
        assertFalse(sourceLane.getCard(card1.getId()).isPresent());
        assertFalse(sourceLane.getCard(card2.getId()).isPresent());
    }


    private Lane createRootStage(String stageId, String stageName) {
        return LaneBuilder.newInstance()
                .laneId(stageId)
                .laneName(stageName)
                .stage()
                .build();
    }
}
