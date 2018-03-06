package mas.behaviours;

import java.util.ArrayList;
import java.util.HashMap;

import jade.core.behaviours.SimpleBehaviour;
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
	
	
	public FirstAgentExplore (final mas.abstractAgent myagent) {
		super(myagent);
		this.finished = false;
		this.graph = ((FirstAgent)myagent).getmyGraph();
		//super(myagent);
	}

	@Override
	public void action() {
		//==================================================
		//Il y a un probleme de noeud qui reviennent dans la bordure
		//==================================================
		//Example to retrieve the current position
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
		this.graph.add();
		//example related to the use of the backpack for the treasure hunt
		ArrayList<String> path = this.graph.NextDijsktra();
		if (path != null){
			String move = path.get(path.size()-1);
			System.out.println(this.myAgent.getLocalName()+": I try to move to : " + move);
			((mas.abstractAgent)this.myAgent).moveTo(move);
		}
		else {
			System.out.println("Fin de l'exploration !"+"\tNombre de noeud exploré : " + this.graph.getGraphStream().getNodeCount());
			this.finished = true;
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int onEnd(){
		return 0;
	}
	
	public boolean done(){
		//return this.finished;
		return true;
	}
}
