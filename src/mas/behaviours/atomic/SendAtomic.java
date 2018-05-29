package mas.behaviours.atomic;

import java.util.ArrayList;
import java.util.List;

import mas.abstractAgent;
import mas.tools.DFManager;

public class SendAtomic extends AtomicBehaviour {
	/* signal 1 = message envoyé sans problèmes
	*/
	
	private Object msg;
	private String idconv;
	private String destinataire;

	public SendAtomic(abstractAgent a, Object msg) {
		super(a);
		this.msg = msg;
		this.destinataire = null;
		this.idconv=null;
	}
	

	public SendAtomic(abstractAgent a, Object msg, String destinataire, String idconv) {
		this(a, msg);
		this.destinataire = destinataire;
		this.idconv = idconv;
	}
	
	public void action() {
		this.agent.print("SendAtomic");
		if(idconv == null) {
			this.agent.mailbox.send(msg);
		}
		else {
			this.agent.mailbox.send(msg, destinataire, idconv);
		}
		this.signal = 1;
		
	}
	

}
