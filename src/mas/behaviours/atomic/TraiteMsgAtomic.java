package mas.behaviours.atomic;

import java.util.HashMap;

import mas.abstractAgent;

public class TraiteMsgAtomic extends AtomicBehaviour {
	/* signal -1 = pas de message
	 * signal 1 = ping
	 * signal 2 = roger
	 * 
	 */

	public TraiteMsgAtomic(abstractAgent a) {
		super(a);
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
			this.signal = -1;
			return;
		}
	}

}
