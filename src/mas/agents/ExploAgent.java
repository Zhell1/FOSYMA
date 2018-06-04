package mas.agents;

import mas.behaviours.newBehaviour.Explorateur2;
import mas.behaviours.newBehaviour.ExploratorBehaviour;
import mas.tools.DFManager;

public class ExploAgent extends GraphAgent {
	public void setup() {
		super.setup();
		DFManager.register(this, "explorer");
		this.addBehaviour(new Explorateur2(this));

	}
}
