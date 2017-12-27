package model;

/**
 * Immutable point object
 * @author L. James Davidson
 *
 */
public class Point {
	
	private double x;
	private double y;
	
	// AF: this.x and this.y are not null.
	
	// RI: this.x != null, this.y != null
	
	/**
	 * Creates a point representation of the two doubles.
	 * @param x is the x component of the point.
	 * @param y is the y component of the point.
	 * @requires x and y must not be null.
	 * @effects Creates a point representation of the two numbers.
	 */
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
		this.checkRep();
	}
	
	/**
	 * @return Returns the x component of this point.
	 */
	public double getX() {
		return this.x;
	}
	
	/**
	 * @return Returns the y component of this point.
	 */
	public double getY() {
		return this.y;
	}
	
	@Override
	/**
	 * Returns true if two points represent the same point.
	 */
	public boolean equals(Object o) {
		if (!(o instanceof Point)) {
			return false;
		}
		Point other = (Point) o;
		if (this.x == other.getX() && this.y == other.getY()) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	/**
	 * Returns an integer representing the hash value of this point.
	 */
	public int hashCode() {
		return (int) (this.x * 3 + 7 * this.y);
	}
	
	/**
	 * Ensures the representation is satisfied.
	 */
	private void checkRep() {
		// RI: this.x != null, this.y != null
		assert ((Double) this.x != null) : "X cannot be null.";
		assert ((Double) this.y != null) : "Y cannot be null.";
	}
}
