
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;

public class CalculoTest {

	CalculatingCost c;

	@Before

	public void SetUp() throws Exception {

		c = new CalculatingCost();

	}

	@Test
	public void testHoras() {
		Assert.assertEquals(1, c.getHoras(0));

	}

	@Test
	public void tesCusto() {
		Assert.assertEquals(20, c.getCusto());

	}

}
