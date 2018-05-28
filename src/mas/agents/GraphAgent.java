package mas.agents;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import env.Attribute;
import env.Couple;
import env.EntityType;
import env.Environment;
import mas.abstractAgent;
import mas.behaviours.FirstFSMBehaviour;
import mas.tools.DFManager;
import mas.tools.Messages;
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
	String position;
	String nextMove;
	ArrayList<String> path;
	boolean succesLastMove;
	boolean switchPath;

	public Messages mailbox;
	private long timeOut;
	private HashMap<String, Object> lastMsg;
	private HashMap<String, Object> lastMap;
	
	
	public HashMap<String, Object> getMsg(){
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		long currentTime = timestamp.getTime();
		long time;
		HashMap<String, Object> msg;
		Object t;
		do { 
			msg = this.mailbox.get2();
			if (msg == null) {
				return null;
			}
			time = (long) msg.get("timeStamp");
			if (currentTime - time < this.timeOut) {
				// print("difference temps " + (currentTime - time));
				t = msg.get("type");
				if (t.equals("String")){
					this.lastMsg = msg;
				}
				if (t.equals("HashMap")) {
					this.lastMap = msg;
				}
				return msg;
			}
			else {
				print("Message non pris car TIMEOUT");
			}
			
			
		}while(true);

	}
	
	public void look() {
		this.print((this.observe().get(0)).toString());
		
	}
	
	public boolean pickTreasure() {
		this.print("pick treasure !");
		look();
		this.print("capacity before pick :" + this.getBackPackFreeSpace());
		int q = ((abstractAgent)this).pick();
		this.print("capacity after pick :" + this.getBackPackFreeSpace());
		Node n = this.graph.getNode(this.getPosition());
		List<Couple<String, List<Attribute>>> L = observe();
		Couple<String, List<Attribute>> cpl = L.get(0);
		List<Attribute> Latt = cpl.getRight();
		for (Attribute a : Latt){
			n.setAttribute(a.getName(), a.getValue());
		}
		
		if (q == 0) {
			return false;
		}
		return true;
	}
	
	public boolean isFull() {
		return (this.getBackPackFreeSpace() == 0);
	}
	
	public HashMap<String, Object> consultLastMsg(){
		return this.lastMsg;
	}
	
	public HashMap<String, Object> consultLastMap(){
		return this.lastMap;
	}
	
	public void setLastMsg(HashMap<String, Object> msg) {
		this.lastMsg = msg;
	}
	
	public Integer getStep(){
		return this.step;
	}
	
	public boolean getSwitchPath() {
		return this.switchPath;
	}
	
	public void setSwitchPath(boolean b) {
		this.switchPath = b;
	}
	
	public String getNextPath() {
		this.print("My treasure type :" + this.getMyTreasureType());

		//le path est inverser donc il faut prendre le dernier element
		if (this.path.isEmpty()) {
			return null;
		}
		int index = this.path.size() - 1;
		String move = this.path.get(index);
		this.path.remove(index);
		this.nextMove = move;
		return move;
	}
	
	public void putMovePath(String m) {
		this.path.add(m);
	}
	
	public ArrayList<String> getPath(){
		return this.path;
	}
	
	public void resetPath() {
		this.path.clear();
	}
	
	public void setPath(ArrayList<String> n) {
		this.path = n;
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
		this.succesLastMove = ((mas.abstractAgent)this).moveTo(move);
		return this.succesLastMove;
	}
	
	public boolean move(String move) {
		this.step++;
		this.succesLastMove = ((mas.abstractAgent)this).moveTo(move);
		this.myGraph.add();
		//wait time
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.succesLastMove;
	}
	

	
	public boolean getSuccesLastMove(){
		return this.succesLastMove;
	}
	
	public String getPosition(){
		return ((mas.abstractAgent)this).getCurrentPosition();
	}
	
	public MyGraph getmyGraph(){
		return this.myGraph;
	}
	
	public List<Node> getMyTreasureList(){
		return this.myGraph.getMyTreasuresList();
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
		//setupgraph();
		this.graph = new SingleGraph("graphAgent");
		initMyGraph();
		this.step = 0;
		this.stepMap = new HashMap<String, Integer>();
		this.path = new ArrayList<String>();
		this.mailbox = new Messages(this);
		this.lastMsg = null;
		this.switchPath = true;
		this.timeOut = 1000 * 5;
		
		System.out.println("the agent "+this.getLocalName()+ " is started");

	}
	
	public void print(String m){
		System.out.println(this.getLocalName()+" : "+m);
	}
	/*
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
	*/
	
	/**
	 * This method is automatically called after doDelete()
	 */
	protected void takeDown(){

	}
}


