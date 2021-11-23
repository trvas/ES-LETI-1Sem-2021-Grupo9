package es.grupo9;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrelloManagerTest {

    @BeforeEach
    void setUp() {
        TrelloManager trelloManager = new TrelloManager(config.API_KEY, config.MY_TOKEN, config.BOARD_ID);
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

    @Test
    void getCardHours() {
        // Card ID = 6160610ad426ed7225e880eb ("Criação da interface gráfica") , hours = 14.87
        Assertions.assertEquals(TrelloManager.getCardHours("6160610ad426ed7225e880eb"),14.87);
    }

    @Test
    void getSprintCost() {
        // Sprint 1 total hours = 35
        // Sprint 1 cost = 35 * 20 = 700
        Assertions.assertEquals(700.0,TrelloManager.getSprintCost(1));
    }
}