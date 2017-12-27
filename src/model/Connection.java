package model;

/**
 * Creates a connection object. Connect two items together with a connection label.
 * @author L. James Davidson
 *
 * @param <T> is the type of objects which will be connected.
 * @param <N> is the connection label bbetween two connected objects.
 */
public class Connection<T extends Comparable<T>, N extends Comparable<N>> {
		
	private final T from;
	private final T to;
	private final N label;
	private final int hashCode;
	
	// Abstract Function:
	// The character name to and from the connection must not be null. The
	// book title of where the characters connected must be non-null.
	
	// Representation Invariant:
	// this.from != null, this.to != null, this.bookTitle != null, 
	
	/**
	 * This will create a connection between two characters.
	 * @param from is where the connection stems from.
	 * @param to is where the connection goes to.
	 * @param bookTitle is the title of the book where the characters 
	 * connected.
	 * @requires to, from, and bookTitle parameters to be non-null.
	 * @effects Creates a new connection from one individual to another.
	 */
	public Connection(T from, T to, N label) {
		this.from = from;
		this.to = to;
		this.label = label;
		this.checkRep();
		// checkRep() is run before calculating the hashCode so that
		// nothing extra is calculated before we are sure that the fields
		// have been initialized correctly. There is no need to call
		// checkRep() after this since Connection is an immutable class.
		this.hashCode = (from.hashCode() + to.hashCode() 
						+ label.hashCode());
	}
	
	/**
	 * @return Where this connection is from.
	 */
	public T getFrom() {
		return this.from;
	}
	
	/**
	 * @return Where this connection is to.
	 */
	public T getTo() {
		return this.to;
	}
	
	/**
	 * @return The edge label for this.
	 */
	public N getLabel() {
		return this.label;
	}
	
	@Override
	/**
	 * Returns a hash code value for this connection.
	 * @return An integer representing this connection.
	 */
	public int hashCode() {
		return this.hashCode;
	}
	
	@Override
	/**
	 * Determines if two connections are equal to one another.
	 * @returns True if the two connections are equal.
	 */
	public boolean equals(Object o) {
		if (!(o instanceof Connection<?, ?>)) {
			return false;
		}
		Connection<?, ?> con = (Connection<?, ?>) o;
		if (this.from.equals(con.from) && this.to.equals(con.to) 
			&& this.label.equals(con.label)) {
			return true;
		} else {
			return false;
	}

	}
	
	/**
	 * This makes sure that the representation invariant for the connection
	 * class is satisfied.
	 */
	private void checkRep() {
		assert (!(this.from.equals(null) || this.to.equals(null) 
				 || this.label.equals(null))): 
				 "None of the Connection class' fields can be null";
	}
	
	/**
	 * Returns a string representation of the connection. The format of the
	 * string is "this.from this.to this.bookTitle"
	 * @returns A string representation of this connection
	 */
	public String toString() {
		return this.from + " " + this.to + " " + this.label;
	}
}
