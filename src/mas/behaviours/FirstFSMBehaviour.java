package mas.behaviours;

import org.graphstream.graph.Graph;

import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import mas.abstractAgent;
import mas.behaviours.FirstAgentExplore;

public class FirstFSMBehaviour {
	FSMBehaviour o;
	public FirstFSMBehaviour(abstractAgent a, Graph g){
		/* Il faut que les comportements implémentent une fonction int onEnd() qui indique le signal de transition */
		this.o = new FSMBehaviour();
		o.registerFirstState(new FirstAgentExplore(a, g), "Explo");
		o.registerState(new ShareMapBehaviour(a), "send");
		o.registerState(new ListenerBehaviour(a), "listener");
		
		
	}
	public void test(){
		
	}
}
