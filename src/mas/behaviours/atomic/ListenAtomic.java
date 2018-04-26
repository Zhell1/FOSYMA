package mas.behaviours.atomic;

import java.util.HashMap;

import env.Couple;
import mas.abstractAgent;

public class ListenAtomic extends AtomicBehaviour {

	public ListenAtomic(abstractAgent a) {
		super(a);
	}
	
	public void action() {
		this.agent.getMsg();
		this.signal = 1;
	}

}
