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
	
	public MoveAndCommunicateBehaviour(abstractAgent agent){
		super(agent);
		
		/*
		 * TODO Quand on re-rentre dans le behaviour, on retourne dans l'état final Success, il faut reinitaliser
		 * les états
		 */
				
		registerFirstState(new MoveAtomic(a), "Move");
		registerState(new SendAtomic(a, "ping"), "Send");
		registerState(new ListenAtomic(a), "Listen");
		registerState(new RandomAtomic(a, 10), "Random");
		registerState(new TraiteMsgAtomic(a), "TraiteMsg");
		registerState(new ShareMapBehaviour(a), "ShareMap");
		registerState(new UpdateMapAtomic(a), "UpdateMap");
		
		registerLastState(new EndAtomic(a, this, 1), "Fin1");
		
			
		registerTransition("Move", "Fin1", -1);
		registerTransition("Move", "Send", 1);
		registerTransition("Move", "Random", 2);
		
		registerTransition("Send", "Listen", 1);
		registerTransition("Listen", "TraiteMsg", 1);
		
		registerTransition("TraiteMsg","Fin1", -1);
		registerTransition("TraiteMsg","ShareMap", 1);
		
//		registerTransition("A1Share", "UpdateMap", 1);
//		registerTransition("A1Share", "Dest", -1);
		
		registerTransition("ShareMap", "UpdateMap", -2);
		registerTransition("ShareMap", "UpdateMap", 1);
		registerTransition("ShareMap", "Fin1", -1);
		
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