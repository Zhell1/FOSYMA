package mas.behaviours;

import java.util.ArrayList;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.util.leap.List;
import mas.tools.DFManager;
import mas.tools.Messages;

public class ShareMapBehaviour extends SimpleBehaviour {
	private Messages mailbox;
	private boolean finished;

	public ShareMapBehaviour (final Agent myagent) {
		super(myagent);
		this.mailbox = new Messages(myagent);
		this.finished = false;
		//super(myagent);
	}

	@Override
	public void action() {
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
		ArrayList<String> listAgents = (ArrayList<String>) DFManager.getAllAgents(this.myAgent);
		//System.out.println(this.myAgent.getLocalName()+" sees : " + listAgents);

		if (myPosition!=""){
			//System.out.println("Agent "+this.myAgent.getLocalName()+ " is trying to reach its friends");
		    this.mailbox.broadcastString("ping", listAgents);
			this.finished = true;
		}

	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return this.finished;
	}
}
