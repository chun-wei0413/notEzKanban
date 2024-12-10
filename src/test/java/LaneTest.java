import com.notezkanban.card.Card;
import com.notezkanban.card.CardType;
import com.notezkanban.lane.Lane;
import com.notezkanban.lane.Stage;
import com.notezkanban.lane.exception.LaneException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LaneTest {

    @Test
    public void createStage() {
        Lane stage = createRootStage("1", "rootStage");

        stage.createStage("1-1", "innerStage");
        stage.getChildren().get(0).createStage("1-1-1", "stage3");

        assertEquals("1-1", stage.getLaneById("1-1").getLaneId());
        assertEquals("1-1-1", stage.getLaneById("1-1").getLaneById("1-1-1").getLaneId());
    }

    @Test
    public void createSwimLane() {
        Lane stage = createRootStage("1", "rootStage");

        stage.createSwimLane("1-1", "innerStage");
        stage.getChildren().get(0).createSwimLane("1-1-1", "stage3");

        assertEquals("1-1", stage.getLaneById("1-1").getLaneId());
        assertEquals("1-1-1", stage.getLaneById("1-1").getLaneById("1-1-1").getLaneId());
    }

    @Test
    public void notAllowStageAndSwimLaneInSameLevel() {
        Lane stage = createRootStage("1", "rootStage");

        stage.createStage("1-1", "innerStage");

        assertThrows(LaneException.class, () -> stage.createSwimLane("1-2", "swimLane"));
    }

    @Test
    public void notAllowAddCardInNonLeafLane() {
        Lane stage = createRootStage("1", "rootStage");

        stage.createStage("1-1", "innerStage");

        assertThrows(LaneException.class, () -> stage.createCard("c1", CardType.Standard, "board1"));
    }

    @Test
    public void countTotalCardInComplexLaneWithLeafRestriction() {
        // 建立 rootStage
        Lane rootStage = createRootStage("root", "Root Stage");

        // 第一層：建立 Stage
        rootStage.createStage("stage1", "Stage 1");

        // 第二層：在 stage1 下建立 Swimlanes
        rootStage.getLaneById("stage1").createSwimLane("swimlane1", "Swimlane 1");
        rootStage.getLaneById("stage1").createSwimLane("swimlane2", "Swimlane 2");

        // 第三層：為 swimlane1 添加 leaf subLane 並添加卡片
        rootStage.getLaneById("stage1").getLaneById("swimlane1").createStage("leaf1", "Leaf 1");
        rootStage.getLaneById("stage1").getLaneById("swimlane1").getLaneById("leaf1").createCard("card1", CardType.Standard, "board1");
        rootStage.getLaneById("stage1").getLaneById("swimlane1").getLaneById("leaf1").createCard("card2", CardType.Standard, "board1");

        // 第三層：為 swimlane2 添加 leaf subLane 並添加卡片
        rootStage.getLaneById("stage1").getLaneById("swimlane2").createStage("leaf2", "Leaf 2");
        rootStage.getLaneById("stage1").getLaneById("swimlane2").getLaneById("leaf2").createCard("card3", CardType.Standard, "board1");
        rootStage.getLaneById("stage1").getLaneById("swimlane2").getLaneById("leaf2").createCard("card4", CardType.Standard, "board1");

        // 第四層：為 swimlane2 添加 Nested Stage（stage2），然後在 stage2 下添加 leaf subLane 並添加卡片
        rootStage.getLaneById("stage1").getLaneById("swimlane2").createStage("stage2", "Nested Stage");
        rootStage.getLaneById("stage1").getLaneById("swimlane2").getLaneById("stage2").createStage("leaf3", "Leaf 3");
        rootStage.getLaneById("stage1").getLaneById("swimlane2").getLaneById("stage2").getLaneById("leaf3").createCard("card5", CardType.Standard, "board1");

        // 驗證卡片總數（目前有 5 張卡片）
        assertEquals(5, rootStage.getTotalCardCount());

        // 新增 stage3 並在其下添加 leaf subLane 和卡片
        rootStage.createStage("stage3", "Stage 3");
        rootStage.getLaneById("stage3").createStage("leaf4", "Leaf 4");
        rootStage.getLaneById("stage3").getLaneById("leaf4").createCard("card6", CardType.Standard, "board1");
        rootStage.getLaneById("stage3").getLaneById("leaf4").createCard("card7", CardType.Standard, "board1");

        // 驗證卡片總數（目前有 7 張卡片）
        assertEquals(7, rootStage.getTotalCardCount());
    }


    @Test
    public void countExpediteCardsInLeafSubLanesOnly() {
        // 建立 rootStage
        Lane rootStage = createRootStage("root", "Root Stage");

        // 第一層：建立 Stage
        rootStage.createStage("stage1", "Stage 1");
        rootStage.createStage("stage2", "Stage 2");

        // 第二層：在 stage1 和 stage2 添加 Swimlanes 和子 Lanes
        rootStage.getLaneById("stage1").createSwimLane("swimlane1", "Swimlane 1");
        rootStage.getLaneById("stage1").createSwimLane("swimlane2", "Swimlane 2");

        rootStage.getLaneById("stage2").createSwimLane("swimlane3", "Swimlane 3");
        rootStage.getLaneById("stage2").createSwimLane("swimlane4", "Swimlane 4");

        // 第三層：在 swimlanes 下建立 leaf subLanes 並添加卡片
        rootStage.getLaneById("stage1").getLaneById("swimlane1").createStage("leaf1", "Leaf 1");
        rootStage.getLaneById("stage1").getLaneById("swimlane1").getLaneById("leaf1").createCard("card1", CardType.Standard, "board1");
        rootStage.getLaneById("stage1").getLaneById("swimlane1").getLaneById("leaf1").createCard("card2", CardType.Expedite, "board1");

        rootStage.getLaneById("stage1").getLaneById("swimlane2").createStage("leaf2", "Leaf 2");
        rootStage.getLaneById("stage1").getLaneById("swimlane2").getLaneById("leaf2").createCard("card3", CardType.Standard, "board1");

        rootStage.getLaneById("stage2").getLaneById("swimlane3").createStage("leaf3", "Leaf 3");
        rootStage.getLaneById("stage2").getLaneById("swimlane3").getLaneById("leaf3").createCard("card4", CardType.Expedite, "board1");
        rootStage.getLaneById("stage2").getLaneById("swimlane3").getLaneById("leaf3").createCard("card5", CardType.Expedite, "board1");

        rootStage.getLaneById("stage2").getLaneById("swimlane4").createStage("leaf4", "Leaf 4");
        rootStage.getLaneById("stage2").getLaneById("swimlane4").getLaneById("leaf4").createCard("card6", CardType.Standard, "board1");

        // 驗證 Expedite 卡片數量
        assertEquals(3, rootStage.getExpediteCardCount());
    }


    private Lane createRootStage(String stageId, String stageName) {
        return new Stage(stageId, stageName);
    }
}
