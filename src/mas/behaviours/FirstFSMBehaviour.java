package mas.behaviours;

import org.graphstream.graph.Graph;

import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import mas.abstractAgent;
import mas.behaviours.FirstAgentExplore;

public class FirstFSMBehaviour extends FSMBehaviour {
	public FirstFSMBehaviour(abstractAgent a){
		/* Il faut que les comportements implémentent une fonction int onEnd() qui indique le signal de transition */
		registerFirstState(new FirstAgentExplore(a), "explo");
		registerState(new ListenerBehaviour(a), "listener");
		registerTransition("listener", "listener", 1);
		registerDefaultTransition("listener", "explo");
		registerDefaultTransition("explo", "listener");
	}
	
}
