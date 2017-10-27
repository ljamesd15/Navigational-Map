# Navigational-Map
A navigational map which will find the shortest path from any two buildings on the University of Washington campus.

I implemented this project over 8 weeks during the summer of 2017. This project was used for CSE 331 at the University of Washington. 
All files save for Implementation test, the Script Files tests, and the Specification test files were written by myself.

This project has two separate text UIs and a GUI which was implemented through Android Studio. The first user interface, GraphPaths.java,
is a simple interaction with a graph. It loads graphs (using .tsv files from a data folder), removes loaded graphs, lists all nodes in 
specified graphs, and can find the lowest cost path between two nodes. The second text user interface allows the user to interact with the 
University of Washington campus. A user can ask for directions between any two buildings on the main campus of UW. The program will output 
the exact directions in which the user needs to walk. The user can also ask to see a list of all the buildings on the campus and their 
abbreviations. Finally, I implemented a GUI through Android Studio. The user can scroll through a list of all the buildings on campus and 
select the two that they need to walk from. After pressing a "Find Path" button the screen snap zooms on a map of the campus. A line will 
show the shortest path the user can take between any two buildings. The application will also highlight where the two buildings are on the 
map.
