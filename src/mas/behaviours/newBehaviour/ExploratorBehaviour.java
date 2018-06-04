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

public class ExploratorBehaviour extends GraphAgentBehaviour{
	private int signal;
	
	public ExploratorBehaviour(abstractAgent agent){
		super(agent);
				
//		registerFirstState(new ExploreDestinationAtomic(a), "Dest");
//		registerState(new MoveAndCommunicateBehaviour(a), "MoveCom");
//		registerLastState(new VoidAtomic(a), "End1");
//		
//		registerTransition("Dest", "MoveCom", 1);
//		registerTransition("Dest", "End1", -1);
//		
//		registerDefaultTransition("MoveCom", "Dest");	
//		registerDefaultTransition("End1", "End1");
		
		
		
		//thomas -> avant on bouclait à l'infini sur movecom & dest
		//          maintenant on fait 1 seul step (pour tester si il y a un tresor dessus avant de rééxplorer)
		//this.a.print("ExploratorBehaviour");
		
		registerFirstState(new ExploreDestinationAtomic(a), "Dest");
		//registerLastState(new MoveAndCommunicateBehaviour(a), "MoveCom");
		//registerLastState(new VoidAtomic(a), "End1");
		registerState(new MoveAndCommunicateBehaviour(a), "MoveCom");
		registerLastState(new EndAtomic(a, this, 1), "End1");
		registerLastState(new EndAtomic(a, this, -1), "End2");
		
		registerTransition("Dest", "MoveCom", 1);
		registerTransition("Dest", "End2", -1);
		
		//registerDefaultTransition("MoveCom", "Dest");
		registerDefaultTransition("MoveCom", "End1");
		
		registerDefaultTransition("End1", "End1");
		registerDefaultTransition("End2", "End2");
		
		
	}
	public int onEnd() {
		this.reset();
		return this.signal;
	}
	
	public void reset() {
		//this.a.print("reset");
		this.forceTransitionTo("Dest");
		//this.a.print("état courant : " + this.currentName);
	}

	
}
