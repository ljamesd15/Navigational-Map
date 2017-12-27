package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import model.Connection;

public class ConnectionTest {
	
	@Test
	public void testConstructor() {
		new Connection<String, String>("testChar1", "testChar2", "testLabel");
	}
	
	@Test
	public void testGetFrom() {
		Connection<String, String> con = new Connection<String, String>
									("testChar1", "testChar2", "testLabel");
		assertEquals("testChar1", con.getFrom());
	}
	
	@Test
	public void testGetTo() {
		Connection<String, String> con = new Connection<String, String>
									("testChar1", "testChar2", "testLabel");
		assertEquals("testChar2", con.getTo());
	}
	
	@Test
	public void testGetLabel() {
		Connection<String, String> con = new Connection<String, String>
									("testChar1", "testChar2", "testLabel");
		assertEquals("testLabel", con.getLabel());
	}
	
	@Test
	public void testHashCode() {
		Connection<String, String> con1 = new Connection<String, String>
									("testChar1", "testChar2", "testLabel");
		Connection<String, String> con2 = new Connection<String, String>
									("testChar1", "testChar2", "testLabel");
		assertTrue(con1.hashCode() == con2.hashCode());
	}
	
	@Test
	public void testEquals() {
		Connection<String, String> con1 = new Connection<String, String>
									("testChar1", "testChar2", "testLabel");
		Connection<String, String> con2 = new Connection<String, String>
									("testChar1", "testChar2", "testLabel");
		assertTrue(con1.equals(con2));
	}
	
	@Test
	public void testToString() {
		Connection<String, String> con1 = new Connection<String, String>
									("testChar1", "testChar2", "testLabel");
		Connection<String, String> con2 = new Connection<String, String>
									("testChar1", "testChar2", "testLabel");
		
		assertEquals("testChar1 testChar2 testLabel", con1.toString());
		assertEquals(con1.toString(), con2.toString());
	}
}
