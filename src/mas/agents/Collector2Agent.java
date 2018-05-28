package mas.agents;

import mas.behaviours.newBehaviour.Collecteur2;
import mas.behaviours.newBehaviour.CollectorBehaviour_old;
import mas.behaviours.newBehaviour.ExploratorBehaviour;
import mas.tools.DFManager;

public class Collector2Agent extends GraphAgent {
	public void setup() {
		super.setup();
		DFManager.register(this, "collector");
		this.addBehaviour(new Collecteur2(this));

	}
}
