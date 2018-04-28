package mas.agents;

import mas.behaviours.newBehaviour.CollectorBehaviour;
import mas.behaviours.newBehaviour.SiloBehaviour;
import mas.tools.DFManager;

public class SiloAgent extends GraphAgent {
	
	public void setup() {
		super.setup();
		DFManager.register(this, "silo");
		this.addBehaviour(new SiloBehaviour(this));

	}

}
