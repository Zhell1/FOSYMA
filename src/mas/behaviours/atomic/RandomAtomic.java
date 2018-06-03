package mas.behaviours.atomic;

import java.util.List;
import java.util.Random;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

import env.Attribute;
import env.Couple;
import mas.abstractAgent;
import mas.agents.GraphAgent;
import mas.tools.MyGraph;

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
		if (this.cpt == 0) {
			/* first */
			this.agent.print("COU         COU                       LXOXO");
			MyGraph g = this.agent.getmyGraph();
			for ( Edge e : g.getGraphStream().getEdgeSet()) {
				Node n1 = e.getNode0();
				Node n2 = e.getNode1();
				if (n1.getId() == this.agent.getCurrentPosition() && n2.getId() == this.agent.lastmove) {
					Object o = (e.getAttribute("weight"));
					
					if (o != null) {
						int w = (int)(o);
						w = w + 10;
						this.agent.print("==================================");
						this.agent.print("New value of weigth :" + w);
						this.agent.print("==================================");
						Edge ne = g.getGraphStream().getEdge(e.getId());
						ne.setAttribute("weight", w);
					}
					
				}
			}
			
		}
		if (this.cpt >= this.timeOut) {
			this.signal = 1;
			Random r2 = new Random();
			int t2 = r2.nextInt(5);
			this.block(t2 * 1000);
			this.cpt = 0;
			this.agent.setSwitchPath(true);
			return;
		}
		
		List<Couple<String,List<Attribute>>> lobs2= this.agent.observe();//myPosition
		Random r = new Random();
		String nextMove = lobs2.get(r.nextInt(lobs2.size()-1)+1).getLeft(); //-1)+1: don't stay at the same place
		this.agent.print("DEBLOCAGE RANDOM");
		this.agent.move(nextMove);
		this.cpt += 1;
		this.signal = 0;
		}

}
