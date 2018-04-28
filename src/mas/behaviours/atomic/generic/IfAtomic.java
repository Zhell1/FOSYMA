package mas.behaviours.atomic.generic;

import mas.abstractAgent;
import mas.behaviours.atomic.AtomicBehaviour;
import mas.tools.Condition;

public class IfAtomic extends AtomicBehaviour {

	private Condition c;

	public IfAtomic(abstractAgent a, Condition c) {
		super(a);
		// TODO Auto-generated constructor stub
		this.c = c;
	}
	
	public void action() {
		if (this.c.check(this.agent)) {
			this.signal = 1;
			return;
		}
		this.signal = -1;
		return;
	}

}
