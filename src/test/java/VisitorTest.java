import com.notezkanban.card.CardType;
import com.notezkanban.lane.Lane;
import com.notezkanban.lane.Stage;
import com.notezkanban.visitor.LaneVisitor;
import com.notezkanban.visitor.visitorImpl.PrettyPrintVisitor;
import com.notezkanban.visitor.visitorImpl.TotalCardVisitor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VisitorTest {

    @Test
    public void testCardStatusVisitor() {
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
        LaneVisitor visitor = new TotalCardVisitor();
        rootStage.accept(visitor);

        assertEquals(5, visitor.getResult());
    }

    @Test
    public void testLanePrettyPrintVisitor() {
        // 建立測試數據結構
        Lane rootStage = createRootStage("root", "Root Stage");

        // 第一層：建立 Stage
        rootStage.createStage("stage1", "Stage 1");

        // 第二層：在 stage1 下建立 Swimlanes
        rootStage.getLaneById("stage1").createSwimLane("swimlane1", "Swimlane 1");
        rootStage.getLaneById("stage1").createSwimLane("swimlane2", "Swimlane 2");

        // 第三層：為 swimlane1 添加 leaf subLane 並添加卡片
        rootStage.getLaneById("stage1")
                .getLaneById("swimlane1")
                .createStage("leaf1", "Leaf 1");
        rootStage.getLaneById("stage1")
                .getLaneById("swimlane1")
                .getLaneById("leaf1")
                .createCard("Task A", CardType.Standard, "board1");

        // 第三層：為 swimlane2 添加卡片
        rootStage.getLaneById("stage1")
                .getLaneById("swimlane2")
                .createCard("Urgent Fix", CardType.Expedite, "board1");

        // 使用 PrettyPrintVisitor
        PrettyPrintVisitor visitor = new PrettyPrintVisitor();
        rootStage.accept(visitor);
        String result = visitor.getResult();

        // 預期輸出
        String expected = 
            "Stage: Root Stage\n" +
            "  Stage: Stage 1\n" +
            "    SwimLane: Swimlane 1\n" +
            "      Stage: Leaf 1\n" +
            "        └─ Card: Task A (Standard)\n" +
            "    SwimLane: Swimlane 2\n" +
            "      └─ Card: Urgent Fix (Expedite)\n";

        // 驗證輸出
        assertEquals(expected, result);
        
        // 輸出實際結果以便查看
        System.out.println("Actual output:");
        System.out.println(result);
    }

    private Lane createRootStage(String stageId, String stageName) {
        return new Stage(stageId, stageName);
    }
}
