package mas.behaviours.newBehaviour;

import mas.abstractAgent;

public class SiloBehaviour extends GraphAgentBehaviour {

	public SiloBehaviour(abstractAgent agent) {
		super(agent);

		//TODO PAS SUR QUE CA SUFFISE SI LE SILO BOUGE CAR IL FAUT UPDATE LA POSITION
		this.g.setSiloPosition(this.a.getCurrentPosition()); 
		this.g.addSilo(); // init le premier noeud
		
		registerFirstState(new SendListenBehaviour(a, "ping silo :"+this.a.getCurrentPosition()), "SendListen");
		//registerState(new SiloMoveAtomic(a), "TestMoveSilo");
		//registerDefaultTransition("SendListen", "TestMoveSilo");
		//registerDefaultTransition("TestMoveSilo", "SendListen");
		registerDefaultTransition("SendListen", "SendListen");
	}

	public int onEnd() {
		this.reset();
		return this.signal;
	}
	
	
	public void reset() {
		//this.a.print("reset");
		this.forceTransitionTo("SendListen");
		//this.a.print("état courant : " + this.currentName);
	}
	
}
