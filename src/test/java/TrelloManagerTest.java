import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.trello4j.Trello;
import org.trello4j.TrelloImpl;

import static org.junit.jupiter.api.Assertions.*;

class TrelloManagerTest {

    TrelloManager trelloManager;

    @BeforeEach
    void setUp() {
        trelloManager = new TrelloManager(config.API_KEY, config.MY_TOKEN, config.TESTBOARD_ID);

    }

    @AfterEach
    void tearDown() {
    }

    // add tests
    @Test
    void getBoardListIdByName() {
        // Lista 1 da Board teste ID = 614deced0f4b33256c0c6a78
        Assertions.assertEquals("614deced0f4b33256c0c6a78",trelloManager.getBoardListIdByName("Lista 1"));
    }

    @Test
    void getFinishedSprintBacklog() {
        Assertions.assertNotEquals(null,trelloManager.getFinishedSprintBacklog(1));
    }
}