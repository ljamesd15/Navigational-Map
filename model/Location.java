package model;

/**
 * Immutable Location object which is a string-string pair of a building's 
 * abbreviated name and longer name.
 * @author L. James Davidson
 */

public class Location implements Comparable<Location>{
	
	// AF: shortName, longName and entrance cannot be null.
	
	// RI: shortName != null, longName != null, entrance != null
	
	public final String shortName;
	public final String longName;
	public final Point entrance;
	
	/**
	 * Instantiates a building object.
	 * @param shortName is the abbreviated name of the building.
	 * @param longName is the longer version of the name of the building.
	 * @param entrance is the 2-dimensional point which represents where the 
	 * entrance is located.
	 * @requires The shortName, longName and entrance to be non-null.
	 */
	public Location(String shortName, String longName, 
			Point entrance) {
		this.shortName = shortName;
		this.longName = longName;
		this.entrance = entrance;
		this.checkRep();
	}
	
	public Location(Point entrance) {
		this.shortName = "x" + ((Integer)((Double)entrance.getX()).intValue())
				.toString() + "y" + ((Integer)((Double)entrance.getY())
						.intValue()).toString();
		this.longName = "x" + ((Double)entrance.getX()).toString() + "y"
				+ ((Double)entrance.getY()).toString();
		this.entrance = entrance;
	}
	
	@Override
	/**
	 * Returns true if these o is an Building and it has the same 
	 * shortName, longName and entrance.
	 */
	public boolean equals(Object o) {
		if (!(o instanceof Location)) {
			return false;
		}
		Location other = (Location) o;
		if (this.shortName.equals(other.shortName) && 
				this.longName.equals(other.longName) &&
				this.entrance.equals(other.entrance)) {
			return true;
		}
		return false;
	}
	
	@Override
	/**
	 * Returns an integer representing the hash value of this object.
	 */
	public int hashCode() {
		return 3 * this.shortName.hashCode() + 5 * this.longName.hashCode() 
			+ 7 * this.entrance.hashCode();
	}
	
	
	/**
	 * Compares this with another location. A lexicographically smaller 
	 * shortName is seen as the 'lesser' location.
	 */
	public int compareTo(Location other) {
		return this.shortName.compareTo(other.shortName);
	}
	
	@Override
	/**
	 * Returns a string representation of this location object in the following
	 * format.
	 * "abbreviated name: long name"
	 */
	public String toString() {
		return shortName + ": " + longName;
	}
	
	/**
	 * Ensures the representation invariant is satisfied.
	 */
	private void checkRep() {
		// RI: shortName != null, longName != null, entrance != null
		assert (this.shortName != null) : "The building must have an "
				+ "abbreviated name.";
		assert (this.longName != null) : "There must be a longer version of "
				+ "the building name.";
		assert (this.entrance != null) : "There must be an entrance.";
	}
}
