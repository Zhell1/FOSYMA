package mas.behaviours.atomic.generic;

import mas.abstractAgent;
import mas.behaviours.atomic.AtomicBehaviour;
import mas.tools.Action;
import mas.tools.Condition;

public class IfAtomic extends AtomicBehaviour {

	private Condition c;
	private Action a1;
	private Action a2;

	public IfAtomic(abstractAgent a, Condition c, Action a1, Action a2) {
		super(a);
		// TODO Auto-generated constructor stub
		this.c = c;
		this.a1 = a1;
		this.a2 = a2;
	}
	
	public void action() {
		if (this.c.check(this.agent)) {
			a1.act(this.agent);
			this.signal = 1;
			return;
		}
		a2.act(this.agent);
		this.signal = -1;
		return;
	}

}
