import com.notezkanban.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardTest {
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

        stage.moveCard(stage2, card);

        assertTrue(stage2.getCard(card.getId()).isPresent());

    }

    private Lane createRootStage(String stageId, String stageName) {
        return LaneBuilder.newInstance()
                .laneId(stageId)
                .laneName(stageName)
                .stage()
                .build();
    }
}
