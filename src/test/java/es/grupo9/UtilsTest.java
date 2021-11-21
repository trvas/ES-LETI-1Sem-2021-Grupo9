package es.grupo9;

import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UtilsTest {

        @Before
        public void SetUp() throws Exception {
        }

        @Test
        public void testGetCostWithValue() {
            Assertions.assertEquals(1, Utils.getCost(1, 40) );
        }

        @Test
        public void testGetCostWithoutValue() {
            Assertions.assertEquals(20, Utils.getCost(1));
        }

    }








