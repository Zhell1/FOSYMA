package mas.behaviours.atomic.generic;

import jade.core.behaviours.Behaviour;
import mas.abstractAgent;
import mas.behaviours.atomic.AtomicBehaviour;
import mas.tools.Condition;

public class DoWhileAtomic extends AtomicBehaviour {

	private Condition c;
	private Behaviour b;

	public DoWhileAtomic(abstractAgent a, Condition c, Behaviour b) {
		super(a);
		// TODO Auto-generated constructor stub
		this.c = c;
		this.b = b;
	}
	
	public void action() {
		this.b.action();
		while (this.c.check(this.agent)){
			this.b.action();
		}
		this.signal = 1;
	}

}
