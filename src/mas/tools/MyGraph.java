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

	public MyGraph(mas.abstractAgent myagent, Graph mygraph) {
		if (myagent == null || mygraph == null){
			System.out.println("Les composants ne sont pas encore prêts");
		}
		this.myAgent = ((mas.abstractAgent) myagent);
		
				//Example to retrieve the current position
		this.graph = mygraph;
		this.bordure = new HashSet<String>();
		this.attributs = new ArrayList<String>(Arrays.asList("explored","value1","value0","tresortype1","tresortype2")) ;

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
		Node bestNode = null;
		for (String name: this.getBordure()){
			Node node = this.graph.getNode(name);
			dist = (float) (dijkstra.getPathLength(node) - 1);
			if (dist < mini){
				mini = dist;
				bestNode = node;
			}
		}
		// Le path est une pile, on doit donc la reverse
		Path path = dijkstra.getPath(bestNode);
		ArrayList <String> res = new ArrayList <String>();
		while (path.size() > 1){
			Node n = path.popNode();
			res.add(n.toString());
		}
	
		String name = this.myAgent.getLocalName();
		System.out.println(name + " is in " + position + " next : " + res + " list: "+path);
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
			n.addAttribute("explored", false);		this.attributs = new ArrayList<String>(Arrays.asList("explored","value1","value0","tresortype1","tresortype2")) ;
			this.bordure.add(position);
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
					if (this.graph.getEdge(inv) == null){
					this.graph.addEdge(liaison, myPosition, voisin).addAttribute("length",1);
					}
				}
			}
		}
		else {
			// Le noeud n'est pas dans le graphe (seulement non initialiser)
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
	}
	}
	
	public JSONObject toJSON(){
		//https://www.geeksforgeeks.org/parse-json-java/
		JSONObject j = new JSONObject();
		JSONObject help = new JSONObject();
		for(Node n : this.graph.getNodeSet()){
			Map m = new HashMap(2);
			m.put("id", n.getId());
			JSONArray L = new JSONArray();
			for(String att : this.attributs){
				JSONObject temp = new JSONObject();
				temp.put(att, n.getAttribute(att));
				L.add(temp);
			}
			m.put("attribute", L);
			help.put("node", m);
		}
		j.put("nodes", help);
		//il nous reste a faire les aretes
		return j;
	}
		
	public String toStringJSON() {
		JSONObject j = new JSONObject();
		
		String node = "nodes : {";
		String temp = "";
		for (Node n : this.graph.getNodeSet()){
			temp = "{id : " + n.getId() + "," + "attribute : (" ;
			for(String att : this.attributs){
				temp += att+" : "+ n.getAttribute(att) +",";
			}
			temp = (String) temp.subSequence(0, temp.length()-1);
			temp += ")";
			//System.out.println(temp);
			node += temp;
		}
		String arretes = "} ; arretes : { ";
		for (Edge e : this.graph.getEdgeSet()){
			String g = e.getNode0().toString();
			String d = e.getNode1().toString();
			arretes += "{" + g + "," + d + "},";
		}
		arretes = (String) arretes.subSequence(0, arretes.length()-1);
		return node+arretes+" }";
		// nodes : { node1: [a1:a2:a3], node2 : [a5:a3:a0]}mas.abstractAgent)
	}
	
	/* ====================================================
	 *              HASH PART
	 ==================================================== */
	
	public HashMap<String, Object> getAttributeHashMap(Node n){
		HashMap<String, Object> res = new HashMap<String, Object>();
			for (String at : n.getAttributeKeySet()){
				res.put(at, n.getAttribute(at));
			}
		return res;
	}
	
	public HashMap<String, Object> toHashMap(){
		HashMap<String, Object> res = new HashMap<String, Object>();
		res.put("nodes", this.graph.getNodeSet());
		res.put("edges", this.graph.getEdgeSet());
		res.put("border", this.bordure);
		Collection<Edge> e = this.graph.getEdgeSet();
		return res;
	}
	
	public MyGraph (abstractAgent myagent, HashMap<String, Object> map){
		Collection<Node>  nodes = (Collection<Node>) map.get("nodes");
		Collection<Edge> edges = (Collection<Edge>) map.get("edges");
		HashSet<String> border = (HashSet<String>) map.get("border");
		this.graph = new SingleGraph("bla");
		Node addedNode;
		HashMap<String, Object> attMap;
		//add Node
		for (Node n : nodes){
			this.graph.addNode(n.getId());
			addedNode = this.graph.getNode(n.getId());
			attMap = getAttributeHashMap(n);
			addedNode.addAttributes(attMap);
		}
		for (Edge e : edges){
			Node p0 = e.getNode0();
			Node p1 = e.getNode1();
			String liaison = p0.toString() + "-" + p1.toString();
			this.graph.addEdge(liaison, p0, p1);
		}
		this.myAgent = ((mas.abstractAgent) myagent);
		this.attributs = new ArrayList<String>(Arrays.asList("explored","value1","value0","tresortype1","tresortype2")) ;
		this.bordure = border;
		
	}
	
	public void merge(MyGraph g){
		//modifie sur place
		Graphs.mergeIn(this.graph, g.getGraphStream());
		this.bordure.retainAll(g.getBordure());
	}
	
	public Graph getGraphStream(){
		return this.graph;
	}
	/* ============================================================ */



}