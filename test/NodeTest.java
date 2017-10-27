package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import model.Node;

public class NodeTest {
	
	@Before
	/**
    * Checks that assertions are enabled. If they are not, an error message
    * is printed, and the system exits.
    */
	public void checkAssertsEnabled() {
		try {
			assert false;

			// assertions are not enabled
			System.err.println("Java Asserts are not currently enabled. Follow "
					+ "homework writeup instructions to enable asserts on all "
					+ "JUnit Test files.");
			System.exit(1);

		} catch (AssertionError e) {
			// do nothing
			// assertions are enabled
		}
	}
	
///////////////////////////////////////////////////////////////////////////////
/// Constructor Tests /////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////
	
	// First test the three arg constructor because from implementing I know 
	// the two arg calls the three arg.
	
	@Test
	public void testConstructorThreeArg() {
		new Node<String>("test", 0.0, null);
		new Node<Double>(17.7, 0.1, new Node<Double>(15.2, 10.1));
		new Node<Integer>(10, 19.1, null);
		try {
			new Node<String>("exception", -1000000000000000.0, null);
			fail();
		} catch (IllegalArgumentException e) {
			String expected = "The weight of this node must be non-negative.";
			assertEquals(expected, e.getMessage());
		}
	}
	
	@Test
	public void testConstructorTwoArg() {
		new Node<String>("test", 0.0);
		new Node<Double>(6.0, 2.3);
		new Node<Integer>(15, 100000000000000000000.0);
		try {
			new Node<String>("exception", -0.1);
			fail();
		} catch (IllegalArgumentException e) {
			String expected = "The weight of this node must be non-negative.";
			assertEquals(expected, e.getMessage());
		}
	}
	
///////////////////////////////////////////////////////////////////////////////
/// Misc. tests ///////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////	
	
	// Testing equals and hash code so that we can use them later on in 
	// future tests.
	
	@Test
	public void testEquals() {
		Node<String> stringNode1 = new Node<String>("test", 0.0, null);
		Node<String> stringNode2 = new Node<String>("test", 2.0, stringNode1);
		Node<String> stringNode3 = new Node<String>("test", 0.0, stringNode2);
		Node<String> stringNode4 = new Node<String>("test4", 0.0, null);
		
		assertTrue(stringNode1.equals(stringNode1));
		
		assertTrue(stringNode1.equals(stringNode2));
		assertTrue(stringNode2.equals(stringNode1));
		
		assertTrue(stringNode2.equals(stringNode3));
		assertTrue(stringNode1.equals(stringNode3));
		
		assertFalse(stringNode4.equals(stringNode1));
	}
	
	@Test
	public void testHashCode() {
		Node<String> stringNode1 = new Node<String>("test", 0.0, null);
		Node<String> stringNode2 = new Node<String>("test", 2.0, stringNode1);
		Node<String> stringNode3 = new Node<String>("test", 0.0, stringNode2);
		
		assertTrue(stringNode1.hashCode() == stringNode2.hashCode());
		assertTrue(stringNode2.hashCode() == stringNode3.hashCode());
		assertTrue(stringNode3.hashCode() == stringNode1.hashCode());
	}
	
	@Test 
	public void testToString() {
		Node<String> stringNode = new Node<String>("test", 0.0, null);
		Node<Double> doubleNode = new Node<Double>(10.2, 2.0, null);
		Node<Integer> intNode = new Node<Integer>(15, 0.0, null);
		
		assertEquals("test", stringNode.toString());
		assertEquals("10.2", doubleNode.toString());
		assertEquals("15", intNode.toString());
	}

///////////////////////////////////////////////////////////////////////////////
/// Getter method tests ///////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////
	
	// Finially test all the getter methods with different generics.
	
	@Test
	public void testGetName() {
		Node<String> stringNode = new Node<String>("test", 0.0);
		assertEquals("test", stringNode.getNodeName());
		
		Node<Double> doubleNode = new Node<Double>(-1.0, 10.0);
		assertEquals(0, Double.compare(-1.0, doubleNode.getNodeName()));
		
		Node<Integer> intNode = new Node<Integer>(-14, 20.0);
		assertEquals(0, Integer.compare(-14, intNode.getNodeName()));
	}
	
	@Test
	public void testPathWeight() {
		Node<String> stringNode = new Node<String>("test", 0.0);
		assertEquals(0, Double.compare(0.0, stringNode.getPathWeight()));
		
		Node<Double> doubleNode = new Node<Double>(-1.0, 10.0);
		assertEquals(0, Double.compare(10.0, doubleNode.getPathWeight()));
		
		Node<Integer> intNode = new Node<Integer>(-14, 20.0);
		assertEquals(0, Double.compare(20.0, intNode.getPathWeight()));
	}
	
	@Test
	public void testGetPrevNode() {
		Node<String> stringNode1 = new Node<String>("test1", 0.0, null);
		Node<String> stringNode2 = new Node<String>("test2", 2.0, stringNode1);
		assertEquals(null, stringNode1.getPrevNode());
		assertEquals(stringNode1, stringNode2.getPrevNode());
		
		Node<Double> doubleNode1 = new Node<Double>(15.2, 10.1);
		Node<Double> doubleNode2 = new Node<Double>(17.7, 0.1, doubleNode1);
		assertEquals(null, doubleNode1.getPrevNode());
		assertEquals(doubleNode1, doubleNode2.getPrevNode());
		
		Node<Integer> intNode1 = new Node<Integer>(10, 12.3, null);
		Node<Integer> intNode2 = new Node<Integer>(10, 19.1, intNode1);
		assertEquals(null, intNode1.getPrevNode());
		assertEquals(intNode1, intNode2.getPrevNode());
	}
	
}
