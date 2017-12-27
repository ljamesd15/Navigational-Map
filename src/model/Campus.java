package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import controller.DijkstrasAlgorithm;
import model.Connection;
import model.Graph;
import model.Node;

/**
 * This is the main model file for the campus paths application. It can be 
 * instantiated to store data pertaining to the campus.
 * @author L. James Davidson
 */
public class Campus {
	// AF: The graph, campus, must be non-null as well as the set of buildings
	// in the campus. There should also be at least one building otherwise it 
	// makes no sense to attempt to find a path between no buildings.
	
	// RI: campus != null, buildings != null, buildings.size() > 1
	
	private Graph<Location, Double> campusGraph;
	private Set<Location> buildings;
	
	/**
	 * Creates a model of data of a campus.
	 * @param campusGraph is the graph containing the data of the campus.
	 * @param buildings is the set of buildings on this campus.
	 * @requires campusGraph and buildings are both non-null. All the elements 
	 * in buildings are also non-null.
	 * @effects Creates a new campus object.
	 */
	public Campus(Graph<Location, Double> campusGraph, 
			Set<Location> buildings) {
		this.campusGraph = campusGraph;
		this.buildings = buildings;
		this.checkRep();
	}
	
	/**
	 * Returns an unmodifiable set of the available buildings on campus.
	 */
	public Set<Location> getBuildings() {
		return Collections.unmodifiableSet(this.buildings);
	}
	
	/**
	 * Returns the location object which has the abbreviated name given by the
	 * parameter.
	 * @param abbreviatedName is the short name of the building.
	 * @requires abbreviatedName is non-null.
	 * @return The location specified by the parameter or null if the location
	 * does not exist.
	 */
	public Location getBuilding(String abbreviatedName) {
		for (Location building : this.buildings) {
			if (building.shortName.equals(abbreviatedName)) {
				return building;
			}
		}
		return null;
	}
	
	/**
	 * Returns a location object which has this entrance.
	 * @param entrance is the entrance point of the building.
	 * @requires abbreviatedName is non-null.
	 * @return The location specified by the parameter or null if the location
	 * does not exist.
	 */
	public Location getBuilding(Point entrance) {
		for (Location building : this.buildings) {
			if (building.entrance.equals(entrance)) {
				return building;
			}
		}
		return null;
	}
	
	/**
	 * Finds the shortest route between two buildings on campus.
	 * @param from is the building that you are going from.
	 * @param to is the building you are going to
	 * @param directions is a list of strings which must be empty.
	 * @requires from and to both need to be valid buildings in this campus.
	 * directions must also be empty. There must be a valid path between the
	 * two locations in this campus.
	 * @effects Adds all the directions of each step to the list parameter.
	 * @modifies The list parameter, directions.
	 * @return A list of connections which in order describes the paths needed
	 * to take to find the shortest path. A null is returned if no path is 
	 * found.
	 */
	public List<Connection<Location, Double>> shortestRouteFromTo
			(Location from, Location to, List<String> directions) {
		
		List<Node<Location>> route = DijkstrasAlgorithm.
				findShortestweightedPath(from, to, this.campusGraph);
		int size = route.size();
		List<Connection<Location, Double>> result = 
				new ArrayList<Connection<Location, Double>>(size - 1);
		
		Node<Location> curr = route.get(size - 1);
		
		// Inv: All connections from the end of route up to but not including 
		// curr and curr.prevNode will be added to result.
		while (curr.getPrevNode() != null) {
			Location conFrom = curr.getPrevNode().getNodeName();
			Location conTo = curr.getNodeName();
			
			Connection<Location, Double> con = new Connection<Location, Double>
					(conFrom, conTo, curr.getPathWeight());
			result.add(0, con);
			directions.add(0, findDirection(conFrom.entrance, conTo.entrance));
			
			// Move backwards down the chain.
			curr = curr.getPrevNode();
		}
		// Q: All the nodes have been looked through and prevNode is null. 
		// Therefore all connections have been added.
		
		
		// The first and last step are 0 ft because each building entrance is
		// connected to the end of the path which is 0 ft. This step is not 
		// necessary to let the user know to move 0 ft.
		result.remove(result.size() - 1);
		directions.remove(directions.size() - 1);
		result.remove(0);
		directions.remove(0);
		
		return result;
	}
	
	/**
	 * Determines the cardinal or intermediate direction between two points.
	 * @param from is the point from which the path starts.
	 * @param to is the point to which the path ends.
	 * @requires from and to are non-null.
	 * @return A string representation of the direction this path goes.
	 */
	private String findDirection(Point from, Point to) {
		// This will make the following method more readable to store Math.PI
		// in a variable.
		double pi = Math.PI;
		
		// Calculate the angle.

		// Multiplying the rise by -1 because the points y values increase as 
		// they go down when normally y increases as it goes up.
		double rise = -1 * (to.getY() - from.getY());
		double run = to.getX() - from.getX();
		double angle = Math.atan2(rise, run);
		
		// Math.atan2 only outputs angles from - pi/2 to pi/2 so if the run is
		// negative then we need to add pi to it so we can get the correct 
		// angle.
		if (run < 0.0) {
			angle = (2* pi) + angle;
		}
		
		// Angles from Math.atan2 can be negative, but in my if else statements
		// I assume all angles are non-negative and less than 2 * pi. This will
		// keep the angle the same but have a valid representation of the same
		// angle.
		if (angle < 0.0) {
			angle = (2 * pi) + angle;
		} else if (angle > 2 * pi) {
			angle = angle % (2 * pi);
		}
		
		// Find the correct direction from the calculated angle.
		if (angle <= pi / 8 || angle >= (15 * pi) / 8) {
			return "E";
		} else if (angle > pi / 8 && angle < (3 * pi) / 8) {
			return "NE";
		} else if (angle >= (3 * pi) / 8 && angle <= (5 * pi) / 8) {
			return "N";
		} else if (angle > (5 * pi) / 8 && angle < (7 * pi) / 8) {
			return "NW";
		} else if (angle >= (7 * pi) / 8 && angle <= (9 * pi) / 8) {
			return "W";
		} else if (angle > (9 * pi) / 8 && angle < (11 * pi) / 8) {
			return "SW";
		} else if (angle >= (11 * pi) / 8 && angle <= (13 * pi) / 8) {
			return "S";
		} else { // angle > (13 * pi) / 8 && angle < (15 * pi) / 8
			return "SE";
		}
	}
	
	@Override
	/**
	 * Returns a string representation of this model. Includes info about the 
	 * buildings and the campus.
	 */
	public String toString() {
		String result = this.campusGraph.toString();
		for (Location loc : this.buildings) {
			result += '\n' + loc.toString();
		}
		return result;
	}
	
	/**
	 * Ensures the representation invariant of Campus is satisfied.
	 */
	private void checkRep() {
		// RI: campus != null, buildings != null, buildings.size() > 1
		assert (this.campusGraph != null) : "Campus cannot be null.";
		assert (this.buildings != null) : "Buildings cannot be null.";
		assert (this.buildings.size() > 0) : "There must be at least one "
				+ "building on the campus.";
	}
}
