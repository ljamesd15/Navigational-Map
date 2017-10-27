package test;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import model.Graph;
import model.Campus;
import model.Location;
import model.Point;

public class CampusTest {

	// Starting info
	private Point testPoint = new Point(331.0, 4.0);
	private Point testPoint2 = new Point(2015.0, 2017.0);
	
	private String shortName = "ABC";
	private String shortName2 = "123";
	
	private String longName = "Easy as do re mi";
	private String longName2 = "Its ABC 123";
	
	private Location loc1 = new Location(shortName, longName, testPoint);
	private Location loc2 = new Location(shortName2, longName2, testPoint2);
	private Location loc3 = new Location(shortName, longName, testPoint);
	
	private Graph<Location, Double> testGraph = new Graph<Location, Double>();
	private Set<Location> buildings = new HashSet<Location>();
	private Campus testCampus;
	
	// Initialize info
	@Before
	public void intialise() {
		buildings.add(loc1);
		buildings.add(loc2);
		buildings.add(loc3);
		
		testCampus = new Campus(testGraph, buildings);
	}
	
	///////////////////////////////////////////////////////////////////////////
	// Constructor tests //////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	
	// Testing constructor since it does not rely on any other methods.
	@Test
	public void testConstructor() {		
		new Campus(testGraph, buildings);
	}
	
	///////////////////////////////////////////////////////////////////////////
	// getter tests ///////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	
	// Testing getBuildings as it does not rely on other methods.
	@Test
	public void testGetBuildings() {
		// Clear info
		testGraph.clear();
		buildings.clear();
		
		// Re-initialize info
		Graph<Location, Double> testGraph = new Graph<Location, Double>();
		Set<Location> buildings = new HashSet<Location>();
		
		buildings.add(loc1);
		buildings.add(loc2);
		buildings.add(loc3);
		
		Campus testCampus = new Campus(testGraph, buildings);
		
		// Test method
		assertEquals(buildings, testCampus.getBuildings());
	}
	
	// Test getBuilding(String) since we know that locations are added 
	// correctly now.
	@Test
	public void testGetBuildingString() {
		assertEquals(loc1, testCampus.getBuilding(shortName));
		assertEquals(loc2, testCampus.getBuilding(shortName2));
		assertEquals(loc3, testCampus.getBuilding(shortName));
	}
	
	// Test getBuilding(Point2D) since we know that locations are added
	// correctly now.
	@Test
	public void testGetBuldingPoint() {
		assertEquals(loc1, testCampus.getBuilding(testPoint));
		assertEquals(loc2, testCampus.getBuilding(testPoint2));
	}
	
	///////////////////////////////////////////////////////////////////////////
	// Other tests ////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	
	// The methods shortestRouteFromTo and findDirection are both tested 
	// through specification testing.
	
	// Testing toString since it does not rely on any other methods.
	@Test
	public void testToString() {
		String result = testGraph.toString();
		for (Location loc : buildings) {
			result += '\n' + loc.toString();
		}
		
		assertEquals(result, testCampus.toString());
	}
}
