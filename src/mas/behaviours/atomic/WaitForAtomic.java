package mas.behaviours.atomic;

import java.sql.Timestamp;
import java.util.HashMap;

import mas.abstractAgent;

public class WaitForAtomic extends AtomicBehaviour {
	/* Attend pour un type de msg : ex carte
	 * signal -1 : delais de garde expiré
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

	public WaitForAtomic(abstractAgent a, String type, int timeOut) {
		super(a);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		long currentTime = timestamp.getTime();
		this.startTime = currentTime;
		//on convertit en milisecondes
		this.timeOut = 1000 * timeOut;
		this.type = type;
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
		
		HashMap<String, Object> msg = this.agent.getMsg();
		if (msg == null) {
			this.signal = 0;
			return;
		}
		Object t = msg.get("type");
		this.agent.print("type :" + t);
		boolean b = msg.get("type").equals(type);
		if (b) {
			this.startTime = -1;
			this.signal = 1;
			return;
			}
		this.signal = 0;
		}
	}

