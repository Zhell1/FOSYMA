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
import mas.tools.DFManager;
import mas.tools.Messages;
import mas.tools.MyGraph;


public class GraphAgent extends abstractAgent{	
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
	private HashMap<String, Integer> lastSentMap;
	String lastsender;
	int nbmodifsmin;
	
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
		this.timeOut = 1000 * 5;	//5 secondes pour timeout
		this.lastsender = null;
		this.lastSentMap = new HashMap<String, Integer>();
		this.nbmodifsmin = 10;		//5 modifs pour renvoyer la carte
		
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
	
	//////////////////////////////////////////////////////////////////////////
	
	
	public HashMap<String, Object> getMsg(){
		return getMsg("broadcast");
	}
	public HashMap<String, Object> getMsg(String idconv){
		if(idconv == null) 
			return null; 
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		long currentTime = timestamp.getTime();
		long time;
		HashMap<String, Object> msg;
		Object t;
		do { 
			msg = this.mailbox.get2(idconv); //private version
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
		// display current case we are on
		this.print((this.observe().get(0)).toString());
		
	}
	
	public boolean pickTreasure() {
		this.print("pick treasure !");
		look(); // display current case we are on
		this.print("capacity before pick :" + this.getBackPackFreeSpace());
		int q = ((abstractAgent)this).pick();
		this.print("capacity after pick :" + this.getBackPackFreeSpace());
		
		if (q == 0) { // pick failed
			print("pick failed :(");
			return false;
		}
		
		Node n = this.graph.getNode(this.getPosition()); //noeud courant
		List<Couple<String, List<Attribute>>> L = observe(); //observe the world around us
		//print(L.toString());
		//find the same case we are on
		for(Couple<String, List<Attribute>> c : L) {
			//print("test case "+c.getLeft() + " =?= "+n.getId());
			if(c.getLeft().equals(n.getId())) { //test same node
				List<Attribute> Latt = c.getRight();
				boolean isTreasure = false;
				boolean isDiamonds = false;
				//print("attributes of this case: "+Latt.toString());
				for (Attribute a : Latt){ //copy all attributes into graph
					n.setAttribute(a.getName(), a.getValue());
					//print("attribut: " + a.getName() + " = " + a.getValue());
					if(a.getName().equals("Diamonds")) {
						isDiamonds = true;
					}else if(a.getName().equals("Treasure")){
						isTreasure = true;
					}
				}
				//on doit aussi gérer les cas où on ne récupère pas d'attributs = case vide
				if(isTreasure == false)
					n.setAttribute("Treasure", 0);
				if(isDiamonds == false)
					n.setAttribute("Diamonds", 0);
				
				break; //sort de la boucle car on ne s'intéressait qu'au noeud courant
			}
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
		this.print("My treasure type: " + this.getMyTreasureType()+ "\t\t | Silo at "+this.getmyGraph().getSiloPosition());
		if(this.path==null) return null;
		
		if (this.path.isEmpty()) {
			return null;
		}
		String move = this.path.get(0);
		print("getNextPath(): moving to "+this.path.get(0).toString());
		this.path.remove(0);
		this.nextMove = move;
		return move;
	}
	
	public void putMovePath(String m) {
		//TODO VERIFIER QU'ON AJOUTE DU BON COTÉ (À GAUCHE LES PREMIERS À PRENDRE)
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
		this.myGraph.add(); //add or update
		
		//wait time
		try {
			Thread.sleep(1000); //TODO A SUPPRIMER (JUSTE POUR DEBUG)
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
	
	public void setlastPing(String sender) {
		this.lastsender = sender;
	}
	public String getlastPing() {
		return this.lastsender;
	}
	
	public void updateLastSentMap(String sender){
		//si il existe pas déjà on commence par le créer
		if (! this.lastSentMap.containsKey(sender)){
			this.lastSentMap.put(sender, 0);
		}
		//on met à jour la valeur
		this.lastSentMap.replace(sender, this.getmyGraph().getnbmodifs());
	}
	//retourne le nombre de modifs effectués pour un agent depuis qu'on lui à envoyé
	public int getDifferenceLastSent(String sender){
		//si il existe pas déjà on commence par le créer
		if (! this.lastSentMap.containsKey(sender)){
			this.lastSentMap.put(sender, 0);
			return this.getmyGraph().getnbmodifs(); //et on retourne le nombre de modifs total
		}
		//si il existe on calcule la différence
		int current = this.getmyGraph().getnbmodifs();
		int last = this.lastSentMap.get(sender);
		return (current - last) ;
	}
	
	public int getnbmodifsmin(){
		return this.nbmodifsmin;
	}
	
	
	
	
	/**
	 * This method is automatically called after doDelete()
	 */
	protected void takeDown(){

	}
}


