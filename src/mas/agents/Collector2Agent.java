package mas.agents;

import mas.behaviours.newBehaviour.CollectorBehaviour;
import mas.behaviours.newBehaviour.ExploratorBehaviour;
import mas.tools.DFManager;

public class Collector2Agent extends GraphAgent {
	public void setup() {
		super.setup();
		DFManager.register(this, "collector");
		this.addBehaviour(new CollectorBehaviour(this));

	}
}
