
package mas.behaviours.atomic;

import java.sql.Timestamp;
import java.util.HashMap;

import mas.abstractAgent;

public class WaitForStringAtomic extends AtomicBehaviour {
	/* Attend pour unstring
	 * signal -1 : delais de garde expiré
	 * signal  0 : attente
	 * signal  1 : objet trouvé
	 * signal -2 : j'ai bien recu ton ping mais je n'ai pas assez de nouvelles infos pour renvoyer la carte
	 */
	
	/* TODO
	 * Pour le moment les messages qui ne sont pas une carte sont jetés
	 */
	
	protected int timeOut;
	protected int cpt;
	private String s;
	private long startTime;
	private boolean privee;


	public WaitForStringAtomic(abstractAgent a, String s, int timeOut) {
		super(a);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		long currentTime = timestamp.getTime();
		this.startTime = currentTime;
		//on convertit en milisecondes
		this.timeOut = 1000 * timeOut;
		this.s = s;
		this.privee =false;
	}
	public WaitForStringAtomic(abstractAgent a, String s, int timeOut, boolean privee) {
		this(a, s, timeOut);
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
			//this.agent.print("privee getMsg from idconv="+destinataire);
			msg = this.agent.getMsg(destinataire);
		}
		
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
			String sender = (String) msg.get("sender");
			//cas roger
			if(s.equals("roger")){
				int nbmodifsmin = this.agent.getnbmodifsmin();
				
				int difflastsent = this.agent.getDifferenceLastSent(sender);
				this.agent.print("***************************************");
				this.agent.print("recu roger, nbmodifs since last = "+difflastsent);
				this.agent.print("***************************************");
				
				if(difflastsent  > nbmodifsmin){
					this.signal = 1;
					return;
				}
				else {
					//pas assez de modifs pour justifier un renvoi de la carte
					this.signal = -2;
					return;
				}
			}
			//cas ack de sharemap
			if(s.equals("ack_map_received")){
				this.agent.print("ACK MAP RECEIVED from "+sender);
				//si on parle d'un ack (de sharemap) il faut MAJ lastSentMap
				this.agent.updateLastSentMap(sender);
			}
			
			this.startTime = -1;
			this.signal = 1;
			return;
		}
		this.signal=0;
		}
	}