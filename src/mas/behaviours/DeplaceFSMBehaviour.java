package mas.behaviours;

import org.graphstream.graph.Graph;

import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import mas.abstractAgent;
import mas.behaviours.FirstAgentExplore;

public class DeplaceFSMBehaviour extends FSMBehaviour {
	private int signal;
	
	public DeplaceFSMBehaviour(abstractAgent a){
		/* Il faut que les comportements implémentent une fonction int onEnd() qui indique le signal de transition */
		
		registerFirstState(new FirstAgentExplore(a, 0), "explo");
		registerState(new DeblocageBehaviour(a), "deblocage");
		
		registerDefaultTransition("listener", "explo");
		
		String toBeReset1[] = {"explo"};
		this.signal = 0;
		registerTransition("explo", "deblocage", 1, toBeReset1);
		
		String toBeReset2[] = {"deblocage"};
		registerTransition("deblocage", "deblocage",0);
		registerTransition("deblocage", "explo", 1, toBeReset2);
	}
	@Override
	public int onEnd() {
		// TODO Auto-generated method stub
		return this.signal;
	}
	
}
