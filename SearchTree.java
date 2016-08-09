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
		children = new ArrayList<Nodes<E>>();
	}
	
	public Nodes(E gp, Nodes<E> p){
		data = gp;
		parent = p;
		sT = p.getTree();
		children = new ArrayList<Nodes<E>>();
	}
	
	public int getWeight(){
		return djWeight;
	}
	
	public void setWeight(){
		if(this.getParent().getData() == null){ 
			djWeight = 0; 
		}
		else{
			djWeight = (int)(((GeographicPoint)(this.getData())).distance((GeographicPoint)(this.getParent().getData()))*1000) + this.getParent().getWeight();
		}
	}
	
	public void setWeight(E goal){
		setWeight();
		djWeight += (int)(((GeographicPoint)(this.getData())).distance((GeographicPoint)(goal))*1000);
	}
	
	public Nodes<E> addChild(E gp){
		Nodes<E> newNode = new Nodes<E>(gp, this);
		children.add(newNode);
		sT.addNodeToTracker(newNode);
		newNode.setWeight();
		return newNode;
	}
	
	public Nodes<E> addChild(E gp, E goal){
		Nodes<E> newNode = new Nodes<E>(gp, this);
		children.add(newNode);
		sT.addNodeToTracker(newNode);
		newNode.setWeight(goal);
		return newNode;
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
