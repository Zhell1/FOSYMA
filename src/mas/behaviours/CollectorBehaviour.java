package mas.behaviours;

import java.util.ArrayList;

import mas.abstractAgent;
import mas.agents.FirstAgent;
import mas.agents.GraphAgent;
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
		this.graph = ((GraphAgent)myagent).getmyGraph();
		this.memory = null;
		this.signal = 0;
		this.backpackfull = false;
		
	}
	@Override
	public void action() {	
		print("COLLLEEECTTTTOOOOROOORRR BEHAVIORUR");
				
		if (this.memory == null){
			this.memory = this.graph.getCollectorNextNode(new HeuristiqueDistance(this.graph.getTrueTreasureType()));
		}
		if (this.memory != null || this.backpackfull){
			/* il y a un tresors ramassable */
			System.out.println("--***--- YOUYOU --***---");
			boolean successMove= true;
			if(this.memory.size() >= 1){ //si on est pas déjà sur la case
				String next = this.memory.get(0);
				this.memory.remove(0);
				successMove = ((GraphAgent)this.myAgent).moveAgent(next);
			}
			if (this.memory.size() == 0 && successMove){
				//TODO
				//il faut modifier le graph et mettre a jour la variable backpack full
				int v = ((GraphAgent)this.myAgent).pick();
				this.signal = 1;
			}
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
