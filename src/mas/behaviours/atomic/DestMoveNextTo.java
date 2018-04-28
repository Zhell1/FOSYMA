package mas.behaviours.atomic;

import java.util.ArrayList;

import mas.abstractAgent;

public class DestMoveNextTo extends AtomicBehaviour {

	private String pos;

	public DestMoveNextTo(abstractAgent a, String pos) {
		super(a);
		this.pos = pos;
		// TODO Auto-generated constructor stub
	}
	
	public void action() {
		if (this.agent.getPath().size() == 1) {
			this.signal = 1;
			return;
		}
		if (this.agent.getSwitchPath()) {
			//recalculate path
			ArrayList<String> path = this.agent.getmyGraph().getShortestPath(this.pos);
			this.agent.setPath(path);
			this.signal = 2;
			return;
		}
		this.signal = 2;
		return;
	}
}
