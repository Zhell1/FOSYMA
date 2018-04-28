package mas.behaviours.atomic;

import java.util.ArrayList;

import mas.abstractAgent;

public class MoveAndCollectAtomic extends AtomicBehaviour {
	/* signal 1 = tresors ramassé
	 * signal -1 = se deplace vers tresors
	 */

	public MoveAndCollectAtomic(abstractAgent a) {
		super(a);
	}
	
	public void action() {
		ArrayList<String> path = this.agent.getPath();
		if (path == null || path.isEmpty()) {
			this.agent.pickTreasure();
			this.signal = 1;
			return;
		}
		this.signal = -1;
	}

}
