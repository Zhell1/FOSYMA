package mas.behaviours.newBehaviour;

import java.util.ArrayList;

import jade.core.behaviours.FSMBehaviour;
import mas.abstractAgent;
import mas.agents.GraphAgent;
import mas.behaviours.atomic.ColectorDestinationAtomic;
import mas.behaviours.atomic.DestMoveNextTo;
import mas.behaviours.atomic.EndAtomic;
import mas.behaviours.atomic.ExploreDestinationAtomic;
import mas.behaviours.atomic.ListenAtomic;
import mas.behaviours.atomic.MoveAndCollectAtomic;
import mas.behaviours.atomic.MoveAtomic;
import mas.behaviours.atomic.RandomAtomic;
import mas.behaviours.atomic.SendAtomic;
import mas.behaviours.atomic.TraiteMsgAtomic;
import mas.behaviours.atomic.UpdateMapAtomic;
import mas.behaviours.atomic.VoidAtomic;
import mas.behaviours.atomic.generic.DirectionManager;
import mas.behaviours.atomic.generic.IfAtomic;
import mas.behaviours.atomic.generic.SingletonAtomic;
import mas.behaviours.atomic.generic.WhileAtomic;
import mas.tools.Action;
import mas.tools.Condition;
import mas.tools.MyGraph;

public class CollectorBehaviour extends GraphAgentBehaviour{
	/* Pour une raison étrange le signal de sortie d'un FSM behaviour n'est pas le signal retourné ....
	 * Il s'agit peut être du numero d'état final ?
	 * 
	 * TODO : Quand ExploDest = 1, il faut ce déplacer jusqu'au trésors, le ramasser et aller jusqu'au pilot
	 */
	private int signal;
	
	public CollectorBehaviour(abstractAgent agent){
		/* MoveTreasor semble mal fonctionner quand il y a 2 agents */
		super(agent);
		
		Condition c1 = a -> {return a.getmyGraph().getSiloPosition() == null;};
		Condition c2 = a -> {return a.getmyGraph().getBestTreasurePath() == null;};
		Condition c3 = a -> {return !(a.getPath().isEmpty());};
		Condition c4 = a -> {return a.getPath().size() != 1;};
		
		Action pathSilo = a -> { MyGraph g = a.getmyGraph();
								a.setPath(g.getShortestPath(g.getSiloPosition()));
								a.setSwitchPath(false);};
								
		Action pick = a -> {a.pickTreasure();};
		Action pathTreasor = a -> {a.setPath(a.getmyGraph().getBestTreasurePath());
									a.setSwitchPath(false);};
		Action put = a -> {a.emptyMyBackPack("Agent5");};
		Action none = a -> {};
		
		

		registerFirstState(new WhileAtomic(a, c2, new ExploratorStep(a)), "SearchTreasor");	
		registerState(new WhileAtomic(a, c3, new MoveAndCommunicateStep(a, new DirectionManager(a, pathTreasor))),"MoveTreasor");
		registerState(new WhileAtomic(a, c1, new ExploratorStep(a)), "FindSilo");
		registerState(new WhileAtomic(a, c4, new MoveAndCommunicateStep(a, new DirectionManager(a, pathSilo))), "MoveSilo");
		registerState(new SingletonAtomic(a, pick), "Pick");
		registerState(new SingletonAtomic(a, put), "Put");
		
		registerDefaultTransition("SearchTreasor", "MoveTreasor");
		registerDefaultTransition("MoveTreasor", "Pick");
		registerDefaultTransition("Pick", "FindSilo");
		registerDefaultTransition("FindSilo", "MoveSilo");
		//registerDefaultTransition("FindSilo", "PathSilo");
		//registerDefaultTransition("PathSilo", "MoveSilo");
		registerDefaultTransition("MoveSilo", "Put");
		registerDefaultTransition("Put", "SearchTreasor");

	}
	
}