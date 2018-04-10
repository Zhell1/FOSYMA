package mas.behaviours;

import java.util.ArrayList;
import java.util.HashMap;

import jade.core.behaviours.SimpleBehaviour;
import mas.abstractAgent;
import mas.tools.MyGraph;
import mas.agents.*;

import org.graphstream.graph.Graph;

/**************************************
 * 
 * 
 * 				BEHAVIOUR
 * 
 * 
 **************************************/


public class FirstAgentExplore extends SimpleBehaviour {
	/**
	 * When an agent choose to move
	 *  
	 */
	private static final long serialVersionUID = 9088209402507795289L;
	private boolean finished;
	private MyGraph graph;
	private int signal; // 1 = blocage, 0 = ok, 2 = tresor trouvé en explo
	boolean isexplo;
	private abstractAgent myagent;
	
	
	public FirstAgentExplore (final mas.abstractAgent myagent, boolean isexplo) {
		super(myagent);
		this.finished = false;
		this.graph = ((FirstAgent)myagent).getmyGraph();
		this.signal = 0;
		//super(myagent);
		this.isexplo = isexplo;
		this.myagent = myagent;
	}

	@Override
	public void action() {
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
				//regarder si il y a un trésor sur le nouveau noeud exploré
				if(isexplo) {
					String type = this.myagent.getMyTreasureType();
					int treasurevalue = this.graph.getTreasureValue(myPosition, type);
					if(treasurevalue > 0) {
						this.signal = 2;
					}
				}
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
	}
	
	private void print(String m){
		System.out.println(this.myAgent.getLocalName()+" : "+m);
	}
	
	public void reset(){
		this.signal = 0;
	}
	
	public int onEnd(){
		return this.signal;
	}
	
	public boolean done(){
		//return this.finished;
		return true;
	}
}
