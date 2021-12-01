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
    void getCardHours() {
        // Card ID = 619c4c7a39589204c105f165 ("Criação da interface gráfica") , hours = 14.87
        Assertions.assertEquals(trelloManager.getCardHours("619c4c7a39589204c105f165"),14.87);
    }

    @Test
    void getEstimateCardHours() {
        // Card ID = 619c4c7a39589204c105f165 ("Criação da interface gráfica") , estimated hours = 14
        Assertions.assertEquals(trelloManager.getEstimateCardHours("619c4c7a39589204c105f165"), 14);
    }

    @Test
    void getSprintHours() throws IOException {
        // Sprint 1 total hours = 35
        Assertions.assertEquals(35.0, trelloManager.getSprintHours(1));
    }

    @Test
    void getEstimateSprintHours() throws IOException {
        // Sprint 1 estimated hours = 28
        Assertions.assertEquals(28.0, trelloManager.getEstimateSprintHours(1));
    }

    @Test
    void getSprintCost() throws IOException {
        // Sprint 1 cost = 35 h * 20 = 700
        Assertions.assertEquals(700.0,trelloManager.getSprintCost(1));
    }

    @Test
    void getSprintHoursByMember() throws IOException {
        // Tatiana Sprint 1 total hours = 10.0
        Assertions.assertEquals(10.0, trelloManager.getSprintHoursByMember("Tatiana",1));
    }


    @Test
    void getEstimateSprintHoursByMember() throws IOException {
        // Tatiana Sprint 1 estimated hours = 12.0
        Assertions.assertEquals(12.0, trelloManager.getEstimateSprintHoursByMember("Tatiana",1));
    }

    @Test
    void getSprintCostByMember() throws IOException {
        // Tatiana Sprint 1 cost = 200.0
        Assertions.assertEquals(200.0, trelloManager.getSprintCostByMember("Tatiana",1));
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
    void getCommittedActivities() throws IOException {
        // To update with new values
        // Current values = [13, 85.64, 1712.8]
        List<Object> expected = new ArrayList<>();
        expected.add(0, 13);
        expected.add(1, 85.64);
        expected.add(2, 1712.8);

        Assertions.assertEquals(expected, trelloManager.getCommittedActivities());
    }

    @Test
    void getNotCommittedActivities() throws IOException {
        // To update with new
        // Current values = [11, 18.43, 368.6]
        List<Object> expected = new ArrayList<>();
        expected.add(0, 11);
        expected.add(1, 18.43);
        expected.add(2, 368.6);

        Assertions.assertEquals(expected, trelloManager.getNotCommittedActivities());
    }

    @Test
    void getCommittedActivitiesByMember() throws IOException {
        // To update with new values
        // Current values Tatiana = [7, 22.14, 442.8]
        List<Object> expected = new ArrayList<>();
        expected.add(0, 7);
        expected.add(1, 22.14);
        expected.add(2, 442.8);

        Assertions.assertEquals(expected, trelloManager.getCommittedActivitiesByMember("Tatiana"));

    }

    @Test
    void getNotCommittedActivitiesByMember() throws IOException {
        // To update with new values
        // Current values Tatiana = [10, 16.43, 328.6]
        List<Object> expected = new ArrayList<>();
        expected.add(0, 10);
        expected.add(1, 16.43);
        expected.add(2, 328.6);

        Assertions.assertEquals(expected, trelloManager.getNotCommittedActivitiesByMember("Tatiana"));

    }
}