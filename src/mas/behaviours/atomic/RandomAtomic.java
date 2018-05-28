package mas.behaviours.atomic;

import java.util.List;
import java.util.Random;

import env.Attribute;
import env.Couple;
import mas.abstractAgent;
import mas.agents.GraphAgent;

public class RandomAtomic extends AtomicBehaviour{
	/* signal 0 = boucle sur lui meme
	 * signal 1 = fin procedure de random
	 */

	private int timeOut;
	private int cpt;

	public RandomAtomic(abstractAgent a, int timeOut) {
		super(a);
		// TODO Auto-generated constructor stub
		this.cpt = 0;
		this.timeOut = timeOut;
	}
	
	public void action() {
		if (this.cpt >= this.timeOut) {
			this.signal = 1;
			this.cpt = 0;
			this.agent.setSwitchPath(true);
			return;
		}
		
		List<Couple<String,List<Attribute>>> lobs2= this.agent.observe();//myPosition
		Random r = new Random();
		String nextMove = lobs2.get(r.nextInt(lobs2.size())).getLeft();
		this.agent.print("DEBLOCAGE RANDOM");
		this.agent.move(nextMove);
		this.cpt += 1;
		this.signal = 0;
		}

}
