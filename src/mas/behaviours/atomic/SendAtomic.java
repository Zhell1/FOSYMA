package mas.behaviours.atomic;

import java.util.ArrayList;
import java.util.List;

import mas.abstractAgent;
import mas.tools.DFManager;

public class SendAtomic extends AtomicBehaviour {
	/* signal 1 = message envoyé sans problèmes
	*/
	
	private Object msg;

	public SendAtomic(abstractAgent a, Object msg) {
		super(a);
		this.msg = msg;
	}
	
	public void action() {
		this.agent.mailbox.send(msg);
		this.signal = 1;
	}
	

}
