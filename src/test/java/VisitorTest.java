import com.notezkanban.Visitor;
import com.notezkanban.WIPCountVisitor;
import com.notezkanban.card.Card;
import com.notezkanban.card.CardFactory;
import com.notezkanban.card.CardType;
import com.notezkanban.lane.Lane;
import com.notezkanban.lane.Stage;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VisitorTest {

    @Test
    public void WIPCountVisitor(){
        Visitor visitor = new WIPCountVisitor();
        String stageId = "1";
        String stageName = "lane1";
        Lane rootStage = new Stage(stageId, stageName);
        Card c1 = CardFactory.createCard("c1", CardType.Standard);
        Card c2 = CardFactory.createCard("c2", CardType.Standard);

        rootStage.addCard(c1);
        rootStage.addCard(c2);

        rootStage.accept(visitor);

        assertEquals(2, visitor.getResult());
    }
}
