package test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;
import model.Connection;
import model.Graph;
import exception.MalformedDataException;

public class GraphTest {

	private Graph<String, String> graph1;
	private Graph<String, String> graph2;
	private Graph<String, String> graph3;
	private Graph<String, String> graph4;
	private Graph<String, String> graph5;
	
	private Set<String> set1 = new TreeSet<>(Arrays.asList("Captain America", 
			"Luke Cage", "Hulk"));
	private Set<String> set2 = new TreeSet<>(Arrays.asList("Captain America", 
			"Captain Marvel", "Iron Man", "Winter Soldier", "Thor", "Hulk"));
	private Set<String> set3 = new TreeSet<>(Arrays.asList("Hulk", "Daredevil", 
			"Spider-Man", "Thor"));
	private Set<String> set4 = new TreeSet<>(Arrays.asList("Captain America", 
			"Luke Cage", "Hulk", "Captain Marvel", "Iron Man", 
			"Winter Soldier", "Thor", "Daredevil", "Spider-Man"));
	private Set<String> set5 = new TreeSet<>(Arrays.asList("", "test"));

	// lists for testing connections
	private String[] list1 = new String[] 
			{ "Captain America", "Luke Cage", "Age of Ultron", 
			"Captain America", "Hulk", "Age of Ultron",
			"Hulk", "Luke Cage", "Age of Ultron" };
	// size = 9

	private String[] list2 = new String[] 
			{ "Captain Marvel", "Iron Man", "Civil War", 
			"Winter Soldier", "Iron Man", "Civil War",
			"Captain America", "Thor", "Axis", 
			"Hulk", "Thor", "Axis" };
	// Size = 12

	private String[] list3 = new String[] 
			{ "Daredevil", "Spider-Man", "Evolutionary War", 
			"Thor", "Spider-Man", "Evolutionary War",
			"Thor", "Daredevil", "Evolutionary War", 
			"Daredevil", "Spider-Man", "Infinity War", 
			"Hulk", "Spider-Man", "Infinity War" };
	// Size = 15

	private String[] list4 = new String[] 
			{ "Captain America", "Luke Cage", "Age of Ultron", 
			"Captain America", "Hulk", "Age of Ultron",
			"Hulk", "Luke Cage", "Age of Ultron", 
			"Captain Marvel", "Iron Man", "Civil War", 
			"Winter Soldier", "Iron Man", "Civil War",
			"Captain America", "Thor", "Axis", 
			"Hulk", "Thor", "Axis",
			"Daredevil", "Spider-Man", "Evolutionary War", 
			"Thor", "Spider-Man", "Evolutionary War", 
			"Thor", "Daredevil", "Evolutionary War", 
			"Daredevil", "Spider-Man", "Infinity War",
			"Hulk", "Spider-Man", "Infinity War" };
	// Size = 36
	
	private String[] list5 = new String[]
			{"", "test", "testTitle",
			"", "", "testTitle",
			"", "", ""};
	// Size = 9

	/**
	 * checks that Java asserts are enabled, and exits if not
	 */
	@Before
	public void testAssertsEnabled() {
		CheckAsserts.checkAssertsEnabled();
	}

	// Making sure all the graph objects are back to there original values.
	@Before
	public void setGraphsToOriginalValues() {
		graph1 = new Graph<String, String>();
		// For loop is hard coded to list1.length / 3 since I know the length.
		for (int i = 0; i < 3; i++) {
			graph1.addConnection(list1[i * 3], list1[i * 3 + 1], list1[i * 3 + 2]);
		}

		graph2 = new Graph<String, String>();
		// For loop is hard coded to list2.length / 3 since I know the length.
		for (int i = 0; i < 4; i++) {
			graph2.addConnection(list2[i * 3], list2[i * 3 + 1],
					list2[i * 3 + 2]);
		}

		graph3 = new Graph<String , String>();
		// For loop is hard coded to list3.length / 3 since I know the length.
		for (int i = 0; i < 5; i++) {
			graph3.addConnection(list3[i * 3], list3[i * 3 + 1],
					list3[i * 3 + 2]);
		}
		
		graph4 = new Graph<String, String>();
		// For loop is hard coded to list4.length / 3 since I know the length.
		for (int i = 0; i < 12; i++) {
			graph4.addConnection(list4[i * 3], list4[i * 3 + 1],
					list4[i * 3 + 2]);
		}

		graph5 = new Graph<String, String>();
		// For loop is hard coded to list5.length / 3 since I know the length.
		for (int i = 0; i < 3; i++) {
			graph5.addConnection(list5[i * 3], list5[i * 3 + 1],
					list5[i * 3 + 2]);
		}
	}
    
	///////////////////////////////////////////////////////////////////////////
	// Constructor tests //////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testNoArgConstructor() {
		new Graph<String, String>();
	}
	
	@Test
	public void testOneArgConstructor() {
		new Graph<String, String>(set1);
		new Graph<String, String>(set2);
		new Graph<String, String>(set3);
		new Graph<String, String>(set4);
		new Graph<String, String>(set5);
	}
	
	@Test
	public void testTwoArgConstructor() {
		Set<Connection<String, String>> conSet = 
				new HashSet<Connection<String, String>>();
		
		// For loop is hard coded to list4.length / 3 since I know the length.
		for (int i = 0; i < 12; i++) {
			Connection<String, String> con = new Connection<String, String>
							(list4[i * 3], list4[i * 3 + 1], list4[i * 3 + 2]);
			conSet.add(con);
		}
		
		Set<String> allNodes = new HashSet<String>();
		for (Connection<String, String> con : conSet) {
			allNodes.add(con.getFrom());
			allNodes.add(con.getTo());
		}
		
		new Graph<String, String>(allNodes, conSet);
	}

	
	///////////////////////////////////////////////////////////////////////////
	// Node tests /////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	
	// Testing getNodes because this method will be useful to test
	// others methods with.
	@Test
	public void testGetNodes() {
		assertTrue((graph1.getNodes().equals(set1)));
		assertTrue((graph2.getNodes().equals(set2)));
		assertTrue((graph3.getNodes().equals(set3)));
		assertTrue((graph4.getNodes().equals(set4)));
		assertTrue((graph5.getNodes().equals(set5)));
		assertTrue((new Graph<String, String>()).getNodes().equals
				(new TreeSet<String>()));
	}
	
	// Testing nodeExists since it doesn't rely on any other methods.
	@Test
	public void testNodeExists() {
		for (String character : set1) {
			assertTrue(graph1.getNodes().contains(character));
		}
		for (String character : set2) {
			assertTrue(graph2.getNodes().contains(character));
		}
		for (String character : set3) {
			assertTrue(graph3.getNodes().contains(character));
		}
		for (String character : set4) {
			assertTrue(graph4.getNodes().contains(character));
		}
		for (String character : set5) {
			assertTrue(graph5.getNodes().contains(character));
		}
	}

	// Testing addNode since we have now tested characterExists.
	@Test
	public void testAddNode() {
		Graph<String, String> test1 = new Graph<String, String>();
		for (String character : set1) {
			assertTrue(test1.addNode(character));
			assertTrue(test1.nodeExists(character));
		}
		Graph<String, String> test2 = new Graph<String, String>();
		for (String character : set2) {
			assertTrue(test2.addNode(character));
			assertTrue(test2.nodeExists(character));
		}
		Graph<String, String> test3 = new Graph<String, String>();
		for (String character : set3) {
			assertTrue(test3.addNode(character));
			assertTrue(test3.nodeExists(character));
		}
		Graph<String, String> test4 = new Graph<String, String>();
		for (String character : set4) {
			assertTrue(test4.addNode(character));
			assertTrue(test4.nodeExists(character));
		}
		Graph<String, String> test5 = new Graph<String, String>();
		for (String character : set5) {
			assertTrue(test5.addNode(character));
			assertTrue(test5.nodeExists(character));
		}
		
		// Testing if a false will be returned when a duplicate node is
		// attempted to be added and that the node will still be in there.
		assertFalse(test4.addNode("Hulk"));
		assertTrue(test4.nodeExists("Hulk"));
		
		assertFalse(test5.addNode(""));
		assertTrue(test5.nodeExists(""));
		
		assertFalse(test2.addNode("Thor"));
		assertTrue(test2.nodeExists("Thor"));
		
		assertFalse(test1.addNode("Luke Cage"));
		assertTrue(test1.nodeExists("Luke Cage"));
	}

	// Testing addNodes(Set) since we just tested addNode.
	@Test
	public void testAddNodesSet() {
		Graph<String, String> test1 = new Graph<String, String>();
		test1.addNodes(set1);
		for (String character : set1) {
			assertTrue(test1.nodeExists(character));
		}
		Graph<String, String> test2 = new Graph<String, String>();
		test2.addNodes(set2);
		for (String character : set2) {
			assertTrue(test2.nodeExists(character));
		}
		Graph<String, String> test3 = new Graph<String, String>();
		test3.addNodes(set3);
		for (String character : set3) {
			assertTrue(test3.nodeExists(character));
		}
		Graph<String, String> test4 = new Graph<String, String>();
		test4.addNodes(set4);
		for (String character : set4) {
			assertTrue(test4.nodeExists(character));
		}
		Graph<String, String> test5 = new Graph<String, String>();
		test5.addNodes(set5);
		for (String character : set5) {
			assertTrue(test5.nodeExists(character));
		}
		
		// Testing if a false will be returned when a duplicate node is
		// attempted to be added and that all nodes are still in there.
		
		assertFalse(test1.addNodes(set1));
		for (String charFromSet1 : set1) {
			assertTrue(test1.nodeExists(charFromSet1));
		}
		
		assertFalse(test2.addNodes(set4));
		for (String charFromSet4 : set4) {
			assertTrue(test2.nodeExists(charFromSet4));
		}
		
		Set<String> testSet = new TreeSet<String>();
		testSet.add("");
		assertFalse(test5.addNodes(testSet));
		for (String charFromTestSet : testSet) {
			assertTrue(test5.nodeExists(charFromTestSet));
		}
	}

	// Testing removeNode.
	@Test
	public void testRemoveNode() {
		for (String character : set1) {
			assertTrue(graph1.removeNode(character));
			assertFalse(graph1.nodeExists(character));
		}

		for (String character : set2) {
			assertTrue(graph2.removeNode(character));
			assertFalse(graph2.nodeExists(character));
		}
		
		for (String character : set3) {
			assertTrue(graph3.removeNode(character));
			assertFalse(graph3.nodeExists(character));
		}
		
		for (String character : set4) {
			assertTrue(graph4.removeNode(character));
			assertFalse(graph4.nodeExists(character));
		}
		
		for (String character : set5) {
			assertTrue(graph5.removeNode(character));
			assertFalse(graph5.nodeExists(character));
		}
		
		// Testing if a false will be returned when an attempted removal occurs
		// on a node not in the graph and that the node still doesn't
		// exist after the call in the graph.
		assertFalse(graph1.removeNode("Hulk"));
		assertFalse(graph1.nodeExists("Hulk"));
		
		assertFalse(graph2.removeNode(""));
		assertFalse(graph2.nodeExists(""));
		
		assertFalse(graph5.removeNode("Thor"));
		assertFalse(graph5.nodeExists("Thor"));
	}

	// Testing removeNode(Set) since we just tested
	// removeNode.
	@Test
	public void testRemoveNodesSet() {
		graph1.removeNodes(set1);
		for (String character : set1) {
			assertFalse(graph1.nodeExists(character));
		}
		
		graph2.removeNodes(set2);
		for (String character : set2) {
			assertFalse(graph2.nodeExists(character));
		}
		
		graph3.removeNodes(set3);
		for (String character : set3) {

			assertFalse(graph3.nodeExists(character));
		}
		
		graph4.removeNodes(set4);
		for (String character : set4) {
			assertFalse(graph4.nodeExists(character));
		}
		
		graph5.removeNodes(set5);
		for (String character : set5) {
			assertFalse(graph5.nodeExists(character));
		}
		
		// Testing if a false will be returned when an attempted removal occurs
		// on a node not in the graph and that none of the nodes
		// exist in the graph after this call.
		
		assertFalse(graph1.removeNodes(set2));
		for (String charFromSet2 : set2) {
			assertFalse(graph1.nodeExists(charFromSet2));
		}
		
		Set<String> testSet = new TreeSet<String>();
		testSet.add("");
		assertFalse(graph2.removeNodes(testSet));
		for (String charFromTestSet : testSet) {
			assertFalse(graph2.nodeExists(charFromTestSet));
		}
		
		assertFalse(graph5.removeNodes(set4));
		for (String charFromSet4 : set4) {
			assertFalse(graph5.nodeExists(charFromSet4));
		}
	}

	
	///////////////////////////////////////////////////////////////////////////
	// Connection tests ///////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	// Testing both addConnection and connectionExists as I couldn't think of
	// a way to test one without the other.
	@Test
	public void testAddConnectionAndConnectionExists() {
		graph1 = new Graph<String, String>();
		graph2 = new Graph<String, String>();
		graph3 = new Graph<String, String>();
		graph4 = new Graph<String, String>();
		graph5 = new Graph<String, String>();
		
		// For loop is hard coded to list1.length / 3 since I know the length.
		for (int i = 0; i < 3; i++) {
				graph1.addConnection(list1[i * 3], list1[i * 3 + 1],
						list1[i * 3 + 2]);
				assertTrue(graph1.connectionExists(list1[i * 3],
						list1[i * 3 + 1], list1[i * 3 + 2]));
		}

		// For loop is hard coded to list2.length / 3 since I know the length.
		for (int i = 0; i < 4; i++) {
			graph2.addConnection(list2[i * 3], list2[i * 3 + 1],
					list2[i * 3 + 2]);
			assertTrue(graph2.connectionExists(list2[i * 3],
					list2[i * 3 + 1], list2[i * 3 + 2]));
		}

		// For loop is hard coded to list3.length / 3 since I know the length.
		for (int i = 0; i < 5; i++) {
			graph3.addConnection(list3[i * 3], list3[i * 3 + 1],
					list3[i * 3 + 2]);
			assertTrue(graph3.connectionExists(list3[i * 3],
					list3[i * 3 + 1], list3[i * 3 + 2]));
		}

		// For loop is hard coded to list4.length / 3 since I know the length.
		for (int i = 0; i < 12; i++) {
			graph4.addConnection(list4[i * 3], list4[i * 3 + 1],
					list4[i * 3 + 2]);
			assertTrue(graph4.connectionExists(list4[i * 3],
					list4[i * 3 + 1], list4[i * 3 + 2]));
		}

		// For loop is hard coded to list4.length / 3 since I know the length.
		for (int i = 0; i < 12; i++) {
			graph5.addConnection(list4[i * 3], list4[i * 3 + 1],
					list4[i * 3 + 2]);
			assertTrue(graph5.connectionExists(list4[i * 3],
					list4[i * 3 + 1], list4[i * 3 + 2]));
		}

		// Testing if a false will be returned when a duplicate connection is
		// attempted to be added and that the connection will still exist in
		// the graph after the call.

		assertFalse(graph5.addConnection(list4[0], list4[1], list4[2]));
		assertTrue(graph5.connectionExists(list4[0], list4[1], list4[2]));

		assertFalse(graph1.addConnection(list1[0], list1[1], list1[2]));
		assertTrue(graph1.connectionExists(list1[0], list1[1], list1[2]));
		
		// Testing to make sure an empty string can be used as a character 
		// name and that a connection can exists from and to the same node.
		// Also testing to make sure a title can be the same string as a
		// character name.
		assertTrue(graph5.addConnection("", "test", "testTitle"));
		assertTrue(graph5.addConnection("", "", "testTitle"));
		assertTrue(graph5.addConnection("", "", ""));
	}
	
	// Testing addConnections(Set) since we just tested adding a singular 
	// connection.
	@Test
	public void testAddConnections() {
		Set<Connection<String, String>> conSet = 
				new HashSet<Connection<String, String>>();
		
		// For loop is hard coded to list4.length / 3 since I know the length.
		for (int i = 0; i < 12; i++) {
			Connection<String, String> con = new Connection<String, String>
							(list4[i * 3], list4[i * 3 + 1], list4[i * 3 + 2]);
			conSet.add(con);
		}
		graph5.addConnections(conSet);
		
		for (Connection<String, String> con : conSet) {
			assertTrue(graph5.connectionExists(con));
		}
	}
	
	// Testing getConnections() since we can now add them as a set.
	@Test
	public void testGetConnections() {
		Set<Connection<String, String>> conSet = 
				new HashSet<Connection<String, String>>();
		graph5.clear();
		
		// For loop is hard coded to list4.length / 3 since I know the length.
		for (int i = 0; i < 12; i++) {
			Connection<String, String> con = new Connection<String, String>
							(list4[i * 3], list4[i * 3 + 1], list4[i * 3 + 2]);
			conSet.add(con);
		}
		graph5.addConnections(conSet);
		Set<Connection<String, String>> returnedSet = graph5.getConnections();
		
		assertTrue(conSet.size() == returnedSet.size());
		for (Connection<String, String> con : returnedSet) {
			assertTrue(conSet.contains(con));
		}
	}

	// Testing removeConnection now that addConnection has been tested.
	@Test
	public void testRemoveConnection() {
		// For loop is hard coded to list1.length / 3 since I know the length.
		for (int i = 0; i < 3; i++) {
			graph1.addConnection(list1[i* 3], list1[i * 3 + 1],
					list1[i * 3 + 2]);
			graph1.removeConnection(list1[i * 3], list1[i * 3 + 1],
					list1[i * 3 + 2]);
			assertFalse(graph1.connectionExists(list1[i * 3],
					list1[i * 3 + 1], list1[i * 3 + 2]));
		}

		// For loop is hard coded to list2.length / 3 since I know the length.
		for (int i = 0; i < 4; i++) {
			graph2.addConnection(list2[i * 3], list2[i * 3 + 1],
					list2[i * 3 + 2]);
			graph2.removeConnection(list2[i * 3], list2[i * 3 + 1],
					list2[i * 3 + 2]);
			assertFalse(graph2.connectionExists(list2[i * 3],
					list2[i * 3 + 1], list2[i * 3 + 2]));
		}

		// For loop is hard coded to list3.length / 3 since I know the length.
		for (int i = 0; i < 5; i++) {
			graph3.addConnection(list3[i * 3], list3[i * 3 + 1],
					list3[i * 3 + 2]);
			graph3.removeConnection(list3[i * 3], list3[i * 3 + 1],
					list3[i * 3 + 2]);
			assertFalse(graph3.connectionExists(list3[i * 3],
					list3[i * 3 + 1], list3[i * 3 + 2]));
		}

		// For loop is hard coded to list4.length / 3 since I know the length.
		for (int i = 0; i < 12; i++) {
			graph4.addConnection(list4[i * 3], list4[i * 3 + 1],
					list4[i * 3 + 2]);
			graph4.removeConnection(list4[i * 3], list4[i * 3 + 1],
					list4[i * 3 + 2]);
			assertFalse(graph4.connectionExists(list4[i * 3],
					list4[i * 3 + 1], list4[i * 3 + 2]));
		}

		// For loop is hard coded to list4.length / 3 since I know the length.
		for (int i = 0; i < 12; i++) {
			graph5.addConnection(list4[i * 3], list4[i * 3 + 1],
					list4[i * 3 + 2]);
			graph5.removeConnection(list4[i * 3], list4[i * 3 + 1],
					list4[i * 3 + 2]);
			assertFalse(graph5.connectionExists(list4[i * 3],
					list4[i * 3 + 1], list4[i * 3 + 2]));
		}

		// Testing if a false will be returned when a duplicate connection is
		// attempted to be added and that the connection will still exist in
		// the graph after the call.

		assertFalse(graph5.removeConnection(list4[0], list4[1], list4[2]));
		assertFalse(graph5.connectionExists(list4[0], list4[1], list4[2]));

		assertFalse(graph1.removeConnection(list1[0], list1[1], list1[2]));
		assertFalse(graph1.connectionExists(list1[0], list1[1], list1[2]));
	}
	
	// Testing removeConnections(Set) since we just tested removing a singular
	// connection.
	@Test
	public void testRemoveConnections() {
		Set<Connection<String, String>> conSet = 
				new HashSet<Connection<String, String>>();
		
		// For loop is hard coded to list4.length / 3 since I know the length.
		for (int i = 0; i < 12; i++) {
			Connection<String, String> con = new Connection<String, String>
							(list4[i * 3], list4[i * 3 + 1], list4[i * 3 + 2]);
			conSet.add(con);
		}
		graph5.addConnections(conSet);
		graph5.removeConnections(conSet);
		
		for (Connection<String, String> con : conSet) {
			assertFalse(graph5.connectionExists(con));
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// Finding information tests //////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////	

	// Testing isConnectedWith now that addConnection has been tested.
	@Test
	public void testIsConnectedWithOneArg() {
		 Set<Connection<String, String>> connectionSet = (graph4.
	        		isConnectedWith("Daredevil"));
	     Set<String> connectedCharSet = new TreeSet<String>();
	     for (Connection<String, String> con : connectionSet) {
	    	 connectedCharSet.add(con.getTo());
	     }
		assertEquals(new TreeSet<String>(Arrays.asList("Spider-Man")),
				connectedCharSet);
		connectionSet.clear();
		connectedCharSet.clear();
		
		connectionSet = (graph4.isConnectedWith("Hulk"));
	    connectedCharSet = new TreeSet<String>();
	    for (Connection<String, String> con : connectionSet) {
	    	connectedCharSet.add(con.getTo());
	    }
		assertEquals(new TreeSet<String>(Arrays.asList("Luke Cage", 
				"Spider-Man", "Thor")), connectedCharSet);
		connectionSet.clear();
		connectedCharSet.clear();
		
		connectionSet = (graph4.isConnectedWith("Thor"));
	    connectedCharSet = new TreeSet<String>();
	    for (Connection<String, String> con : connectionSet) {
	    	 connectedCharSet.add(con.getTo());
	     }
		assertEquals(new TreeSet<String>(Arrays.asList("Daredevil", 
				"Spider-Man")), connectedCharSet);
		try {
			assertEquals(new TreeSet<String>(), 
					graph5.isConnectedWith("Deadpool"));
			fail();
		} catch (IllegalArgumentException e) {
			String expectedMessage = "This node does not exist in the graph.";
			assertEquals(expectedMessage, e.getMessage());
		}
		try {
			assertEquals(new TreeSet<String>(), 
					graph5.isConnectedWith("Doctor Strange"));
			fail();
		} catch (IllegalArgumentException e) {
			String expectedMessage = "This node does not exist in the graph.";
			assertEquals(expectedMessage, e.getMessage());
		}
	}

	// Testing isConnectedWith now that addConnection has been tested.
	@Test
	public void testIsConnectedWithTwoArgs() {
		assertEquals(new TreeSet<String>(Arrays.asList("Evolutionary War", 
				"Infinity War")),
				graph4.isConnectedWith("Daredevil", "Spider-Man"));
		assertEquals(new TreeSet<String>(Arrays.asList("Axis")),
				graph4.isConnectedWith("Hulk", "Thor"));
		assertEquals(new TreeSet<String>(Arrays.asList("Age of Ultron")),
				graph4.isConnectedWith("Hulk", "Luke Cage"));
		try {
			assertEquals(new TreeSet<String>(),
					graph5.isConnectedWith("Deadpool", "Hulk"));
			fail();
		} catch (IllegalArgumentException e) {
			String expectedMessage = "One or more of the nodes do not "
									 + "exist in this graph.";
			assertEquals(expectedMessage, e.getMessage());
		}
		try {
			assertEquals(new TreeSet<String>(),
					graph5.isConnectedWith("Deadpool", "Doctor Strange"));
			fail();
		} catch (IllegalArgumentException e) {
			String expectedMessage = "One or more of the nodes do not "
									 + "exist in this graph.";
			assertEquals(expectedMessage, e.getMessage());
		}
	}
	
	// Testing getAConnection(String, String)
	@Test
	public void testGetAConnection() {
		assertTrue(graph4.connectionExists("Hulk", "Thor", 
				graph4.getFirstConnectionLabel("Hulk", "Thor")));
		
		assertEquals(null, graph4.getFirstConnectionLabel("Thor", "Hulk"));

		try {
			assertTrue(graph5.connectionExists("test", "anotherName", 
					graph5.getFirstConnectionLabel("test", "anotherName")));
			fail();
		} catch (IllegalArgumentException e) {
			String expectedMessage = "One or more of the nodes do not "
									 + "exist in this graph.";
			assertEquals(expectedMessage, e.getMessage());
		}
		try {
			assertTrue(graph5.connectionExists("test1", "test2", 
					graph5.getFirstConnectionLabel("test1", "test2")));
			fail();
		} catch (IllegalArgumentException e) {
			String expectedMessage = "One or more of the nodes do not "
									 + "exist in this graph.";
			assertEquals(expectedMessage, e.getMessage());
		}
	}
	
	// Testing numOfConnections(String) now that addConnection has been tested.
	@Test
	public void testNumOfConnectionsOneArg() {
		assertEquals(2, graph4.numOfConnections("Daredevil"));
		assertEquals(3, graph4.numOfConnections("Hulk"));
		assertEquals(0, graph4.numOfConnections("Iron Man"));
		try {
			assertEquals(0, graph5.numOfConnections("Doctor Strange"));
			fail();
		} catch (IllegalArgumentException e) {
			String expectedMessage = "This node does not exist in the "
									 + "graph.";
			assertEquals(expectedMessage, e.getMessage());
		}
		try {
			assertEquals(0, graph5.numOfConnections("Black Widow"));
			fail();
		} catch (IllegalArgumentException e) {
			String expectedMessage = "This node does not exist in the "
									 + "graph.";
			assertEquals(expectedMessage, e.getMessage());
		}
	}

	// Testing numOfConnections(String, String) now that addConnection and 
	// isConnectedWith have been tested.
	@Test
	public void testNumOfConnectionsTwoArgs() {
		assertEquals(2, graph4.numOfConnections("Daredevil", "Spider-Man"));
		assertEquals(1, graph4.numOfConnections("Hulk", "Thor"));
		assertEquals(1, graph4.numOfConnections("Hulk", "Luke Cage"));
		try {
			assertEquals(0, graph5.numOfConnections("Deadpool", "Hulk"));
			fail();
		} catch (IllegalArgumentException e) {
			String expectedMessage = "One or more of the nodes"
									 + " do not exist in this graph.";
			assertEquals(expectedMessage, e.getMessage());
		}
		try {
			assertEquals(0, 
					graph5.numOfConnections("Deadpool", "Doctor Strange"));
			fail();
		} catch (IllegalArgumentException e) {
			String expectedMessage = "One or more of the nodes"
									 + " do not exist in this graph.";
			assertEquals(expectedMessage, e.getMessage());
		}
	}
	
	// Testing numOfConnectedChars since we have tested addConnection
	@Test
	public void testNumOfConnectedNodes() {
		assertEquals(2, graph1.numOfConnectedNodes("Captain America"));
		assertEquals(1, graph1.numOfConnectedNodes("Hulk"));

		assertEquals(1, graph2.numOfConnectedNodes("Winter Soldier"));
		assertEquals(0, graph2.numOfConnectedNodes("Thor"));

		assertEquals(0, graph3.numOfConnectedNodes("Spider-Man"));
		assertEquals(2, graph3.numOfConnectedNodes("Thor"));

		assertEquals(1, graph4.numOfConnectedNodes("Captain Marvel"));
		assertEquals(1, graph4.numOfConnectedNodes("Daredevil"));
		assertEquals(3, graph4.numOfConnectedNodes("Captain America"));
		assertEquals(3, graph4.numOfConnectedNodes("Hulk"));
		assertEquals(1, graph4.numOfConnectedNodes("Winter Soldier"));
		assertEquals(2, graph4.numOfConnectedNodes("Thor"));
		assertEquals(0, graph4.numOfConnectedNodes("Spider-Man"));
		assertEquals(0, graph4.numOfConnectedNodes("Iron Man"));
		
		try {
			assertEquals(0, graph5.numOfConnections("Doctor Strange"));
			fail();
		} catch (IllegalArgumentException e) {
			String expectedMessage = "This node does not exist in the "
									 + "graph.";
			assertEquals(expectedMessage, e.getMessage());
		}
		try {
			assertEquals(0, graph5.numOfConnections("Black Widow"));
			fail();
		} catch (IllegalArgumentException e) {
			String expectedMessage = "This node does not exist in the "
									 + "graph.";
			assertEquals(expectedMessage, e.getMessage());
		}
		
	}

	
	///////////////////////////////////////////////////////////////////////////
	// Other tests ////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	
	// Testing equals() since it does not rely on other methods.
	@Test
	public void testEquals() {
		Graph<String, String> test1 = new Graph<String, String>();
		// For loop is hard coded to list1.length / 3 since I know the length.
		for (int i = 0; i < 3; i++) {
			test1.addConnection(list1[i * 3], list1[i * 3 + 1], list1[i * 3 + 2]);
		}
		assertTrue(test1.equals(graph1));

		Graph<String, String> test2 = new Graph<String, String>();
		// For loop is hard coded to list2.length / 3 since I know the length.
		for (int i = 0; i < 4; i++) {
			test2.addConnection(list2[i * 3], list2[i * 3 + 1],
					list2[i * 3 + 2]);
		}
		assertTrue(test2.equals(graph2));

		Graph<String, String> test3 = new Graph<String, String>();
		// For loop is hard coded to list3.length / 3 since I know the length.
		for (int i = 0; i < 5; i++) {
			test3.addConnection(list3[i * 3], list3[i * 3 + 1],
					list3[i * 3 + 2]);
		}
		assertTrue(test3.equals(graph3));

		Graph<String, String> test4= new Graph<String, String>();
		// For loop is hard coded to list4.length / 3 since I know the length.
		for (int i = 0; i < 12; i++) {
			test4.addConnection(list4[i * 3], list4[i * 3 + 1],
					list4[i * 3 + 2]);
		}
		assertTrue(test4.equals(graph4));

		Graph<String, String> test5 = new Graph<String, String>();
		// For loop is hard coded to list5.length / 3 since I know the length.
		for (int i = 0; i < 3; i++) {
			test5.addConnection(list5[i * 3], list5[i * 3 + 1],
					list5[i * 3 + 2]);
		}
		assertTrue(test5.equals(graph5));
	}

	// Testing hashCode() since we just tested equals()
	@Test
	public void testHashCode() {
		Graph<String, String> test1 = new Graph<String, String>();
		// For loop is hard coded to list1.length / 3 since I know the length.
		for (int i = 0; i < 3; i++) {
			test1.addConnection(list1[i * 3], list1[i * 3 + 1], list1[i * 3 + 2]);
		}
		assertTrue(test1.hashCode() == graph1.hashCode());

		Graph<String, String> test2 = new Graph<String, String>();
		// For loop is hard coded to list2.length / 3 since I know the length.
		for (int i = 0; i < 4; i++) {
			test2.addConnection(list2[i * 3], list2[i * 3 + 1],
					list2[i * 3 + 2]);
		}
		assertTrue(test2.hashCode() == graph2.hashCode());

		Graph<String, String> test3 = new Graph<String, String>();
		// For loop is hard coded to list3.length / 3 since I know the length.
		for (int i = 0; i < 5; i++) {
			test3.addConnection(list3[i * 3], list3[i * 3 + 1],
					list3[i * 3 + 2]);
		}
		assertTrue(test3.hashCode() == graph3.hashCode());

		Graph<String, String> test4 = new Graph<String, String>();
		// For loop is hard coded to list4.length / 3 since I know the length.
		for (int i = 0; i < 12; i++) {
			test4.addConnection(list4[i * 3], list4[i * 3 + 1],
					list4[i * 3 + 2]);
		}
		assertTrue(test4.hashCode() == graph4.hashCode());

		Graph<String, String> test5 = new Graph<String, String>();
		// For loop is hard coded to list5.length / 3 since I know the length.
		for (int i = 0; i < 3; i++) {
			test5.addConnection(list5[i * 3], list5[i * 3 + 1],
					list5[i * 3 + 2]);
		}
		assertTrue(test5.hashCode() == graph5.hashCode());
	}
	
	// Testing clone() now that we have tested equals.
	@Test
	public void testClone() {
		assertEquals(graph1, graph1.clone());
		assertEquals(graph2, graph2.clone());
		assertEquals(graph3, graph3.clone());
		assertEquals(graph4, graph4.clone());
		assertEquals(graph5, graph5.clone());
		
	}
	
	// Testing clear() since it we have tested equals.
	@Test
	public void testClear() {
		// Clear the graphs.
		graph1.clear();
		graph2.clear();
		graph3.clear();
		graph4.clear();
		graph5.clear();

		// Compare them to an empty graph.
		assertTrue(graph1.equals(new Graph<String, String>()));
		assertTrue(graph2.equals(new Graph<String, String>()));
		assertTrue(graph3.equals(new Graph<String, String>()));
		assertTrue(graph4.equals(new Graph<String, String>()));
		assertTrue(graph5.equals(new Graph<String, String>()));
	}


	// Testing iterator() returns a iterator which iterates in the correct
	// order.
	@Test
	public void testIterator() {
		Iterator<String> it = graph4.iterator();
		String iterString = "";
		while (it.hasNext()) {
			iterString += it.next() + " ";
		// I'm okay with a whitespace at then end because the correctOutput 
		// will have one too.
		}
		String correctOutput = "";	
		String[] orderedFromToLabel = new String[] 
				{"Captain America", "Age of Ultron",
				"Captain America", "Axis", 
				"Captain Marvel", "Civil War",
				"Daredevil", "Evolutionary War",
				"Daredevil", "Infinity War",
				"Hulk", "Age of Ultron",
				"Hulk", "Axis",
				"Hulk", "Infinity War" ,
				"Thor", "Evolutionary War", 
				"Winter Soldier", "Civil War" };
		for (int i = 0; i < orderedFromToLabel.length; i++) {
			correctOutput += orderedFromToLabel[i] + " ";
		}
		assertEquals(correctOutput, iterString);
	}
	
	// Testing toString() now that we have tested iterator()
	@Test
	public void testToString() throws MalformedDataException {
		
		String expectedOutput = "";
		expectedOutput += "Captain America Age of Ultron" + '\n';
		expectedOutput += "Hulk Age of Ultron" + '\n';
		
		assertEquals(expectedOutput, graph1.toString());
	}
}
