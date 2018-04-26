
package mas.behaviours.atomic;

import java.sql.Timestamp;
import java.util.HashMap;

import mas.abstractAgent;

public class WaitForStringAtomic extends AtomicBehaviour {
	/* Attend pour unstring
	 * signal -1 : delais de garde expiré
	 * signal 0 : attente
	 * signal 1 : objet trouvé
	 */
	
	/* TODO
	 * Pour le moment les messages qui ne sont pas une carte sont jetés
	 */
	
	protected int timeOut;
	protected int cpt;
	private String s;
	private long startTime;


	public WaitForStringAtomic(abstractAgent a, String s, int timeOut) {
		super(a);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		long currentTime = timestamp.getTime();
		this.startTime = currentTime;
		//on convertit en milisecondes
		this.timeOut = 1000 * timeOut;
		this.s = s;
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
		boolean b = msg.get("type").equals("String");
		if (!b) {
			this.signal = 0;
			return;
			}
		//c'est un string
		if (msg.get("content").equals(s)) {
			this.startTime = -1;
			this.signal = 1;
			return;
		}
		this.signal=0;
		}
	}