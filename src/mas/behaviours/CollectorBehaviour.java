package mas.behaviours;

import java.util.ArrayList;

import mas.abstractAgent;
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
	private int signal;
	private boolean backpackfull;
	private abstractAgent a;

	public CollectorBehaviour(final mas.abstractAgent myagent){
		super(myagent);
		this.a = (mas.abstractAgent)myagent;
		this.finished = false;
		this.mailbox = new Messages(this.myAgent);
		this.graph = ((FirstAgent)myagent).getmyGraph();
		this.memory = null;
		this.signal = 0;
		this.backpackfull = false;
		
	}
	@Override
	public void action() {
		if (this.memory == null){
			this.memory = this.graph.getCollectorNextNode(new HeuristiqueDistance(a.getMyTreasureType()));
		}
		if (this.memory != null || this.backpackfull){
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
			 * ou si il a déjà son backpack plein et il cherche le silo
			 */
			//Example to retrieve the current position
			String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
			this.graph.add();
			//example related to the use of the backpack for the treasure hunt
			ArrayList<String> path = this.graph.NextDijsktra();
			if (path != null){
				String move = path.get(path.size()-1);
				print("I try to move to : " + move);
				boolean successMove = ((FirstAgent)this.myAgent).moveAgent(move);
				if (!successMove){
					print("blocage");
					this.signal = 1;
				}
				else {
					this.signal = 0;
				}
			}
			else {
				print("Fin de l'exploration !"+"\tNombre de noeud exploré : " + this.graph.getGraphStream().getNodeCount());
				this.finished = true;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*System.out.println("Je fous rien"); */
		}
		
	}
	private void print(String m){
		System.out.println(this.myAgent.getLocalName()+" : "+m);
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}
