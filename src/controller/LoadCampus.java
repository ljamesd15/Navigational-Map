package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import exception.MalformedDataException;
import model.Campus;
import model.Connection;
import model.Graph;
import model.Location;
import model.Point;

/**
 * Initializes all the information for the model files to access and manipulate
 * in this application.
 * @author L. James Davidson
 */
public class LoadCampus {
	// This class does not represent an ADT. It merely loads the campus 
	// information and gives the UI the ability to access other model 
	// representation.
	
	/**
	 * Loads up the graph and other necessary information for the University 
	 * of Washington campus.
	 * @throws IOException if the file for the building does not exist.
	 * @throws MalformedDataException if the data within the campus data
	 * files are not formatted properly.
	 * @return The model containing all the necessary info for the application.
	 */
	public static Campus loadInfo() throws IOException, 
			MalformedDataException {
		
		//long start = System.nanoTime();
		
		// Initialize the graph for the all model files.
		Graph<Location, Double> campus = new Graph<Location, Double>();
		
		// Parse both data files for the information.
		Set<Location> buildings = parseBuildingData();
		Set<Connection<Location, Double>> paths = parsePathData();

		// Connect buildings to paths
		connectBuildingsToPaths(buildings, paths);
			
		// Add data to the graph.
		campus.addNodes(buildings);
		campus.addConnections(paths);
		
		// Initialize the model of the application.
		Campus UofW = new Campus(campus, buildings);
		
		//long end = System.nanoTime();
		//double difference = (double) (end - start) / 1000000000.0;
		//System.out.printf("It took %.2f", difference);
		//System.out.println(" seconds to load the campus.");
		
		return UofW;
	}
	
	/**
	 * Parses the data from the buildings file to get the information about 
	 * the buildings and their entrances.
	 * @throws IOException if the file for the building does not exist.
	 * @throws MalformedDataException if the data within the campus buildings
	 * file is not formatted properly.
	 * @return A set of the buildings parsed from the campus buildings file.
	 */
	private static Set<Location> parseBuildingData() throws IOException, 
											MalformedDataException {
		File buildings = new File("src/data/campus_buildings.dat");
		
		// List of all buildings to be added to the graph as nodes.
		Set<Location> buildingNodes = new HashSet<Location>();
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(buildings));
			String inputLine = reader.readLine();
			
			// Inv: While there is data left to process.
			while (inputLine != null) {
				
				// Splitting data at the tab delineations between tokens.
				String[] tokens = inputLine.split("\t");
				
				// If it isn't in the proper format then throw a 
				// MalformedDataException.
				if (tokens.length != 4) {
	                throw new MalformedDataException("Line "
	                		+ "should contain exactly three tabs: " 
	                		+ inputLine);
				}
				
				// Extract info from tokens and create necessary info.
				Double xCoord = Double.parseDouble(tokens[2]);
				Double yCoord = Double.parseDouble(tokens[3]);
				Point entrance = new Point(xCoord, yCoord);
				Location building = new Location(tokens[0], tokens[1], 
						entrance);
				buildingNodes.add(building);
				
				inputLine = reader.readLine();
			}
		} catch (IOException e) {
			throw new IOException();
		} finally {
	        if (reader != null) {
	            try {
	                reader.close();
	            } catch (IOException e) {
	                System.err.println(e.toString());
	                e.printStackTrace(System.err);
	            }
	        }
		}
		return buildingNodes;
	}
	
	/**
	 * Parses the data from the paths file to get a set of connections between 
	 * each set of coordinates.
	 * @throws IOException if the file for the building does not exist.
	 * @throws MalformedDataException if the data within the campus paths
	 * file is not formatted properly.
	 * @return A set of connections between locations parsed from the campus 
	 * paths file.
	 */
	private static Set<Connection<Location, Double>> parsePathData() 
			throws IOException, MalformedDataException {
		File paths = new File("src/data/campus_paths.dat");
		
		// Set of paths to be added as connections.
		Set<Connection<Location, Double>> pathConnections = 
				new HashSet<Connection<Location, Double>>();
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(paths));
			String inputLine = reader.readLine();
			
			// Inv: While there is data left to process.
			while (inputLine != null) {
				
				// Splitting line so I can access the data.
				String[] coordinates = inputLine.split(",");
				
				// If it isn't in the proper format then throw a 
				// MalformedDataException.
				if (coordinates.length != 2) {
					throw new MalformedDataException("Line "
	                		+ "should contain two doubles seperated by a comma"
	                		+ ": " + inputLine);
				}
				
				// Create the starting location.
				Double xStartCoord = Double.parseDouble(coordinates[0]);
				Double yStartCoord = Double.parseDouble(coordinates[1]);
				Point startPoint = new Point(xStartCoord, yStartCoord);
				Location startLoc = new Location(startPoint);

				// Inv: While there are still more end points to create 
				// connections with the starting point.
				inputLine = reader.readLine();
				while (inputLine != null && inputLine.indexOf("\t") == 0) {

					String[] connectionInfo = inputLine.split(" ");
					
					// If it isn't in the proper format then throw a 
					// MalformedDataException.
					if (coordinates.length != 2) {
						throw new MalformedDataException("Line "
		                		+ "should contain two doubles seperated by a "
		                		+ "space: " + inputLine);
					}
					Double distance = Double.parseDouble(connectionInfo[1]);
					
					// Splitting the first token again to get the coordinate 
					// info.
					coordinates = connectionInfo[0].split(",");
					
					// If it isn't in the proper format then throw a 
					// MalformedDataException.
					if (coordinates.length != 2) {
						throw new MalformedDataException("Line "
		                		+ "should contain two doubles seperated by a "
		                		+ "comma: " + inputLine);
					}
					
					// Removing the colon from the end of the yCoord.
					coordinates[1] = coordinates[1].substring
							(0, coordinates[1].length() - 1);
					
					// Create the ending location.
					Double xEndCoord = Double.parseDouble(coordinates[0]);
					Double yEndCoord = Double.parseDouble(coordinates[1]);
					Point endPoint = new Point(xEndCoord, yEndCoord);
					Location endLoc = new Location(endPoint);
					
					// Create the connection between the two coordinates with 
					// the distance as the connection label and add it to the 
					// set of paths.
					Connection<Location, Double> path = new Connection
							<Location, Double>(startLoc, endLoc, distance);
					pathConnections.add(path);
					
					// Create the opposite version to preserve 
					// bi-directionality to the paths.
					Connection<Location, Double> oppositePath = new Connection
							<Location, Double>(endLoc, startLoc, distance);
					pathConnections.add(oppositePath);
					
					// Move onto next line to either add the next connection 
					// or move onto next starting point.
					inputLine = reader.readLine();
					
				}
				// Q: No more connections to make with this starting point.
				
				// No need to get next line for the next starting point since 
				// it was primed in the do-while loop above.
			}
		} catch (IOException e) {
			throw new IOException();
		} finally {
	        if (reader != null) {
	            try {
	                reader.close();
	            } catch (IOException e) {
	                System.err.println(e.toString());
	                e.printStackTrace(System.err);
	            }
	        }
		}
		
		return pathConnections;
	}
	
	/**
	 * This method ensures that all buildings are connected to the paths that 
	 * start and end at their location.
	 * @param allBuildings is the set of all the buildings on this campus.
	 * @param allPaths is the set of all paths on this campus.
	 * @requires allPaths and allBuildings and all of each of their contents 
	 * to be non-null.
	 * @effects Add connections between buildings to paths which start/end at 
	 * their location.
	 * @modifies allPaths
	 */
	private static void connectBuildingsToPaths(Set<Location> allBuildings, 
			Set<Connection<Location, Double>> allPaths) {
		
		// Copying allPaths so when we add we won't get a concurrent 
		// modification error.
		Set<Connection<Location, Double>> copiedSet = 
				new HashSet<Connection<Location, Double>>(allPaths);
		
		// Inv: Each building already seen will have connections with all the 
		// paths that end/start at its location.
		for (Location building : allBuildings) {
			for (Connection<Location, Double> path : copiedSet) {
				Location from = path.getFrom();
				Location to = path.getTo();
				
				// Checking to see if the buildings are connected to one part 
				// of any of the connections. If it is add a connection between
				// that building and the beginning of the path and set it's 
				// cost to zero.
				if (building.entrance.equals(from.entrance)) {
					
					// Create a two connections from this relationship to 
					// maintain bi-directionality.
					Connection<Location, Double> con = new Connection
							<Location, Double>(building, from, 0.0);
					Connection<Location, Double> reverseCon = new Connection
							<Location, Double>(from, building, 0.0);
					
					// Add them this connection to all the paths.
					allPaths.add(con);
					allPaths.add(reverseCon);
				} else if (building.entrance.equals(to.entrance)) {
					// Doing the same exact thing except where the connection 
					// is to so that paths can go to the building.
					
					// Create a two connections from this relationship to 
					// maintain bi-directionality.
					Connection<Location, Double> con = new Connection
							<Location, Double>(building, to, 0.0);
					Connection<Location, Double> reverseCon = new Connection
							<Location, Double>(to, building, 0.0);
					
					// Add them this connection to all the paths.
					allPaths.add(con);
					allPaths.add(reverseCon);
				}
			}
		
		}
		// Q: All buildings have been seen. All buildings will have connections
		// with all the paths that end/start at its location.
	}
}
