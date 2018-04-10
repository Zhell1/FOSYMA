package mas.agents;

import java.util.HashMap;
import java.util.Iterator;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import env.EntityType;
import env.Environment;
import mas.abstractAgent;
import mas.behaviours.FirstFSMBehaviour;
import mas.tools.DFManager;
import mas.tools.MyGraph;


public class GraphAgent extends abstractAgent{

	/**
	 * 
	 */
	
	public void supersetup(){
		super.setup();
	}
	
	private static final long serialVersionUID = -1784844593772918359L;
	
	// graphe partiel connu
	Graph graph;
	MyGraph myGraph;
	Iterator<Node> iter;
	HashMap<String, Integer> stepMap;
	Integer step;
	
	public Integer getStep(){
		return this.step;
	}
	
	public Integer getStepId(String idAgent){
		Integer v = this.stepMap.get(idAgent);
		if (v == null){
			this.stepMap.put(idAgent, 0);
			return 0;
		}
		return v;
	}
	
	public void updateStep(String idAgent){
		this.stepMap.put(idAgent, step);
	}
	
	public boolean moveAgent(String move){
		this.step++;
		return ((mas.abstractAgent)this).moveTo(move);	
	}
	
	public MyGraph getmyGraph(){
		return this.myGraph;
	}
	
	

	public void initMyGraph(){
		this.myGraph = new mas.tools.MyGraph((mas.abstractAgent)this, this.graph);
	}

	/**
	 * This method is automatically called when "agent".start() is executed.
	 * Consider that Agent is launched for the first time. 
	 * 			1) set the agent attributes 
	 *	 		2) add the behaviours
	 *          
	 */

	protected void setup(){

		super.setup();

		final Object[] args = getArguments();
		if(args!=null && args[0]!=null && args[1]!=null){
			deployAgent((Environment) args[0],(EntityType)args[1]);
		}else{
			System.err.println("Malfunction during parameter's loading of agent"+ this.getClass().getName());
                        System.exit(-1);
                }
		//setup graph
		setupgraph();
		//this.graph = new SingleGraph("test");
		initMyGraph();
		this.step = 0;
		this.stepMap = new HashMap<String, Integer>();

		DFManager.register(this, "explorer");
		
		System.out.println("the agent "+this.getLocalName()+ " is started");

	}
	protected void setupgraph() {
		//color of a node according to its type
		String defaultNodeStyle= "node {"+"fill-color: black;"+" size-mode:fit;text-alignment:under; text-size:14;text-color:white;text-background-mode:rounded-box;text-background-color:black;}";
		String nodeStyle_wumpus= "node.wumpus {"+"fill-color: red;"+"}";
		String nodeStyle_agent= "node.agent {"+"fill-color: blue;"+"}";
		String nodeStyle_treasure="node.treasure {"+"fill-color: yellow;"+"}";
		String nodeStyle_EntryExit="node.exit {"+"fill-color: green;"+"}";
		
		String nodeStyle=defaultNodeStyle+nodeStyle_wumpus+nodeStyle_agent+nodeStyle_treasure+nodeStyle_EntryExit;
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

		this.graph = new SingleGraph("Illustrative example");//generateGraph(true, 30);
		
		this.iter=graph.getNodeIterator();
		
		//SingleGraph graph = new SingleGraph("Tutorial 1");
		graph.setAttribute("ui.stylesheet",nodeStyle);
		
		//pas besoin d'affichage
		//Viewer viewer = graph.display();
		//SpriteManager sman = new SpriteManager(graph);
		
	}
	public void print(String m){
		System.out.println(this.getLocalName()+" : "+m);
	}
	/**
	 * This method is automatically called after doDelete()
	 */
	protected void takeDown(){

	}
}


