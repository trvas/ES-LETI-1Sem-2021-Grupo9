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
        trelloManager = new TrelloManager("APIKEY", "TOKEN", "BOARD");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getFinishedSprintBacklog() throws IOException {
        // Primeiro cartão do Sprint Backlog = 6197c87a5866b715bad2059e
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
    void getMemberIdByName() throws IOException {
        // Tatiana Member ID = 614dd7696ae49f2cea41608b
        Assertions.assertEquals("614dd7696ae49f2cea41608b", trelloManager.getMemberIdByName("Tatiana"));
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
        Double[] expected = new Double[]{5.659999999999999, 8.0};

        Assertions.assertArrayEquals(expected, trelloManager.getCardHours("6160c5f03670208ff6030598"));
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

    @Test
    void getSprintDate() throws IOException {
        String expected = "Data de início: 12/10/2021 \nData de fim: 26/10/2021 ";
        Assertions.assertEquals(expected, trelloManager.getSprintDate(1));
    }

    @Test
    void getProjectName() {
        Assertions.assertEquals("ES-LETI-1Sem-2021-Grupo9", trelloManager.getProjectName());
    }

    @Test
    void getBeginningDate() throws IOException {
        Assertions.assertEquals(" 12/10/2021 ", trelloManager.getBeginningDate());
    }
}