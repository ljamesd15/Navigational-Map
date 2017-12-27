package test;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import model.Connection;
import model.Graph;

public class GenericsTest {
	
	@Test
	public void testGraphClass() {
		Set<String> stringSet = new HashSet<String>();
		Set<Integer> intSet = new HashSet<Integer>();
		Set<Double> doubleSet = new HashSet<Double>();
		
		for (int i = 0; i < 10; i++) {
			stringSet.add("character" + i);
			intSet.add(i);
			doubleSet.add(Math.PI * i);
		}

		Graph<String, Double> strToDoub = new Graph<String, Double>(stringSet);
		Graph<Integer, String> intToStr = new Graph<Integer, String>(intSet);
		Graph<Double,Integer> doubToInt = new Graph<Double, Integer>(doubleSet);
		
		strToDoub.addConnection("prof", "csStudent", 331.0);
		intToStr.addConnection(142, 143, "CSE 331");
		doubToInt.addConnection(142.0, 143.0, 331);
	}
	
	@Test
	public void testConnectionsClass() {
		new Connection<Double, String>(142.0, 143.0, "CSE 331");
		new Connection<String, Integer>("prof", "csStudent", 331);
		new Connection<Integer, Double>(142, 143, 331.0);
	}
}
