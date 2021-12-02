package es.grupo9;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        Double[] expected = new Double[3];

        expected[0] = 35.0;
        expected[1] = 28.0;
        expected[2] = 700.0;

        Assertions.assertEquals(expected[0], trelloManager.getSprintHours(1)[0]);
        Assertions.assertEquals(expected[1], trelloManager.getSprintHours(1)[1]);
        Assertions.assertEquals(expected[2], trelloManager.getSprintHours(1)[2]);
    }

    @Test
    void getSprintHoursByMember() throws IOException {
        // Values for Tatiana, Sprint 1 = [10.0, 12.0, 200.0]
        Double[] expected = new Double[3];

        expected[0] = 10.0;
        expected[1] = 12.0;
        expected[2] = 200.0;

        Assertions.assertEquals(expected[0], trelloManager.getSprintHoursByMember(1, "Tatiana")[0]);
        Assertions.assertEquals(expected[1], trelloManager.getSprintHoursByMember(1, "Tatiana")[1]);
        Assertions.assertEquals(expected[2], trelloManager.getSprintHoursByMember(1, "Tatiana")[2]);
    }

    @Test
    void getCardHours() throws IOException {
        // Card ID = 6160c5f03670208ff6030598
        // Values for card = [5.66, 8.0]
        Double[] expected = new Double[2];

        expected[0] = 5.66;
        expected[1] = 8.0;

        Assertions.assertEquals(expected[0], trelloManager.getCardHours("6160c5f03670208ff6030598")[0]);
        Assertions.assertEquals(expected[1], trelloManager.getCardHours("6160c5f03670208ff6030598")[1]);
    }


    @Test
    void getCommittedActivities() throws IOException {
        // To update with new values
        // Current values = [14.0, 91.89, 1837.8];

        Double[] expected = new Double[3];
        expected[0] = 14.0;
        expected[1] = 91.89;
        expected[2] = 1837.8;

        Assertions.assertArrayEquals(expected, trelloManager.getCommittedActivities());
    }


    @Test
    void getNotCommittedActivities() throws IOException {
        // To update with new values
        // Current values = [11, 18.43, 368.6]

        Double[] expected = new Double[3];
        expected[0] = 11.0;
        expected[1] = 18.43;
        expected[2] = 368.6;

        Assertions.assertArrayEquals(expected, trelloManager.getNotCommittedActivities());
    }


    @Test
    void getCommittedActivitiesByMember() throws IOException {
        // To update with new values
        // Current values Tatiana = [8.0, 23.39, 467.8]
        Double[] expected = new Double[3];
        expected[0] = 8.0;
        expected[1] = 23.39;
        expected[2] = 467.8;

        Assertions.assertArrayEquals(expected, trelloManager.getCommittedActivitiesByMember("Tatiana"));
    }

    @Test
    void getNotCommittedActivitiesByMember() throws IOException {
        // To update with new values
        // Current values Tatiana = [10.0, 16.43, 328.6]
        Double[] expected = new Double[3];
        expected[0] = 10.0;
        expected[1] = 16.43;
        expected[2] = 328.6;

        Assertions.assertArrayEquals(expected, trelloManager.getNotCommittedActivitiesByMember("Tatiana"));

    }
}