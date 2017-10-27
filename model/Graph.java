package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import model.Connection;

/**
 * Creates a graph object.
 * @author L. James Davidson
 *
 * @param <T> is the type of object which will define the nodes.
 * @param <N> is the type of object which will define paths between nodes.
 */
public class Graph<T extends Comparable<T>, N extends Comparable<N>> {
	

	public Set<Connection<T, N>> connections;
	public Set<T> nodes;
	private static final boolean DEBUG_MODE = false;
	
	// Abstract function:
	// The set of connections must be non-null.
	// The set of nodes is non-null.
	// All of the edges are between characters which are in this.characters.
	
	// Representation invariant:
	// this.connections != null
	// this.nodes != null
	// for (Connection con : connections) 
	// { this.nodes.contains(con.getFrom()) == true }
	// { this.nodes.contains(con.getTo()) == true }
		
	/**
	 * Creates an empty graph with no nodes or connections in it.
	 * @effects Creates the graph.
	 */
	public Graph() {
		this.connections = new HashSet<Connection<T, N>>();
		this.nodes = new HashSet<T>();
		this.checkRep();
	}
	
	/**
	 * Creates a new graph containing the nodes whose values are in the set
	 * parameter. If the set is empty then it will create an empty graph.
	 * @param set is the set of names which the new graph will contain.
	 * @requires set to be non-null.
	 * @effects Creates the new graph with the set of nodes.
	 */
	public Graph(Set<T> initialNodes) {
		this();
		this.addNodes(initialNodes);
		// No need to use checkRep() here because the representation invariant
		// is checked immediately after every node gets added in the 
		// addNode(T) file which is where this will eventually go.
	}
	
	/**
	 * Creates a graph containing some initial set of nodes and connections. 
	 * @param initialConnections is the set of connections that this will be 
	 * created with.
	 * @param initialNodes is the set of nodes that this graph will be created
	 * with.
	 * @requires Both sets and all of their contents to be non-null.
	 * @effects Creates a new graph with a base set of nodes and connections.
	 */
	public Graph(Set<T> initialNodes, Set<Connection<T, N>> initialConnections) {
		this(initialNodes);
		this.addConnections(initialConnections);
	}
	
	/**
	 * Adds a new node to the graph.
	 * @param name of the node that is added.
	 * @requires name to be non-null.
	 * @effects This graph now contains the new node.
	 * @modifies This graph.
	 * @returns A true value if the node was successfully added to this graph
	 * and false if the node could not be added or were already added.
	 */
	public boolean addNode(T name) {
		boolean result = !(this.nodeExists(name));
		if (result) {
			this.nodes.add(name);
			this.checkRep();
		}
		return result;
	}

	
	/**
	 * Adds the set of nodes to the graph.
	 * @param setOfNames is the set containing all the values of the nodes that
	 * will be added to this.
	 * @requires All names in the set to be non-null.
	 * @effects New nodes from the input set will be added to this graph.
	 * @modifies This graph.
	 * @returns A true value if every node in the parameter set was
	 * successfully added to this graph and a false if either one or more of
	 * the nodes could not be added or were already in this graph.
	 */
	public boolean addNodes(Set<? extends T> setOfNames) {
		boolean result = true;
		for (T name : setOfNames) {
			boolean nodeAdded = this.addNode(name);
			result = result && nodeAdded;
		}
		// No checkRep here because all the modification is occurring the 
		// the add method call.
		return result;
	}

	
	/**
	 * Removes a node from this and all connections to the node.
	 * @param name is the node that will be removed.
	 * @requires The node name to be non-null.
	 * @effects Removes the node and their connections from this.
	 * @modifies The node in this.
	 * @returns A true value if the node was in this graph and was removed
	 * successfully.
	 */
	public boolean removeNode(T name) {
		if (!(this.nodeExists(name))) {
			return false;
		}
		Set<Connection<T, N>> connectionsToRemove = 
				new HashSet<Connection<T, N>>();
		for (Connection<T, N> con : this.connections) {
			if (con.getFrom().equals(name) || con.getTo().equals(name)) {
				connectionsToRemove.add(con);
			}
		}
		for (Connection<T, N> con : connectionsToRemove) {
			this.connections.remove(con);
		}
		this.nodes.remove(name);
		this.checkRep();
		return true;
	}
	
	/**
	 * Removes multiple nodes from this. If a node is not currently in this 
	 * then nothing will happen.
	 * @param setOfNames is a set of the names of all the nodes that will be
	 * removed from this.
	 * @requires setOfNames to be non-null, all of the names within the set are
	 * non-null.
	 * @effects Removes all the nodes from this which are in setOfNames
	 * @modifies The nodes in this.
	 * @returns A true value if every node name in the parameter set was
	 * successfully removed from this graph.
	 */
	public boolean removeNodes(Set<? extends T> setOfNames) {
		boolean result = true;
		for (T name : setOfNames) {
			boolean nodeRemoved = this.removeNode(name);
			result = result && nodeRemoved;
		}
		// No checkRep here because all the modification is occurring the 
		// the remove method call.
		return result;
	}

	
	/**
	 * Removes all the nodes and connections from this.
	 * @effects Removes all nodes and connections.
	 * @modifies The nodes and connections in this.
	 */
	public void clear() {
		this.connections.clear();
		this.checkRep();
	}	
	
	/**
	 * Adds a connection to this graph.
	 * @param from is the name of the node where the connection is coming from.
	 * @param to is the name of the node where this connection is going to.
	 * @param label is the label of the connection.
	 * @requires from, to, and label to all be non-null.
	 * @effects The connections from the node.
	 * @modifies The connections the node from has.
	 * @returns True if the connection was added successfully, and false if the
	 * connection could either not be added or was already there.
	 */
	public boolean addConnection(T from, T to, N label) {
		Connection<T, N> con = new Connection<T, N>(from, to, label);
		return this.addConnection(con);

	}
	
	/**
	 * Adds a connection to this graph.
	 * @param con is the connection being added.
	 * @requires con to be non-null.
	 * @effects The connections from the node.
	 * @modifies The connections the node from has.
	 * @returns True if the connection was added successfully, and false if the
	 * connection could either not be added or was already there.
	 */
	public boolean addConnection(Connection<T, N> con) {
		if (this.connectionExists(con)) {
			return false;
		}
		if (!this.nodes.contains(con.getFrom())) {
			this.nodes.add(con.getFrom());
		}
		if (!this.nodes.contains(con.getTo())) {
			this.nodes.add(con.getTo());
		}
		boolean result = this.connections.add(con);
		this.checkRep();
		return result;
	}
	
	/**
	 * Adds the set of connections to the graph.
	 * @param allCons is the set containing all the connections that will be 
	 * added to this.
	 * @requires All connections in the set and the set to be non-null.
	 * @effects New connections from the input set will be added to this graph.
	 * @modifies This graph.
	 * @returns A true value if every connection in the parameter set was
	 * successfully added to this graph and a false if either one or more of
	 * the connections could not be added or were already in this graph.
	 */
	public boolean addConnections(Set<Connection<T, N>> allCons) {
		boolean result = true;
		for (Connection<T, N> con : allCons) {
			boolean conAdded = this.addConnection(con);
			result = result && conAdded;
		}
		return result;
	}
	
	/**
	 * Removes the specified connection between two nodes.
	 * @param from is the name of the node that the connection is from.
	 * @param to is the name node that the connection is to.
	 * @param label is the label of the connection.
	 * @requires from, to, and label must be non-null.
	 * @effects Removes the connection from one node to another.
	 * @modifies The connections of a node from.
	 * @returns A true if the connection was removed successful and a false if 
	 * the connection was not in the graph.
	 */
	public boolean removeConnection(T from, T to, N label) {
		Connection<T, N> con = new Connection<T, N>(from, to, label);
		return this.removeConnection(con);
	}
	
	/**
	 * Removes a connection to this graph.
	 * @param con is the connection being removed.
	 * @requires con to be non-null.
	 * @effects The connections from the node.
	 * @modifies The connections the node from has.
	 * @returns True if the connection was removed successfully, and false if 
	 * the connection does not exist.
	 */
	public boolean removeConnection(Connection<T, N> con) {
		if (!(this.connectionExists(con))) {
			return false;
		}

		boolean result = this.connections.remove(con);
		if (this.numOfConnections(con.getFrom()) == 0) {
			result = result && this.removeNode(con.getFrom());
		}
		if (this.numOfConnections(con.getTo()) == 0) {
			result = result && this.removeNode(con.getTo());
		}
		
		this.checkRep();
		return result;
	}
	
	/**
	 * Removes the set of connections to the graph.
	 * @param allCons is the set containing all the connections that will be 
	 * removed from this.
	 * @requires All connections in the set and the set to be non-null.
	 * @effects New connections from the input set will be removed from this
	 * graph.
	 * @modifies This graph.
	 * @returns A true value if every connection in the parameter set was
	 * successfully removed from this graph.
	 */
	public boolean removeConnections(Set<Connection<T, N>> allCons) {
		boolean result = true;
		for (Connection<T, N> con : allCons) {
			boolean conRemoved = this.removeConnection(con);
			result = result && conRemoved;
		}
		return result;
	}
	
	/**
	 * Returns whether a connection is in this graph.
	 * @param from is the name of the node the connection is from.
	 * @param to is the name of the node which the connection is to.
	 * @param label is the label of the connection.
	 * @requires All parameters to be non-null.
	 * @return A true if the connection exists in this graph or a false if it
	 * does not.
	 */
	public boolean connectionExists(T from, T to, N label) {
		Connection<T, N> con = new Connection<T, N>(from, to, label);
		return this.connections.contains(con);
	}
	
	/**
	 * Returns whether a connection is in this graph.
	 * @param con is the connection in question.
	 * @param label is the label of the connection.
	 * @requires con to be non-null.
	 * @return A true if the connection exists in this graph or a false if it
	 * does not.
	 */
	public boolean connectionExists(Connection<T, N> con) {
		return this.connections.contains(con);
	}

	/**
	 * Returns a set of nodes which are in the graph.
	 * @return A set of nodes in this graph.
	 */
	public Set<T> getNodes() {
		return new HashSet<T>(this.nodes);
	}
	
	/**
	 * Returns a set of all connections within this graph.
	 * @return The set of connections.
	 */
	public Set<Connection<T, N>> getConnections() {
		return new HashSet<Connection<T, N>>(this.connections);
	}
	
	/**
	 * Checks the existence of a node in this graph.
	 * @param name of the node.
	 * @requires name must be non-null.
	 * @return True if the node is in this graph.
	 */
	public boolean nodeExists(T name) {
		return this.nodes.contains(name);
	}

	/**
	 * Returns a set of all connections that the node is connected with.
	 * @param name of the node.
	 * @requires name must be non-null.
	 * @return A set of connections that from is connected to.
	 * @throws IllegalArguementException if from is not currently in the graph.
	 */
	public Set<Connection<T, N>> isConnectedWith(T name) {
		if (!this.nodeExists(name)) {
			throw new IllegalArgumentException("This node does not exist in "
					+ "the graph.");
		}
		Set<Connection<T, N>> connectedNodes = new HashSet<Connection<T, N>>();
		for (Connection<T, N> con : this.connections) {
			if (con.getFrom().equals(name)) {
				connectedNodes.add(con);
			}
		}
		return connectedNodes;
	}
	
	/**
	 * Returns a set of connection labels between two nodes. If there are no 
	 * connections between the two nodes then it will return an empty set.
	 * @param from is the name of the node connected from.
	 * @param to is the name of the node connected to.
	 * @requires from and to must be non-null.
	 * @return A set of connection labels.
	 * @throws IllegalArguementException if either of the nodes are not
	 * currently in this graph.
	 */
	public Set<N> isConnectedWith(T from, T to) {
		if (!(this.nodeExists(from) && this.nodeExists(to))) {
			throw new IllegalArgumentException("One or more of the nodes do "
					+ "not exist in this graph.");
		}
		Set<N> differentLabels = new HashSet<N>();
		for (Connection<T, N> con : this.connections) {
			if (con.getFrom().equals(from) && con.getTo().equals(to)) {
				differentLabels.add(con.getLabel());
			}
		}
		return differentLabels;
	}
	
	/**
	 * Returns the alphabetically first connection label between two nodes in 
	 * this graph. Will return null if there is no connection between the two.
	 * @param from is the name of the node which the connection is from.
	 * @param to is the name of the node which the connection is to.
	 * @requires from and to must be non-null.
	 * @throws IllegalArguementException if either of the nodes are not
	 * currently in this graph.
	 * @return A label of a connection that goes from 'from' and to 'to'. Null
	 * will be returned if there is no such connection.
	 */
	public N getFirstConnectionLabel(T from, T to) {
		if (!(this.nodeExists(from) && this.nodeExists(to))) {
			throw new IllegalArgumentException("One or more of the nodes"
											+ " do not exist in this graph.");
		}
		Set<N> connectionLabels = new TreeSet<N>(this.isConnectedWith(from, to));
		Iterator<N> it = connectionLabels.iterator();
		if (it.hasNext()) {
			return it.next();
		} else {
			return null;
		}
	}
	
	/**
	 * Returns the number of nodes a specific node is connected to.
	 * @param name of the node.
	 * @requires name to be non-null
	 * @return The number of node a specific node is connected to.
	 * @throws IllegalArgumentException if the node is not currently in
	 * this graph.
	 */
	public int numOfConnectedNodes(T name) {
		Set<T> characters = new HashSet<T>();
		for (Connection<T, N> con : this.connections) {
			if (con.getFrom().equals(name)) {
				characters.add(con.getTo());
			}
		}
		return characters.size();
	}
	
	/**
	 * Returns the number of connections coming from this node.
	 * @param name of the node where the connections are coming from.
	 * @requires from to be non-null.
	 * @return The number of connections coming in this graph from the
	 * parameter from.
	 * @throws IllegalArgumentException if the node is not currently in
	 * this graph.
	 */
	public int numOfConnections(T name) {
		if (!this.nodeExists(name)) {
			throw new IllegalArgumentException("This node does not exist in "
					+ "the graph.");
		}
		int connections = 0;
		for (Connection<T, N> con : this.connections) {
			if (con.getFrom().equals(name)) {
				connections++;
			}
		}
		return connections;
	}
	
	/**
	 * Returns the number of times a certain node is connected with
	 * another node.
	 * @param from is the name of the node connected from.
	 * @param to is the name of the node connected to.
	 * @requires to and from parameters to be non-null.
	 * @return The number of times a node is connected with another node.
	 * @throws InvalidArgumentExcpetion if either of the nodes are not
	 * currently in this graph.
	 */
	public int numOfConnections(T from, T to) {
		if (!(this.nodeExists(from) && this.nodeExists(to))) {
			throw new IllegalArgumentException("One or more of the nodes do "
					+ "not exist in this graph.");
		}
		int connections = 0;
		for (Connection<T, N> con : this.connections) {
			if (con.getFrom().equals(from) && con.getTo().equals(to)) {
				connections++;
			}
		}
		return connections;
	}
	
	@Override
	/**
	 * Compares this with another object to see if the two are equal. A
	 * separate graph is equal if and only if the other object is a graph and
	 * they both contain the exact same nodes each with the exact same
	 * connections.
	 * @returns A boolean true if the two objects are equal and a false if they
	 * do not.
	 */
	public boolean equals(Object o) {
		if (!(o instanceof Graph<?, ?>)) {
			return false;
		}
		Graph<?, ?> other = (Graph<?, ?>) o;
		if (!(this.connections.size() == other.connections.size())) {
			return false;
		}
		for (Connection<T, N> con : this.connections) {
			if (!(other.connections.contains(con))) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Clones the graph to another graph object.
	 * @return A clone of this.
	 */
	public Graph<T, N> clone() {
		Graph<T, N> clone = new Graph<T, N>();
		clone.nodes = new HashSet<T>(this.nodes);
		clone.connections = new HashSet<Connection<T, N>>(this.connections);
		return clone;
	}
	
	@Override
	/**
	 * Returns a hash representation of this graph.
	 * @returns An integer representing the graph.
	 */
	public int hashCode() {
		return this.connections.hashCode();
	}
	
	@Override
	/**
	 * Creates a string representation of this graph and its contents.
	 * @returns A string representation of this graph.
	 */
	public String toString() {
		String result = "";
		Map<T, TreeSet<N>> fromToLabel = new TreeMap<T, TreeSet<N>>();
		for (Connection<T, N> con : this.connections) {
			if (!fromToLabel.containsKey(con.getFrom())) {
				fromToLabel.put(con.getFrom(), new TreeSet<N>());
			}
			fromToLabel.get(con.getFrom()).add(con.getLabel());				
		}

		Set<T> keys = fromToLabel.keySet();
		for (T node : keys) {
			Set<N> allLabels = fromToLabel.get(node);
			for (N label : allLabels) {
				result += node.toString() + " ";
				result += label.toString() + '\n';
			}
		}
		return result;
	}

	
	/**
	 * Creates an iterator which will cycle through the graph. In an 
	 * alphabetical fashion is will go through where the connection is from 
	 * and the book they were in.
	 * @return An iterator over this graph.
	 */
	public Iterator<String> iterator() {
		Map<T, TreeSet<N>> fromToLabel = new TreeMap<T, TreeSet<N>>();
		for (Connection<T, N> con : this.connections) {
			if (!fromToLabel.containsKey(con.getFrom())) {
				fromToLabel.put(con.getFrom(), new TreeSet<N>());
			}
			fromToLabel.get(con.getFrom()).add(con.getLabel());
		}
		
		ArrayList<String> info = new ArrayList<String>();
		Set<T> keys = fromToLabel.keySet();
		for (T node : keys) {
			Set<N> allLabels = fromToLabel.get(node);
			for (N label : allLabels) {
				info.add(node.toString());
				info.add(label.toString());
			}
		}
		return info.iterator();
	}
	
	private void checkRep() {
		// this.connections != null
		assert (this.connections != null) : "The storage device of the "
										+ "connections cannot be null.";
		
		// this.characters != null
		assert (this.nodes != null) : "The storage device of the "
										+ "nodes cannot be null.";
		
		// Very expensive check here therefore it can be toggled off for
		// larger graphs.
		if (DEBUG_MODE) {
			for (Connection<T, N> con1 : this.connections) {
				// for (Connection con : connections) 
				// { characters.contains(con.from) == true }
				assert (this.nodes.contains(con1.getFrom()) == true);
				assert (this.nodes.contains(con1.getTo()) == true);
			}
		}
	}
	
}

