package mas.behaviours;

import org.graphstream.graph.Graph;

import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import mas.abstractAgent;
import mas.behaviours.FirstAgentExplore;

public class CollectorFSMBehaviour extends FSMBehaviour {
	public CollectorFSMBehaviour(abstractAgent a){
		
		/*
		 * aller voir MyGraph todo ligne 460		
		 */
		registerFirstState(new FirstAgentExplore(a, 1), "explo");
		registerState(new ListenerBehaviour(a), "listener");
		registerState(new DeblocageBehaviour(a), "deblocage");
		registerState(new CollectorBehaviour(a), "collector");
		registerState(new FirstAgentExplore(a, 1), "explo");
		
		registerTransition("listener", "listener", 1);
		registerDefaultTransition("listener", "explo");
		
		String toBeReset1[] = {"explo"};
		registerTransition("explo", "listener",0);
		registerTransition("explo", "deblocage", 1, toBeReset1);
		registerTransition("explo", "collector", 2);
		
		String toBeReset2[] = {"deblocage"};
		registerTransition("deblocage", "deblocage",0);
		registerTransition("deblocage", "explo", 1, toBeReset2);
		
		registerTransition("collector", "explo", 1);
	}
	
}
