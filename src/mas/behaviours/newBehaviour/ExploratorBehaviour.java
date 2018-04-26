package mas.behaviours.newBehaviour;

import java.util.ArrayList;

import jade.core.behaviours.FSMBehaviour;
import mas.abstractAgent;
import mas.agents.GraphAgent;
import mas.behaviours.atomic.ExploreDestinationAtomic;
import mas.behaviours.atomic.ListenAtomic;
import mas.behaviours.atomic.MoveAtomic;
import mas.behaviours.atomic.RandomAtomic;
import mas.behaviours.atomic.SendAtomic;
import mas.behaviours.atomic.TraiteMsgAtomic;
import mas.behaviours.atomic.UpdateMapAtomic;
import mas.behaviours.atomic.VoidAtomic;
import mas.tools.MyGraph;

public class ExploratorBehaviour extends GraphAgentBehaviour{
	private int signal;
	
	public ExploratorBehaviour(abstractAgent agent){
		super(agent);
		//Pour laisser le temps à l'environement de ce charger
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		 * TODO Quand on re-rentre dans le behaviour, on retourne dans l'état final Success, il faut reinitaliser
		 * les états
		 */
				
		registerFirstState(new ExploreDestinationAtomic(a), "Dest");
		registerState(new MoveAtomic(a), "Move");
		registerState(new SendAtomic(a, "ping"), "Send");
		registerState(new ListenAtomic(a), "Listen");
		registerState(new VoidAtomic(a), "Void");
		registerState(new RandomAtomic(a, 10), "Random");
		registerState(new TraiteMsgAtomic(a), "TraiteMsg");
		registerState(new ShareMapBehaviour(a), "ShareMap");
		
//		registerState(new A1ShareMapBehaviour(a), "A1Share");
//		registerState(new A2ShareMapBehaviour(a), "A2Share");
		
		registerState(new UpdateMapAtomic(a), "UpdateMap");
	
		registerTransition("Dest", "Move", 1);
		registerTransition("Dest", "Void", 2);
		
		registerTransition("Move", "Dest", -1);
		registerTransition("Move", "Send", 1);
		registerTransition("Move", "Random", 2);
		
		registerTransition("Send", "Listen", 1);
		registerTransition("Listen", "TraiteMsg", 1);
		
		registerTransition("TraiteMsg","Dest", -1);
		registerTransition("TraiteMsg","ShareMap", 1);
		
//		registerTransition("A1Share", "UpdateMap", 1);
//		registerTransition("A1Share", "Dest", -1);
		
		registerTransition("ShareMap", "UpdateMap", -2);
		registerTransition("ShareMap", "UpdateMap", 1);
		registerTransition("ShareMap", "Dest", -1);
		
		registerDefaultTransition("UpdateMap","Dest");
		
		registerDefaultTransition("Void", "Void");
		
		registerTransition("Random", "Random", 0);
		registerTransition("Random", "Dest", 1);
	}
	
}
