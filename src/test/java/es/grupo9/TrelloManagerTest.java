package es.grupo9;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrelloManagerTest {

    TrelloManager trelloManager;
    final String BOARD_ID = "614de300aa6df33863299b6c"; // ID of the board we are currently using

    @BeforeEach
    void setUp() {
        TrelloManager trelloManager = new TrelloManager("e3ee0d6a1686b4b43ba5d046bbce20af", config.MY_TOKEN, BOARD_ID);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getBoardListIdByName() {
        // ID Lista "Sprints" da Board = 61606295191d043999a57bcb
        Assertions.assertEquals("61606295191d043999a57bcb", TrelloManager.getBoardListIdByName("Sprints"));
    }

    @Test
    void getFinishedSprintBacklog() {
        Assertions.assertNotEquals(null,TrelloManager.getFinishedSprintBacklog(1));
    }

    @Test
    void getMeetings() {
        // ID to primeiro cartão da lista Meetings = 616485eb23537a5ed11aec71
        Assertions.assertEquals(TrelloManager.getMeetings().get(1).get(0).getId(),"616485eb23537a5ed11aec71");
    }

    @Test
    void getSprintCount() {
        // Número de SPRINTS = 3
        Assertions.assertEquals(TrelloManager.getSprintCount(),3);
    }
}