package mas.behaviours.atomic;

import java.util.HashMap;

import mas.abstractAgent;
import mas.agents.GraphAgent;

public class TraiteMsgAtomic extends AtomicBehaviour {
	/* signal -1 = pas de message
	 * signal 1 = ping
	 * signal 2 = roger
	 * 
	 */

	private int cpt;

	public TraiteMsgAtomic(abstractAgent a) {
		super(a);
		this.cpt = 0;
		// TODO Auto-generated constructor stub
	}
	
	public void action() {
		HashMap<String, Object> msg = this.agent.consultLastMsg();

		if (msg == null) {
			this.signal = -1;
			return;
		}
		if (msg.get("type").equals("String")) {
			String msgstring = (String) msg.get("content");
			if(msgstring.startsWith("ping silo")) {
				//si on ne sait pas où est le silo
				GraphAgent graphagent = ((GraphAgent)this.myAgent);
				if( graphagent.getmyGraph().getSiloPosition() == null ) {
					String[] lres = msgstring.split(":");
					String siloposition = lres[1];	 //recup silo position
					graphagent.getmyGraph().setSiloPosition(siloposition);
					graphagent.print("TraiteMsgAtomic: found silo from his ping at "+siloposition);
				}
				this.signal = 1;
				return;
			}
			else if (msgstring.equals("ping")){
				this.signal = 1;
				return;
			}
			if (cpt <= 100){ //traite 100 messages
				this.signal = 0;
				cpt ++;
				return ;
			}
			this.signal = -1;
			this.cpt = 0;
			return;
		}
	}

}
