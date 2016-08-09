package roadgraph;

import java.util.Comparator;
import java.util.PriorityQueue;

import geography.GeographicPoint;
import java.math.*;

public class PQcomp implements Comparator<Nodes<GeographicPoint>>{

	private SearchTree<GeographicPoint> myTree;
	private GeographicPoint goal;
	private int i = 0;
	
    public PQcomp(){
    	myTree = null;
    	goal = null;
    }
    
    public PQcomp(SearchTree<GeographicPoint> t){
    	myTree = t;
    	goal = null;
    	
    }
    
    public PQcomp(SearchTree<GeographicPoint> t, GeographicPoint gp){
    	myTree = t;
    	goal = gp;
    }
    
	@Override
	public int compare(Nodes<GeographicPoint> o1, Nodes<GeographicPoint> o2) {
		// TODO Auto-generated method stub
		return o1.getWeight();
	}
}