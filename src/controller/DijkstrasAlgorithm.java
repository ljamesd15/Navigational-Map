package controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import model.Connection;
import model.Graph;
import model.Node;

/**
 * Finds the shortest weighted path between any two nodes in a graph provided the graph labels are 
 * comparable.
 * @author L. James Davidson
 *
 */
public class DijkstrasAlgorithm {
	
	/**
	 * Finds the shortest weighted path from the start node to the target node.
	 * @param startNode is the name of the start point in the graph.
	 * @param targetNode is the name of the node for which the path is going 
	 * to.
	 * @param multigraph is the graph where the path will be found in.
	 * @requires startNode, targetNode, and multigraph all be non-null.
	 * @throws IllegalArgumentException if one or more of the node name 
	 * parameters are not in the graph.
	 * @return  A null value will be returned if there is no path between these
	 * two nodes. A list of nodes that make up the steps of the lowest cost path 
	 * takes.
	 */
	public static <T extends Comparable<T>>  List<Node<T>> findShortestweightedPath
			(T startNode, T targetNode, Graph<T, Double> multigraph) {
		//long start = System.nanoTime();
		
		if (!(multigraph.nodeExists(startNode) && 
				multigraph.nodeExists(targetNode))) {
			throw new IllegalArgumentException("One or more of the nodes does "
					+ "not exist in the multigraph.");
		}

		Node<T> dest = new Node<T>(targetNode, Double.POSITIVE_INFINITY);
		Set<T> finished = new HashSet<T>();
		Set<Connection<T, Double>> remainingCons = multigraph.getConnections();
		
		// Set up a priority queue which will sort by the pathWeights.
		Queue<Node<T>> active = new PriorityQueue<Node<T>>(
				1, new Comparator<Node<T>>()
				{ public int compare(Node<T> n1, Node<T> n2) { 
					int result =  Double.compare(n1.getPathWeight(), 
												n2.getPathWeight());
					if (result != 0) {
						return result;
					}
					return n1.getNodeName().compareTo(n2.getNodeName());
				}});
		
		active.add(new Node<T>(startNode, 0.0));
		
		// Inv: The solution has not been found, active contains all paths to 
		// unfinished nodes that only pass through finished nodes via least 
		// cost paths to those nodes.
		while (!(active.isEmpty())) {
			Node<T> currNode = active.remove();
			T currName = currNode.getNodeName();
			
			// If its in finished then the shortest possible path has already 
			// been found. No since searching the same node again.
			if (finished.contains(currName)) {
				continue;
			}
			
			// If this is our final node then pack up the info and return.
			if (currNode.equals(dest)) {

				List<Node<T>> backtrack = new ArrayList<Node<T>>();
				backtrack.add(currNode);
				
				while (currNode.getPrevNode() != null) {
					currNode = currNode.getPrevNode();
					backtrack.add(0, currNode);
				}
				// Q: currNode is the startNode
				return backtrack;
			}

			// This is the shortest path to this node all other paths to this 
			// node will take longer.
			finished.add(currName);
			
			// Get a set of connections from the current node.
			Set<Connection<T, Double>> connections = 
					new HashSet<Connection<T, Double>>();
			for (Connection<T, Double> con : remainingCons) {
				if (con.getFrom().equals(currName)) {
					connections.add(con);
				}
			}
			
			// Inv: All children processed are either in active or finished.
			for (Connection<T, Double> con : connections) {
				// These connections have been used so am removing them from 
				// future searches since they have already been used.
				remainingCons.remove(con);
				
				T nodeToName = con.getTo();

				
				// Create nodes out of all the children.
				Double pathWeight = currNode.getPathWeight() 
						+ con.getLabel();
				Node<T> childNode = new Node<T>(nodeToName, pathWeight, 
						currNode);				

				active.add(childNode);
			}
			// Q: All children have been processed and therefore all children
			// are now in active.
		}
		// Q: There was no solution found.
		return null;
		
	}
	
}
