package mas.tools;

import java.util.List;

import org.graphstream.graph.Node;

import env.Couple;

public interface HeuristiqueInterface {
	abstract Node consult(List<Couple<Node, Integer>> L); 
}
