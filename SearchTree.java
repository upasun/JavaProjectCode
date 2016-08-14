package roadgraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import geography.GeographicPoint;

//import geography.GeographicPoint;

//This is my helper class I created to trace the patch from start to goal
//The methods are fairly self-explanatory 

//I keep a tree backed by a hashmap (tracker) so that I can reconstruct a path to start, with easy access to nodes through the tracker
//while performing a BFS

//The inspiration for the buildPathToRoot method comes from the method with the same name from Course 2, WPTree!

//The generic search tree containing a root and a tracker
public class SearchTree<E> {
	private Nodes<E> root;
	private HashMap<E, Nodes<E>> tracker;
	
	public SearchTree(){
		root = null;
	}
	
	public SearchTree(E gp){
		tracker = new HashMap<E, Nodes<E>>();
		root = new Nodes<E>(this);
		root.addChild(gp);
	}
	
	public void addNodeToTracker(Nodes<E>  n){
		if(n.getData() != null){
			tracker.put(n.getData(), n);
		}
	}
	
	public HashMap<E, Nodes<E>> getTracker(){
		return tracker;
	}
	
	public Nodes<E> getNode(E gp){
		return tracker.get(gp);
	}
	
	public void remNode(E gp, Nodes<E> n){
		tracker.remove(gp, n);
	}
	
	public Nodes<E> getRoot(){
		return root;
	}
	
	public boolean contains(E gp){
		return tracker.keySet().contains(gp);
	}
	
}

//Generic search nodes containing data, a parent, children, and belonging to a specific search tree
class Nodes<E> implements Comparable<Nodes<E>>{
	private E data;
	private Nodes<E> parent;
	private List<Nodes<E>> children;
	private SearchTree<E> sT;
	private int djWeight;
	private int aStarWeight;
	
	public Nodes(){
		data = null;
		parent = null;
		sT = null;
		children = new ArrayList<Nodes<E>>();
	}
	
	public Nodes(SearchTree<E> tree){
		data = null;
		parent = null;
		sT = tree;
		djWeight = 0;
		children = new ArrayList<Nodes<E>>();
	}
	
	public Nodes(E gp, Nodes<E> p){
		data = gp;
		parent = p;
		sT = p.getTree();
		children = new ArrayList<Nodes<E>>();
	}
	
	public int getWeight(){
		return aStarWeight;
	}
	public int getDjWeight(){
		return djWeight;
	}
	
	public double durationWeight(){
		double x;
		double speedLim;
		DurationEdge par = (DurationEdge)(this.getParent().getData());
		String y =((DurationEdge) this.getData()).getType();
		GeographicPoint gp = ((DurationEdge) this.getData()).getPoint();
		GeographicPoint parPoint = par.getPoint();
		
		if(y.equals("motorway")){
			speedLim = 65.0;
		}
		else if(y.equals("motorway_link")){
			speedLim = 35.0;
		}
		else if(y.equals("residential")){
			speedLim = 25.0;
		}
		else if(y.equals("primary")){
			speedLim = 50.0;
		}
		else if(y.equals("secondary_link")){
			speedLim = 30.0;
		}
		else if(y.equals("secondary")){
			speedLim = 45.0;
		}
		else if(y.equals("tertiary")){
			speedLim = 35.0;
		}
		else{
			speedLim = 20.0;
		}
		speedLim = speedLim * 1.60934;
		x = ((gp.distance(parPoint)*1000)/speedLim);
		
		return x;
	}
	
	public void setWeight(){
		if(data instanceof GeographicPoint){
			if(this.getParent().getData() == null){ 
				djWeight = 0; 
			}
			else{
				djWeight = (int)(((GeographicPoint)(this.getData())).distance((GeographicPoint)(this.getParent().getData()))*1000.0) + this.getParent().getDjWeight();
				aStarWeight = djWeight;
			}
		}
		else if(data instanceof DurationEdge){
			if(this.getParent().getData() == null){ 
				djWeight = 0; 
			}
			else{
				djWeight = (int)(durationWeight() + this.getParent().getWeight());
			}
		}
	}
	
	public void setWeight(E goal){
		setWeight();
		aStarWeight = djWeight + (int)(((GeographicPoint)(this.getData())).distance((GeographicPoint)(goal))*1000.0);
	}
	
	public Nodes<E> addChild(E gp){
		boolean contains = sT.contains(gp);
		Nodes<E> tempNode = new Nodes<E>();
		if(contains){
			tempNode = sT.getNode(gp);
			//System.out.println("\n" + contains + " " + tempNode.getWeight());
		}
		Nodes<E> newNode = new Nodes<E>(gp, this);
		newNode.setWeight();
		return finishBuild(newNode, tempNode, contains);
	}
	
	public Nodes<E> addChild(E gp, E goal){
		boolean contains = sT.contains(gp);
		Nodes<E> tempNode = new Nodes<E>();
		if(contains){
			tempNode = sT.getNode(gp);
			//System.out.println("\n" + contains + " " + tempNode.getWeight());
		}
		Nodes<E> newNode = new Nodes<E>(gp, this);
		newNode.setWeight(goal);
		return finishBuild(newNode, tempNode, contains);
	}
	
	public Nodes<E> finishBuild(Nodes<E> newNode, Nodes<E> tempNode, boolean contains){
		if(contains && newNode.getWeight() < tempNode.getWeight()){
			sT.addNodeToTracker(newNode);
			children.add(newNode);
			return newNode;
		}
		else if(this.data == null){
			sT.addNodeToTracker(newNode);
			children.add(newNode);
			return newNode;
		}
		else if(!contains){
			sT.addNodeToTracker(newNode);
			children.add(newNode);
			return newNode;
		}
		return null;
	}
	
	public List<Nodes<E>> getChildren(){
		return this.children;
	}
	
	public Nodes<E> getParent(){
		return this.parent;
	}
	
	public SearchTree<E> getTree(){
		return sT;
	}
	
	public E getData(){
		return data;
	}
	
	public List<E> buildPathToRoot() {
        Nodes<E> curr = this;
        List<E> path = new LinkedList<E>();
        while(curr != null) {
        	if(curr.getData() == null){ break; }
            path.add(0,curr.getData());
            curr = curr.parent; 
        }
        return path;
    }

	@Override
	public int compareTo(Nodes<E> o) {
		// TODO Auto-generated method stub
		o = sT.getRoot().getChildren().get(0);
		
		
		return (int)((GeographicPoint)(this.data)).distance((GeographicPoint)o.data);
	}
	
}
