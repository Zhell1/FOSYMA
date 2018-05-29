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
import mas.behaviours.atomic.WaitAtomic;
import mas.tools.MyGraph;

public class SendListenBehaviour extends GraphAgentBehaviour{
	private int signal;
	
	public SendListenBehaviour(abstractAgent agent, Object msg){
		super(agent);
		//Pour laisser le temps à l'environement de se charger
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
				
		registerFirstState(new WaitAtomic(a, 2), "Wait");
		registerState(new SendAtomic(a, msg), "Send");
		registerState(new ListenAtomic(a), "Listen");
		registerState(new TraiteMsgAtomic(a), "TraiteMsg");
		registerState(new ShareMapBehaviour(a), "ShareMap");
		registerState(new UpdateMapAtomic(a), "UpdateMap");
		registerLastState(new EndAtomic(a, this, -1), "NoMsg");
		registerLastState(new EndAtomic(a, this, -2), "NoMap");
		registerLastState(new EndAtomic(a, this, 1), "HaveMap");
		
		registerDefaultTransition("Wait", "Send");
				
		registerTransition("Send", "Listen", 1);
		registerTransition("Listen", "TraiteMsg", 1);
		
		registerTransition("TraiteMsg","NoMsg", -1);
		registerTransition("TraiteMsg","ShareMap", 1);
		registerTransition("TraiteMsg", "Listen", 0);
		
		
		registerTransition("ShareMap", "UpdateMap", -2);
		registerTransition("ShareMap", "UpdateMap", 1);
		registerTransition("ShareMap", "NoMap", -1);
		registerTransition("ShareMap", "NoMap", 2);
		
		registerDefaultTransition("UpdateMap","HaveMap");
		registerDefaultTransition("NoMsg", "NoMsg");
		registerDefaultTransition("NoMap", "NoMap");
		registerDefaultTransition("HaveMap", "HaveMap");
		
	}
	
	public void reset() {
		this.forceTransitionTo("Wait");
	}
	
	public int onEnd() {
		this.reset();
		this.a.print("reset ");
		return this.signal;
		
	}
	
}