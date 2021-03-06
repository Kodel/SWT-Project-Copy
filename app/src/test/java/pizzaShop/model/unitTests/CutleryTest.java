package pizzaShop.model.unitTests;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import static org.salespointframework.core.Currencies.EURO;

import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;

import pizzaShop.model.OrderSystem.Cutlery;

public class CutleryTest {
	LocalDateTime time;
	Cutlery c;
	
	@Before
	public void setUp() throws Exception {
		time = LocalDateTime.now();
		c = new Cutlery(Money.of(15.0, EURO),time);
		c.getDate();
	}

	@Test
	public void testgetDate() {
		assertEquals(c.getDate(),time.plusDays(28));
		
	}
	
	@Test
	public void testsetDate()
	{
		c.setDate(time);
		assertEquals(c.getDate(),time.plusDays(28));
	}

}
