package roadgraph;

import java.util.Comparator;
import java.util.PriorityQueue;

import geography.GeographicPoint;
import java.math.*;

public class PQcomp2 implements Comparator<GeographicPoint>{

	private SearchTree<GeographicPoint> myTree;
	
    public PQcomp2(){
    	myTree = null;
    }
    
    public PQcomp2(SearchTree<GeographicPoint> t){
    	myTree = t;
    }
    
	@Override
	public int compare(GeographicPoint o1, GeographicPoint o2) {
		// TODO Auto-generated method stub
		
		if (myTree.getNode(o1).getWeight() > myTree.getNode(o2).getWeight()){
            return +1;
        }
        else if (myTree.getNode(o1).getWeight() < myTree.getNode(o2).getWeight()){
            return -1;
        }
        else {  // equal
            return 0;
        }
	}
}