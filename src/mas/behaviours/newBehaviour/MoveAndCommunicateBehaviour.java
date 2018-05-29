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

public class MoveAndCommunicateBehaviour extends GraphAgentBehaviour{
	private int signal;
	/*
	 * this behaviour makes a move, sends a ping, treats received messages
	 * and share the map if needed
	 * 
	 * */
	public MoveAndCommunicateBehaviour(abstractAgent agent){
		super(agent);
				
		registerFirstState(new MoveAtomic(a), "Move");
		registerState(new SendAtomic(a, "ping"), "Send");
		registerState(new ListenAtomic(a), "Listen");
		registerState(new RandomAtomic(a, 3), "Random");
		registerState(new TraiteMsgAtomic(a), "TraiteMsg");
		registerState(new ShareMapBehaviour(a), "ShareMap");
		registerState(new UpdateMapAtomic(a), "UpdateMap");
		
		registerState(new SendAtomic(a, "ping"), "Send2");
		registerState(new ListenAtomic(a), "Listen2");
		
		registerState(new ShareMapBehaviour(a), "ShareMap2");
		registerState(new UpdateMapAtomic(a), "UpdateMap2");
		registerState(new TraiteMsgAtomic(a), "TraiteMsg2");
		registerLastState(new EndAtomic(a, this, 1), "Fin1");
		
			
		registerTransition("Move", "Fin1", -1);
		registerTransition("Move", "Send", 1); //normal ping
		registerTransition("Move", "Send2", 2); //ping because we are blocked and there is an agent on next case
		
		registerDefaultTransition("Send2", "Listen2");
		registerDefaultTransition("Listen2", "TraiteMsg2");
		
		registerTransition("TraiteMsg2","Fin1", -1);
		registerTransition("TraiteMsg2","ShareMap2", 1);
		registerTransition("TraiteMsg2", "Listen2", 0);
		
		
		registerTransition("ShareMap2", "UpdateMap2", -2);
		registerTransition("ShareMap2", "UpdateMap2", 1);
		registerTransition("ShareMap2", "Random", -1); // we are blocked and sharemap failed => go random
		registerTransition("ShareMap2", "Random", 2); // TODO A VOIR (lautre à recu ma carte mais ne m'a rien envoyé)
		//en cas de bloquage il faudrait forcer l'envoi des maps pour recalculer un chemin
		
		registerDefaultTransition("UpdateMap2", "Fin1");
		
		
		registerTransition("Send", "Listen", 1);
		registerTransition("Listen", "TraiteMsg", 1);
		
		registerTransition("TraiteMsg","Fin1", -1);
		registerTransition("TraiteMsg","ShareMap", 1);
		registerTransition("TraiteMsg", "Listen", 0);
		
		
		registerTransition("ShareMap", "UpdateMap", -2);
		registerTransition("ShareMap", "UpdateMap", 1);
		registerTransition("ShareMap", "Fin1", -1);
		registerTransition("ShareMap", "Fin1", 2);
		
		registerDefaultTransition("UpdateMap","Fin1");
		
		registerTransition("Random", "Random", 0);
		registerTransition("Random", "Fin1", 1);
		
		registerTransition("Fin1", "Fin1", 1);
	}
	
	public void reset() {
		this.forceTransitionTo("Move");
	}
	
	public int onEnd() {
		this.reset();
		return this.signal;
	}
	
}