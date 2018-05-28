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
import mas.behaviours.atomic.generic.DoWhileAtomic;
import mas.behaviours.atomic.generic.IfAtomic;
import mas.behaviours.atomic.generic.SingletonAtomic;
import mas.behaviours.atomic.generic.WhileAtomic;
import mas.tools.Action;
import mas.tools.Condition;
import mas.tools.MyGraph;

public class CollectorBehaviour_old extends GraphAgentBehaviour{
	/* Pour une raison étrange le signal de sortie d'un FSM behaviour n'est pas le signal retourné ....
	 * Il s'agit peut être du numero d'état final ?
	 * 
	 * TODO : Quand ExploDest = 1, il faut ce déplacer jusqu'au trésors, le ramasser et aller jusqu'au pilot
	 */
	private int signal;
	
	public CollectorBehaviour_old(abstractAgent agent){
		/* MoveTreasor semble mal fonctionner quand il y a 2 agents */
		super(agent);
		
		Condition c1 = a -> {
							return a.getmyGraph().getSiloPosition() == null;};
		Condition c2 = a -> {a.print(a.getmyGraph().getExplored().toString());
							a.print("Bordure : " + a.getmyGraph().getBordure().toString());
								return a.getmyGraph().getBestTreasurePath() == null && !(a.getmyGraph().getBordure().isEmpty());};
		Condition c3 = a -> {a.print("move tresor ?= "+!a.getPath().isEmpty());
							return !(a.getPath().isEmpty());};
		Condition c4 = a -> {a.print("deplace silo (path size)= " + a.getPath().size());
							return a.getPath().size() > 1;};
		
		Action pathSilo = a -> { MyGraph g = a.getmyGraph();
								ArrayList<String> p = g.getShortestPath(g.getSiloPosition());
								a.print(" \n \n PATH SILO");
								a.print(p.toString());
								a.setPath(p);
								a.setSwitchPath(false);};
								
		Action pick = a -> {a.pickTreasure();};
		Action pathTreasor = a -> {a.setPath(a.getmyGraph().getBestTreasurePath());
									a.setSwitchPath(false);};
		Action put = a -> {boolean b = a.emptyMyBackPack("AgentSilo");
						   a.print("I EMPTY MY BACKPACK :" + b + " ( I am at " + a.getPosition() + ", silo is at "+a.getmyGraph().getSiloPosition());};
		Action end = a -> {a.print("END OF THE AGENT");};
		Action none = a -> {};
		Condition expAll = a -> {return a.getmyGraph().getBordure().isEmpty() && a.getmyGraph().getBestTreasurePath() ==  null;};
		
		
		

		registerFirstState(new WhileAtomic(a, c2, new ExploratorStep(a)), "SearchTreasor");
		
		registerState(new DoWhileAtomic(a, c3, new MoveAndCommunicateStep(a, new SingletonAtomic(a, pathTreasor))),"MoveTreasor");
		registerState(new WhileAtomic(a, c1, new ExploratorStep(a)), "FindSilo");
		registerState(new DoWhileAtomic(a, c4, new MoveAndCommunicateStep(a, new SingletonAtomic(a, pathSilo) )), "MoveSilo");
		registerState(new SingletonAtomic(a, pick), "Pick");
		registerState(new SingletonAtomic(a, put), "Put");
		registerState(new IfAtomic(a, expAll, end, none), "CheckEnd");
		registerLastState(new SingletonAtomic(a, none), "END");
		
		registerDefaultTransition("SearchTreasor", "CheckEnd");
		registerTransition("CheckEnd", "END", 1);
		registerTransition("CheckEnd", "MoveTreasor", -1);
		registerDefaultTransition("MoveTreasor", "Pick");
		registerDefaultTransition("Pick", "FindSilo");
		registerDefaultTransition("FindSilo", "MoveSilo");
		registerDefaultTransition("MoveSilo", "Put");
		registerDefaultTransition("Put", "SearchTreasor");

	}
	
}