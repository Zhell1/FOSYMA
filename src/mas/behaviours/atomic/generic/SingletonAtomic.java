package mas.behaviours.atomic.generic;

import mas.abstractAgent;
import mas.behaviours.atomic.AtomicBehaviour;
import mas.tools.Action;

public class SingletonAtomic extends AtomicBehaviour {

	private Action ac;

	public SingletonAtomic(abstractAgent a, Action ac) {
		super(a);
		// TODO Auto-generated constructor stub
		this.ac = ac;
	}
	
	public void action() {
		this.ac.act(this.agent);
	}
	
}
