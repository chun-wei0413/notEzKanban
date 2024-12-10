//import com.notezkanban.Board;
//import com.notezkanban.Workflow;
//import com.notezkanban.boardMember.BoardMember;
//import com.notezkanban.boardMember.boardMemberImpl.Admin;
//import com.notezkanban.card.Card;
//import com.notezkanban.card.CardType;
//import com.notezkanban.command.Command;
//import com.notezkanban.command.UseCaseImpl.MoveCardUseCase;
//import com.notezkanban.lane.Lane;
//import com.notezkanban.lane.Stage;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class MoveCardTest {
//    @Test
//    public void moveCardTest(){
//        // Given
//        BoardMember frank = new Admin("1", "Frank");
//        Board board = new Board("b1", "boardName");
//        board.addBoardMember(frank);
//
//        Workflow workflow = new Workflow("wf1", "workflowName");
//        board.addWorkflow(workflow);
//        Stage lane1 = new Stage("l1", "l1");
//        Stage lane2 = new Stage("l2", "l2");
//        workflow.addRootStage(lane1);
//        workflow.addRootStage(lane2);
//
//        Card card = new Card("c1", CardType.Standard, "b1");
//        lane1.addCard(card);
//        // When
//        Command moveCard = new MoveCardUseCase(lane1, lane2, card);
//        moveCard.execute();
//        // Then
//
//        assertEquals("Card moved from l1 to l2", frank.getMessage());
//    }
//}
