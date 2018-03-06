package mas.behaviours;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

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
	private int signalOut;
	private int timeout;
	private int cpt;
	private boolean mapsent;
	private boolean mapreceived;
	private MyGraph graph;
	
	public ListenerBehaviour(final Agent myagent) {
		super(myagent);
		this.mailbox = new Messages(myagent);
		this.timeout = 100;
		this.cpt = 0;
		this.finished = false;
		this.signalOut = 0;
		this.mapsent = false;
		this.mapreceived = false;
		this.graph = ((FirstAgent)myagent).getmyGraph();
	}


	@Override
	public void action() {
		// TODO Auto-generated method stub
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
		ArrayList<String> listAgents = (ArrayList<String>) DFManager.getAllAgents(this.myAgent);
		String msgString = null ;
		Object msgObject = null;
		this.cpt++;
		if (myPosition!="" && this.signalOut == 0){
		//	System.out.println("I send ping");
		    this.mailbox.broadcastString("ping", listAgents);
		}
		
		if (this.signalOut == 1 && this.cpt > this.timeout){
			System.out.println("TIMEOUT");
			this.signalOut = 0;
			this.cpt = 0;
		}
		if(this.mapsent && this.mapreceived) {
			System.out.println("SENT & RECEIVED");
			this.signalOut = 0;
			//reinit
			this.mapsent = false;
			this.mapreceived = false;
			this.cpt = 0;
		}
		else {
			//Couple couple = mailbox.getMsg();
			//getMsgObject et getString prennent un object dans la boite au lettre peu import que ce soit un string ou un object
			msgString = this.mailbox.getMsgString(); 
			msgObject = this.mailbox.getMsgObject();
				
			if (msgString != null){
			//	System.out.println("I receive the message : " + msgString);
				if (msgString.equals("ping")){
					//on répond
			//		System.out.println("I received the msg : ping, i stop and wait for the map");
					this.mailbox.broadcastString("roger", listAgents);
					this.signalOut = 1;
					//on s'arrete
				}
				if (msgString.equals("roger")){
					//System.out.println("WOW");
					//partager cartes
					HashMap<String, Object> send = this.graph.toHashMap();
					System.out.println("I AM SENDING MY MAP : " + send);
					this.mailbox.broadcastObject((Serializable)(send), listAgents);
					this.mapsent = true;
				}
			}
				
			if (msgObject != null){
				if (msgObject instanceof HashMap){
					//merger todo
					System.out.println("I AM RECEIVEING A MAP");
					HashMap<String, Object> received = (HashMap<String, Object>)msgObject;
					MyGraph newMap = new mas.tools.MyGraph((abstractAgent) this.myAgent, received);
					this.graph.merge(newMap);
					
					this.mapreceived = true;
				}
			}
		
			else {
			//Pour eviter de boucler comme une brute
			block(100);
			}
		}
		
	}
	
	public int onEnd(){
		return this.signalOut;
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return true;
	}

}
