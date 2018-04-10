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
	int inputsignal;
	private abstractAgent myagent;
	private ArrayList<String> path;
	
	/*
	 * inputsignal:
	 * 0 -> explorateur
	 * 1 -> collecteur qui cherche un trésor
	 * 2 -> collecteur qui à un backpack et cherche le silo
	 */
	public FirstAgentExplore (final mas.abstractAgent myagent, int inputsignal) {
		super(myagent);
		this.finished = false;
		this.graph = ((GraphAgent)myagent).getmyGraph();
		this.signal = 0;
		//super(myagent);
		this.inputsignal= inputsignal;
		this.myagent = myagent;
	}


	@Override
	public void action() {
		//Example to retrieve the current position
		String myPosition=((GraphAgent)this.myAgent).getCurrentPosition();
		this.graph.add();
		
		if(this.inputsignal==1){
			print("IIIII AAAAAAAAAAAMMMMMMMMMMMMMM AGENT 33333333333333333");
		}
		
		// si collecteur qui cherche un trésor et en trouve un
		if (this.graph.getMyTreasuresList().size() > 0 && this.inputsignal==1){
			//si il trouve un tresors il sort du behaviors explorer
			print("EXPLOOOOOOOOOOO COLLLEECTTOOOOOOR");
			this.signal = 2;
			return;
		}
		// si collecteur qui cherche le silo et le trouve
		if (this.graph.getMyTreasuresList().size() > 0 && this.inputsignal==2){
			//si il trouve le silo il sort du behaviors explorer
			this.signal = 3;
			return;
		}
		//
		path = graph.NextDijsktra();
		if (path != null){
			String move = path.get(path.size()-1);
			print("I try to move to : " + move);
			boolean successMove = ((GraphAgent)this.myAgent).moveAgent(move);
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
