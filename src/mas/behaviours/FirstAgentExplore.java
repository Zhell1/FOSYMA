package mas.behaviours;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.graphstream.algorithm.generator.DorogovtsevMendesGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.GridGenerator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;

import env.Attribute;
import env.Couple;
import jade.core.behaviours.TickerBehaviour;

/**************************************
 * 
 * 
 * 				BEHAVIOUR
 * 
 * 
 **************************************/


public class FirstAgentExplore extends GraphBehaviour{
	/**
	 * When an agent choose to move
	 *  
	 */
	private static final long serialVersionUID = 9088209402507795289L;
	private boolean finished;
	
	
	public FirstAgentExplore (final mas.abstractAgent myagent, Graph g) {
		super(myagent, g);
		this.finished = false;
		//super(myagent);
	}

	@Override
	public void action() {
		System.out.println(toJSON());
		//Example to retrieve the current position
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
		add();
		//example related to the use of the backpack for the treasure hunt
		ArrayList<String> path = NextDijsktra();
		if (path != null){
			String move = path.get(path.size()-1);
			System.out.println(this.myAgent.getLocalName()+": I try to move to : " + move);
			((mas.abstractAgent)this.myAgent).moveTo(move);
		}
		else {
			System.out.println("Fin de l'exploration !"+"\tNombre de noeud exploré : " + this.graph.getNodeCount());
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
