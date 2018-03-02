package mas.behaviours;

import java.util.ArrayList;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import mas.tools.DFManager;
import mas.tools.Messages;

public class ListenerBehaviour extends SimpleBehaviour {
	private boolean finished;
	private Messages mailbox;
	
	public ListenerBehaviour(final Agent myagent) {
		super(myagent);
		this.mailbox = new Messages(myagent);
		this.finished = false;
	}


	@Override
	public void action() {
		// TODO Auto-generated method stub
		String msg = mailbox.getMsgString();
		if (msg != null){
			System.out.println("I receive the message : " + msg);
			if (msg == "ping"){
				ArrayList<String> listAgents = (ArrayList<String>) DFManager.getAllAgents(this.myAgent);
				this.mailbox.broadcastString("roger", listAgents);
			}
		}
		block(100);
		
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}
