package test.specTest1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import model.Connection;
import model.Graph;

/**
 * This class implements a testing driver which reads test scripts
 * from files for testing Graph.
 * 
 * @author L. James Davidson
 **/
public class TestDriver1{

    public static void main(String args[]) {
        try {
            if (args.length > 1) {
                printUsage();
                return;
            }

            TestDriver1 td;

            if (args.length == 0) {
                td = new TestDriver1(new InputStreamReader(System.in),
                                       new OutputStreamWriter(System.out));
            } else {

                String fileName = args[0];
                File tests = new File (fileName);

                if (tests.exists() || tests.canRead()) {
                    td = new TestDriver1(new FileReader(tests),
                                           new OutputStreamWriter(System.out));
                } else {
                    System.err.println("Cannot read from " + tests.toString());
                    printUsage();
                    return;
                }
            }

            td.runTests();

        } catch (IOException e) {
            System.err.println(e.toString());
            e.printStackTrace(System.err);
        }
    }

    private static void printUsage() {
        System.err.println("Usage:");
        System.err.println("to read from a file: java hw5.test.HW5TestDriver "
        		+ "<name of input script>");
        System.err.println("to read from standard in: java "
        		+ "hw5.test.HW5TestDriver");
    }

    /** String -> Graph: maps the names of graphs to the actual graph **/
    private final Map<String, Graph<String, String>> graphs = 
    		new HashMap<String, Graph<String, String>>();
    private final PrintWriter output;
    private final BufferedReader input;

    /**
     * @requires r != null && w != null
     *
     * @effects Creates a new HW5TestDriver which reads command from
     * <tt>r</tt> and writes results to <tt>w</tt>.
     **/
    public TestDriver1(Reader r, Writer w) {
        input = new BufferedReader(r);
        output = new PrintWriter(w);
    }

    /**
     * @effects Executes the commands read from the input and writes results to
     * the output
     * @throws IOException if the input or output sources encounter an 
     * IOException
     **/
    public void runTests()
        throws IOException
    {
        String inputLine;
        while ((inputLine = input.readLine()) != null) {
            if ((inputLine.trim().length() == 0) ||
                (inputLine.charAt(0) == '#')) {
                // echo blank and comment lines
                output.println(inputLine);
            }
            else
            {
                // separate the input line on white space
                StringTokenizer st = new StringTokenizer(inputLine);
                if (st.hasMoreTokens()) {
                    String command = st.nextToken();

                    List<String> arguments = new ArrayList<String>();
                    while (st.hasMoreTokens()) {
                        arguments.add(st.nextToken());
                    }

                    executeCommand(command, arguments);
                }
            }
            output.flush();
        }
    }

    private void executeCommand(String command, List<String> arguments) {
        try {
            if (command.equals("CreateGraph")) {
                createGraph(arguments);
            } else if (command.equals("AddNode")) {
                addNode(arguments);
            } else if (command.equals("AddEdge")) {
                addEdge(arguments);
            } else if (command.equals("ListNodes")) {
                listNodes(arguments);
            } else if (command.equals("ListChildren")) {
                listChildren(arguments);
            } else {
                output.println("Unrecognized command: " + command);
            }
        } catch (Exception e) {
            output.println("Exception: " + e.toString());
        }
    }

    private void createGraph(List<String> arguments) {
        if (arguments.size() != 1) {
            throw new CommandException("Bad arguments to CreateGraph: " 
            		+ arguments);
        }

        String graphName = arguments.get(0);
        createGraph(graphName);
    }

    private void createGraph(String graphName) {
        graphs.put(graphName, new Graph<String, String>());
        output.println("created graph " + graphName);
    }

    private void addNode(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new CommandException("Bad arguments to addNode: " 
            		+ arguments);
        }

        String graphName = arguments.get(0);
        String nodeName = arguments.get(1);

        addNode(graphName, nodeName);
    }

    private void addNode(String graphName, String nodeName) {
		if (!(graphs.containsKey(graphName))) {
			output.println("The graph '" + graphName + "' does not exist.");
			return;
		}
        Graph<String, String> currGraph = graphs.get(graphName);
        boolean added = currGraph.addNode(nodeName);
        if (added) {
        	output.println("added node " + nodeName + " to " + graphName);
        } else {
        	output.println("The node " + nodeName + 
        				   " was unsuccessfully added to " + graphName);
        }
    }

    private void addEdge(List<String> arguments) {
        if (arguments.size() != 4) {
            throw new CommandException("Bad arguments to addEdge: " 
            		+ arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        String childName = arguments.get(2);
        String edgeLabel = arguments.get(3);

        addEdge(graphName, parentName, childName, edgeLabel);
    }

    private void addEdge(String graphName, String parentName, String childName,
            String edgeLabel) {
		if (!(graphs.containsKey(graphName))) {
			output.println("The graph '" + graphName + "' does not exist.");
			return;
		}
        Graph<String, String> currGraph = graphs.get(graphName);
        boolean added = currGraph.addConnection(parentName, childName, 
        										edgeLabel);
        if (added) {
        	output.println("added edge " + edgeLabel + " from " + parentName +
        			   	   " to " + childName + " in " + graphName);
        } else {
        	output.println("The edge " + edgeLabel + " from " + parentName +
        				   " to " + childName + " was unsuccessfully added to "
        				   + graphName);
        }
    }

    private void listNodes(List<String> arguments) {
        if (arguments.size() != 1) {
            throw new CommandException("Bad arguments to listNodes: " 
            		+ arguments);
        }

        String graphName = arguments.get(0);
        listNodes(graphName);
    }

    private void listNodes(String graphName) {
		if (!(graphs.containsKey(graphName))) {
			output.println("The graph '" + graphName + "' does not exist.");
			return;
		}
        Graph<String, String> currGraph = graphs.get(graphName);
        Set<String> characterSet = new TreeSet<String>(currGraph.getNodes());
        output.print(graphName + " contains:");
        for (String character: characterSet) {
        	output.print(" " + character);
        }
        output.println();
    }

    private void listChildren(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new CommandException("Bad arguments to listChildren: " 
            		+ arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        listChildren(graphName, parentName);
    }

    private void listChildren(String graphName, String parentName) {
		if (!(graphs.containsKey(graphName))) {
			output.println("The graph '" + graphName + "' does not exist.");
			return;
		}
        Graph<String, String> currGraph = graphs.get(graphName);
        Set<Connection<String, String>> connectionSet = (currGraph.
        		isConnectedWith(parentName));
        Set<String> connectedCharSet = new TreeSet<String>();
        output.print("the children of " + parentName + " in " + graphName + 
        			   " are:");
        for (Connection<String, String> con : connectionSet) {
        	connectedCharSet.add(con.getTo());
        }
        for (String character : connectedCharSet) {
        	Set<String> connectionLabelSet = new TreeSet<String>(
        			currGraph.isConnectedWith(parentName, character));
        	for (String connectionLabel: connectionLabelSet) {
        		output.print(" " + character + "(" + connectionLabel + ")");
        	}
        }
        output.println();
    }

    /**
     * This exception results when the input file cannot be parsed properly
     **/
    static class CommandException extends RuntimeException {

        public CommandException() {
            super();
        }
        public CommandException(String s) {
            super(s);
        }

        public static final long serialVersionUID = 3495;
    }
}
