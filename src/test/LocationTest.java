package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import model.Location;
import model.Point;

public class LocationTest {
	
	// Starting info
	private Point testPoint = new Point(331.0, 4.0);
	private Point testPoint2 = new Point(331.0, 4.0);
	
	private String shortName = "ABC";
	private String shortName2 = "123";
	
	private String longName = "Easy as do re mi";
	private String longName2 = "Its ABC 123";

	///////////////////////////////////////////////////////////////////////////
	// Constructor tests //////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	
	// Testing 3 arg constructor since it relies on no other methods.
	@Test
	public void testConstructorThreeargs() {
		new Location(shortName, longName, testPoint);
		new Location(shortName2, longName, testPoint2);
	}
	
	// Testing one arg constructor since it relies on the 3 arg constructor.
	@Test
	public void testConstructorOneArg() {
		new Location(testPoint);
		new Location(testPoint2);
	}
	
	///////////////////////////////////////////////////////////////////////////
	// Equals & hash code tests ///////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	
	// Testing equals since it relies on no other methods.
	@Test
	public void testEquals() {
		// Creating locations (one arg)
		Location loc1 = new Location(testPoint);
		Location loc2 = new Location(testPoint);
		Location loc3 = new Location(testPoint2);
		
		// Testing equals, first transitivity, then reflexivity, and finally 
		// symmetry.
		assertTrue(loc1.equals(loc2));
		assertTrue(loc2.equals(loc3));
		assertTrue(loc3.equals(loc1));
		
		assertTrue(loc1.equals(loc1));
		assertTrue(loc2.equals(loc2));
		assertTrue(loc3.equals(loc3));
		
		assertTrue(loc2.equals(loc1));
		assertTrue(loc3.equals(loc2));
		assertTrue(loc1.equals(loc3));
		
		
		// Creating locations (three args)
		Location loc4 = new Location(shortName, longName, testPoint);
		Location loc5 = new Location(shortName2, longName2, testPoint);
		Location loc6 = new Location(shortName, longName, testPoint2);
		
		assertTrue(loc4.equals(loc6));
		assertFalse(loc4.equals(loc5));
	}
	
	// Testing hash code since we just tested equals.
	@Test
	public void testHashCode() {
		// One arg locations
		Location loc1 = new Location(testPoint);
		Location loc2 = new Location(testPoint);
		Location loc3 = new Location(testPoint2);
		
		// Three arg locations
		Location loc4 = new Location(shortName, longName, testPoint);
		Location loc6 = new Location(shortName, longName, testPoint2);
		
		assertEquals(loc1.hashCode(), loc2.hashCode());
		assertEquals(loc3.hashCode(), loc1.hashCode());
		
		assertEquals(loc4.hashCode(), loc6.hashCode());
	}
	
	///////////////////////////////////////////////////////////////////////////
	// toString & other tests /////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	
	// Testing toString since it relies on no other methods.
	@Test
	public void testToString() {
		Location loc4 = new Location(shortName, longName, testPoint);
		Location loc5 = new Location(shortName2, longName2, testPoint);
		Location loc6 = new Location(shortName, longName, testPoint2);
		
		assertEquals(shortName + ": " + longName, loc4.toString());
		assertEquals(shortName2 + ": " + longName2, loc5.toString());
		assertEquals(shortName + ": " + longName, loc6.toString());
	}
	
	// Test comparator
	@Test
	public void testCompareTo() {
		Location loc4 = new Location(shortName, longName, testPoint);
		Location loc5 = new Location(shortName2, longName2, testPoint);
		Location loc6 = new Location(shortName, longName, testPoint2);
		
		assertTrue(loc4.compareTo(loc5) > 0);
		assertTrue(loc5.compareTo(loc6) < 0);
		assertTrue(loc4.compareTo(loc6) == 0);
	}
}
