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

public class Explorateur2 extends GraphAgentBehaviour{

	private int signal;
	
	
	public Explorateur2(abstractAgent agent){
		super(agent);
		
		registerFirstState(new ExploratorBehaviour(a), "Explo1");
		
		registerDefaultTransition("Explo1", "Explo1");
		
	}
}