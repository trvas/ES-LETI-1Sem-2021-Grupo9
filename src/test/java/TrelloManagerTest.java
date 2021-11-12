import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.trello4j.Trello;
import org.trello4j.TrelloImpl;

import static org.junit.jupiter.api.Assertions.*;

class TrelloManagerTest {

    @BeforeEach
    void setUp() {
        TrelloManager trelloManager = new TrelloManager("e3ee0d6a1686b4b43ba5d046bbce20af", config.MY_TOKEN, "614de300aa6df33863299b6c");

    }

    @AfterEach
    void tearDown() {
    }

    // add tests
    @Test
    void getBoardListIdByName() {



    }
}