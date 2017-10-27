package view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import controller.FileParser;
import exception.MalformedDataException;
import model.Connection;
import model.Graph;

/**
 * Allows users to interact with graphs. They can load graphs, remove graphs, 
 * see the nodes in the graph, and find a fewest edge path between two nodes.
 * @author L. James Davidson
 */
public class GraphPaths {

	// Holds all the graphs in a map so that the user can receive the correct
	// graph when they give the name which is tied to the graph.
	private static Map<String, Graph<String, String>> graphHolder;
	private static final String FILE_PREFIX = "src\\data\\";
	
	/**
	 * User interface where the user can interact with graphs.
	 * @param args
	 * @throws MalformedDataException if the file is not well-formed: each line
	 * contains exactly two tokens separated by a tab, or else starting with a 
	 * # symbol to indicate a comment line.
	 * @throws InterruptedException if interrupt is called during a slight 
	 * pause.
	 */
	public static void main(String[] args) throws MalformedDataException, 
												  InterruptedException {
		graphHolder = new HashMap<String, Graph<String, String>>();
		Scanner input = new Scanner(System.in);
		String userInput;

		System.out.println("Welcome to Graph Paths!");
		System.out.println("Type '0' to see a list of commands.");
		System.out.print("What would you like to do? ");

		do {
			userInput = input.nextLine();
			if (userInput.equals("0")) {
				getCommands();
			} else if (userInput.equals("1")) {
				loadAGraph(input);
			} else if (userInput.equals("2")) {
				removeAGraph(input);
			} else if (userInput.equals("3")) {
				listGraphs();
			} else if (userInput.equals("4")) {
				getCharacters(input);
			} else if (userInput.equals("5")) {
				findAPath(input);
			} else if (userInput.equals("exit")) {
				System.out.println(
						"Thank you for using Graph Paths! Goodbye.");
				input.close();
				System.exit(1);
			} else {
				System.out.println("unrecognized command: " + userInput);
				System.out.println("Type '0' to see a list of "
						+ "valid commands." + '\n');
			}
			System.out.print("What else would you like to do? ");
		} while (true);
		// Using exit commands and then System.exit(1).
	}

	/**
	 * Prints a list of available commands.
	 */
	private static void getCommands() {
		System.out.println('\n' + "Commands are:");
		System.out.println("'1' to load a new graph.");
		System.out.println("'2' to remove a graph.");
		System.out.println("'3' to list all of the loaded graphs.");
		System.out.println("'4' to get an alphabetized list of characters.");
		System.out.println("'5' to search for a path between two nodes in "
				+ "a multigraph.");
		System.out.println("'exit' to leave." + '\n');
	}

	/**
	 * Loads a graph and adds it to graphHolder
	 * @param input reads user input.
	 * @throws MalformedDataException if the file is not well-formed: each line
	 * contains exactly two tokens separated by a tab, or else starting with a
	 * # symbol to indicate a comment line.
	 */
	private static void loadAGraph(Scanner input) {
		System.out.print('\n' + "What file would you like to load? ");
		String filename = input.nextLine();
		while (graphHolder.containsKey(filename)) {
			System.out.println("The graph '" + filename + "' has already been "
					+ "loaded. Please try loading another file.");
			System.out.println("If you do not wish to load a new file then "
					+ "type 'back' to return to the menu.");
			System.out.print('\n' + "What file would you like to load? ");
			filename = input.next();
			if (filename.equals("back")) {
				System.out.println();
				return;
			}
		}
		boolean properFormat = true;
		File f = new File(FILE_PREFIX + filename);
		Graph<String, String> loadedGraph = new Graph<String, String>();
		boolean properFile = true;
		
		do {
			try {
				loadedGraph = loadGraph(filename);
				properFormat = true;
			} catch (IOException e) {
		        System.err.println(e.toString());
		        e.printStackTrace(System.err);
				System.out.println('\n' + "'" + filename + "' does not exist. "
						+ "Please try a different file.");
				System.out.println("If you do not wish to load a new file then"
						+ " type 'back' to return to the menu.");
			} catch (MalformedDataException e) {
				System.out.println('\n' + "'" + filename + "' is not properly "
						+ "formatted. Please try a different file.");
				properFormat = false;
			}
			properFile = f.exists() && properFormat;
			if (!properFile) {
				System.out.print('\n' + "What graph would you like to load? ");
				filename = input.nextLine();
				if (filename.toLowerCase().equals("back")) {
					System.out.println();
					return;
				}
				f = new File(FILE_PREFIX + filename);
			}
		} while (!properFile);
		graphHolder.put(filename, loadedGraph);
		System.out.println("Succesfully loaded graph: " + filename + '\n');
	}

	/**
	 * Removes a graph from graphHolder
	 * @param input to read user input.
	 */
	private static void removeAGraph(Scanner input) {
		if (graphHolder.keySet().size() == 0) {
			System.out.println("There are currently no graphs to remove." 
					+ '\n');
		} else {
			System.out.print('\n' + "What graph would you like to remove? ");
			String filename = input.nextLine().toLowerCase();
			while (!graphHolder.containsKey(filename)) {
				System.out.println('\n' + "The graph '" + filename + "' has "
						+ "not been loaded. Please try removing another "
						+ "graph.");
				System.out.println("Type 'graphs' if you would like to "
						+ "see a list of currently loaded graphs.");
				System.out.println("If you do not wish to remove a new file "
						+ "then type 'back' to return to the menu.");
				System.out.print('\n' + "What graph would you like to "
						+ "remove? ");
				filename = input.nextLine().toLowerCase();
				if (filename.equals("graphs")) {
					listGraphs();
					System.out.print('\n' + "What graph would you like to "
							+ "remove? ");
					filename = input.nextLine().toLowerCase();
				} else if (filename.equals("back")) {
					System.out.println();
					return;
				}
			}
			graphHolder.remove(filename);
			System.out.println("Succesfully removed graph: " + filename 
					+ '\n');
		}
	}
	
	/**
	 * Lists the current graphs that have been loaded by the user up to this 
	 * point.
	 */
	private static void listGraphs() {
		Set<String> setOfGraphs = new TreeSet<String>(graphHolder.keySet());
		if (setOfGraphs.size() == 0) {
			System.out.println("There are currently no graphs loaded." + '\n');
		} else {
			for (String graphName : setOfGraphs) {
				System.out.println(graphName);
			}
			System.out.println();
		}
	}

	/**
	 * Prints a list of characters from a specific graph.
	 * @param input to read user input.
	 */
	private static void getCharacters(Scanner input) {
		if (graphHolder.keySet().size() == 0) {
			System.out.println("There are currently no graphs to retrieve "
					+ "characters from." + '\n');
		} else {
			System.out.print('\n' + "From what graph would you like to see the"
					+ " list of characters? ");
			String userInput = input.nextLine().toLowerCase();
			while (!graphHolder.containsKey(userInput)) {
				System.out.println('\n' + "The graph '" + userInput + "' has not"
						+ " been loaded. Please type the name of a currently "
						+ "loaded graph.");
				System.out.println("If you would like to see a list of valid "
						+ "graph names please type 'graphs'.");
				System.out.println("If you would like leave and return to the "
						+ "main menu type 'back'.");
				System.out.print('\n' + "From what graph would you like to "
						+ "see the list of characters? ");
				userInput = input.nextLine().toLowerCase();
				if (userInput.equals("graphs")) {
					listGraphs();
					System.out.print("From what graph would you like to"
							+ " see the list of characters? ");
					userInput = input.nextLine().toLowerCase();
				} else if (userInput.equals("back")) {
					System.out.println();
					return;
				}
			}
			Graph<String, String> targetGraph = graphHolder.get(userInput);
			Set<String> characterList = targetGraph.getNodes();
			System.out.println("Characters from " + userInput + ":");
			for (String character : characterList) {
				System.out.println(character);
			}
			System.out.println();
		}
	}

	/**
	 * Finds a path between two characters in a specific graph.
	 * @param input to read user input.
	 */
	private static void findAPath(Scanner input) {
		if (graphHolder.keySet().size() == 0) {
			System.out.println("There are currently no loaded graphs, "
					+ "please load a graph before attempting to find a path." 
					+ '\n');
		} else {
			System.out.print('\n' + "What graph would you like to find a path "
					+ "in? " );
			String userInput = input.nextLine().toLowerCase();
			while (!graphHolder.containsKey(userInput)) {
				System.out.println('\n' + "The graph '" + userInput + "' has "
						+ "not been loaded. Please type the name of a "
						+ "currently loaded graph.");
				System.out.println("If you would like to see a list of valid "
						+ "graph names please type 'graphs'.");
				System.out.println("If you would like leave and return to the "
						+ "main menu type 'back'.");
				System.out.print('\n' + "From what graph would you like to "
						+ "find a path in? ");
				userInput = input.nextLine().toLowerCase();
				if (userInput.equals("graphs")) {
					listGraphs();
					System.out.print("From what graph would you like to find"
							+ " a path in? ");
					userInput = input.nextLine().toLowerCase();
				} else if (userInput.equals("back")) {
					System.out.println();
					return;
				}
			}
			String graphName = userInput;
			Graph<String, String> multigraph = graphHolder.get(graphName);
			boolean validResponse = false;
			String[] characterNames = new String[0];
			
			System.out.println('\n' + "Please type the character names in the "
					+ "following format");
			System.out.println("<CHARACTER-FROM> <CHARACTER-TO>");
			System.out.println("To see a valid list of characters type 'list'"
					+ '\n');
			System.out.println("Remember to use proper casing for "
					+ "the character names.");
			System.out.print("From what two characters would you like to"
					+ " find a path. ");
			userInput = input.nextLine();
			
			do {
				characterNames = userInput.split(" ");
				if (!(characterNames.length == 2 || userInput.toLowerCase().
						equals("get list of characters"))) {
					System.out.println('\n' + "Invalid character format, "
							+ "please type the character names in the "
							+ "following format");
					System.out.println("<CHARACTER-FROM> <CHARACTER-TO>");
					System.out.println("To see a valid list of characters type"
							+ " 'list'");
					System.out.println("If you would like leave and return to"
							+ " the main menu type 'back'." + '\n');
					System.out.println("Remember to use proper casing for "
							+ "the character names.");
					System.out.print("From what two characters would you like "
							+ "to find a path. ");
					userInput = input.nextLine();
				} else if (userInput.toLowerCase().equals("list")) {
					Set<String> characterList = multigraph.getNodes();
					System.out.println("Characters from " + graphName + ":");
					for (String character : characterList) {
						System.out.println(character);
					}
					System.out.println('\n' + "Remember to use proper casing "
							+ "for the character names.");
					System.out.print("From what two characters would you "
							+ "like to find a path. ");
					userInput = input.nextLine();
				} else if (userInput.toLowerCase().equals("back")) {
					System.out.println();
					return;
				} else {
					validResponse = true;
				}
				
			} while (!validResponse);
			
			characterNames = userInput.split(" ");
			String from = characterNames[0];
			String to = characterNames[1];
			
			if (!(multigraph.nodeExists(from) 
					&& multigraph.nodeExists(to))) {
				System.out.print("unknown character");
				if (!multigraph.nodeExists(from)) {
					System.out.print(" " + from);
				}
				if (!multigraph.nodeExists(to)) {
					System.out.print(" " + to);
				}
				System.out.println("");
			} else {
				String[] solution;
				if (from.equals(to)) {
					solution = new String[1];
					solution[0] = from;
				} else {
					solution = GraphPaths.findPath(from, to, multigraph);
				}
		
				System.out.println("path from " + from + " to " + to + ":");
				if (solution == null) {
					System.out.println("no path found");
				} else {
					int size = solution.length;
					for (int i = 0; i < size - 1; i++) {
						String char1 = solution[i];
						String char2 = solution[i + 1];
						String conName = multigraph.
								getFirstConnectionLabel(char1, char2);
						System.out.println(char1 + " to " + char2 + " via " 
								+ conName);
					}
				}
			}
			System.out.println();
		}
	}

	/**
	 * This creates a graph from a file of character names and books that they
	 * were in. The file must be in the specific format described below.
	 * "character" "book" "character" "book" for the entire file.
	 * @param filename is the name of the file.
	 * @effects Creates a new graph based on the characters and the books they
	 * were in given by the information in the file.
	 * @throws MalformedDataException if the file is not well-formed: each line
	 * contains exactly two tokens separated by a tab, or else starting with a 
	 * # symbol to indicate a comment line.
	 * @throws IOException if the file does not exist.
	 * @return The loaded graph.
	 */
	public static Graph<String, String> loadGraph(String filename)
			throws MalformedDataException, IOException {

		filename = "src/data/" + filename;
		Graph<String, String> loadedGraph = new Graph<String, String>();
		Set<String> allCharacters = new HashSet<String>();
		Map<String, List<String>> booksToChars = new HashMap<String, 
				List<String>>();
		FileParser.parseData(filename, allCharacters, booksToChars);
		loadedGraph.addNodes(allCharacters);

		Set<String> allBookTitles = booksToChars.keySet();
		for (String bookTitle : allBookTitles) {
			ArrayList<String> charsInBook = new ArrayList<String>();
			charsInBook.addAll(booksToChars.get(bookTitle));
			int size = charsInBook.size();

			// If there is only one character in the book there are no
			// connections to add.
			if (size > 1) {
				// Inv: There are connections with all the characters in
				// charsInBook for the characters from 0 to i - 1.
				for (int i = 0; i < size; i++) {

					// Inv: There are connections between the character at
					// charsInBook.get(i) and characters from 0 to j - 1.
					for (int j = 0; j < size; j++) {
						if (i != j) {
							loadedGraph.addConnection(charsInBook.get(i),
									charsInBook.get(j), bookTitle);

						}
					}
					// Q: j = size. There are connections between the character
					// at charsInBook.get(i) and characters from 0 to size - 1.

				}

				// Q: i = size. There are connections with all the characters
				// in charsInBook for the characters from 0 to size - 1.
			}
		}
		return loadedGraph;
	}

	/**
	 * Finds the lexicographically shortest path between two nodes in the same
	 * multigraph. If there is no path it will return null.
	 * @param startNode is the starting node.
	 * @param targetNode is the node that is attempted to be found.
	 * @param multigraph is the multigraph in which the path or lack of path 
	 * will be found in.
	 * @requires startNode, targetNode, and the multigraph must be all 
	 * non-null.
	 * @throws IllegalArgumentException if one or more nodes are not in the 
	 * multigraph.
	 * @return A string array of the steps of the path from the starting
	 * node to the target node. Null will be returned if there is no path.
	 */
	public static <T extends Comparable<T>, N extends Comparable<N>> String[] 
			findPath(T startNode, T targetNode, Graph<T, N> multigraph) {
		if (startNode.equals(targetNode)) {
			String[] solution = new String[1];
			solution[0] = startNode.toString();
			return solution;
		}
		
		// It would improve the speed of the search if we could remove searched
		// characters from the map. This is a clone of it that allows me to do
		// that.
		Graph<T, N> modifiableGraph = multigraph.clone();

		String solution = "";

		// The map will contain all the searched characters and the paths that
		// led this method to them.
		Map<T, String> searchedCharacters = new TreeMap<T, String>();
		searchedCharacters.put(startNode, startNode.toString());

		// We initially want to start our search at the start node.
		Queue<T> charactersToSearch = new LinkedList<T>();
		charactersToSearch.add(startNode);

		// Inv: charactersToSearch.size() is the number of characters left to
		// search.

		boolean foundSolution = false;
		while (!charactersToSearch.isEmpty()) {

			T currCharacter = charactersToSearch.remove();
			String partialSolution = searchedCharacters.get(currCharacter);
			Set<Connection<T, N>> connections = modifiableGraph
					.isConnectedWith(currCharacter);
			Set<T> childrenOfCurrCharacter = new TreeSet<T>();
			for (Connection<T, N> con : connections) {
				childrenOfCurrCharacter.add(con.getTo());
			}

			modifiableGraph.removeNode(currCharacter);

			for (T child : childrenOfCurrCharacter) {
				if (child.equals(targetNode)) {
					// We have found our target node.
					solution = partialSolution + "_" + child;
					foundSolution = true;
					break;
				}
				if (!searchedCharacters.containsKey(child)) {
					// Get the child's name so we can add it to our partial
					// path.
					searchedCharacters.put(child,
							partialSolution + "_" + child);
					charactersToSearch.add(child);
				}
				// else { we have already or will search for this character }
			}
			if (foundSolution) {
				break;
			}
		}
		// Q: No characters left to search, we either found a solution or lack
		// thereof.
		if (solution.isEmpty()) {
			return null;
		} else {
			return solution.split("_");
		}
	}
}
