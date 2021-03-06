package mas.behaviours.newBehaviour;

import java.util.ArrayList;
import java.util.List;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

import env.Attribute;
import env.Couple;
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
		super(agent);
		
		Condition exploFinie = a-> {	
										//si plus aucun trésor on considère l'explo comme fini
										if(a.getmyGraph().anyTreasureLeft()==false) return true;
										boolean bordureempty = (a.getmyGraph().getBordure().size() == 0);
										boolean ismytreasureleft = (a.getmyGraph().getBestTreasurePath() != null);
										//si bordure finie et plus de trésor à ramasser de mon type
										boolean explofinie = bordureempty && !ismytreasureleft;
										a.print("exploFinie? ="+explofinie);
										return explofinie;
									};
									
		Condition anyTreasureLeft = a-> { 	a.print("*** anyTreasureLeft ? = " + a.getmyGraph().anyTreasureLeft());
											//a.getmyGraph().addnbmodifs(1000); //+1000 = on envoi la carte aux autres pour les prévenir
											return a.getmyGraph().anyTreasureLeft();  };
		
		Condition checkRemakePath = a -> { 		a.print("remakePath? ="+a.getremakepath());
												if(a.getremakepath()){
													a.setremakepath(false);
													return true;
												} else return false;
										};
		
		Condition silofound = a -> { a.print("silofound ? = " + (a.getmyGraph().getSiloPosition() != null ));
									 a.print("path to silo found ? = " + (a.getmyGraph().pathtosilofound()));
									 //a.print("\texplored: "+a.getmyGraph().getExplored().size()+"\tbordure: "+a.getmyGraph().getBordure().size());
//									 if(a.getmyGraph().getBordure().size() == 1){
//										 a.print("\tbordure= "+a.getmyGraph().getBordure());
//									 }
									 boolean res = (a.getmyGraph().getSiloPosition() != null) && a.getmyGraph().pathtosilofound(); 
			                         //a.print("--> res = "+res);
									 return res;  };
							
		Condition tresfound = a -> { //a.print("explo finie ? bordure=" + a.getmyGraph().getBordure().size());
									 if(a.getmyGraph().getBordure().size() == 1)
										 a.print("\tbordure = "+a.getmyGraph().getBordure());
		 
									 a.print("tresfound ? = "+ (a.getmyGraph().getBestTreasurePath() != null));
									//a.print("explored : " + a.getmyGraph().getExplored().toString());
							        //a.print("Bordure : " + a.getmyGraph().getBordure().toString());
							        return (a.getmyGraph().getBestTreasurePath() != null);  }; // && !(a.getmyGraph().getBordure().isEmpty());};
							
		Condition pathOver = a -> { a.print("pathOver ? = " + a.getPath().isEmpty());
						        	return (a.getPath().isEmpty());  };
							
//		Condition SiloOver = a -> {   a.print("deplace silo, path size) = " + a.getPath().size());
//						        	return a.getPath().size() >= 0;  };
						        	
		Condition siloOver = a -> { if(a.getPath() != null){
										a.print("siloOver ? = " + a.getPath().isEmpty());
										return (a.getPath().isEmpty());
									} else {
										//path problem: reset
										a.setPath(g.getShortestPath(g.getSiloPosition()));
										a.print("siloOver ? = null -> false");
										a.print("path : "+a.getPath().toString());
										return false;
									}
			/*
									boolean siloover = false;
									String silopos = this.g.getSiloPosition();
									List<Couple<String, List<Attribute>>> observe = this.a.observe();
									for(int i = 0; i < observe.size(); i++){
										if (observe.get(i).getLeft().equals(silopos)) siloover = true;
									}
									a.print("siloOver ? = "+siloover);
									return siloover;
									*/
						        	  };
						        	
		
		Action pathSilo = a -> { //a.print("pathSilo calculating");
								 MyGraph g = a.getmyGraph();
								 //a.print("silo at: "+g.getSiloPosition());
								 ArrayList<String> p = g.getShortestPath(g.getSiloPosition());
								// a.print("PATH SILO (before) : " + p.toString() + "\tsilo at " + g.getSiloPosition());
								 //a.print("p = " + p);
								 if(p != null) {
									 if(p.size() >= 1)
										 p = g.formatsiloPath(p); // retire le dernier noeud du path (car on ne vas pas sur la case du silo)
							      }
								 //a.print("PATH SILO (after) : " + p.toString() + "\tsilo at " + g.getSiloPosition());
								 a.setPath(p);
//								 a.setSwitchPath(false); //todo à supprimer ?
								};
								
		Action pick = a -> {   
								int valrestant = a.getmyGraph().getTreasureValue(a.getPosition(),a.getMyTreasureType());
								//a.print("valrestant before pick = " + valrestant);
								a.pickTreasure();  
								valrestant =  a.getmyGraph().getTreasureValue(a.getPosition(), a.getMyTreasureType());
								a.print("valrestant after pick = " + valrestant);
							};
		
		Action pathTresor = a -> {  //a.print("calculating pathTresor");
									a.setPath(a.getmyGraph().getBestTreasurePath());
									//ArrayList<String> Lpath = a.getmyGraph().getBestTreasurePath();
									//a.print("path set to new treasure at "+ Lpath.get(Lpath.size()-1));
									a.setSwitchPath(false);   };
									
		Action put = a -> {boolean b = a.emptyMyBackPack("Tank");
						   a.print("I EMPTY MY BACKPACK :" + b + " ( I am at " + a.getPosition() + ", silo is at "+a.getmyGraph().getSiloPosition() + " )");};
						   
		Action end = a -> {a.print("END OF THE AGENT");};
		
		Action none = a -> { //a.print("none");
								};
		
		Condition expAll = a -> {return a.getmyGraph().getBordure().isEmpty() && a.getmyGraph().getBestTreasurePath() ==  null;};
		
		
		
		// ---------------------------------- states ----------------------------------
		
		registerFirstState(new IfAtomic(a, tresfound, pathTresor, none), "CheckTres");
		registerState(new ExploratorBehaviour(a), "Explo1");
		
		registerState(new IfAtomic(a, pathOver, none, none), "PathTresOver");
		registerState(new MoveAndCommunicateBehaviour(a), "MC1");
		
		registerState(new IfAtomic(a, silofound, pathSilo, none), "CheckSilo");
		registerState(new ExploratorBehaviour(a), "Explo2");
		
		registerState(new IfAtomic(a, siloOver, none, none), "PathSiloOver");
		registerState(new MoveAndCommunicateBehaviour(a), "MC2");
		
		registerState(new SingletonAtomic(a, pick), "Pick");
		registerState(new SingletonAtomic(a, put), "Put");
		

		registerState(new IfAtomic(a, checkRemakePath, pathTresor, none), "CheckRemakePathTres");
		registerState(new IfAtomic(a, checkRemakePath, pathSilo, none),   "CheckRemakePathSilo");
		

		registerState(new IfAtomic(a, exploFinie, none, none), "CheckExplo1");
		registerState(new IfAtomic(a, exploFinie, none, none), "CheckExplo2");
		

		registerState(new IfAtomic(a, anyTreasureLeft, none, end),   "CheckAnyTreasureLeft"); //to check not our type of treasure
		registerState(new ExploratorBehaviour(a), "Explo3"); //quand il à tout exploré et plus de trésor de notre type
		
		registerLastState(new VoidAtomic(a), "EndSuccess"); // quand plus aucun trésor d'aucun type
		
		//------------------------------- transitions ------------------------------
		
		registerTransition("CheckTres", "Explo1", -1);
		registerTransition("CheckTres", "PathTresOver", 1);
		
		//registerDefaultTransition("Explo1", "CheckTres");
		registerTransition("Explo1", "CheckTres", 1);
		registerTransition("Explo1", "CheckExplo1", -1);
		registerTransition("Explo1", "CheckExplo1", 0); // TODO POURQUOI PARFOIS ON RENVOI 0 ICI ???
		registerTransition("CheckExplo1", "Explo3", 1);
		registerTransition("CheckExplo1", "CheckTres", -1);
		
		registerTransition("PathTresOver", "MC1", -1);
		registerTransition("PathTresOver", "CheckRemakePathTres", 1);
		
		registerTransition("CheckRemakePathTres", "Pick", -1);
		registerTransition("CheckRemakePathTres", "CheckTres", 1);
		
		registerTransition("MC1", "PathTresOver", 1);
		registerTransition("MC1", "PathTresOver", 0);
		
		registerDefaultTransition("Pick", "CheckSilo");
		registerTransition("CheckSilo", "Explo2", -1);
		registerTransition("CheckSilo", "PathSiloOver", 1);
		
		//registerDefaultTransition("Explo2", "CheckSilo");
		registerTransition("Explo2", "CheckSilo", 1);
		registerTransition("Explo2", "CheckExplo2", -1);
		registerTransition("Explo2", "CheckExplo2", 0); // TODO POURQUOI PARFOIS ON RENVOI 0 ICI ???
		registerTransition("CheckExplo2", "Explo3", 1);
		registerTransition("CheckExplo2", "CheckSilo", -1);
		
		registerTransition("PathSiloOver", "MC2", -1);
		registerTransition("PathSiloOver", "CheckRemakePathSilo",  1);

		registerTransition("CheckRemakePathSilo", "Put", -1);
		registerTransition("CheckRemakePathSilo", "CheckSilo", 1);
		
		registerTransition("MC2", "PathSiloOver", 1);
		registerTransition("MC2", "PathSiloOver", 0);
		
		registerDefaultTransition("Put", "CheckTres");
		
		registerDefaultTransition("Explo3", "CheckAnyTreasureLeft");
		registerTransition("CheckAnyTreasureLeft", "Explo3", 1);
		registerTransition("CheckAnyTreasureLeft", "EndSuccess", -1);
		

		registerDefaultTransition("EndSuccess", "EndSuccess");
		

	}
	
}