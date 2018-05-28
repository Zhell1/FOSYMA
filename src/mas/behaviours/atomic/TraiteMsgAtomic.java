package mas.behaviours.atomic;

import java.util.HashMap;

import mas.abstractAgent;

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
			if (msg.get("content").equals("ping")){
				this.signal = 1;
				return;
			}
			if (cpt <= 4){
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
