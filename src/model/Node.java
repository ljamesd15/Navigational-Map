package model;

/**
 * Creates an immutable node object.
 * @author L. James Davidson
 *
 * @param <T> is the element type used to give the nodes their name.
 */
public class Node<T> {
		
	private final T nodeName;
	private final Double pathWeight;
	private final Node<T> prevNode;
	
	// AF: Both fields are non-null and the path weight is a non-negative 
	//    value.
	
	// RI: this.nodeName != null, this.pathWeight != null, 
	//    this.pathWeight >= 0,
	
	/**
	 * Creates a node object with the specified node name, sets the path
	 * list to the list of nodes passed in with this node appended to it, 
	 * and path weight to the sum of all the path weights of the nodes 
	 * before it.
	 * @param nodeName is the name of the node.
	 * @param pathWeight is the weight of the path to this node.
	 * @param prevNode is the last node in the chain of the least cost path 
	 * from the start to this node. 
     * path from the start node to this node.
	 * @requires nodeName and pathWeight are non-null
	 * @effects Creates a node with these specified values and prevNode is the
	 * node just before this on the least cost path from start. before.
	 * @throws IllegalArgumentException if the path weight is a negative 
	 * number.
	 */
	public Node(T nodeName, Double weight, Node<T> prevNode) {
		if (weight < 0) {
			throw new IllegalArgumentException("The weight of this node must"
					+ " be non-negative.");
		}
		this.nodeName = nodeName;
		this.pathWeight = weight;
		this.prevNode = prevNode;
		this.checkRep();
	}
	
	/**
	 * Creates a node object with the specified node name and a specified
	 * path weight.
	 * @param nodeName is the name of the node.
	 * @param pathWeight is the smallest weight of the path from the 
	 * starting node to this node found so far.
	 * @requires nodeName and pathWeight to be non-null.
	 * @effects Creates a node with these specified values and no node before 
	 * it.
	 * @throws IllegalArgumentException if the path weight is a negative 
	 * number.
	 */
	public Node(T nodeName, Double pathWeight) {
		this(nodeName, pathWeight, null);
	}
	
	/**
	 * @return The name of the node.
	 */
	public T getNodeName() {
		return this.nodeName;
	}
	
	/**
	 * @return The weight of the path to this node.
	 */
	public Double getPathWeight() {
		return this.pathWeight;
	}
	
	/**
	 * @return The node just before this node.
	 */
	public Node<T> getPrevNode() {
		return this.prevNode;
	}
	
	@Override
	/**
	 * @return The hash code representation of this node.
	 */
	public int hashCode() {
		//Path independent.
		return 3 * this.nodeName.hashCode();
	}
	
	@Override
	/**
	 * @return True if the other object is a node which contains the same 
	 * node name as this.
	 */
	public boolean equals(Object o) {
		if (!(o instanceof Node<?>)) {
			return false;
		}
		Node<?> other = (Node<?>) o;
		if (this.nodeName.equals(other.nodeName)) {
			// Only looking at node name as we don't care how we got to 
			// this node (pathList and pathWeight).
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	/**
	 * @return A string representation of this node.
	 */
	public String toString() {
		return this.nodeName.toString();
	}
	
	/**
	 * Makes sure that the representation invariant of the node class 
	 * is satisfied.
	 */
	private void checkRep() {
		assert(this.nodeName != null);
		assert(this.pathWeight != null);
		assert(this.pathWeight >= 0);
	}
}

