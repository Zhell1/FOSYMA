package mas.behaviours;

import java.util.ArrayList;

import jade.core.behaviours.SimpleBehaviour;
import mas.tools.MyGraph;

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
	
	
	public FirstAgentExplore (final mas.abstractAgent myagent, Graph g) {
		super(myagent);
		this.finished = false;
		this.graph = new mas.tools.MyGraph(myagent, g);
		//super(myagent);
	}

	@Override
	public void action() {
		//==================================================
		String s = this.graph.toJSON();
		//this.graph.unparse(s);
		this.graph.test(s);
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
	
	public boolean done(){
		return this.finished;
	}
}
