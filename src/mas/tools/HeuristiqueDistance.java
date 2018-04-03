package mas.tools;

import java.util.List;

import org.graphstream.graph.Node;

import env.Couple;

public class HeuristiqueDistance implements HeuristiqueInterface{
	
	private String type;

	public HeuristiqueDistance(String type){
		this.type = type;
	}
	
	Node consultTreasure1(List<Couple<Node, Integer>> L){
		float minDist = Float.MAX_VALUE;
		Node bestNode = null;
		for ( Couple<Node, Integer> c : L){
			Node n = c.getLeft();
			if ((boolean) n.getAttribute("treasuretype1")){
				if (c.getRight() < minDist){
					minDist = c.getRight();
					bestNode = n;
				}
			}
		}
		return bestNode;
	}
	
	Node consultTreasure2(List<Couple<Node, Integer>> L){
		float minDist = Float.MAX_VALUE;
		Node bestNode = null;
		for ( Couple<Node, Integer> c : L){
			Node n = c.getLeft();
			if ((boolean) n.getAttribute("treasuretype2")){
				if (c.getRight() < minDist){
					minDist = c.getRight();
					bestNode = n;
				}
			}
		}
		return bestNode;
	}

	@Override
	public Node consult(List<Couple<Node, Integer>> L) {
		if (this.type.equals("treasuretype1")){
			return consultTreasure1(L);
		}
		if (this.type.equals("treasuretype2")){
			return consultTreasure2(L);
		}
		return null;
		
	}

}
