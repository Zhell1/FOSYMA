package mas.behaviours;

import jade.core.behaviours.FSMBehaviour;
import mas.abstractAgent;
import mas.agents.GraphAgent;
import mas.behaviours.atomic.ExploreDestinationAtomic;
import mas.behaviours.atomic.ListenAtomic;
import mas.behaviours.atomic.MoveAtomic;
import mas.behaviours.atomic.SendAtomic;
import mas.behaviours.atomic.VoidAtomic;
import mas.tools.MyGraph;

public class ExploratorBehavior extends GraphAgentBehavior{
	private int signal;
	
	public ExploratorBehavior(abstractAgent agent){
		super(agent);
		//Pour laisser le temps à l'environement de ce charger
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		registerFirstState(new ExploreDestinationAtomic(a), "Dest");
		registerState(new MoveAtomic(a), "Move");
		registerState(new SendAtomic(a, "ping"), "Send");
		registerState(new ListenAtomic(a), "Listen");
		registerState(new VoidAtomic(a), "Void");
	
		registerTransition("Dest", "Move", 1);
		registerTransition("Dest", "Void", 2);
		registerTransition("Move", "Send", 1);
		//interbloquage
		registerTransition("Move", "Move", 2);
		registerTransition("Send", "Listen", 1);
		registerTransition("Listen", "Dest", 1);
		registerDefaultTransition("Void", "Void");
	}
}
