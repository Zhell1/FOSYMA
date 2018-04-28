package mas.behaviours.atomic;

import java.util.ArrayList;
import java.util.List;

import mas.abstractAgent;
import mas.tools.DFManager;
import mas.tools.MyGraph;

public class SendMapAtomic extends AtomicBehaviour {
	/* signal 1 = message envoyé sans problèmes
	*/
	
	private Object msg;
	private MyGraph g;

	public SendMapAtomic(abstractAgent a) {
		super(a);
		MyGraph g = this.agent.getmyGraph();
		this.g = g;
	}
	
	public void action() {
		msg = g.toHashMap();
		this.agent.mailbox.send(msg);
		this.signal = 1;
	}
	

}
