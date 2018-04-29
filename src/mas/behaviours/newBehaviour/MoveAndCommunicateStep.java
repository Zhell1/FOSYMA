package mas.behaviours.newBehaviour;

import java.util.ArrayList;

import jade.core.behaviours.Behaviour;
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

public class MoveAndCommunicateStep extends GraphAgentBehaviour{
	private int signal;
	
	public MoveAndCommunicateStep(abstractAgent agent){
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
			
		registerTransition("Move", "Move", -1);
		registerTransition("Move", "Send", 1);
		registerTransition("Move", "Random", 2);
		
		registerTransition("Send", "Listen", 1);
		registerTransition("Listen", "TraiteMsg", 1);
		
		registerTransition("TraiteMsg","Move", -1);
		registerTransition("TraiteMsg","ShareMap", 1);
		
//		registerTransition("A1Share", "UpdateMap", 1);
//		registerTransition("A1Share", "Dest", -1);
		
		registerTransition("ShareMap", "UpdateMap", -2);
		registerTransition("ShareMap", "UpdateMap", 1);
		registerTransition("ShareMap", "Move", -1);
		
		registerDefaultTransition("UpdateMap","Move");
		
		registerTransition("Random", "Random", 0);
		registerTransition("Random", "Move", 1);
		
		// ?? registerTransition("Move", "Move", 1);
	}
	
	public MoveAndCommunicateStep(abstractAgent agent, Behaviour dest) {
		super(agent);

		registerFirstState(dest , "Dest");
		registerState(new MoveAtomic(a), "Move");
		registerState(new SendAtomic(a, "ping"), "Send");
		registerState(new ListenAtomic(a), "Listen");
		registerState(new RandomAtomic(a, 10), "Random");
		registerState(new TraiteMsgAtomic(a), "TraiteMsg");
		registerState(new ShareMapBehaviour(a), "ShareMap");
		registerState(new UpdateMapAtomic(a), "UpdateMap");
		
		registerDefaultTransition("Dest", "Move");
			
		registerTransition("Move", "Dest", -1);
		registerTransition("Move", "Send", 1);
		registerTransition("Move", "Random", 2);
		
		registerTransition("Send", "Listen", 1);
		registerTransition("Listen", "TraiteMsg", 1);
		
		registerTransition("TraiteMsg","Move", -1);
		registerTransition("TraiteMsg","ShareMap", 1);
		
//		registerTransition("A1Share", "UpdateMap", 1);
//		registerTransition("A1Share", "Dest", -1);
		
		registerTransition("ShareMap", "UpdateMap", -2);
		registerTransition("ShareMap", "UpdateMap", 1);
		registerTransition("ShareMap", "Move", -1);
		
		registerDefaultTransition("UpdateMap","Dest");
		
		registerTransition("Random", "Random", 0);
		registerTransition("Random", "Dest", 1);
	}
	
	
}