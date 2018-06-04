package mas.agents;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.Graphs;
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
	int sleepbetweenmove;

	public Messages mailbox;
	public String lastmove;
	private long timeOut;
	private HashMap<String, Object> lastMsg;
	private HashMap<String, Object> lastMap;
	private HashMap<String, Integer> lastSentMap;
	String lastsender;
	int nbmodifsmin;
	boolean remakepath;
	int nbmoverandom;
	int nbmoverandomoriginal;
	
	private HashMap<String, Graph> toSendMap; // TODO use this to send only part of map TODO make it private and some getters
	
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
		
		//############ PARAMS ##########

		this.nbmodifsmin 		= 30;			//nb modifs minimum pour renvoyer la carte
		this.timeOut 			= 1000 * 4;		//secondes pour timeout des messages (*1000 car il faut en ms)
		this.sleepbetweenmove 	= 200;			//in MS
		this.nbmoverandom		= 4;			// nb random moves by default
		
		//#############################
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
		this.lastsender = null;
		this.lastSentMap = new HashMap<String, Integer>(); //nbmodifs
		this.remakepath = false; // changes to true if the map changed in a way that requires a new path
		this.nbmoverandomoriginal = this.nbmoverandom;
		
		this.toSendMap = new HashMap<String, Graph>(); //actual hashmap graph
		
		System.out.println("the agent "+this.getLocalName()+ " is started");
	}
	
	public void print(String m){
		System.out.println(this.getLocalName()+" : "+m);
	}
	
	public int getnbmoverandom() {
		return this.nbmoverandom;
	}
	public void setnbmoverandom(int newnb) {		
		this.nbmoverandom = newnb;
	}
	public void resetnbmoverandom() {		
		this.nbmoverandom = this.nbmoverandomoriginal;
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
	
	public HashMap<String,Object> gettoSendMap(String destinataire) {
		// si on à jamais envoyé
		//this.print("gettosendmap for agent "+destinataire+" : "+this.toSendMap.containsKey(destinataire));
		if(this.toSendMap.containsKey(destinataire) == false) {
			//on met à jour
			Graph fullmap = this.getmyGraph().getGraphStream(); // tout envoyer
			Graph tosendmapGraph = Graphs.clone(fullmap);
			this.toSendMap.put(destinataire, tosendmapGraph); //on crée une entrée avec la fullmap
			this.print("creating a toSendMap for "+destinataire);
			//attente ack pour clean : normalement ça marche puisque l'agent se déplace pas tant que sharemap est pas terminé (ack recu ou timeout)
			return this.getmyGraph().toHashMap2(fullmap); // on envoie tout converti !
		}
		//si on à déjà envoyé
		else {
			//this.print(this.toSendMap.get(destinataire).toString());
			//on retourne la map à envoyer
			Graph map = this.toSendMap.get(destinataire);
			return this.getmyGraph().toHashMap2(map); // on envoie converti
		}
	}
	
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
		//Le pick à réussi !
		this.getmyGraph().addnbmodifs(1); //ajoute 1 modif dans le graph
		
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
					//test si il reste du trésor
					if(a.getName().equals("Diamonds")) {
						if((int)a.getValue() > 0){
							isDiamonds = true;
							//met à jour les tosendmap with current observed node
							updatevaluetosendmap(n.getId(), "Diamonds", (int)a.getValue());
						}
					}else if(a.getName().equals("Treasure")){
						if((int)a.getValue() > 0){
							isTreasure = true;
							updatevaluetosendmap(n.getId(),"Treasure", (int)a.getValue());
						}
					}
				}
				//on doit aussi gérer les cas où on ne récupère pas d'attributs = case vidée
				//dans ce cas on doit aussi supprimer la case de la liste de trésor dans mygraph
				if(isTreasure == false){
					n.setAttribute("Treasure", 0);
					this.getmyGraph().removeFromListTreasure(n.getId());
					//on suppose que si on pick c'est qu'il y avait quelque chose avant
					updatevaluetosendmap(n.getId(), "Treasure", 0);
				}
				if(isDiamonds == false){
					n.setAttribute("Diamonds", 0);
					this.getmyGraph().removeFromListDiamonds(n.getId());
					//on suppose que si on pick c'est qu'il y avait quelque chose avant
					updatevaluetosendmap(n.getId(), "Diamonds", 0);
				}
				
				break; //sort de la boucle car on ne s'intéressait qu'au noeud courant
			}
		}
		return true;
	}
	/*************** update to send map **************/
	//update the treasure value of a node on all tosendmap
	public void updatevaluetosendmap(String nodename, String attname, int newvalue){
		for(String dest : this.toSendMap.keySet()) {
			Graph destmap = this.toSendMap.get(dest);
			Node n = destmap.getNode(nodename);
			if(n == null) {
				updatetosendmap_addfromfullgraph(nodename); //on l'a supprimé donc on le recrée comme
				n = destmap.getNode(nodename);
			}
			n.setAttribute(attname, newvalue); // add or replace
		}
	}
	//add a voisin on all tosendmap
		public void updatetosendmapaddvoisin(String voisin){
			for(String dest : this.toSendMap.keySet()) {
				Graph destmap = this.toSendMap.get(dest);
				Node n = destmap.addNode(voisin);
				n.addAttribute("explored", false);
				n.addAttribute("Treasure", 0);
				n.addAttribute("Diamonds", 0);
				n.addAttribute("timeStamp", new Date().getTime());
			}
		}
		//TODO merge et add2
		public void updatetosendmap_addnode(String id, HashMap<String, Object> att) {
			for(String dest : this.toSendMap.keySet()) {
				Graph destmap = this.toSendMap.get(dest);
				Node newNode = destmap.getNode(id);
				if(newNode == null)
					newNode = destmap.addNode(id);
				newNode.addAttributes(att);
			}
		}	
		public void updatetosendmap_updatenodeattribute(String id, String attname, Object attval) {
			for(String dest : this.toSendMap.keySet()) {
				Graph destmap = this.toSendMap.get(dest);
				Node n = destmap.getNode(id);
				if(n==null) {
					updatetosendmap_addfromfullgraph(id); //on l'a supprimé donc on le recrée comme
					n = destmap.getNode(id);
				}
				n.setAttribute(attname,  attval);
			}
		}
	public void updatetosendmap_addedge(String edgename, String node1name, String node2name, int weight){
		for(String dest : this.toSendMap.keySet()) {
			Graph destmap = this.toSendMap.get(dest);
			
			String[] res = edgename.split("_");
			String edgenameinv = res[1]+"_"+res[0];
			
			Edge newedge = destmap.getEdge(edgename);
			Edge newedgeinv = destmap.getEdge(edgenameinv);
			if(newedge == null && newedgeinv == null){
				Node node1 = destmap.getNode(node1name);
				Node node2 = destmap.getNode(node2name);
				if(node1 == null) {
					updatetosendmap_addfromfullgraph(node1name);
					node1 = destmap.getNode(node1name);
				}
				if(node2 == null){
					updatetosendmap_addfromfullgraph(node2name);
					node2 = destmap.getNode(node2name);
				}
				destmap.addEdge(edgename, node1name, node2name);
				destmap.getEdge(edgename).setAttribute("weight", weight);
			}
		}
	}
	//récupère un noeud prééexistant et le rajoute dans les graphes tosendmap (pour modif attribut)
	public void updatetosendmap_addfromfullgraph(String idnode) {
		for(String dest : this.toSendMap.keySet()) {
			Graph destmap = this.toSendMap.get(dest);
			Node n = destmap.getNode(idnode);
			if(n==null)
				n = destmap.addNode(idnode);
			Node norig = this.getmyGraph().getNode(idnode);
			n.addAttribute("explored", (boolean)norig.getAttribute("explored"));
			n.addAttribute("Treasure", (int)norig.getAttribute("Treasure"));
			n.addAttribute("Diamonds", (int)norig.getAttribute("Diamonds"));
			n.addAttribute("timeStamp", (long)norig.getAttribute("timeStamp"));
		}
	}
	
	//la bordure est recalculée par le récepteur donc rien à faire
	/************* fin update to send map *************************/
	
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
		String printstring = "My treasure type: " + this.getMyTreasureType();
		printstring += "\t | Silo at "+this.getmyGraph().getSiloPosition();
		printstring += "\t | explored: "+this.getmyGraph().getExplored().size();
		printstring += "\t | bordure: "+this.getmyGraph().getBordure().size();
		printstring += "\t | freespace: "+this.getBackPackFreeSpace();
		printstring += "\t | myTreasureLeft: "+(this.getmyGraph().getBestTreasurePath()!=null);
		this.print(printstring);
		
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
		this.path.add(0, m); //we need to put it back at the front
	}
	
	public ArrayList<String> getPath(){
		return this.path;
	}
	
	public void resetPath() {
		this.path.clear();
		this.remakepath = true;
	}
	
	public void setPath(ArrayList<String> n) {
		this.path = n;
	}
	public boolean getremakepath(){
		return this.remakepath;
	}	
	public void setremakepath(boolean newval){
		this.remakepath = newval;
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
		this.lastmove = move;
		this.myGraph.add(); //add or update
		
		//wait time
		try {
			Thread.sleep(this.sleepbetweenmove); //TODO A SUPPRIMER (JUSTE POUR DEBUG)
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
		this.myGraph = new mas.tools.MyGraph(this, this.graph);
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
			this.lastSentMap.put(sender, 0); //int = nbmodifs
			// attention : ne pas créer dans tosendmap ici, on le fait dans gettosendmap
		}
		//on met à jour la valeur
		this.lastSentMap.replace(sender, this.getmyGraph().getnbmodifs());
		Graph currgraph = toSendMap.get(sender);
		if(currgraph != null)
			currgraph.clear(); //vide la tosendmap car on à recu ack
		//toSendMap.replace(sender, currgraph); 
	}
	
	//retourne le nombre de modifs effectués pour un agent depuis qu'on lui à envoyé
	public int getDifferenceLastSent(String sender){
		//si il existe pas déjà on commence par le créer
		if (! this.lastSentMap.containsKey(sender)){
			this.lastSentMap.put(sender, 0); //this one only counts modifications as int
			//this.toSendMap.put(sender, null); //don't do it here !!!
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


