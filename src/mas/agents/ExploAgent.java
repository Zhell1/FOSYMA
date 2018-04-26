package mas.agents;

import mas.behaviours.newBehaviour.ExploratorBehaviour;
import mas.tools.DFManager;

public class ExploAgent extends GraphAgent {
	public void setup() {
		super.setup();
		DFManager.register(this, "explorer");
		this.addBehaviour(new ExploratorBehaviour(this));

	}
}
