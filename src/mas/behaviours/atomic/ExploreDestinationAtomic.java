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
		boolean b = this.agent.getSwitchPath();
		ArrayList<String> p = this.agent.getPath();
		boolean b2 = p.isEmpty();		
		if (b || b2) {
			MyGraph g = this.agent.getmyGraph();
			ArrayList<String> path = g.NextDijsktra();
			this.agent.print("Path : " + path);
			//fin de l'exploratio
			if (path == null) {
				this.signal = -1;
				return;
			}
			this.agent.setPath(path);
			this.agent.setSwitchPath(false);
			this.signal = 1;
		}
		else {
			this.signal = 1;
		}
	}

}
