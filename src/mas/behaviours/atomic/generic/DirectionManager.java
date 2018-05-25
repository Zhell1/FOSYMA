package mas.behaviours.atomic.generic;

import java.util.ArrayList;

import mas.abstractAgent;
import mas.behaviours.atomic.AtomicBehaviour;
import mas.tools.Action;
import mas.tools.Condition;
import mas.tools.MyGraph;

public class DirectionManager extends AtomicBehaviour {

	private Action a1;

	public DirectionManager(abstractAgent a, Action a1) {
		super(a);
		this.a1 = a1;
		
	}
		
	public void action() {
		boolean b1 = this.agent.getSwitchPath();
		boolean b2 = this.agent.getPath().isEmpty();		
		if (b1 || b2) {
			a1.act(this.agent);
			ArrayList<String> path = this.agent.getPath();
			if (path == null) {
				this.signal = -1;
				return;
			}
			this.signal = 1;
		}
		else {
			this.signal = 1;
		}
	}

}
