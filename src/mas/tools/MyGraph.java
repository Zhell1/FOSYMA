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

	private ArrayList<String> tresorList1;

	private ArrayList<String> tresorList2;

	//private HashSet<String> history;

	public MyGraph(mas.abstractAgent myagent, Graph mygraph) {
		if (myagent == null || mygraph == null){
			System.out.println("Les composants ne sont pas encore prêts");
		}
		this.myAgent = ((mas.abstractAgent) myagent);
		
				//Example to retrieve the current position
		this.graph = mygraph;
		this.bordure = new HashSet<String>();
		this.attributs = new ArrayList<String>(Arrays.asList("explored","value1","value0","tresortype1","tresortype2","timeStamp"));
		this.tresorList1 = new ArrayList<String>();
		this.tresorList2 = new ArrayList<String>();
		//this.history = new HashSet<String>();

		//super(myagent);
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
		/* Le path retourner par Dijsktra comporte l'element lui meme en premier element 
		*/
		if (this.getBordure().isEmpty()){
			return null;
		}
		String position = (this.myAgent.getCurrentPosition());
		Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.NODE, null, null);
		dijkstra.init(this.graph);
		dijkstra.setSource(this.graph.getNode(position));
		dijkstra.compute();

		// Print the lengths of the new shortest paths
		Float mini = Float.MAX_VALUE;
		float dist;
		int cpt = 0;
		List<Node> LbestNode = new ArrayList<Node>();
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
		// Le path est une pile, on doit donc la reverse
		Random r = new Random();
		Node bestNode = LbestNode.get(r.nextInt(LbestNode.size()));
		Path path = dijkstra.getPath(bestNode);
		ArrayList <String> res = new ArrayList <String>();
		while (path.size() > 1){
			Node n = path.popNode();
			res.add(n.toString());
		}
		
	
		String name = this.myAgent.getLocalName();
		//System.out.println(name + " is in " + position + " next : " + res + " list: "+path);
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
	

	public List<Node> toNode(List<String> v){
		ArrayList<Node> res = new ArrayList<Node>();
		for(String s : v){
			res.add(this.graph.getNode(s));
		}
		return res;
	}
	
	
	public Set<String> getBordure(){
		return this.bordure;
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
			//this.attributs = new ArrayList<String>(Arrays.asList("explored","value1","value0","tresortype1","tresortype2"));
			/*if (this.history.contains(position)){
				System.out.println("=========== Ajout d'un noeud deja exploré =============");
			}
			*/
			this.bordure.add(position);
			//this.history.add(position);
		}
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
		int value1 = 0, value2 = 0;
		boolean tresortype1 = false, tresortype2=false;
		
		if (myPosition!=""){
			List<Couple<String,List<Attribute>>> lobs=this.myAgent.observe();//myPosition
			//list of attribute associated to the currentPosition
			List<Attribute> lattribute= lobs.get(0).getRight();
			
			for(Attribute a:lattribute){
				switch (a) {
				case TREASURE:
					tresortype1=true;
					value1 = (int)a.getValue();
					break;
				case DIAMONDS:
					tresortype2=true;
					value2 = (int)a.getValue();
				default:
					break;
			}
		}
		//test si déjà dans graphe
		Node n= this.graph.getNode(myPosition);
		if (n != null){
		//	System.out.println("Position :" + myPosition);
		//	System.out.println(n.toString());
			boolean explored = n.getAttribute("explored");
			n.setAttribute("timeStamp", new Date().getTime() );
			if (explored){
				n.setAttribute("tresortype1", tresortype1);
				n.setAttribute("value1", value1);
				n.setAttribute("tresortype2", tresortype2);
				n.setAttribute("value2", value2);
			}
			else {
				// le noeud appartenait à la frontière
				n.setAttribute("explored", true);
				n.addAttribute("tresortype1", tresortype1);
				n.addAttribute("value1", value1);
				n.addAttribute("tresortype2", tresortype2);
				n.addAttribute("value2", value2);
				this.bordure.remove(myPosition);
				if (tresortype1){
					this.tresorList1.add(myPosition);
				}
				if (tresortype2){
					this.tresorList2.add(myPosition);
				}
			
				for (int i =1; i < lobs.size(); i++){
					String voisin = (lobs.get(i)).getLeft();
					String liaison = myPosition +"_"+ voisin;
					String inv = voisin + "_" + myPosition;
					addVoisin(voisin);
					/* Pour une raison étrange apres le merge, on a des probleme de creation d'arrete deja existante */
					if (this.graph.getEdge(inv) == null && this.graph.getEdge(liaison) == null){
					this.graph.addEdge(liaison, myPosition, voisin).addAttribute("length",1);
					}
				}
			}
		}
		else {
			// Le noeud n'est pas dans le graphe (seulement non initialiser)
			print("Initialisation du graphe en : " + myPosition);
			n = this.graph.addNode(myPosition);
			n.addAttribute("explored", true);
			n.addAttribute("tresortype1", tresortype1);
			n.addAttribute("value1", value1);
			n.addAttribute("tresortype2", tresortype2);
			n.addAttribute("value2", value2);
			if (tresortype1){
				this.tresorList1.add(myPosition);
			}
			if (tresortype2){
				this.tresorList2.add(myPosition);
			}
			for (int i =1; i < lobs.size(); i++){
				String voisin = (lobs.get(i)).getLeft();
				String liaison = myPosition + "_" + voisin;
				addVoisin(voisin);
				this.graph.addEdge(liaison, myPosition, voisin).addAttribute("length",1);
			}
			
		}
		/*
		boolean test = bordureConsistance();
		if (!(test)){
			System.out.println("\n\n\n ===============================");
			System.out.println("Probleme consistance add");
			System.out.println("\n\n\n ===============================");
		}
		*/
	}
	}
	
	public ArrayList<String> getMyTreasuresList(){
		String type = this.myAgent.getMyTreasureType();
		if (type.equals("tresortype1")){
			return this.tresorList1;
		}
		else {
			return this.tresorList2;
		}
	}
	public int getTreasureValue(String nodename, String type){
		boolean res = false;
		Node n = this.graph.getNode(nodename);
		if(type.equals("tresortype1")){
			return n.getAttribute("value1");
		}
		else {
			return n.getAttribute("value2");
		}
	}
	
	public ArrayList<Node> getTreasuresList(){
		ArrayList<Node> L = new ArrayList<Node>();
		for (Node n : this.graph.getNodeSet()){
			if ((boolean)n.getAttribute("tresortype1") || (boolean)n.getAttribute("tresortype2")){
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
		Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.NODE, null, null);
		dijkstra.init(this.graph);
		dijkstra.setSource(this.graph.getNode(position));
		dijkstra.compute();
		
		ArrayList<Node> L = (ArrayList<Node>) toNode(getMyTreasuresList());
		ArrayList<Couple<Node, Integer>> distMap = new ArrayList<Couple<Node, Integer>>();
		Float mini = Float.MAX_VALUE;
		float dist;
		int cpt = 0;
		for (Node node: L){
			dist = (float) (dijkstra.getPathLength(node) - 1);
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
		 * Je fais des conversion car les types nodes et Edges ne sont pas serializable donc je convertis tous en String ... */
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
			this.graph.addEdge(liaison, p0, p1);
		}
		this.myAgent = ((mas.abstractAgent) myagent);
		this.attributs = new ArrayList<String>(Arrays.asList("explored","value1","value0","tresortype1","tresortype2")) ;
		this.bordure = border;
		
	}
	/*
	 * TODO
	 * quand on merge stocker les modifs dans une liste
	 * qu'on pourra ensuite appeler depuis le FirstAgentExplore quand isexplo=true (agent explorateur)
	 * en début d'action() afin de voir si on à de nouveaux trésors, auquel cas on  sort du behavior
	 * avec un signal spécifique qui va indiquer au FSM de passer en collector
	 */
	public void merge(MyGraph g){
		/* modifie sur place
		 * Pour la bordure on est obliger de la recalculer
		 */
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
			int timestamp1 = 0;
			int v1_1=0; // valeur du tresor
			int v1_2=0;
			if(node1 != null) {
				explored1 = node1.getAttribute("explored");
				timestamp1 = node1.getAttribute("timeStamp");
				v1_1 = node1.getAttribute("value1");
				v1_2 = node1.getAttribute("value2");
			}
			boolean explored2 = false;
			int timestamp2 = 0;
			int v2_1=0; // valeur du tresor
			int v2_2=0;
			if(node2 != null) {
				explored2 = node2.getAttribute("explored");
				timestamp2 = node2.getAttribute("timeStamp");
				v2_1 = node2.getAttribute("value1");
				v2_2 = node2.getAttribute("value2");
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
			
			/*mise à jour des attributs des trésors*/
			//si plus récent
			if(timestamp2 > timestamp1) {
				n.setAttribute("value1",v2_1);
				n.setAttribute("value2",v2_2);
				if(v2_1 == 0) {
					n.setAttribute("tresortype1",false);
				}
				if(v2_2 == 0) {
					n.setAttribute("tresortype2",false);
				}
				n.setAttribute("timeStamp", timestamp2);
			}
			//sinon on remet l'ancien
			else {
				n.setAttribute("value1",v1_1);
				n.setAttribute("value2",v1_2);
				if(v1_1 == 0) {
					n.setAttribute("tresortype1",false);
				}
				if(v1_2 == 0) {
					n.setAttribute("tresortype2",false);
				}
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
	
	public void printGraph(MyGraph mygraph){
		System.out.println("\n######## GRAPH ##########");
		System.out.println("Noeuds : " + mygraph.graph.getNodeSet());
		System.out.println("Bordure: " + mygraph.getBordure());
		System.out.println("Noeuds Explorés?:");
		for (Node n : mygraph.graph.getNodeSet()){
			System.out.println(n.getId()+" "+n.getAttribute("explored"));
		}
		System.out.println("###########################\n");
	}
	
	public Graph getGraphStream(){
		return this.graph;
	}
	private void print(String m){
		System.out.println(this.myAgent.getLocalName()+" : "+m);
	}
	/* ============================================================ */



}