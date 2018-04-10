package mas.behaviours;

import java.util.ArrayList;
import java.util.HashSet;

import env.Couple;
import mas.agents.DummySilo;
import mas.agents.FirstAgent;
import mas.agents.GraphAgent;
import mas.tools.DFManager;
import mas.tools.HeuristiqueDistance;
import mas.tools.Messages;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

public class DummySiloBehaviour extends SimpleBehaviour{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2058134622078521998L;
	private Messages mailbox;
	private DummySilo a;
	private int stopmoving;
	private HashSet<ShareMapBehaviour> communicationSet;

	/**
	 * An agent tries to contact its friend and to give him its current position
	 * @param myagent the agent who posses the behaviour
	 *  
	 */
	public DummySiloBehaviour (final Agent myagent) {
		super(myagent);
		this.mailbox = new Messages(myagent);
		this.a = (DummySilo)(myagent);
		this.stopmoving = 0;
		this.communicationSet = new HashSet<ShareMapBehaviour>();
	}

	@Override
	public void action() {	
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
		ArrayList<String> listAgents = (ArrayList<String>) DFManager.getAllAgents(this.myAgent);
		String msgString = null ;
		Object msgObject = null;
		String idsender = null;
		if (myPosition!="" && this.stopmoving == 0){
			System.out.println("I send ping");
			String s = "silo_"+myPosition;
			print(s);
		    this.mailbox.broadcastString(s, listAgents);
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
				if (true){ // condition de renvoi de la carte (avant on avait mis 10 pas)
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

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}