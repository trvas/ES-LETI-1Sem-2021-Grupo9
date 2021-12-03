package es.grupo9;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UtilsTest {

    @Before
    public void SetUp() {
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
}







