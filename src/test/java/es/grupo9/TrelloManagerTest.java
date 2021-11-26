package es.grupo9;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class TrelloManagerTest {

    @BeforeEach
    void setUp() {
        TrelloManager trelloManager = new TrelloManager(config.API_KEY, config.MY_TOKEN, config.BOARD_ID);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getFinishedSprintBacklog() throws IOException {
        // Primerio cartão do Sprint Backlog = 6197c87a5866b715bad2059e
        Assertions.assertEquals("6197c87a5866b715bad2059e",TrelloManager.getFinishedSprintBacklog(1).get(0).getId());
    }

    @Test
    void getMeetings() throws IOException {
        // ID to primeiro cartão da lista Meetings = 616485eb23537a5ed11aec71
        Assertions.assertEquals(TrelloManager.getMeetings().get(1).get(0).getId(),"616485eb23537a5ed11aec71");
    }

    @Test
    void getCardHours() {
        // Card ID = 619c4c7a39589204c105f165 ("Criação da interface gráfica") , hours = 14.87
        Assertions.assertEquals(TrelloManager.getCardHours("619c4c7a39589204c105f165"),14.87);
    }

    @Test
    void getEstimateCardHours() {
        // Card ID = 619c4c7a39589204c105f165 ("Criação da interface gráfica") , estimated hours = 14
        Assertions.assertEquals(TrelloManager.getEstimateCardHours("619c4c7a39589204c105f165"), 14);
    }

    @Test
    void getSprintHours() throws IOException {
        // Sprint 1 total hours = 35
        Assertions.assertEquals(35.0, TrelloManager.getSprintHours(1));
    }

    @Test
    void getEstimateSprintHours() throws IOException {
        // Sprint 1 estimated hours = 28
        Assertions.assertEquals(28.0, TrelloManager.getEstimateSprintHours(1));
    }

    @Test
    void getSprintCost() throws IOException {
        // Sprint 1 cost = 35 h * 20 = 700
        Assertions.assertEquals(700.0,TrelloManager.getSprintCost(1));
    }

    @Test
    void getSprintHoursByMember() throws IOException {
        // Tatiana Sprint 1 total hours = 10.0
        Assertions.assertEquals(10.0, TrelloManager.getSprintHoursByMember("Tatiana",1));
    }


    @Test
    void getEstimateSprintHoursByMember() throws IOException {
        // Tatiana Sprint 1 estimated hours = 12.0
        Assertions.assertEquals(12.0, TrelloManager.getEstimateSprintHoursByMember("Tatiana",1));
    }

    @Test
    void getSprintCostByMember() throws IOException {
        // Tatiana Sprint 1 cost = 200.0
        Assertions.assertEquals(200.0, TrelloManager.getSprintCostByMember("Tatiana",1));
    }

    @Test
    void getSprintCount() throws IOException {
        // Número de SPRINTS = 3
        Assertions.assertEquals(TrelloManager.getSprintCount(),3);
    }

    @Test
    void getBoardListIdByName() throws IOException {
        // ID Lista "Sprints" da Board = 61606295191d043999a57bcb
        Assertions.assertEquals("61606295191d043999a57bcb", TrelloManager.getBoardListIdByName("Sprints"));
    }

    @Test
    void getMemberIdByName() throws IOException {
        // Tatiana Member ID = 614dd7696ae49f2cea41608b
        Assertions.assertEquals("614dd7696ae49f2cea41608b", TrelloManager.getMemberIdByName("Tatiana"));
    }

}