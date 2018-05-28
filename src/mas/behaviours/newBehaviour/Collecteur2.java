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

public class Collecteur2 extends GraphAgentBehaviour{
	/* Pour une raison étrange le signal de sortie d'un FSM behaviour n'est pas le signal retourné ....
	 * Il s'agit peut être du numero d'état final ?
	 * 
	 * TODO : Quand ExploDest = 1, il faut ce déplacer jusqu'au trésors, le ramasser et aller jusqu'au pilot
	 */
	private int signal;
	
	public Collecteur2(abstractAgent agent){
		/* MoveTreasor semble mal fonctionner quand il y a 2 agents */
		super(agent);
		
		Condition silofound = a -> {
							return a.getmyGraph().getSiloPosition() != null;};
							
		Condition tresfound = a -> {a.print("explored : " + a.getmyGraph().getExplored().toString());
							//a.print("Bordure : " + a.getmyGraph().getBordure().toString());
							return a.getmyGraph().getBestTreasurePath() != null ;}; // && !(a.getmyGraph().getBordure().isEmpty());};
		Condition pathOver = a -> {a.print("move tresor ?= "+a.getPath().isEmpty());
							return (a.getPath().isEmpty());};
		Condition nextTo = a -> {a.print("deplace silo (path size)= " + a.getPath().size());
							return a.getPath().size() >= 1 ;};
		
		Action pathSilo = a -> { MyGraph g = a.getmyGraph();
								ArrayList<String> p = g.getShortestPath(g.getSiloPosition());
								a.print(" \n \n PATH SILO");
								a.print(p.toString());
								a.setPath(p);
								a.setSwitchPath(false);};
								
		Action pick = a -> {a.pickTreasure();};
		Action pathTreasor = a -> {
									a.setPath(a.getmyGraph().getBestTreasurePath());
									a.setSwitchPath(false);};
		Action put = a -> {boolean b = a.emptyMyBackPack("Agent5");
						   a.print("I EMPTY MY BACKPACK :" + b + " ( I am at " + a.getPosition() + ", silo is at "+a.getmyGraph().getSiloPosition());};
		Action end = a -> {a.print("END OF THE AGENT");};
		Action none = a -> {};
		Condition expAll = a -> {return a.getmyGraph().getBordure().isEmpty() && a.getmyGraph().getBestTreasurePath() ==  null;};
		
		
		
		
		registerFirstState(new IfAtomic(a, tresfound, none , pathTreasor), "CheckTres");
		registerState(new ExploratorBehaviour(a), "Explo1");
		registerState(new IfAtomic(a, pathOver, none, pathTreasor), "PathTresOver");
		registerState(new MoveAndCommunicateBehaviour(a), "MC1");
		
		registerState(new IfAtomic(a, silofound, none, none), "SiloFound");
		registerState(new ExploratorBehaviour(a), "Explo2");
		
		registerState(new IfAtomic(a, nextTo, none, pathSilo), "nextTo");
		registerState(new MoveAndCommunicateBehaviour(a), "MC2");
		
		registerState(new SingletonAtomic(a, pick), "Pick");
		registerState(new SingletonAtomic(a, put), "Put");
		
		registerTransition("CheckTres", "Explo1", -1);
		registerTransition("CheckTres","PathTresOver", 1);
		
		registerDefaultTransition("Explo1", "CheckTres");
		
		registerTransition("PathTresOver", "MC1", -1);
		registerTransition("PathTresOver", "Pick", 1);
		
		registerDefaultTransition("MC1", "PathTresOver");
		
		registerDefaultTransition("Pick", "SiloFound");
		registerTransition("SiloFound", "Explo2", -1);
		registerTransition("SiloFound", "nextTo", 1);
		
		registerDefaultTransition("Explo2", "SiloFound");
		
		registerTransition("nextTo", "MC2", -1);
		registerDefaultTransition("MC2", "nextTo");
		
		registerTransition("nextTo","Put", 1);
		
		registerDefaultTransition("Put", "CheckTres");
		

	}
	
}