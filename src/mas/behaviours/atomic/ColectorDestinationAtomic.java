package mas.behaviours.atomic;

import java.util.ArrayList;

import mas.abstractAgent;
import mas.tools.HeuristiqueDistance;
import mas.tools.MyGraph;

public class ColectorDestinationAtomic extends AtomicBehaviour {
	/* signal -1 = pas de chemin vers un tresors
	 * signal 1 = chemin vers un tresors 
	 * signal 2 = on continue de suivre le chemin en cours
	 */

	private MyGraph g;
	private HeuristiqueDistance f;

	public ColectorDestinationAtomic(abstractAgent a) {
		super(a);
		MyGraph g = this.agent.getmyGraph();
		this.g = g;
	}
	
	public void action() {
		boolean b1 = this.agent.getPath().isEmpty();
		boolean b2 = this.agent.getSwitchPath();
		if (b1 || b2) {
			ArrayList<String> path = this.g.getBestTreasurePath();
			if (path == null) {
				//on cacule le chemin d'une autre manière
				this.signal = -1;
				return;
			}
			this.agent.setPath(path);
			this.signal = 1;
			return;
		}
		//on continue de suivre le chemin en cours
		this.signal = 2;
	}

}
