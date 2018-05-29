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
	private String idconv;
	private String destinataire;

	public SendMapAtomic(abstractAgent a) {
		super(a);
		MyGraph g = this.agent.getmyGraph();
		this.g = g;
		this.destinataire=null;
		this.idconv = null;
	}
	public SendMapAtomic(abstractAgent a, String destinataire, String idconv) {
		this(a);
		this.destinataire = destinataire;
		this.idconv = idconv;
	}
	
	public void action() {
		msg = g.toHashMap();
		if(this.idconv == null)
			this.agent.mailbox.send(msg);
		else
			this.agent.mailbox.send(msg, destinataire, idconv); 
		this.signal = 1;
	}
	

}
