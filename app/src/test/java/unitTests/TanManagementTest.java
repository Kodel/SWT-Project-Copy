package unitTests;

import static org.junit.Assert.*;

import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import pizzaShop.model.ManagementSystem.Tan_Management.Tan;
import pizzaShop.model.ManagementSystem.Tan_Management.TanManagement;
import pizzaShop.model.ManagementSystem.Tan_Management.TanStatus;

public class TanManagementTest {
	
	TanManagement TanMan;
	
	@Before
	public void setUp()  
	{
	
		TanMan = new TanManagement();
	}

	@Test
	public void test1() {
		
		String testTelephoneNumber = "123456";
		
		Tan newTan = TanMan.generateNewTan(testTelephoneNumber);
		
		TanMan.confirmTan(newTan);
		
		assertEquals(newTan, TanMan.getTan(testTelephoneNumber));
		
		assertEquals(testTelephoneNumber, TanMan.getTelephoneNumber(newTan));
		
		Tan newTan2 = TanMan.getTan("222222");
		
		assertEquals(TanMan.NOT_FOUND_TAN, newTan2);
	}
	
	@Test
	public void checkTanTest() {
		
		String testTelephoneNumber = "123456";
		
		Tan newTan = TanMan.generateNewTan(testTelephoneNumber);
		
		TanMan.confirmTan(newTan);
		
		assertTrue(TanMan.checkTan(newTan, testTelephoneNumber)); //check Tan either takes a Tan as a the first Parameter or a String 
		
		assertTrue(TanMan.checkTan(newTan.getTanNumber(), testTelephoneNumber));
		
		assertFalse(TanMan.checkTan(newTan.getTanNumber(), "222222"));
		
		assertFalse(TanMan.checkTan("00000", "123456"));
					
	}
	
	@Test
	public void invalidateTanTest() {
		
		String testTelephoneNumber = "123456";
		
		Tan newTan = TanMan.generateNewTan(testTelephoneNumber);
		
		Iterable<Entry<Tan,String>> test = TanMan.getAllNotConfirmedTans();
		
		for(Entry<Tan, String> entry : test)
		{
			System.out.println(entry.getValue());
			System.out.println(entry.getKey().getTanNumber());
			System.out.println(entry.getKey().getStatus());
		}
		
		System.out.println("end");
		
		TanMan.confirmTan(newTan);
		
		Iterable<Entry<Tan,String>> test3 = TanMan.getAllTans();
		
		for(Entry<Tan, String> entry : test3)
		{
			System.out.println(entry.getValue());
			System.out.println(entry.getKey().getTanNumber());
			System.out.println(entry.getKey().getStatus());
		}
		
		System.out.println("end");
		
		//TanMan.invalidateTan(newTan);
		
		Tan newTan2 = TanMan.generateNewTan(testTelephoneNumber);
		
		TanMan.confirmTan(newTan2);
		
		Iterable<Entry<Tan,String>> test2 = TanMan.getAllTans();
		
		for(Entry<Tan, String> entry : test2)
		{
			System.out.println(entry.getValue());
			System.out.println(entry.getKey().getTanNumber());
			System.out.println(entry.getKey().getStatus());
		}
		
		System.out.println("end");
		
		assertEquals(TanStatus.USED,newTan.getStatus());
				
	}
	
	@Test
	public void deleteNotConfirmedTanTest() {
		
		String testTelephoneNumber = "123456";
		
		Tan newTan = TanMan.generateNewTan(testTelephoneNumber);
		
		assertTrue(TanMan.deleteNotConfirmedTan(newTan));
		
				
	}
	
	

}
