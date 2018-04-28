package mas.behaviours.atomic;

import jade.core.behaviours.SimpleBehaviour;
import mas.abstractAgent;
import mas.agents.GraphAgent;

public class AtomicBehaviour extends SimpleBehaviour {

	protected GraphAgent agent;
	protected int signal;

	public AtomicBehaviour(abstractAgent a) {
		super(a);
		this.agent = (GraphAgent)a;
		this.signal = 1;
	}
	
	@Override
	public void action() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public int onEnd() {
		return this.signal;
	}

}
