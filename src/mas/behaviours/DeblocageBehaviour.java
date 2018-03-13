package mas.behaviours;

import java.util.List;
import java.util.Random;

import org.graphstream.graph.Node;

import env.Attribute;
import env.Couple;
import mas.agents.FirstAgent;
import mas.tools.MyGraph;
import jade.core.behaviours.SimpleBehaviour;

public class DeblocageBehaviour extends SimpleBehaviour{

	private MyGraph graph;
	private int signal;
	private int timeOut;
	private int cpt;

	public DeblocageBehaviour(final mas.abstractAgent myagent){
		super(myagent);
		this.graph = ((FirstAgent)myagent).getmyGraph();
		this.signal = 0;
		this.timeOut = 1000;
		this.cpt = 0;
	}
	
	@Override
	public void action() {
		// TODO Auto-generated method stub
		if (this.cpt > this.timeOut){
			System.out.println("RANDOM TIMEOUT");
			this.signal = 1;
		}
		else {
			String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
			this.graph.add();
			List<Couple<String,List<Attribute>>> lobs2=((mas.abstractAgent)this.myAgent).observe();//myPosition
			Random r = new Random();
			String nextMove = lobs2.get(r.nextInt(lobs2.size())).getLeft();
			System.out.println("Random move to :" + nextMove);
			((mas.abstractAgent)this.myAgent).moveTo(nextMove);
			this.cpt++;
		}
	}
	
	public int onEnd(){
		return this.signal;
	}
	
	public void reset(){
		this.signal = 0;
		this.cpt = 0;
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return true;
	}

}
