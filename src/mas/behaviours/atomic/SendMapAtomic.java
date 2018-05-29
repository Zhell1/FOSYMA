package mas.behaviours.atomic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mas.abstractAgent;
import mas.tools.DFManager;
import mas.tools.MyGraph;

public class SendMapAtomic extends AtomicBehaviour {
	/* signal 1 = message envoyé sans problèmes
	*/
	
	private Object msg;
	private MyGraph g;
	private String idconv;
	private boolean privee;

	public SendMapAtomic(abstractAgent a) {
		super(a);
		MyGraph g = this.agent.getmyGraph();
		this.g = g;
		this.privee = false;
	}
	public SendMapAtomic(abstractAgent a, boolean privee) {
		this(a);
		this.privee = privee;
	}
	
	public void action() {
		msg = g.toHashMap();
		if(this.privee == false)
			this.agent.mailbox.send(msg);
		else{

			String destinataire = this.agent.getlastPing();
			String myname = this.agent.getLocalName();
			HashMap<String, Object> lastSended = this.agent.SendedMap.get(destinataire);
			if (lastSended == null) {
				this.agent.mailbox.send(msg, destinataire, myname);
				this.signal = 1;
				return;
			}
			Object newMsg = this.agent.getmyGraph().DifMap(lastSended, (HashMap<String, Object>)msg);
			this.agent.mailbox.send(newMsg, destinataire, myname);
		}
		this.signal = 1;
	}
	

}
