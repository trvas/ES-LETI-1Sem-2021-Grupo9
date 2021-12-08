package es.grupo9;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class UtilsTest {

    GitManager gitManager;
    TrelloManager trelloManager;

    @BeforeEach
    public void SetUp() throws IOException {
        gitManager = new GitManager("AUTH", "Henrique-deSousa", "test_repo");
        trelloManager = new TrelloManager("APIKEY", "TOKEN", "614de300aa6df33863299b6c");
    }

    @Test
    public void setPrice() {
        Utils.setPrice(40);

        Assertions.assertEquals(40, Utils.getCost(1.0) );
    }

    @Test
    public void getCost() {
        Assertions.assertEquals(20, Utils.getCost(1.0));
    }

    @Test
    void getSum() {
        Double[] array = new Double[]{0.1,2.0,0.1};
        Assertions.assertEquals(2.2,Utils.getSum(array));
    }

    @Test
    void exportCSV() throws Exception {
        Utils.exportCSV(trelloManager, gitManager);
        Assertions.assertTrue(new File("info.csv").exists());
    }
}