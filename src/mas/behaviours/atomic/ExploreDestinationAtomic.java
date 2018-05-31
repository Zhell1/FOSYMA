package mas.behaviours.atomic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import env.Attribute;
import env.Couple;
import mas.abstractAgent;
import mas.tools.MyGraph;

public class ExploreDestinationAtomic extends AtomicBehaviour {

	public ExploreDestinationAtomic(abstractAgent a) {
		super(a);
		/* signal 1  = chemin trouvé
		 * signal 2  = chemin non trouvé
		 * signal -1 = fin de l'exploration
		 */
	}
	
	public void action() {
     	//this.agent.print("ExploreDestinationAtomic");
		
		MyGraph g = this.agent.getmyGraph();
		
		//test si on à déjà exploré tout le graphe
		if(g.getBordure().size() == 0) {
			// dans ce cas on move random
			List<Couple<String,List<Attribute>>> lobs= this.agent.observe();//myPosition
			Random r = new Random();
			String nextMove = lobs.get(r.nextInt(lobs.size()-1)+1).getLeft();
			this.agent.print("FINI, I MOVE RANDOM");
			ArrayList<String> arrl = new ArrayList<String>();
			arrl.add(nextMove);
			this.agent.setPath(arrl);
			this.signal = 1;
			return;
		}
		
		boolean b_getswitchpath = this.agent.getSwitchPath();
		ArrayList<String> p = this.agent.getPath();
		
		if (p == null){ //on recalcule le chemin
			ArrayList<String> path = g.NextDijsktra(); //puisque le path était null on cherche dans la bordure
			//this.agent.print("Path : " + path);
			//fin de l'exploration
			if (path == null) {
				this.agent.print("ExploreDestinationAtomic: fin de l'exploration 1");
				//this.agent.print("BORDURE CONSISTANCE :" + g.bordureConsistance());
				this.signal = -1;
				return;
			}
			this.agent.setPath(path);
			this.agent.setSwitchPath(false);
			this.signal = 1;
			return;
		}
		//else
		
		//if (b_getswitchpath || p.isEmpty()) {
		if (p.isEmpty()) {
			MyGraph g1 = this.agent.getmyGraph(); //pourquoi pas réutiliser g ??
			ArrayList<String> path = g1.NextDijsktra(); //puisque le path était vide on cherche dans la bordure
			//this.agent.print("Path : " + path);
			//fin de l'exploration
			if (path == null) {
				this.agent.print("ExploreDestinationAtomic: fin de l'exploration 2");
				//this.agent.print("BORDURE CONSISTANCE :" + g1.bordureConsistance());
				this.signal = -1;
				return;
			}
			this.agent.setPath(path);
			this.agent.setSwitchPath(false);
			this.signal = 1;
			return;
		}
		//else
		this.signal = 1;
		return;
	}
}
