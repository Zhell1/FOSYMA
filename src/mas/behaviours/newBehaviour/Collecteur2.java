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
	 * TODO : Quand ExploDest = 1, il faut se déplacer jusqu'au trésor, le ramasser puis aller jusqu'au silo
	 */
	private int signal;
	
	public Collecteur2(abstractAgent agent){
		/* MoveTreasor semble mal fonctionner quand il y a 2 agents ,  todo à tester*/
		super(agent);
		
		Condition silofound = a -> { a.print("silofound ? = " + (a.getmyGraph().getSiloPosition() != null ));
			                         return a.getmyGraph().getSiloPosition() != null;};
							
		Condition tresfound = a -> {//a.print("explored : " + a.getmyGraph().getExplored().toString());
							        //a.print("Bordure : " + a.getmyGraph().getBordure().toString());
							        return a.getmyGraph().getBestTreasurePath() != null;  }; // && !(a.getmyGraph().getBordure().isEmpty());};
							
		Condition pathOver = a -> { a.print("pathOver ? = " + a.getPath().isEmpty());
						        	return (a.getPath().isEmpty());  };
							
//		Condition SiloOver = a -> {   a.print("deplace silo, path size) = " + a.getPath().size());
//						        	return a.getPath().size() >= 0;  };
						        	
		Condition siloOver = a -> { a.print("siloOver ? = " + a.getPath().isEmpty());
						        	return (a.getPath().isEmpty());  };
						        	
		
		Action pathSilo = a -> { a.print("pathSilo calculating");
								 MyGraph g = a.getmyGraph();
								 a.print("silo at: "+g.getSiloPosition());
								 ArrayList<String> p = g.getShortestPath(g.getSiloPosition());
								// a.print("PATH SILO (before) : " + p.toString() + "\tsilo at " + g.getSiloPosition());
								 //a.print("p = " + p);
								 if(p.size() >= 1)
									 p = g.siloPath(p); // retire le dernier noeud du path (car on ne vas pas sur la case du silo)
								 a.print("PATH SILO (after) : " + p.toString() + "\tsilo at " + g.getSiloPosition());
								 a.setPath(p);
//								 a.setSwitchPath(false); //todo à supprimer ?
								};
								
		Action pick = a -> {   
								int valrestant = a.getmyGraph().getTreasureValue(a.getPosition(),a.getMyTreasureType());
								a.print("valrestant before pick = " + valrestant);
								a.pickTreasure();  
								valrestant =  a.getmyGraph().getTreasureValue(a.getPosition(), a.getMyTreasureType());
								a.print("valrestant after pick = " + valrestant);
								try {
									Thread.sleep(1000); // TODO SUPPRIMER CELA, juste utile pour les tests
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							};
		
		Action pathTresor = a -> {  a.setPath(a.getmyGraph().getBestTreasurePath());
									//ArrayList<String> Lpath = a.getmyGraph().getBestTreasurePath();
									//a.print("path set to new treasure at "+ Lpath.get(Lpath.size()-1));
									a.setSwitchPath(false);   };
									
		Action put = a -> {boolean b = a.emptyMyBackPack("AgentSilo");
						   a.print("I EMPTY MY BACKPACK :" + b + " ( I am at " + a.getPosition() + ", silo is at "+a.getmyGraph().getSiloPosition() + " )");};
						   
		Action end = a -> {a.print("END OF THE AGENT");};
		
		Action none = a -> { a.print("none"); };
		
		Condition expAll = a -> {return a.getmyGraph().getBordure().isEmpty() && a.getmyGraph().getBestTreasurePath() ==  null;};
		
		
		
		
		registerFirstState(new IfAtomic(a, tresfound, pathTresor, none), "CheckTres");
		registerState(new ExploratorBehaviour(a), "Explo1");
		
		registerState(new IfAtomic(a, pathOver, none, pathTresor), "PathTresOver");
		registerState(new MoveAndCommunicateBehaviour(a), "MC1");
		
		registerState(new IfAtomic(a, silofound, pathSilo, none), "CheckSilo");
		registerState(new ExploratorBehaviour(a), "Explo2");
		
		registerState(new IfAtomic(a, siloOver, none, pathSilo), "PathSiloOver");
		registerState(new MoveAndCommunicateBehaviour(a), "MC2");
		
		registerState(new SingletonAtomic(a, pick), "Pick");
		registerState(new SingletonAtomic(a, put), "Put");
		
		registerTransition("CheckTres", "Explo1", -1);
		registerTransition("CheckTres", "PathTresOver", 1);
		
		registerDefaultTransition("Explo1", "CheckTres");
		
		registerTransition("PathTresOver", "MC1", -1);
		registerTransition("PathTresOver", "Pick", 1);
		
		registerDefaultTransition("MC1", "PathTresOver");
		
		registerDefaultTransition("Pick", "CheckSilo");
		registerTransition("CheckSilo", "Explo2", -1);
		registerTransition("CheckSilo", "PathSiloOver", 1);
		
		registerDefaultTransition("Explo2", "CheckSilo");
		
		registerTransition("PathSiloOver", "MC2", -1);
		registerTransition("PathSiloOver", "Put",  1);
		
		registerDefaultTransition("MC2", "PathSiloOver");
		
		registerDefaultTransition("Put", "CheckTres");
		

	}
	
}