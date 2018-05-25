package mas.behaviours.atomic.generic;

import jade.core.behaviours.Behaviour;
import mas.abstractAgent;
import mas.behaviours.atomic.AtomicBehaviour;
import mas.behaviours.atomic.ExploreDestinationAtomic;
import mas.behaviours.newBehaviour.GraphAgentBehaviour;
import mas.behaviours.newBehaviour.MoveAndCommunicateBehaviour;
import mas.behaviours.newBehaviour.SendListenBehaviour;

public class GenericDeplacement extends GraphAgentBehaviour {

	public GenericDeplacement(abstractAgent a, Behaviour DManager, Behaviour Move, Behaviour Debloquage, Behaviour CommunicationManager, Behaviour Com) {
		super(a);
		// TODO Auto-generated constructor stub
		//registerFirstState(new DManager(a), "Dest");
		registerState(new MoveAndCommunicateBehaviour(a), "MoveCom");
		registerState(new SendListenBehaviour(a, "ping"), "SendListen");
		
		registerTransition("Dest", "MoveCom", 1);
		registerTransition("Dest", "SendListen", -1);
		
		registerDefaultTransition("MoveCom", "Dest");
		
		registerDefaultTransition("SendListen", "SendListen");
	}

}
