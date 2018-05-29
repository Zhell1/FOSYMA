package mas.behaviours.newBehaviour;

import mas.abstractAgent;

public class SiloBehaviour extends GraphAgentBehaviour {

	public SiloBehaviour(abstractAgent agent) {
		super(agent);

		
		registerFirstState(new SendListenBehaviour(a, "ping silo :"+a.getPosition()), "SendListen");
		
		registerDefaultTransition("SendListen", "SendListen");
	}

}
