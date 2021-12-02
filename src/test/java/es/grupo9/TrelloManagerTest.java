package es.grupo9;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class TrelloManagerTest {

    TrelloManager trelloManager;

    @BeforeEach
    void setUp() {
        trelloManager = new TrelloManager(config.API_KEY, config.MY_TOKEN, config.BOARD_ID);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getFinishedSprintBacklog() throws IOException {
        // Primerio cartão do Sprint Backlog = 6197c87a5866b715bad2059e
        Assertions.assertEquals("6197c87a5866b715bad2059e",trelloManager.getFinishedSprintBacklog(1).get(0).getId());
    }

    @Test
    void getMeetings() throws IOException {
        // ID to primeiro cartão da lista Meetings do Sprint 1 = 616485eb23537a5ed11aec71
        Assertions.assertEquals("616485eb23537a5ed11aec71",trelloManager.getMeetings(1).get(0).getId());
    }


    @Test
    void getSprintCount() throws IOException {
        // Número de SPRINTS = 3
        Assertions.assertEquals(trelloManager.getSprintCount(),3);
    }

    @Test
    void getBoardListIdByName() throws IOException {
        // ID Lista "Sprints" da Board = 61606295191d043999a57bcb
        Assertions.assertEquals("61606295191d043999a57bcb", trelloManager.getBoardListIdByName("Sprints"));
    }

    @Test
    void getMemberIdByName() throws IOException {
        // Tatiana Member ID = 614dd7696ae49f2cea41608b
        Assertions.assertEquals("614dd7696ae49f2cea41608b", trelloManager.getMemberIdByName("Tatiana"));
    }

    @Test
    void getSprintHours() throws IOException {
        // Values for Sprint 1 = [35.0, 28.0, 700.0]
        Double[] expected = new Double[]{35.0, 28.0, 700.0};

        Assertions.assertArrayEquals(expected, trelloManager.getSprintHours(1));
    }

    @Test
    void getSprintHoursByMember() throws IOException {
        // Values for Tatiana, Sprint 1 = [10.0, 12.0, 200.0]
        Double[] expected = new Double[]{10.0, 12.0, 200.0};

        Assertions.assertArrayEquals(expected, trelloManager.getSprintHoursByMember("Tatiana",1));
    }

    @Test
    void getCardHours() throws IOException {
        // Card ID = 6160c5f03670208ff6030598
        // Values for card = [5.66, 8.0]
        Double[] expected = new Double[]{5.66, 8.0};

        Assertions.assertArrayEquals(expected, trelloManager.getCardHours("6160c5f03670208ff6030598"));
    }


    @Test
    void getCommittedActivities() throws IOException {
        // Values sprint 1 = [7.0, 35.0, 700.0]
        Double[] expected = new Double[]{7.0, 35.0, 700.0};

        Assertions.assertArrayEquals(expected, trelloManager.getCommittedActivities(1));
    }


    @Test
    void getNotCommittedActivities() throws IOException {
        // Values sprint 1 = [4.0, 7.0, 140.0]
        Double[] expected = new Double[]{4.0, 7.0, 140.0};

        Assertions.assertArrayEquals(expected, trelloManager.getNotCommittedActivities(1));
    }


    @Test
    void getCommittedActivitiesByMember() throws IOException {
        // Values Tatiana Sprint 1 = [4.0, 10.0, 200.0]
        Double[] expected = new Double[]{4.0, 10.0, 200.0};

        Assertions.assertArrayEquals(expected, trelloManager.getCommittedActivitiesByMember("Tatiana", 1));
    }

    @Test
    void getNotCommittedActivitiesByMember() throws IOException {
        // Values Tatiana Sprint 1 = [3.0, 5.0, 100.0]
        Double[] expected = new Double[]{3.0, 5.0, 100.0};

        Assertions.assertArrayEquals(expected, trelloManager.getNotCommittedActivitiesByMember("Tatiana", 1));

    }
}