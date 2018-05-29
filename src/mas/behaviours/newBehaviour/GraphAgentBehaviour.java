package mas.behaviours.newBehaviour;

import java.util.ArrayList;

import jade.core.behaviours.FSMBehaviour;
import mas.abstractAgent;
import mas.agents.GraphAgent;
import mas.tools.MyGraph;

public class GraphAgentBehaviour extends FSMBehaviour{
	
	protected MyGraph g;
	protected GraphAgent a;
	protected int signal;

	public GraphAgentBehaviour(abstractAgent agent) {
	
		/* Il ne faut pas mettre l'attente ici car sinon dans tous les behaviors composé de FSM behaviour on a une attente cumulé
		 * lors de la création
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		GraphAgent a = (GraphAgent)agent;
		this.a = a;
		MyGraph g = a.getmyGraph();
		this.g = g;
		//on rajoute le premier noeud
		g.add();
	}
	
	public int onEnd() {
		return this.signal;
	}
	
	public void setSignal(int endSignal) {
		this.signal = endSignal;
	}
	
	public int getSignal() {
		return this.signal;
	}
	
	
}
