package mas.behaviours.atomic;

import mas.abstractAgent;

public class WaitAtomic extends AtomicBehaviour{

	private int time;

	public WaitAtomic(abstractAgent a, int time) {
		super(a);
		this.time = 1000 * time;
		// TODO Auto-generated constructor stub
	}
	
	public void action() {
		try {
			Thread.sleep(this.time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.signal = 1;
	}

}
