package mas.tools;

import java.util.List;

import org.graphstream.graph.Node;

import env.Couple;

public class HeuristiqueDistance implements HeuristiqueInterface{
	
	private String type;

	public HeuristiqueDistance(String type){
		this.type = type;
	}
	
	Node consultTreasure(List<Couple<Node, Integer>> L){
		Integer minDist = Integer.MAX_VALUE;
		Node bestNode = null;
		for ( Couple<Node, Integer> c : L){
			Node n = c.getLeft();
			if ((boolean) n.getAttribute("Treasure")){
				if (c.getRight() < minDist){
					minDist = c.getRight();
					bestNode = n;
				}
			}
		}
		return bestNode;
	}
	
	Node consultDiamonds(List<Couple<Node, Integer>> L){
		Integer minDist = Integer.MAX_VALUE;
		Node bestNode = null;
		for ( Couple<Node, Integer> c : L){
			Node n = c.getLeft();
			if ((boolean) n.getAttribute("Diamonds")){
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
		if (this.type.equals("Treasure")){
			return consultTreasure(L);
		}
		if (this.type.equals("Diamonds")){
			return consultDiamonds(L);
		}
		System.out.println("================================== I AM GOING TO DIE =======================");
		return null;
		
	}

}
