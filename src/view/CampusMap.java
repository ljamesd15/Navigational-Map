package view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import controller.LoadCampus;
import exception.MalformedDataException;
import model.Campus;
import model.Connection;
import model.Location;

/**
 * Text user interface for the campus map application.
 * @author L. James Davidson
 *
 */
public class CampusMap {
	// This class does not represent an ADT it is the UI for finding paths 
	// on the University of Washington Campus.
	
	private static Campus UofW;
	
	/**
	 * Allows user interaction of the campus application. This application 
	 * allows users to see a list of buildings and find the shortest path 
	 * between two buildings.
	 * @param args is unused.
	 */
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		String inputLine;
		boolean keepGoing = true;
		initialise();
		printMenu();
		
		do {

			System.out.print('\n' +  
					"Enter an option ('m' to see the menu): ");
			inputLine = input.nextLine();
			
			// Echo necessary lines until the input line should not be echoed.
			do {
				if (inputLine.equals("")) {
					// Echo lines of white space
					System.out.println("");
				
				} else if (inputLine.charAt(0) == '#') {
					// Also echo lines starting with the pound sign.
					System.out.println(inputLine);
					
				} else {
					break;
				}
				inputLine = input.nextLine();
			} while (true);

			
			if (inputLine.equals("b")) {
				// List all buildings
				printBuildings();
				
			} else if (inputLine.equals("r")) {			
				// Initialize necessary data structures.
				List<Connection<Location, Double>> steps = 
						new ArrayList<Connection<Location, Double>>();
				List<String> directions = new ArrayList<String>();
				boolean validBuildings;
				
				// Get building names from user and check if they are valid.
				List<String> buildingNames = new ArrayList<String>();
				List<Location> buildings = new ArrayList<Location>();
				getBuildingInfo(input, buildingNames);
				validBuildings = areValidBuildings(buildingNames, buildings);
				Location buildingFrom = buildings.get(0);
				Location buildingTo = buildings.get(1);

				// If valid then attempt to find a path.
				if (validBuildings) {
					steps = UofW.shortestRouteFromTo(buildingFrom, buildingTo,
							directions);
					printDirections(steps, directions, buildingFrom,
							buildingTo);
				}
			} else if (inputLine.equals("q")) {
				// Set keepGoing to false so that this is the last interaction
				// with this user at this time.
				keepGoing = false;
				
			} else if (inputLine.equals("m")) {
				// Print available commands
				printMenu();
				
			} else {
				System.out.println("Unknown option");
			}
			
		} while (keepGoing);
		input.close();
	}
	
	/**
	 * This initializes all of the information regarding the UofW campus.
	 */
	private static void initialise() {
		try {
			UofW = LoadCampus.loadInfo();
		} catch (IOException e ) {
			System.out.println("The files to initialise the UofW campus are "
					+ "not in the proper location.");
		} catch (MalformedDataException e) {
			System.out.println("The files to initialise the UofW campus are "
					+ "improperly formatted.");
		}
	}
	
	/**
	 * Prints out a list of all the buildings on the campus alphabetically
	 * by abbreviated name.
	 */
	private static void printBuildings() {
		// Sort them by abbreviated name.
		Set<Location> sortedBuildings = new TreeSet<Location>();
		sortedBuildings.addAll(UofW.getBuildings());
		System.out.println("Buildings:");
		
		// Now printing all of the buildings whose toString are already in the
		// correct format.
		for (Location building : sortedBuildings) {
			System.out.println(building);
		}
	}

	/**
	 * Prints the initial info before directions are given. 
	 * @param from is the location the path starts from.
	 * @param to is the location where the path ends.
	 * @requires from and to must be non-null.
	 */
	private static void getBuildingInfo(Scanner input, List<String> buildings) {
		// Get info from user.
		System.out.print("Abbreviated name of starting building: ");
		String from = input.nextLine().trim();
		System.out.print("Abbreviated name of ending building: ");
		String to = input.nextLine().trim();
		
		// Store info.
		buildings.add(from);
		buildings.add(to);
	}
	
	/**
	 * Determines if two buildings exist in this campus.
	 * @param from is the abbreviated name of where the path is from.
	 * @param to is the abbreviated name of where the path ends.
	 * @requires from and to must be non-null.
	 * @return True if both buildings are in this campus.
	 */
	private static boolean areValidBuildings(List<String> buildingName, 
			List<Location> buildings) {
		boolean validBuildings = true;
		
		String from = buildingName.get(0);
		String to = buildingName.get(1);
		
		if (UofW == null) {
			System.out.println("null");
		}
		
		// Store information
		buildings.add(UofW.getBuilding(from));
		buildings.add(UofW.getBuilding(to));
		
		if (!(UofW.getBuildings().contains(buildings.get(0)))) {
			printUknownBuilding(from);
			validBuildings = false;
		}
		if (!(UofW.getBuildings().contains(buildings.get(1)))) {
			printUknownBuilding(to);
			validBuildings = false;
		}
		return validBuildings;
	}
	
	/**
	 * Alerts user to the fact that a building is not in this campus.
	 */
	private static void printUknownBuilding(String unkBuilding) {
		System.out.println("Unknown building: " + unkBuilding);
	}
	
	/**
	 * This will print the directions given by the parameters.
	 * @param steps is the list of connections from one step to the next.
	 * @param directions is the list of string directions corresponding the 
	 * same index of steps.
	 * @requires steps, directions and all of their contents are non-null. The
	 * directions at index i must corresponding to the direction going in the 
	 * steps at index i.
	 */
	private static void printDirections(
			List<Connection<Location, Double>> steps,
			List<String> directions, Location from, Location to) {
		
		int size = steps.size();
		double totalDistance = 0.0;
		
		System.out.println("Path from " + from.longName + " to " 
				+ to.longName + ":");
				
		// Inv: The steps from 0 to i - 1 have been printed out successfully.
		for (int i = 0; i < size; i++) {
			
			Connection<Location, Double> nextStep = steps.get(i);
			
			// Each label is the combination of all of the previous steps due
			// to how Dijkstras is implemented.
			double distance = nextStep.getLabel() - totalDistance;		
			
			// Print the info.
			System.out.print('\t' + "Walk ");
			System.out.printf("%.0f", distance);
			
			System.out.print(" feet " + directions.get(i) + " to (");
			System.out.printf("%.0f", nextStep.getTo().entrance.getX());
			
			System.out.print(", ");
			System.out.printf("%.0f", nextStep.getTo().entrance.getY());
			
			System.out.println(")");
			
			// Add to the total distance.
			totalDistance += distance;
		}
		// Q: i = size. All steps from 0 to and including size - 1 are printed.
		
		// Print the final bit of info for the route.
		System.out.print("Total distance: ");
		System.out.printf("%.0f", totalDistance);
		System.out.println(" feet");
	}
	
	/**
	 * Prints the available menu commands.
	 */
	private static void printMenu() {
		System.out.println("Menu:");
		System.out.println('\t' + "r to find a route");
		System.out.println('\t' + "b to see a list of all buildings");
		System.out.println('\t' + "q to quit");

	}
}
