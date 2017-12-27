package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.Graph;
import exception.MalformedDataException;

/**
 * Allows users to interact with graphs. They can load graphs, remove graphs, 
 * see the nodes in the graph, and find a lowest edge cost path between two nodes.
 * @author L. James Davidson
 */
public class LoadGraph {
	
	private static final String FILE_PREFIX = "src\\data\\";
	
	/**
	 * Loads a graph from a file in src/data into a graph. This graph will
	 * have weighted edges between nodes of 1 / number of connections between
	 * two nodes. 
	 * @param filename is the name of the file in the directory src/data
	 * @requires The file must be a properly formatted tsv file.
	 * @return A loaded graph as described above.
	 * @throws MalformedDataException if the file is not well-formed: each line
	 * contains exactly two tokens separated by a tab, or else starting with a 
	 * # symbol to indicate a comment line.
	 * @throws IOException if the file name given does not exist.
	 */
	public static Graph<String, Double> loadWeightedGraph(String filename) 
			throws MalformedDataException, IOException {
		Set<String> allNodes = new HashSet<String>();
		Map<String, List<String>> labelsToNodes = 
				new HashMap<String, List<String>>();
		filename = FILE_PREFIX + filename;
		Graph<String, Double> loadedGraph = new Graph<String, Double>();
		FileParser.parseData(filename, allNodes, labelsToNodes);
		loadedGraph.addNodes(allNodes);
		
		// This is a map which maps characters to a map. The value map then 
		// maps connected characters to the number of times they are connected.
		Map<String, Map<String, Integer>> nodesToNumTimesConnectedTo = 
				new HashMap<String, Map<String, Integer>>();
		Set<String> allLabels = labelsToNodes.keySet();
		
		// Inv: nodesToNumTimesConnectedTo contains the bi-directional 
		// connections between each pair of nodes and the number of times each 
		// pair of nodes is connected to one another for each label processed 
		// in labelsToNodes. No connections between the same node.
		for (String label : allLabels) {
			List<String> nodesInLabel = labelsToNodes.get(label);
			int size = nodesInLabel.size();
			
			// Inv: Nodes in the list nodesInLabel from index 0 to i - 1 will 
			// have the number of connections updated for the all the 
			// characters in the list not including connections to itself.
			for (int i = 0; i < size; i++) {
				
				// Get node which we will add connections from.
				String nodeFrom = nodesInLabel.get(i);
				
				// If character isn't already in map of maps then add it.
				if (!(nodesToNumTimesConnectedTo.containsKey(nodeFrom))) {
					nodesToNumTimesConnectedTo.put(nodeFrom, 
							new HashMap<String, Integer>());
				}

				
				// Inv: Nodes in the list nodesInLabel from index 0 to i - 1 
				// will have the number of connections updated for characters
				// from 0 to j - 1 not including connections to itself.
				for (int j = 0; j < size; j++) {
					if (i != j) {
						// Get node which we will add connections to.
						String nodeTo = nodesInLabel.get(j);
						
						Map<String, Integer> nodeMap = 
								nodesToNumTimesConnectedTo.get(nodeFrom);
						
						// If the nodes aren't already connected then add 
						// connection and set number to 0.
						if (!(nodeMap.containsKey(nodeTo))) {
							nodeMap.put(nodeTo, 0);
						}
						
						// Get number of connections between nodes and 
						// increment it.
						Integer numOfConnections = nodeMap.get(nodeTo);
						numOfConnections++;
						// Put it back in the node map.
						nodeMap.put(nodeTo, numOfConnections);
						// Put node map back into map of maps.
						nodesToNumTimesConnectedTo.put(nodeFrom, nodeMap);
					}

				}
				// Q: j = size. Nodes in the list nodesInLabel from index 0 to
				// i - 1 will have the number of connections updated for 
				// characters from 0 to size - 1 not including connections to 
				// itself.
				
			}
			// Q: i = size. Nodes in the list nodesInLabel from index 0 to 
			// size - 1 will have the number of connections updated for the all
			// the characters in the list not including connections to itself.
		}
		// Q: All labels in labelsToNodes processed. nodesToNumTimesConnectedTo
		// contains all the bi-directional connections between each pair of 
		// nodes and the number of times each pair of nodes is connected to 
		// one another. No connections between the same node.
		
		// We now have all the information we want. Creating the graph now.
		Set<String> nodesWithConnections = nodesToNumTimesConnectedTo.keySet();
		
		// Inv: All connections are added to the graph for all nodes processed
		// from nodesWithConnections.
		for (String nodeFrom : nodesWithConnections) {
			
			// Get this nodes map of connection information.
			Map<String, Integer> nodeMap = nodesToNumTimesConnectedTo.get(nodeFrom);
			Set<String> nodesConnectedTo = nodeMap.keySet();
			
			// Inv: All connections from nodeFrom to all node keys processed 
			// within nodeMap are added to the graph. 
			for (String nodeTo : nodesConnectedTo) {
				
				// Get edge weight from number of connections between the two
				// nodes.
				Double numOfConnections = (double) nodeMap.get(nodeTo);
				Double edgeWeight = 1 / numOfConnections;
				
				// Add the connection between the two nodes.
				loadedGraph.addConnection(nodeFrom, nodeTo, edgeWeight);
			}
			// Q: All node keys from nodeMap are processed so all connections 
			// from nodeFrom are added to the graph.
		}
		// Q: All nodes with connections have been processed therefore all
		// connection have been added.
		
		// All the individual nodes were added to the graph at the very start.
		// Now all connections have been added so we just return the graph.
		return loadedGraph;
	}
	
}
