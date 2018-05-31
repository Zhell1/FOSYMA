package mas.behaviours.atomic;

import java.sql.Timestamp;
import java.util.HashMap;

import mas.abstractAgent;

public class WaitForAtomic extends AtomicBehaviour {
	/* Attend pour un type de msg : ex carte
	 * signal -1 : delai de garde expiré
	 * signal 0 : attente
	 * signal 1 : objet trouvé
	 */
	
	/* TODO
	 * Pour le moment les messages qui ne sont pas une carte sont jetés
	 */
	
	protected int timeOut;
	protected int cpt;
	private String type;
	private long startTime;
	private boolean privee;

	public WaitForAtomic(abstractAgent a, String type, int timeOut) {
		super(a);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		long currentTime = timestamp.getTime();
		this.startTime = currentTime;
		this.timeOut = 1000 * timeOut;  //on converti en milisecondes
		this.type = type;
		this.privee = false;
	}
	

	public WaitForAtomic(abstractAgent a, String type, int timeOut, boolean privee) {
		this(a, type, timeOut);
		this.privee = privee;
	}
	
	
	public void action() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		long currentTime = timestamp.getTime();
		//Pour reset
		if (this.startTime == -1) {
			this.startTime = currentTime;
		}
		
		if (currentTime - this.startTime > this.timeOut) {
			this.startTime = -1;
			this.signal = -1;
			return;
		}
		HashMap<String, Object> msg;
		if(privee == false) {
			msg = this.agent.getMsg();
		}
		else{
			String destinataire = this.agent.getlastPing();
			msg = this.agent.getMsg(destinataire);
		}
		
		if (msg == null) {
			this.signal = 0;
			return;
		}
		Object t = msg.get("type");
		//this.agent.print("type :" + t);
		boolean b = msg.get("type").equals(type);
		
		//String sender = (String) msg.get("sender");
		//System.out.println(this.agent.getLocalName() + " received msg '"+type+"' from "+sender);
		
		if (b) {
			this.startTime = -1;
			this.signal = 1;
			return;
			}
		this.signal = 0;
		}
	}

