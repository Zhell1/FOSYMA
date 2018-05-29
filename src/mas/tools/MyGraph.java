package mas.tools;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.function.Function;
import java.util.Date;

import env.Attribute;
import env.Couple;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.TickerBehaviour;
import mas.abstractAgent;
import mas.agents.Collector2Agent;
import mas.agents.GraphAgent;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.algorithm.generator.DorogovtsevMendesGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.GridGenerator;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.Graphs;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.graphstream.ui.view.Viewer;

import java.util.Random;

/**************************************
 * 
 * 
 * 				BEHAVIOUR
 * 
 * 
 **************************************/


public class MyGraph {
	/**
	 * When an agent choose to move
	 *  
	 */
	private static final long serialVersionUID = 9088209402507795289L;
	
	protected Graph graph;
	private Set<String> bordure;
	private List<String> attributs;


	private abstractAgent myAgent;

	private ArrayList<String> ListTreasure;

	private ArrayList<String> ListDiamonds;

	private String siloPosition;

	//private HashSet<String> history;
	int nbmodifs; //kind of timestamp of modifications on the graph
	
	
	public MyGraph(mas.abstractAgent myagent, Graph mygraph) {
		if (myagent == null || mygraph == null){
			System.out.println("Les composants ne sont pas encore prêts");
		}
		this.myAgent = ((mas.abstractAgent) myagent);
		
				//Example to retrieve the current position
		this.graph = mygraph;
		this.bordure = new HashSet<String>();
		this.attributs = new ArrayList<String>(Arrays.asList("explored","Treasure","Diamonds","timeStamp"));
		this.ListTreasure = new ArrayList<String>();
		this.ListDiamonds = new ArrayList<String>();
		this.siloPosition = null;
		this.nbmodifs = 0;
	}
	
	public String getSiloPosition() {
		return this.siloPosition;
	}
	
	public void setSiloPosition(String position) {
		this.siloPosition = position;
		this.nbmodifs += 1000; //this is so important that we want to make sure we will send the graph
	}
	

	/*
	public void test(){
		String sourcename = this.myAgent.getCurrentPosition();
		Node source = this.graph.getNode(sourcename);
		
		Iterator<? extends Node> k = source.getBreadthFirstIterator();

        while (k.hasNext()) {
            Node next = k.next();
            next.setAttribute("ui.class", "marked");
        }
	}
	*/
	public ArrayList<String> NextDijsktra(){
		/* Le path retourné par Dijsktra comporte l'élément lui-même en premier élément 
		*/
		if (this.getBordure().isEmpty()){
			return null;
		}
		String position = (this.myAgent.getCurrentPosition());
		Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.EDGE, "weight", null);
		dijkstra.init(this.graph);
		dijkstra.setSource(this.graph.getNode(position));
		dijkstra.compute();

		// Print the lengths of the new shortest paths
		Float mini = Float.MAX_VALUE;
		float dist;
		int cpt = 0;
		List<Node> LbestNode = new ArrayList<Node>(); //liste des meilleurs noeuds visibles
		for (String name: this.getBordure()){
			Node node = this.graph.getNode(name);
			dist = (float) (dijkstra.getPathLength(node) - 1);
			if (dist < mini){
				mini = dist;
				LbestNode.clear();
				LbestNode.add(node);
			}
			if (dist == mini){
				LbestNode.add(node);
			}
		}
		if (LbestNode.isEmpty()) {
			return null;
		}

		//on prend un noeud au hasard vers lequel aller parmi les meilleurs
		Random r = new Random();		
		Node bestNode = LbestNode.get(r.nextInt(LbestNode.size()));
		Path path = dijkstra.getPath(bestNode);

		//create the return list
		ArrayList <String> res = new ArrayList <String>();
		for(Node node : path.getNodePath()) {
			res.add(node.toString());
		}
		 res.remove(0); // we remove the case where we are currently
	
		String name = this.myAgent.getLocalName();
		System.out.println(name + " is in " + position + " next : " + res + " list: "+path);
		return res;
	}
	
	public boolean bordureConsistance(){
		Set<String> L = new HashSet<String>();
		
		yield(this.graph.getNodeSet().toString());
		
		for (Node n: this.graph.getNodeSet()){
			Object o = n.getAttribute("explored");
			//probleme car les noeuds false ne sont pas toujours rajouter
			if (!(boolean)(n.getAttribute("explored"))){
				L.add(n.getId());
			}
		}
		//yield("Lstring :" + L.toString() + "\n bordure : " + this.bordure+"\n"+L.equals(this.bordure));
		return L.equals(this.bordure);
		
	}
	
	public void calculateBordure() {
		//reset la bordure et recalcule;
		this.bordure = new HashSet<String>();
		for (Node n : this.graph.getNodeSet()) {
			boolean b = n.getAttribute("explored");
			if (!b) {
				this.bordure.add(n.getId());
			}
		}
		
	}
	

	public ArrayList<Node> toNode(ArrayList<String> v){
		ArrayList<Node> res = new ArrayList<Node>();
		for(String s : v){
			res.add(this.graph.getNode(s));
		}
		return res;
	}
	
	public ArrayList<String> NodeToString(ArrayList<Node> L){
		ArrayList<String> res = new ArrayList<String>();
		for (Node n : L) {
			res.add(n.getId());
		}
		return res;
	}
	
	
	public Set<String> getBordure(){
		return this.bordure;
	}
	
	public Set<String> getExplored(){
		HashSet<String> res = new HashSet<String>();
		for(Node  n : this.graph.getNodeSet()) {
			if((boolean)(n.getAttribute("explored"))){
				res.add(n.getId());
			}
		}
		
		return res;
	}
	
	public List<String> Next(){
		List<String> res = new ArrayList<String>();
		String myPosition=this.myAgent.getCurrentPosition();
		List<Couple<String,List<Attribute>>> lobs=this.myAgent.observe();
		for (int i=1; i < lobs.size(); i++){
			res.add(lobs.get(i).getLeft());
		}
		return res;
	}
	
	public void addVoisin(String position){
		Node n = this.graph.getNode(position);
		if (n == null){
			n = this.graph.addNode(position);
			n.addAttribute("explored", false);
			n.addAttribute("timeStamp", new Date().getTime());
			this.bordure.add(position);
		}
		this.nbmodifs++; // we count this as 1 modif, not very important
	}
	
	public boolean inGraph(String position){
		if (this.graph.getNode(position) != null){
			return true;
		}
		else{
			return false;
		}
	}
	
	public void yield(String msg){
		System.out.println("\n\n\n==========================================");
		System.out.println(msg);
		System.out.println("\n\n\n==========================================");
	}
	
	public void add() {
		String myPosition=this.myAgent.getCurrentPosition();	
		
		// créer le noeud actuel
		int valuediamonds = 0, valuetreasure = 0;
		boolean typediamonds = false, typetreasure=false;
		
		if (myPosition!=""){
			List<Couple<String,List<Attribute>>> lobs=this.myAgent.observe();//myPosition
			List<Attribute> lattribute= lobs.get(0).getRight();
			
			for(Attribute a:lattribute){
				switch (a) {
				case TREASURE:
					typetreasure=true;
					valuetreasure = (int)a.getValue();
					break;
				case DIAMONDS:
					typediamonds=true;
					valuediamonds = (int)a.getValue();
				default:
					break;
			}
		}
		//test si déjà dans graphe
		Node n= this.graph.getNode(myPosition);
		if (n != null){
			//le noeud existe dans le graphe
			boolean explored = n.getAttribute("explored");;
			n.setAttribute("timeStamp", new Date().getTime() );
			if (explored){
				//if the treasure has changed
				if((int)n.getAttribute("Treasure") != valuetreasure  || (int)n.getAttribute("Diamonds") != valuediamonds) {
					this.nbmodifs++;
				}
				//update treasure values
				n.setAttribute("Treasure", valuetreasure);
				n.setAttribute("Diamonds", valuediamonds);
			}
			else {
				// le noeud appartenait à la frontière
				this.nbmodifs++;
				n.setAttribute("explored", true);
				n.addAttribute("Treasure", valuetreasure);
				n.addAttribute("Diamonds", valuediamonds);
				//on supprime le noeud
				this.bordure.remove(myPosition);
				if (typetreasure){
					this.ListTreasure.add(myPosition);
				}
				if (typediamonds){
					this.ListDiamonds.add(myPosition);
				}
			
				for (int i =1; i < lobs.size(); i++){
					String voisin = (lobs.get(i)).getLeft();
					String liaison = myPosition +"_"+ voisin;
					String inv = voisin + "_" + myPosition;
					addVoisin(voisin);
					/* Pour une raison étrange apres le merge, on a des probleme de creation d'arrete deja existante */
					if (this.graph.getEdge(inv) == null && this.graph.getEdge(liaison) == null){
						this.graph.addEdge(liaison, myPosition, voisin).addAttribute("weight",1);
						
					}
				}
			}
		}
		else {
			// Le noeud n'est pas dans le graphe (seulement non initialiser)
			this.nbmodifs++;
			print("Initialisation du graphe en : " + myPosition);
			n = this.graph.addNode(myPosition);
			n.addAttribute("explored", true);
			n.addAttribute("Treasure", valuetreasure);
			n.addAttribute("Diamonds", valuediamonds);
			n.addAttribute("timeStamp", new Date().getTime() );
			if (typetreasure){
				this.ListTreasure.add(myPosition);
			}
			if (typediamonds){
				this.ListDiamonds.add(myPosition);
			}
			for (int i =1; i < lobs.size(); i++){
				String voisin = (lobs.get(i)).getLeft();
				String liaison = myPosition + "_" + voisin;
				addVoisin(voisin);
				this.graph.addEdge(liaison, myPosition, voisin).addAttribute("weight",1);
			}
			
		}
	}
	}
	
	public ArrayList<String> getShortestPath(String pos){
		if (pos == null) return null;
		String position = (this.myAgent.getCurrentPosition());
		Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.EDGE, "weight", null);
		dijkstra.init(this.graph);
		dijkstra.setSource(this.graph.getNode(position));
		dijkstra.compute();
		
		Node bestNode = this.graph.getNode(pos);
		if (bestNode == null) {
			return null;
		}
		System.out.println("calculating shortest path from "+position+" to "+pos);
		Path path = dijkstra.getPath(bestNode);
		
		Node n;
		System.out.println("XOXOXO_1 path :" + path.toString());
		//create the return list
		ArrayList <String> res = new ArrayList <String>();
		for(Node node : path.getNodePath()) {
			res.add(node.toString());
		}
		res.remove(0); // we remove the case where we are currently
		//System.out.println("getShortestPath() res = " + res.toString());
		return res;
	}
	
	public ArrayList<String> getShortestPath(ArrayList<String> L){
		if (L == null || L.isEmpty()) {
			return null;
		}
		String position = (this.myAgent.getCurrentPosition());
		if (position == null){
			System.out.println("BUG LA POSITION DE l'agent est vide");
			return null;
		}
		Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.EDGE, "weight", null);
		dijkstra.init(this.graph);
		dijkstra.setSource(this.graph.getNode(position));
		dijkstra.compute();

		// Print the lengths of the new shortest paths
		Float mini = Float.MAX_VALUE;
		Float dist;
		Node bestNode = null;
		Node n;
		for (String s : L) {
			n = this.graph.getNode(s);
			if (n != null) {
				dist = (float) (dijkstra.getPathLength(n) - 1);
				if (dist < mini) {
					bestNode = n;
					mini = dist;
				}
			}
		}
		if (bestNode == null){
			System.out.println("L : " + L.toString());
		}

		System.out.println("calculating shortest path from "+position+" to "+bestNode.getId());
		Path path = dijkstra.getPath(bestNode);
		System.out.println("XOXOXO_2 path :" + path.toString());
		//create the return list
		ArrayList <String> res = new ArrayList <String>();
		for(Node node : path.getNodePath()) {
			res.add(node.toString());
		}
		res.remove(0); // we remove the case where we are currently
		//System.out.println(res.toString());
		return res;
	}
	
	
	public ArrayList<String> siloPath(ArrayList<String> Lin) {
		ArrayList<String> Lout = new ArrayList<String>();
		int size = Lin.size();
		for (int i = 0 ; i < size-1; i++){
			Lout.add(Lin.get(i));
		}
		return Lout;
	}
	
	
	public ArrayList<String> getBestTreasurePath() {
		ArrayList<Node> L = this.getMyTreasuresList();
		ArrayList<String> L2 = this.NodeToString(L);
		ArrayList<String> res = this.getShortestPath(L2);
		return res;
	
	}
	
	/*
	public ArrayList<String> getMyTreasuresList(){
		String type = this.myAgent.getMyTreasureType();
		if (type.equals("tresortype1")){
			return this.tresorList1;
		}
		else {
			return this.tresorList2;
		}
	}
	
	*/
	
	
	public ArrayList<Node> getMyTreasuresList(){
		String type = this.myAgent.getMyTreasureType();
		//print("treasuretype : "+type);
		ArrayList<Node> L = new ArrayList<Node>();
		for (Node n : this.graph.getNodeSet()){
			//print("this.graph.getNodeSet() = "+ this.graph.getNodeSet().toString());
			if (n != null){
				//print("n.getAttribute(type) = " + n.getAttribute(type));
				if(n.getAttribute(type) != null) { //si le noeud n'est pas dans la bordure
					if ((int)n.getAttribute(type) > 0){ //si il reste de ce type de trésor dessus
						L.add(n); // on l'ajoute à la liste
					}
				}
			}
		}
		return L;
	}
	
	public int getTreasureValue(String nodename, String type){
		boolean res = false;
		Node n = this.graph.getNode(nodename);
		//System.out.println("Node n " + n.toString());
		Collection<String> att = n.getAttributeKeySet();
		//System.out.println("att : " + att.toString());
		if(type.equals("Treasure")){
			return n.getAttribute("Treasure");
		}
		else if(type.equals("Diamonds")){
			return n.getAttribute("Diamonds");
		}
		else {
			((GraphAgent) this.myAgent).print("getTreasureValue(): ERROR TYPE : "+ type);
			return 0;
		}
	}
	
	public ArrayList<Node> getTreasuresList(){
		ArrayList<Node> L = new ArrayList<Node>();
		for (Node n : this.graph.getNodeSet()){
			if ((boolean)n.getAttribute("Treasure") || (boolean)n.getAttribute("Diamonds")){
				L.add(n);
			}
		}
		return L;
	}
	
	public ArrayList<String> getCollectorNextNode(HeuristiqueInterface f){
		/* f : la fonction heuristique */
		if (this.graph.getNodeCount() == 0){
			return null;
		}
		String position = (this.myAgent.getCurrentPosition());
		Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.EDGE, "weight", null);
		dijkstra.init(this.graph);
		dijkstra.setSource(this.graph.getNode(position));
		dijkstra.compute();
		
		ArrayList<Node> L = getMyTreasuresList();
		ArrayList<Couple<Node, Integer>> distMap = new ArrayList<Couple<Node, Integer>>();
		Float mini = Float.MAX_VALUE;
		Integer dist;
		int cpt = 0;
		for (Node node: L){
			dist = (int) (dijkstra.getPathLength(node) - 1);
			distMap.add(new Couple(node, dist));
		}
		// on applique une fonction heurisitique ()
		Node pos = f.consult(distMap);
		Path path = dijkstra.getPath(pos);
		ArrayList <String> res = new ArrayList <String>();
		while (path.size() > 1){
			Node n = path.popNode();
			res.add(n.toString());
		}
		return res;
		
	}
	

	
	/* ====================================================
	 *              HASH PART
	 ==================================================== */
	
	public HashMap<String, Object> getAttributeHashMap(Node n){
		HashMap<String, Object> res = new HashMap<String, Object>();
			for (String at : n.getAttributeKeySet()){
				if (this.attributs.contains(at)){
				res.put(at, n.getAttribute(at));
				}
			}
		return res;
	}
	
	public Set<String> getNodeIDSet(){
		Set<String> res = new HashSet<String>();
		for (Node n : this.graph.getNodeSet()){
			res.add(n.getId());
		}
		return res;
	}
	
	
	public HashMap<String, Object> toHashMap(){
		/* Normalement c'est Serializable maintenent
		 * Je fais des conversions car les types nodes et Edges ne sont pas serializable donc je converti tous en String ... */
		HashMap<String, Object> res = new HashMap<String, Object>();
		HashMap<String, Object> att;
		String id;
		List<Couple> nodes = new ArrayList<Couple>();
		List<Couple> edges = new ArrayList<Couple>();
		for (Node n : this.graph.getNodeSet()){
			id = (n.getId());
			att = getAttributeHashMap(n);
			Couple couple = new Couple(id, att);
			nodes.add(couple);
		}
		for (Edge e : this.graph.getEdgeSet()){
			Node p0 = e.getNode0();
			Node p1 = e.getNode1();
			Couple couple = new Couple(p0.toString(), p1.toString());
			edges.add(couple);
		}
		res.put("nodes", nodes);
		res.put("edges", edges);
		res.put("border", this.bordure);
		res.put("position", this.myAgent.getCurrentPosition());
		res.put("siloPosition", this.siloPosition);
		return res;
	}
	
	public MyGraph (abstractAgent myagent, HashMap<String, Object> map){
		Collection<Couple>  nodes = (Collection<Couple>) map.get("nodes");
		Collection<Couple> edges = (Collection<Couple>) map.get("edges");
		HashSet<String> border = (HashSet<String>) map.get("border");
		this.graph = new SingleGraph("bla");
		Node addedNode;
		HashMap<String, Object> attMap;
		//add Node
		String n;
		HashMap<String, Object> att;
		for (Couple c : nodes){
			n = (String)c.getLeft();
			att = (HashMap<String, Object>)c.getRight();
			this.graph.addNode(n);
			addedNode = this.graph.getNode(n);
			addedNode.addAttributes(att);
		}
		for (Couple c : edges){
			String p0 = (String)c.getLeft();
			String p1 = (String)c.getRight();
			String liaison = p0 + "_" + p1;
			this.graph.addEdge(liaison, p0, p1).addAttribute("weight", 1);;
		}
		this.myAgent = ((mas.abstractAgent) myagent);
		this.attributs = new ArrayList<String>(Arrays.asList("explored","Treasure","Diamonds","timeStamp")) ;
		this.bordure = border;
		
	}
	/*
	 * TODO
	 * quand on merge stocker les modifs dans une liste
	 * qu'on pourra ensuite appeler depuis le FirstAgentExplore quand isexplo=true (agent explorateur)
	 * en début d'action() afin de voir si on à de nouveaux trésors, auquel cas on  sort du behavior
	 * avec un signal spécifique qui va indiquer au FSM de passer en collector
	 */
	public int get(Node n, String at){
		if (n.getAttribute(at) == null){
			return 0;
		}
		return n.getAttribute(at);
	}
	
	public void merge(HashMap<String, Object> map2) {
		
		Collection<Couple>  nodes = (Collection<Couple>) map2.get("nodes");
		Collection<Couple> edges = (Collection<Couple>) map2.get("edges");
		HashSet<String> border = (HashSet<String>) map2.get("border");
		String position = (String) map2.get("position");
		String siloPos = (String) map2.get("siloPosition");
		
		String id;
		HashMap<String, Object> att;
		Node n;
		Node newNode;
		boolean explored1;
		boolean explored2;
		boolean b;
		for (Couple c : nodes) {
			id = (String) c.getLeft();
			att = (HashMap<String, Object>) c.getRight();
			
			n = this.graph.getNode(id);
			//le noeud n'existe pas dans mon graphe
			if (n == null) {
				this.nbmodifs++;
				this.graph.addNode(id);
				newNode = this.graph.getNode(id);
				newNode.addAttributes(att);
			}
			else {
				explored1 = n.getAttribute("explored");
				explored2 = (boolean) att.get("explored");
				b = explored1 || explored2;
				if(explored1 != b) { //si on à changé l'état du noeud
					this.nbmodifs++;
				}
				n.setAttribute("explored", b);
			}
			
		}
		// création du noeud silo
		if (this.siloPosition == null && siloPos != null) {
			this.nbmodifs+=1000; //make sure we send the graph since this info is very important
			this.siloPosition = siloPos;
			if (this.graph.getNode(siloPos) == null){
				this.graph.addNode(this.siloPosition);
			}
			Node SP = this.graph.getNode(this.siloPosition);
			SP.addAttribute("explored", true);
			for (Edge e : SP.getEdgeSet()){
				e.setAttribute("weight", 100000); // we want to avoid walking on silo
			}
	
		}
		
		//Tous les noeuds du graphe ne sont pas forcement exploré donc il faut recalculer la bordure
		this.calculateBordure();	
	}

	
	/*
	 //this is old and does not work 
	public void merge(MyGraph g){
		// modifie sur place
		// Pour la bordure on est obliger de la recalculer
		 
		boolean printdebug = false; //passer à true pour afficher le debug
		
		if(printdebug) {
			System.out.println("\n\n---------MERGE ----------");
			System.out.println("current graph:");
			printGraph(this);
			System.out.println("new graph to merge:");
			printGraph(g);
		}
		
		//Graphs.mergeIn(this.graph, g.getGraphStream());  //!\\ merge les noeuds mais force les attributs du nouveau graph
		Graph newGraph = Graphs.merge(this.graph, g.getGraphStream()); //!\\ les attributs sont foireux aussi avec ça
		MyGraph myNewGraph = new MyGraph(this.myAgent, newGraph);
		
		//System.out.println("after graphstream mergeIn():");
		//printGraph(myNewGraph);
		
		//System.out.println("mise à jour des attributs et de la bordure:");
		
		if(printdebug) System.out.println("noeud déjà explorés dans l'union des 2 précédents:");
		
		Set<String> newBordure = new HashSet<String>();
		for (Node n : newGraph.getNodeSet()){
			String nid = n.getId();
			Node node1 = this.graph.getNode(nid);
			Node node2 = g.graph.getNode(nid);
			
			boolean explored1 = false;
			long timestamp1 = 0;
			int v1_treasure=0; // valeur du tresor
			int v1_diamonds=0;
			if(node1 != null) {
				explored1 = node1.getAttribute("explored");
				timestamp1 = node1.getAttribute("timeStamp");
				v1_treasure = get(node1, "Treasure");
				v1_diamonds = get(node1, "Diamonds");
			}
			boolean explored2 = false;
			long timestamp2 = 0;
			int v2_treasure=0; // valeur du tresor
			int v2_diamonds=0;
			if(node2 != null) {
				explored2 = node2.getAttribute("explored");
				timestamp2 = node2.getAttribute("timeStamp");
				v2_treasure = get(node2, "Treasure");
				v2_diamonds = get(node2, "Diamonds");
			}
			
			boolean b = (explored1 || explored2);
			if(printdebug) {
				if(b) System.out.println(n.getId());
			}
			n.setAttribute("explored",b);
			//System.out.println("test du noeud"+n.getId()+" mis à "+b);
			
			if (!((boolean) n.getAttribute("explored"))){
				//System.out.println("the node " + n.getId() +  "is added to the bordure");
				newBordure.add(n.getId());
			}
			
			//mise à jour des attributs des trésors
			//si plus récent
			if(timestamp2 > timestamp1) {
				n.setAttribute("Treasure",v2_treasure);
				n.setAttribute("timeStamp", timestamp2);
			}
			//sinon on remet l'ancien
			else {
				n.setAttribute("Treasure",v1_treasure);
				n.setAttribute("Diamonds",v1_diamonds);
				n.setAttribute("timeStamp", timestamp1);
			}
		}
		this.bordure = newBordure;
		this.graph = newGraph;
		
		if(printdebug){
			System.out.println("\nnew graph obtained after merging:");
			printGraph(this);
			System.out.println("\n\n-------------------------");
		}
	}
	*/
	
	public void printGraph(MyGraph mygraph){
		System.out.println("\n######## GRAPH ##########");
		System.out.println("Noeuds : " + mygraph.graph.getNodeSet());
		System.out.println("Bordure: " + mygraph.getBordure());
		System.out.println("Noeuds Explorés?:");
		for (Node n : mygraph.graph.getNodeSet()){
			System.out.println(n.getId()+" "+n.getAttribute("explored"));
		}
		System.out.println("nbmodifs: "+this.nbmodifs);
		System.out.println("###########################\n");
	}
	
	public Graph getGraphStream(){
		return this.graph;
	}
	private void print(String m){
		System.out.println(this.myAgent.getLocalName()+" : "+m);
	}
	
	public static ArrayList<String> visitedNode(HashMap<String, Object> map) {
		ArrayList<String> res = new ArrayList<String>();
		
		return res;
	}
	/* ============================================================ */



}