package mas.behaviours;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import env.Couple;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import mas.abstractAgent;
import mas.tools.DFManager;
import mas.tools.Messages;
import mas.tools.MyGraph;
import mas.agents.*;

public class ListenerBehaviour extends SimpleBehaviour {
	private boolean finished;
	private Messages mailbox;
	private int stopmoving;
	private int timeout;
	private int cpt;
	private boolean mapsent;
	private boolean mapreceived;
	private MyGraph graph;
	private Set<ShareMapBehaviour> communicationSet;
	private GraphAgent a;
	private int treshold; //nombre de pas avant de renvoyer la map (ou recevoir)
	
	public ListenerBehaviour(final Agent myagent) {
		super(myagent);
		this.mailbox = new Messages(myagent);
		this.a = (GraphAgent)(myagent);
		this.timeout = 2000;
		this.cpt = 0;
		this.stopmoving = 0;
		this.mapsent = false;
		this.mapreceived = false;
		this.graph = ((GraphAgent)myagent).getmyGraph();
		this.communicationSet = new HashSet<ShareMapBehaviour>();
		this.treshold = 5; 
	}


	@Override
	public void action() {
		// TODO Auto-generated method stub
		String myPosition=((GraphAgent)this.myAgent).getCurrentPosition();
		ArrayList<String> listAgents = (ArrayList<String>) DFManager.getAllAgents(this.myAgent);
		String msgString = null ;
		Object msgObject = null;
		String idsender = null;
		boolean allDead = true;
		for (ShareMapBehaviour c : this.communicationSet){
			if (!(c.done())){
				allDead = false;
			}
		}
		if (allDead && this.stopmoving == 1){
			this.stopmoving = 0;
		}
		else {
			if (myPosition!="" && this.stopmoving == 0){
				System.out.println("I send ping");
			    this.mailbox.broadcastString("ping", listAgents);
			}
			//Couple couple = mailbox.getMsg();
			//getMsgObject et getString prennent un object dans la boite au lettre peu import que ce soit un string ou un object
			Couple couple = this.mailbox.getMsgStringAndSender("broadcast"); 
			if (couple != null){
				msgString = (String) couple.getLeft();
				idsender = (String) couple.getRight();
			}
				
			if (msgString != null){
			//	System.out.println("I receive the message : " + msgString);
				if (msgString.equals("ping")){
					//on répond
					System.out.println("I received the msg : ping, i stop and wait for the map");
					if (a.getStep() - a.getStepId(idsender) >= this.treshold){ //10 pas avant de renvoyer map
						this.mailbox.broadcastString("roger", listAgents);
						this.stopmoving = 1; //on s'arrete
					}
					else {
						//trop récent on fait rien
					}
				}
				if (msgString.equals("roger")){
					ShareMapBehaviour b = new ShareMapBehaviour((GraphAgent)this.myAgent, idsender);
					this.myAgent.addBehaviour(b);
					this.communicationSet.add(b);
				}
				if(msgString.startsWith("silo")){
					print("**********************************************************");
					String s = "silo";
					String siloposition = (String) msgString.subSequence(s.length()+1, msgString.length());
					print("siloposition : "+siloposition);
					print("**********************************************************");
					
					//get position
					//this.graph.
				}
				
			}

		}
		
		//Pour eviter de boucler comme une brute
		block(100);
		
			
	}
	
	public void onStart(){
		this.stopmoving= 0;
		this.cpt = 0;
	}
	
	public int onEnd(){
		return this.stopmoving;
	}
	private void print(String m){
		System.out.println(this.myAgent.getLocalName()+" : "+m);
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return true;
	}

}
