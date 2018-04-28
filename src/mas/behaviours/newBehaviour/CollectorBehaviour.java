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
		super(agent);
		
		Condition c1 = a -> {return a.getmyGraph().getSiloPosition() == null;};
		Condition c2 = a -> {return a.getmyGraph().getBestTreasurePath() == null;};
		Condition c3 = a -> {return !(a.getPath().isEmpty());};
		Condition c4 = a -> {return a.getPath().size() != 1;};
		
		Action pathSilo = a -> { MyGraph g = a.getmyGraph();
								a.setPath(g.getShortestPath(g.getSiloPosition()));
		};
		Action pick = a -> {a.pickTreasure();};
		Action pathTreasor = a -> {a.setPath(a.getmyGraph().getBestTreasurePath());};
		Action put = a -> {a.emptyMyBackPack("SiloAgent");};
		
		

		registerFirstState(new WhileAtomic(a, c2, new ExploratorStep(a)), "SearchTreasor");	
		registerState(new SingletonAtomic(a, pathTreasor), "PathTresor");
		registerState(new WhileAtomic(a, c3, new MoveAndCommunicateStep(a)),"MoveTreasor");
		registerState(new WhileAtomic(a, c1, new ExploratorStep(a)), "FindSilo");
		registerState(new SingletonAtomic(a, pathSilo), "PathSilo");
		registerState(new WhileAtomic(a, c4, new MoveAndCommunicateStep(a)), "MoveSilo");
		registerState(new SingletonAtomic(a, pick), "Pick");
		registerState(new SingletonAtomic(a, put), "Put");
		
		registerDefaultTransition("SearchTreasor", "PathTresor");
		registerDefaultTransition("PathTresor", "MoveTreasor");
		registerDefaultTransition("MoveTreasor", "Pick");
		registerDefaultTransition("Pick", "FindSilo");
		registerDefaultTransition("FindSilo", "PathSilo");
		registerDefaultTransition("PathSilo", "MoveSilo");
		registerDefaultTransition("MoveSilo", "Put");
		registerDefaultTransition("Put", "SearchTreasor");

		
		
//		registerFirstState(new ColectorDestinationAtomic(a), "CollectorDest");
//		registerState(new ExploreDestinationAtomic(a), "ExploDest");
//		registerState(new ExploreDestinationAtomic(a), "ExploDest2");
//		registerState(new MoveAndCommunicateBehaviour(a), "MoveCom");
//		registerState(new MoveAndCommunicateBehaviour(a), "MoveToTreasure");
//		registerState(new MoveAndCollectAtomic(a), "DestTreasure");
//		
//		registerState(new DestMoveNextTo(a, g.getSiloPosition()), "MoveNextTo");
//		
//		registerState(new EndAtomic(a, this, 1), "Last1");
//		registerState(new SingletonAtomic(a, put), "Put");
//		
//		registerTransition("CollectorDest", "ExploDest", -1);
//		registerTransition("CollectorDest", "DestTreasure", 1);
//		registerTransition("CollectorDest", "MoveCom", 2);
//		
//		registerTransition("ExploDest", "MoveCom", 1);
//		registerTransition("ExploDest", "CollectorDest", -1);
//		
//		// registerTransition("MoveCom", "CollectorDest", 1);
//		registerTransition("MoveCom", "CollectorDest", 0);
//		
//		registerTransition("DestTreasure", "MoveToTreasure", -1);
//		registerTransition("DestTreasure", "FoundSilo", 1);
//		
//		registerTransition("FoundSilo", "ExploDest2", -1);
//		registerTransition("FoundSilo", "MoveNextTo", 1);
//		
//		registerTransition("MoveNextTo", "Put", 1);
//		
//		registerTransition("ExploDest2", "MoveCom", 1);
//		registerTransition("ExploDest2", "FoundSilo", -1);
//
//		
//		
//		registerDefaultTransition("MoveToTreasure","DestTreasure");
		

	}
	
}