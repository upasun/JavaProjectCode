/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which reprsents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
package roadgraph;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

import geography.GeographicPoint;
import util.GraphLoader;

/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which represents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
public class MapGraph {
	//TODO: Add your member variables here in WEEK 2
	private Map<GeographicPoint, ArrayList<GeographicPoint>> theGraph;
	private int inc = 0;
	//private Map<GeographicPoint, ArrayList<DurationEdge>> extensionGraph;
	
	/** 
	 * Create a new empty MapGraph 
	 */
	public MapGraph()
	{
		// TODO: Implement in this constructor in WEEK 2
		theGraph = new HashMap<GeographicPoint, ArrayList<GeographicPoint>>();
	}
	
	/**
	 * Get the number of vertices (road intersections) in the graph
	 * @return The number of vertices in the graph.
	 */
	public int getNumVertices()
	{
		//TODO: Implement this method in WEEK 2
		int numV = theGraph.keySet().size();
		return numV;
	}
	
	/**
	 * Return the intersections, which are the vertices in this graph.
	 * @return The vertices in this graph as GeographicPoints
	 */
	public Set<GeographicPoint> getVertices()
	{
		//TODO: Implement this method in WEEK 2
		return theGraph.keySet();
	}
	
	/**
	 * Get the number of road segments in the graph
	 * @return The number of edges in the graph.
	 */
	public int getNumEdges()
	{
		//TODO: Implement this method in WEEK 2
		int numEdge = 0;
		for(ArrayList<GeographicPoint> gp : theGraph.values()){
			numEdge += gp.size();
		}
		return numEdge;
	}

	
	
	/** Add a node corresponding to an intersection at a Geographic Point
	 * If the location is already in the graph or null, this method does 
	 * not change the graph.
	 * @param location  The location of the intersection
	 * @return true if a node was added, false if it was not (the node
	 * was already in the graph, or the parameter is null).
	 */
	public boolean addVertex(GeographicPoint location)
	{
		// TODO: Implement this method in WEEK 2
		if(theGraph.containsKey(location) || location == null){
			return false;
		}
		theGraph.put(location, new ArrayList<GeographicPoint>());
		return true;
	}
	
	/**
	 * Adds a directed edge to the graph from pt1 to pt2.  
	 * Precondition: Both GeographicPoints have already been added to the graph
	 * @param from The starting point of the edge
	 * @param to The ending point of the edge
	 * @param roadName The name of the road
	 * @param roadType The type of the road
	 * @param length The length of the road, in km
	 * @throws IllegalArgumentException If the points have not already been
	 *   added as nodes to the graph, if any of the arguments is null,
	 *   or if the length is less than 0.
	 */
	public void addEdge(GeographicPoint from, GeographicPoint to, String roadName,
			String roadType, double length) throws IllegalArgumentException {
		//TODO: Implement this method in WEEK 2
		if((!theGraph.containsKey(from)) || (!theGraph.containsKey(to)) || length < 0){
			throw new IllegalArgumentException();
		}
		else{
			theGraph.get(from).add(to);
		}
	}
	

	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return bfs(start, goal, temp);
	}
	
	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, 
			 					     GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		// TODO: Implement this method in WEEK 2
		if((!theGraph.containsKey(start)) || (!theGraph.containsKey(goal))){
			throw new IllegalArgumentException();
		}
		Queue<GeographicPoint> myQ = new LinkedList<GeographicPoint>();
		//create my search tree and add first child (start)
		SearchTree<GeographicPoint> myT = new SearchTree<GeographicPoint>(start);
		
		myQ.add(start);
		
		GeographicPoint temp = start;
		
		//BFS algo
		while((temp != null) && (!myQ.isEmpty() || (temp.getX() != goal.getX()) || (temp.getY() != goal.getY()))){
			temp = myQ.poll(); //poll queue for GeograpicPoint temp
			Nodes<GeographicPoint> tNode = myT.getTracker().get(temp); //find node being explored in SearchTree
			nodeSearched.accept(temp);
			//find connections to temp in graph
			ArrayList<GeographicPoint> theL = theGraph.get(temp);
			if(theL != null){
				for(int i = 0; i < theL.size(); i++){
					GeographicPoint gp = theL.get(i);
					//The search tree contains a hashmap in which I can check for the existence of a certain GeographicPoint
					//So if a connection has not been explored
					if(!myT.contains(gp)){
						tNode.addChild(gp);
						//if that connection is the goal
						if((gp.getX() == goal.getX()) && (gp.getY() == goal.getY())){
							//System.out.println("hi");
							return myT.getTracker().get(gp).buildPathToRoot();
						}
						myQ.add(gp);
					}
				}
			}
		}
		// Hook for visualization.  See writeup.
		//nodeSearched.accept(next.getLocation());

		return null;
	}
	

	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
		// You do not need to change this method.
        Consumer<GeographicPoint> temp = (x) -> {};
        return dijkstra(start, goal, temp);
	}
	
	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, 
										  GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		GeographicPoint astar = null;
		return algoSearch2(start, goal, nodeSearched, astar);
	}
	
	public List<GeographicPoint> algoSearch2(GeographicPoint start, 
			  GeographicPoint goal, Consumer<GeographicPoint> nodeSearched, GeographicPoint astar)
	{
		inc = 0;
		if((!theGraph.containsKey(start)) || (!theGraph.containsKey(goal))){
			throw new IllegalArgumentException();
		}
		
		//create my search tree and add first child (start)
		SearchTree<GeographicPoint> myT = new SearchTree<GeographicPoint>(start);

		Comparator<GeographicPoint> myComp2 = new PQcomp2(myT);
		PriorityQueue<GeographicPoint> myQ2 = new PriorityQueue<GeographicPoint>(11, myComp2);
			
		myQ2.add(start);
		
		System.out.println(inc);
		GeographicPoint temp = start;
		
		HashSet<GeographicPoint> set = new HashSet<GeographicPoint>();
		Nodes<GeographicPoint> tNode = new Nodes<GeographicPoint>();
		//Search algo
		
		while(!myQ2.isEmpty() || (temp.getX() != goal.getX() && temp.getY() != goal.getY())){
			do{
				temp = myQ2.poll();
			}while(set.contains(temp));
			inc++;
			
			tNode = myT.getNode(temp);
			
			if((temp.getX() == goal.getX()) && (temp.getY() == goal.getY())){
				System.out.println(inc);
				return tNode.buildPathToRoot();
			}
			
			if(!set.contains(temp)){
				nodeSearched.accept(temp);
				
				List<GeographicPoint> tList = theGraph.get(temp);
				if(tList!=null && !tList.isEmpty()){
					for(GeographicPoint gp : tList){
						if(!set.contains(gp)){
							Nodes<GeographicPoint> x;
							
							if(astar == null){
								x = tNode.addChild(gp);
							}
							else{
								x = tNode.addChild(gp, astar);
							}
							if(x != null){
								myQ2.add(gp);
							}
							/*if((gp.getX() == goal.getX()) && (gp.getY() == goal.getY()) && x != null){
								System.out.println(inc);
								return x.buildPathToRoot();
							}*/
						}
					}
				}
				set.add(temp);
			}
		}
		return null;
	}
	
	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return aStarSearch(start, goal, temp);
	}
	
	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, 
											 GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		GeographicPoint astar = goal;
		return algoSearch2(start, goal, nodeSearched, astar);
		// TODO: Implement this method in WEEK 3
		// TODO: Implement this method in WEEK 3
	}
	
	public List<GeographicPoint> shortestDurationSearch(GeographicPoint start, GeographicPoint goal){
		
		return null;
	}

	
	
	public static void main(String[] args)
	{
		System.out.print("Making a new map...");
		MapGraph firstMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", firstMap);
		System.out.println("DONE.");
		
		// You can use this method for testing.  
		
		
		/* Here are some test cases you should try before you attempt 
		 * the Week 3 End of Week Quiz, EVEN IF you score 100% on the 
		 * programming assignment.
		 */
		
		MapGraph simpleTestMap = new MapGraph();
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", simpleTestMap);
		
		GeographicPoint testStart = new GeographicPoint(1.0, 1.0);
		GeographicPoint testEnd = new GeographicPoint(8.0, -1.0);
		
		System.out.println("Test 1 using simpletest: Dijkstra should be 9 and AStar should be 5");
		List<GeographicPoint> testroute = simpleTestMap.dijkstra(testStart,testEnd);
		List<GeographicPoint> testroute2 = simpleTestMap.aStarSearch(testStart,testEnd);
		
		
		MapGraph testMap = new MapGraph();
		GraphLoader.loadRoadMap("data/maps/utc.map", testMap);
		
		// A very simple test using real data
		testStart = new GeographicPoint(32.869423, -117.220917);
		testEnd = new GeographicPoint(32.869255, -117.216927);
		System.out.println("Test 2 using utc: Dijkstra should be 13 and AStar should be 5");
		testroute = testMap.dijkstra(testStart,testEnd);
		testroute2 = testMap.aStarSearch(testStart,testEnd);
		
		
		// A slightly more complex test using real data
		testStart = new GeographicPoint(32.8674388, -117.2190213);
		testEnd = new GeographicPoint(32.8697828, -117.2244506);
		System.out.println("Test 3 using utc: Dijkstra should be 37 and AStar should be 10");
		testroute = testMap.dijkstra(testStart,testEnd);
		testroute2 = testMap.aStarSearch(testStart,testEnd);
		
		
		
		/* Use this code in Week 3 End of Week Quiz */
		
		MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
		System.out.println("DONE.");

		GeographicPoint start = new GeographicPoint(32.8648772, -117.2254046);
		GeographicPoint end = new GeographicPoint(32.8660691, -117.217393);
		
		
		List<GeographicPoint> route = theMap.dijkstra(start,end);
		List<GeographicPoint> route2 = theMap.aStarSearch(start,end);

		
		
	}
	
}
