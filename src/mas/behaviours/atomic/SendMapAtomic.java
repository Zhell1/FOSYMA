package mas.behaviours.atomic;

import java.util.ArrayList;
import java.util.List;

import java.util.HashMap;
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
		if(this.privee == false) // normalement ça n'arrive plus maintenant ?
			this.agent.mailbox.send(msg);
		else{
			String destinataire = this.agent.getlastPing();
			String myname = this.agent.getLocalName();
			this.agent.print("SEND my map to: "+destinataire);
			//this.agent.mailbox.send(msg, destinataire, myname);
			
			HashMap<String,Object> toSendMap = this.agent.gettoSendMap(destinataire);
			this.agent.mailbox.send(toSendMap, destinataire, myname);
			
		}
		this.signal = 1;
	}
	

}
