package mas.behaviours.atomic;

import env.Couple;
import mas.abstractAgent;

public class ListenAtomic extends AtomicBehavior {

	public ListenAtomic(abstractAgent a) {
		super(a);
	}
	
	public void action() {
		Couple<Object, String> msg = this.agent.mailbox.get();
		this.agent.setLastMsg(msg);
		this.signal = 1;
	}

}
