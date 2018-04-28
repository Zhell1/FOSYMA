package mas.behaviours.newBehaviour;

import mas.abstractAgent;

public class SiloBehaviour extends GraphAgentBehaviour {

	public SiloBehaviour(abstractAgent agent) {
		super(agent);
		
		registerFirstState(new SendListenBehaviour(a, "ping"), "SendListen");
		
		registerDefaultTransition("SendListen", "SendListen");
	}

}
