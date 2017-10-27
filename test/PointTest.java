package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import model.Point;

public class PointTest {
	
	///////////////////////////////////////////////////////////////////////////
	///// Constructor test ////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	
	// Testing constructor first.
	@Test
	public void testConstructor() {
		new Point(0.0, 0.0);
		new Point(-1.0, 1.5);
		new Point(12.3, -15.2);
		new Point(12.5, 16.3);
		new Point(-5.6, -9.7);
	}
	
	///////////////////////////////////////////////////////////////////////////
	///// Other tests /////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	
	// getX() does not rely on any other methods.
	@Test
	public void testGetX() {
		Point p1 = new Point(0.0, 0.0);
		Point p2 = new Point(12.5, 16.3);
		Point p3 = new Point(-5.6, -9.7);
		
		assertTrue(0.0 == p1.getX());
		assertTrue(12.5 == p2.getX());
		assertTrue(-5.6 == p3.getX());
	}
	
	// getX() does not rely on any other methods.
	@Test
	public void testGetY() {
		Point p1 = new Point(0.0, 0.0);
		Point p2 = new Point(12.5, 16.3);
		Point p3 = new Point(-5.6, -9.7);
		
		assertTrue(0.0 == p1.getY());
		assertTrue(16.3 == p2.getY());
		assertTrue(-9.7 == p3.getY());
	}
	
	// Test equals since the getters have been tested.
	@Test
	public void testEquals() {
		Point p1 = new Point(0.0, 0.0);
		Point p2A = new Point(12.5, 16.3);
		Point p2B = new Point(12.5, 16.3);
		
		assertTrue(p2A.equals(p2A));
		assertTrue(p2A.equals(p2B));
		assertTrue(p2B.equals(p2A));
		assertFalse(p2B.equals(p1));
	}
	
	// Testing hashCode now that equals has been tested
	@Test
	public void testHashCode() {
		Point p2A = new Point(12.5, 16.3);
		Point p2B = new Point(12.5, 16.3);
		
		Point p3A = new Point(-5.6, -9.7);
		Point p3B = new Point(-5.6, -9.7);
		
		assertEquals(p2A.hashCode(), p2B.hashCode());
		assertEquals(p3A.hashCode(), p3B.hashCode());
	}
}
