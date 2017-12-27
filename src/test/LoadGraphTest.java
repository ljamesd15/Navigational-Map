package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import org.junit.BeforeClass;
import org.junit.Test;

import controller.LoadGraph;
import exception.MalformedDataException;
import model.Graph;

public class LoadGraphTest {
	
	private static Graph<String, Double> testGraph;
	private static Set<String> animals;
	
	// test if connections are bi-directional
	// test weights are correctly done, x3
	// no connections from and to same node
	
	@BeforeClass
	public static void initialiseGraph()  throws MalformedDataException{
		try {
			testGraph = LoadGraph.loadWeightedGraph("animals_v2.tsv");
		} catch (IOException e) {
			e.printStackTrace();
		}
		animals = new HashSet<String>(Arrays.asList("Dog", "Cat", "Lion", 
							"Wolf", "Fish", "Mouse", "Hamster", "Parrot"));
	}
	
	// Testing if the connections themselves exist between nodes when they 
	// should.
	@Test
	public void testCorrectConnectionsExist() {
		
		assertFalse(testGraph.isConnectedWith("Dog", "Cat").isEmpty());
		assertFalse(testGraph.isConnectedWith("Dog", "Wolf").isEmpty());
		assertFalse(testGraph.isConnectedWith("Cat", "Lion").isEmpty());
		assertFalse(testGraph.isConnectedWith("Mouse", "Hamster").isEmpty());
		assertTrue(testGraph.isConnectedWith("Fish", "Wolf").isEmpty());
		assertTrue(testGraph.isConnectedWith("Dog", "Mouse").isEmpty());
		assertTrue(testGraph.isConnectedWith("Parrot").isEmpty());
	}
	
	// Testing no connections exist from and to the same node.
	@Test
	public void testNoSelfConnection() {
		for (String animal : animals) {
			assertTrue(testGraph.isConnectedWith(animal, animal).isEmpty());
		}
	}
	
	// Test that only one connection exists between each node.
	@Test
	public void testNumConnectionsIsOne() {
		assertEquals(1, testGraph.isConnectedWith("Dog", "Cat").size());
		assertEquals(1, testGraph.isConnectedWith("Dog", "Wolf").size());
		assertEquals(1, testGraph.isConnectedWith("Cat", "Lion").size());
		assertEquals(1, testGraph.isConnectedWith("Mouse", "Hamster").size());
	}
	
	// Now that we have tested that the connections are there and there is only
	// one of them lets test to see if the weights are added correctly.
	@Test
	public void testCorrectWeights() {
		assertTrue(Double.compare(1.0, (double)testGraph.
				getFirstConnectionLabel("Dog", "Hamster")) == 0);
		assertTrue(Double.compare(0.5, (double)testGraph.
				getFirstConnectionLabel("Dog", "Cat")) == 0);
		assertTrue(Double.compare(0.5, (double)testGraph.
				getFirstConnectionLabel("Cat", "Lion")) == 0);
		assertTrue(Double.compare((1.0 / 3.0), (double)testGraph.
				getFirstConnectionLabel("Dog", "Wolf")) == 0);
	}
	
	// Testing if connections are indeed bi-directional now that we have 
	// tested that the weights are correct.
	@Test
	public void testBiDirectionality() {
		assertFalse(testGraph.isConnectedWith("Dog", "Cat").isEmpty());
		assertFalse(testGraph.isConnectedWith("Dog", "Wolf").isEmpty());
		assertFalse(testGraph.isConnectedWith("Cat", "Lion").isEmpty());
		assertFalse(testGraph.isConnectedWith("Mouse", "Hamster").isEmpty());
		
		assertFalse(testGraph.isConnectedWith("Cat", "Dog").isEmpty());
		assertFalse(testGraph.isConnectedWith("Wolf", "Dog").isEmpty());
		assertFalse(testGraph.isConnectedWith("Lion", "Cat").isEmpty());
		assertFalse(testGraph.isConnectedWith("Hamster", "Mouse").isEmpty());
	}
	
	// Now that we know the graph loads correctly lets see how long it takes 
	// to load marvel.tsv
	@Test
	public void testTimeToCreate() throws MalformedDataException {
		//long start = System.nanoTime();
		try {
			LoadGraph.loadWeightedGraph("marvel.tsv");
		} catch (IOException e) {
			e.printStackTrace();
		}
		//long end = System.nanoTime();
		//double difference = (double) (end - start) / 1000000000.0;
		//System.out.printf("It took %.2f", difference);
		//System.out.println(" seconds to create marvel.tsv");
	}
	
}
