package mas.behaviours.atomic;

import java.util.ArrayList;
import java.util.List;

import mas.abstractAgent;
import mas.tools.DFManager;

public class SendAtomic extends AtomicBehaviour {
	/* signal 1 = message envoyé sans problèmes
	*/
	
	private Object msg;
	private String destinataire;
	boolean privee;

	public SendAtomic(abstractAgent a, Object msg) {
		super(a);
		this.msg = msg;
		this.destinataire = null;
		this.privee = false;
	}
	

	public SendAtomic(abstractAgent a, Object msg, boolean privee) {
		this(a, msg);
		this.privee = privee;
	}
	
	public void action() {
		//this.agent.print("SendAtomic");
		if(privee == false) {
			this.agent.mailbox.send(msg);
		}
		else {
			String destinataire = this.agent.getlastPing();
			String myname = this.agent.getLocalName();
			//this.agent.print(" SEND "+msg);
			this.agent.mailbox.send(msg, destinataire, myname);
		}
		this.signal = 1;
		
	}
	

}
