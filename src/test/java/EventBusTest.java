import com.notezkanban.notifier.Notifier;
import com.notezkanban.notifier.notifierImpl.EmailNotifier;
import com.notezkanban.notifier.notifierImpl.LineNotifier;
import com.notezkanban.notifier.notifierImpl.TelegramNotifier;
import com.notezkanban.EventBus;
import com.notezkanban.Event;
import com.notezkanban.DomainEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class EventBusTest {
    
    private EventBus eventBus;
    private static final String BOARD_ID = "board-123";
    private Notifier emailNotifier;
    private Notifier lineNotifier;
    private Notifier telegramNotifier;

    @BeforeEach
    void setUp() {
        eventBus = EventBus.getInstance();
        emailNotifier = new EmailNotifier();
        lineNotifier = new LineNotifier();
        telegramNotifier = new TelegramNotifier();
    }

    @Test
    void shouldNotifyRegisteredNotifiers() {
        // Arrange
        eventBus.registerNotifier(BOARD_ID, emailNotifier);
        eventBus.registerNotifier(BOARD_ID, lineNotifier);
        Event event = new DomainEvent(BOARD_ID, "test message");

        // Act
        eventBus.publish(event);

        // Assert
        assertEquals("test message", emailNotifier.message());
        assertEquals("test message", lineNotifier.message());
    }

    @Test
    void shouldNotNotifyUnregisteredNotifiers() {
        // Arrange
        eventBus.registerNotifier(BOARD_ID, emailNotifier);
        eventBus.registerNotifier(BOARD_ID, lineNotifier);
        eventBus.unregisterNotifier(BOARD_ID, lineNotifier);
        Event event = new DomainEvent(BOARD_ID, "test message");

        // Act
        eventBus.publish(event);

        // Assert
        assertEquals("test message", emailNotifier.message());
        assertNull(lineNotifier.message());
    }

    @Test
    void shouldHandleMultipleNotifiersForSameBoard() {
        // Arrange
        eventBus.registerNotifier(BOARD_ID, emailNotifier);
        eventBus.registerNotifier(BOARD_ID, lineNotifier);
        eventBus.registerNotifier(BOARD_ID, telegramNotifier);
        Event event = new DomainEvent(BOARD_ID, "team message");

        // Act
        eventBus.publish(event);

        // Assert
        assertEquals("team message", emailNotifier.message());
        assertEquals("team message", lineNotifier.message());
        assertEquals("team message", telegramNotifier.message());
    }

    @Test
    void shouldHandleDifferentBoardsIndependently() {
        // Arrange
        String board1 = "board-1";
        String board2 = "board-2";
        
        eventBus.registerNotifier(board1, emailNotifier);
        eventBus.registerNotifier(board2, lineNotifier);

        // Act
        eventBus.publish(new DomainEvent(board1, "board1 message"));
        eventBus.publish(new DomainEvent(board2, "board2 message"));

        // Assert
        assertEquals("board1 message", emailNotifier.message());
        assertEquals("board2 message", lineNotifier.message());
    }

    @Test
    void shouldHandleUnregisteredBoard() {
        // Arrange
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(errContent));
        
        String nonExistentBoardId = "non-existent-board";
        Event event = new DomainEvent(nonExistentBoardId, "test message");

        try {
            // Act
            eventBus.publish(event);

            // Assert
            assertEquals("No team registered for boardId: " + nonExistentBoardId + System.lineSeparator(), 
                        errContent.toString());
        } finally {
            // Restore original error stream
            System.setErr(originalErr);
        }
    }

    @Test
    void shouldRemoveBoardWhenLastNotifierUnregistered() {
        // Arrange
        eventBus.registerNotifier(BOARD_ID, emailNotifier);
        Event event = new DomainEvent(BOARD_ID, "test message");

        // Act
        eventBus.unregisterNotifier(BOARD_ID, emailNotifier);
        eventBus.publish(event);

        // Assert
        assertNull(emailNotifier.message());
    }
}
