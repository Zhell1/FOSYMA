package mas.behaviours;

import java.util.ArrayList;

import mas.agents.FirstAgent;
import mas.tools.HeuristiqueDistance;
import mas.tools.Messages;
import mas.tools.MyGraph;
import jade.core.behaviours.SimpleBehaviour;

public class CollectorBehaviour extends SimpleBehaviour{

	
	private boolean finished;
	private Messages mailbox;
	private MyGraph graph;
	ArrayList<String> memory;

	public CollectorBehaviour(final mas.abstractAgent myagent){
		super(myagent);
		this.finished = false;
		this.mailbox = new Messages(this.myAgent);
		this.graph = ((FirstAgent)myagent).getmyGraph();
		this.memory = null;
		
	}
	@Override
	public void action() {
		if (this.memory == null){
			this.memory = this.graph.getCollectorNextNode(new HeuristiqueDistance("treasuretype1"));
		}
		if (this.memory != null){
			/* il y a un tresors ramassable */
			System.out.println("--***--- YOUYOU --***---");
			String next = this.memory.get(0);
			this.memory.remove(0);
			boolean successMove = ((FirstAgent)this.myAgent).moveAgent(next);
			if (this.memory.size() == 0 && successMove){
				//on ramasse le tresors
				int v = ((FirstAgent)this.myAgent).pick();
			}
		}
		
		else {
			/* Si il n'y a pas de trésor ramassable il va explorer le graphe
			 * **************** TODO ***************************************
			 */
			System.out.println("Je fous rien");
		}
		
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}
