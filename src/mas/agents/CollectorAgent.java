package mas.agents;

import java.util.HashMap;

import mas.behaviours.CollectorBehaviour;
import mas.behaviours.CollectorFSMBehaviour;
import mas.behaviours.FirstFSMBehaviour;
import mas.tools.DFManager;
import env.Environment;

public class CollectorAgent extends GraphAgent {
	protected void setup(){

		supersetup();
		//get the parameters given into the object[]. In the current case, the environment where the agent will evolve
		final Object[] args = getArguments();
		if(args[0]!=null){

			deployAgent((Environment) args[0]);

		}else{
			System.err.println("Malfunction during parameter's loading of agent"+ this.getClass().getName());
			System.exit(-1);
		}
		//setup graph
		setupgraph();
		//this.graph = new SingleGraph("test");
		initMyGraph();
		this.step = 0;
		this.stepMap = new HashMap<String, Integer>();
		//Add the behaviours
		addBehaviour(new CollectorFSMBehaviour(this));

		DFManager.register(this, "collector");
		
		System.out.println("the agent "+this.getLocalName()+ " is started");

	}
}
