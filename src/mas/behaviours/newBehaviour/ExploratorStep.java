package mas.behaviours.newBehaviour;

import java.util.ArrayList;

import jade.core.behaviours.FSMBehaviour;
import mas.abstractAgent;
import mas.agents.GraphAgent;
import mas.behaviours.atomic.EndAtomic;
import mas.behaviours.atomic.ExploreDestinationAtomic;
import mas.behaviours.atomic.ListenAtomic;
import mas.behaviours.atomic.MoveAtomic;
import mas.behaviours.atomic.RandomAtomic;
import mas.behaviours.atomic.SendAtomic;
import mas.behaviours.atomic.TraiteMsgAtomic;
import mas.behaviours.atomic.UpdateMapAtomic;
import mas.behaviours.atomic.VoidAtomic;
import mas.tools.MyGraph;

public class ExploratorStep extends GraphAgentBehaviour{
	/* La fonction action() fais une itération (exécute un sous behiavour et change l'etat)
	 * Les behaviours step n'on pas besoin de fonction onEnd et reset() car ils sont appelé de l'extérieur manuellement
	 * à l'aide la fonction action() !
	 * 
	 */
	private int signal;
	
	public ExploratorStep(abstractAgent agent){
		super(agent);
		registerFirstState(new ExploreDestinationAtomic(a), "Dest");
		registerState(new MoveAndCommunicateBehaviour(a), "MoveCom");
		registerState(new EndAtomic(a, this, 1), "End");
		
		registerTransition("Dest", "MoveCom", 1);
		registerTransition("Dest", "End", -1);
		
		registerDefaultTransition("MoveCom", "Dest");
		registerDefaultTransition("End", "End");
	}
	
}
