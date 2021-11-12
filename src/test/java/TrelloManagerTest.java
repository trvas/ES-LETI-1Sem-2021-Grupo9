import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrelloManagerTest {

    TrelloManager trelloManager;
    final String BOARD_ID = "614de300aa6df33863299b6c"; // ID of the board we are currently using

    @BeforeEach
    void setUp() {
        trelloManager = new TrelloManager(config.API_KEY, config.MY_TOKEN, BOARD_ID);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getFinishedSprintBacklog() {
        Assertions.assertNotEquals(null,trelloManager.getFinishedSprintBacklog(1));
    }
}