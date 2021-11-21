package es.grupo9;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrelloManagerTest {

    @BeforeEach
    void setUp() {
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