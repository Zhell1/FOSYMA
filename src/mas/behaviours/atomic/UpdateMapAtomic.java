package mas.behaviours.atomic;

import java.util.HashMap;

import mas.abstractAgent;
import mas.tools.MyGraph;

public class UpdateMapAtomic extends AtomicBehaviour {
	/* BUG : la bordure n'est pas mis correctement à jour
	 * les sommets ne sont pas enregistrer
	 * 
	 */

	public UpdateMapAtomic(abstractAgent a) {
		super(a);
		// TODO Auto-generated constructor stub
	}
	
	public void action() {
		HashMap<String, Object> msg = this.agent.consultLastMap();
		if (msg == null) {
			this.signal = -1;
			return;
		}
		if (!msg.get("type").equals("HashMap")) {
			//l'objet n'est pas une carte
			this.agent.print("Erreur l'object n'est pas une carte !");
			this.signal = -1;
			return;
		}
		HashMap<String, Object> map = (HashMap<String, Object>)msg.get("content");
		MyGraph newMap = new mas.tools.MyGraph((abstractAgent) this.myAgent, map);
		MyGraph g = this.agent.getmyGraph();
		//this.agent.print("My map :" + g.toHashMap());
		g.merge(newMap);
		//this.agent.print("After merge :" + g.toHashMap());
		this.agent.print("The map have been updated");
		this.agent.setSwitchPath(true);
		this.signal = 1;
		return;
	}

}
