package mas.behaviours.atomic;

import java.util.ArrayList;
import java.util.List;

import mas.abstractAgent;
import mas.tools.DFManager;

public class SendAtomic extends AtomicBehavior {
	/* signal 1 = message envoyé sans problèmes
	*/
	
	private String msg;

	public SendAtomic(abstractAgent a, String msg) {
		super(a);
		this.msg = msg;
	}
	
	public void action() {
		ArrayList<String> listAgents = DFManager.getAllAgents(this.agent);
		this.agent.mailbox.broadcastString(msg, listAgents);
		this.signal = 1;
	}
	

}
