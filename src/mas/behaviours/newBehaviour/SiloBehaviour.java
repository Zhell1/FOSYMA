package mas.behaviours.newBehaviour;

import mas.abstractAgent;

public class SiloBehaviour extends GraphAgentBehaviour {

	public SiloBehaviour(abstractAgent agent) {
		super(agent);

		//TODO PAS SUR QUE CA SUFFISE SI LE SILO BOUGE CAR IL FAUT UPDATE LA POSITION
		this.g.setSiloPosition(this.a.getCurrentPosition()); 
		
		registerFirstState(new SendListenBehaviour(a, "ping"), "SendListen");
		
		registerDefaultTransition("SendListen", "SendListen");
	}

}
