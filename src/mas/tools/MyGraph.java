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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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

	//private HashSet<String> history;

	public MyGraph(mas.abstractAgent myagent, Graph mygraph) {
		if (myagent == null || mygraph == null){
			System.out.println("Les composants ne sont pas encore prêts");
		}
		this.myAgent = ((mas.abstractAgent) myagent);
		
				//Example to retrieve the current position
		this.graph = mygraph;
		this.bordure = new HashSet<String>();
		this.attributs = new ArrayList<String>(Arrays.asList("explored","value1","value0","tresortype1","tresortype2")) ;
		//this.history = new HashSet<String>();

		//super(myagent);
	}
	
	// à override
	public void onTick() {}
	
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
		yield("Lstring :" + L.toString() + "\n bordure : " + this.bordure+"\n"+L.equals(this.bordure));
		return L.equals(this.bordure);
		
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
			this.attributs = new ArrayList<String>(Arrays.asList("explored","value1","value0","tresortype1","tresortype2"));
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
			if (explored){
				return;
			}
			else {
				// le noeud appartenait à la frontière
				n.setAttribute("explored", true);
				n.addAttribute("tresortype1", tresortype1);
				n.addAttribute("value1", value1);
				n.addAttribute("tresortype2", tresortype2);
				n.addAttribute("value2", value2);
				this.bordure.remove(myPosition);
			
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
			System.out.println("Initialisation du graphe en : " + myPosition);
			n = this.graph.addNode(myPosition);
			n.addAttribute("explored", true);
			n.addAttribute("tresortype1", tresortype1);
			n.addAttribute("value1", value1);
			n.addAttribute("tresortype2", tresortype2);
			n.addAttribute("value2", value2);
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
	
	public void merge(MyGraph g){
		/* modifie sur place
		 * Pour la bordure on est obliger de la recalculer
		 */
		boolean test = bordureConsistance();
		if (!test){
			yield("Erreur consistance merge");
		}
		Graphs.mergeIn(this.graph, g.getGraphStream());
		Set<String> newBordure = new HashSet<String>();
		for (Node n : this.graph.getNodeSet()){
			boolean e = n.getAttribute("explored");
			//System.out.println("node :" + n.getId());
			Node other = g.graph.getNode(n.getId());
			
			if (other != null) {
				boolean other_e = other.getAttribute("explored");
				boolean b = e || other_e;
				System.out.println("node : " + n.getId() + ",e : " + e + " other e : " + other_e + " resultat ou : " + b);
				n.setAttribute("explored",b);
			}
			if (!((boolean) n.getAttribute("explored"))){
				System.out.println("the node " + n.getId() +  "is added to the bordure");
				newBordure.add(n.getId());
			}
		}
		if(this.bordure.size() == 0 && newBordure.size() > 0) {
			System.out.println("\n\n################## \n");
			System.out.println();
			System.out.println("Noeuds :" + this.graph.getNodeSet());
			for (Node n : this.graph.getNodeSet()){
				System.out.println(n.getId()+" "+n.getAttribute("explored"));
			}
			System.out.println("\n\n################## \n");
			
		}
		this.bordure = newBordure;
	}
	
	public Graph getGraphStream(){
		return this.graph;
	}
	/* ============================================================ */



}