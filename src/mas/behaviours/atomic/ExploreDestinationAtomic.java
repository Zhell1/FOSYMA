package mas.behaviours.atomic;

import java.util.ArrayList;

import mas.abstractAgent;
import mas.tools.MyGraph;

public class ExploreDestinationAtomic extends AtomicBehaviour {

	public ExploreDestinationAtomic(abstractAgent a) {
		super(a);
		/* signal 1 = chemin trouvé
		 * signal 2 = chemin non trouvé
		 */
	}
	
	public void action() {
		this.agent.print("ExploreDestination");
		MyGraph g = this.agent.getmyGraph();
		boolean b_getswitchpath = this.agent.getSwitchPath();
		ArrayList<String> p = this.agent.getPath();
		
		if (p == null){ //on recalcule le chemin
			ArrayList<String> path = g.NextDijsktra();
			this.agent.print("Path : " + path);
			//fin de l'exploration
			if (path == null) {
				this.agent.print("BORDURE CONSISTANCE :" + g.bordureConsistance());
				this.signal = -1;
				return;
			}
			this.agent.setPath(path);
			this.agent.setSwitchPath(false);
			this.signal = 1;
			return;
		}
		//else
		boolean b_pathempty = p.isEmpty();
		
		if (b_getswitchpath || b_pathempty) {
			MyGraph g1 = this.agent.getmyGraph(); //pourquoi pas réutiliser g ??
			ArrayList<String> path = g1.NextDijsktra();
			this.agent.print("Path : " + path);
			//fin de l'exploration
			if (path == null) {
				this.agent.print("BORDURE CONSISTANCE :" + g1.bordureConsistance());
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
