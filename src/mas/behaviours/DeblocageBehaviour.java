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
		this.timeOut = 5;
		this.cpt = 0;
	}
	
	@Override
	public void action() {
		// TODO Auto-generated method stub
		if (this.cpt > this.timeOut){
			print("RANDOM TIMEOUT");
			this.signal = 1;
		}
		else {
			String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
			this.graph.add();
			List<Couple<String,List<Attribute>>> lobs2=((mas.abstractAgent)this.myAgent).observe();//myPosition
			Random r = new Random();
			String nextMove = lobs2.get(r.nextInt(lobs2.size())).getLeft();
			print("Random move to :" + nextMove);
			boolean successMove =((FirstAgent)this.myAgent).moveTo(nextMove);
			/*
			if (!successMove){
				print("blocage");
				this.signal = 0;
			}
			else {
				this.signal = 1;
			}
			*/
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
	private void print(String m){
		System.out.println(this.myAgent.getLocalName()+" : "+m);
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return true;
	}

}
