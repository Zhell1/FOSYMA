package mas.behaviours.newBehaviour;

import java.util.ArrayList;

import jade.core.behaviours.FSMBehaviour;
import mas.abstractAgent;
import mas.agents.GraphAgent;
import mas.behaviours.atomic.ExploreDestinationAtomic;
import mas.behaviours.atomic.ListenAtomic;
import mas.behaviours.atomic.MoveAtomic;
import mas.behaviours.atomic.RandomAtomic;
import mas.behaviours.atomic.SendAtomic;
import mas.behaviours.atomic.TraiteMsgAtomic;
import mas.behaviours.atomic.UpdateMapAtomic;
import mas.behaviours.atomic.VoidAtomic;
import mas.tools.MyGraph;

public class ExploratorBehaviour extends GraphAgentBehaviour{
	private int signal;
	
	public ExploratorBehaviour(abstractAgent agent){
		super(agent);
				
		registerFirstState(new ExploreDestinationAtomic(a), "Dest");
		registerState(new MoveAndCommunicateBehaviour(a), "MoveCom");
		registerState(new SendListenBehaviour(a, "ping"), "SendListen");
		
		registerTransition("Dest", "MoveCom", 1);
		registerTransition("Dest", "SendListen", -1);
		
		registerDefaultTransition("MoveCom", "Dest");
		
		registerDefaultTransition("SendListen", "SendListen");
	}
	
}
