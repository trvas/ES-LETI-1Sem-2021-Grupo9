

import com.example.demo.Utils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;

public class UtilsTest {









        @Before
        public void SetUp() throws Exception {



        }

        @Test
        public void testgetcustowithvalue
                () {
            Assert.assertEquals(1, Utils.getCusto(1, 40) );

        }

        @Test
        public void testgetcustowithoutvalue

            () {
            Assert.assertEquals(20, Utils.getCusto(1));


        }

    }








