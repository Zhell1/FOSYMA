package mas.behaviours.atomic;

import mas.abstractAgent;
import mas.behaviours.newBehaviour.GraphAgentBehaviour;

public class EndAtomic extends AtomicBehaviour{

	private int endSignal;
	private GraphAgentBehaviour b;

	public EndAtomic(abstractAgent a, GraphAgentBehaviour b, int endSignal) {
		super(a);
		// TODO Auto-generated constructor stub
		this.b = b;
		this.endSignal = endSignal;
	}
	
	public void action() {
		this.agent.print("Fin : " + this.endSignal);
		b.setSignal(this.endSignal);
		this.signal = 1;
	}
 
}
