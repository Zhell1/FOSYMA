package mas.behaviours;

import java.util.ArrayList;
import java.util.HashMap;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

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
		this.timeout = 10;
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
		if (myPosition!=""){
			System.out.println("I send ping");
		    this.mailbox.broadcastString("ping", listAgents);
		}
		
		if (this.cpt > this.timeout){
			System.out.println("TIMEOUT");
			this.signalOut = 0;
		}
		if(this.mapsent && this.mapreceived) {
			System.out.println("SENT & RECEIVED");
			this.signalOut = 0;
			//reinit
			this.mapsent = false;
			this.mapreceived = false;
		}
		else {
			String msg = mailbox.getMsgString();
			Object msgobject = mailbox.getMsgObject();
				
			if (msg != null){
				System.out.println("I receive the message : " + msg);
				if (msg.equals("ping")){
					//on répond
					this.mailbox.broadcastString("roger ", listAgents);
					this.signalOut = 1;
					//on s'arrete
				}else if (this.signalOut == 1 && msg.equals("roqer")){
					//partager cartes
					System.out.println("I AM SENDING MY MAP");
					HashMap<String, Object> send = this.graph.toHashMap();
					this.mailbox.broadcastObject(send, listAgents);
					this.mapsent = true;
				} else if(this.signalOut == 1 && this.mapreceived == false && msgobject instanceof HashMap) {
					//merger todo
					System.out.println("I AM RECEIVEING A MAP");
					HashMap<String, Object> received = (HashMap<String, Object>)msgobject;
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
