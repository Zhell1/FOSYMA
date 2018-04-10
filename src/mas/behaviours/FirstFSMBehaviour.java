package mas.behaviours;

import org.graphstream.graph.Graph;

import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import mas.abstractAgent;
import mas.behaviours.FirstAgentExplore;

public class FirstFSMBehaviour extends FSMBehaviour {
	public FirstFSMBehaviour(abstractAgent a){
		/* Il faut que les comportements implémentent une fonction int onEnd() qui indique le signal de transition */
		
		registerFirstState(new FirstAgentExplore(a, false), "explo");
		registerState(new ListenerBehaviour(a), "listener");
		registerState(new DeblocageBehaviour(a), "deblocage");
		
		registerTransition("listener", "listener", 1);
		registerDefaultTransition("listener", "explo");
		
		String toBeReset1[] = {"explo"};
		registerTransition("explo", "listener",0);
		registerTransition("explo", "deblocage", 1, toBeReset1);
		
		String toBeReset2[] = {"deblocage"};
		registerTransition("deblocage", "deblocage",0);
		registerTransition("deblocage", "explo", 1, toBeReset2);
		
	}
	
}
