package mas.behaviours.atomic;

import java.util.HashMap;

import jade.core.AID;
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
		MyGraph g = this.agent.getmyGraph();

		this.agent.print("My map :" + g.toHashMap());
		this.agent.print("I receive :" + map);
		g.merge(map);
		this.agent.print("After merge :" + g.toHashMap());
		this.agent.print("The map has been updated");
		String s = (String) msg.get("sender");
		if (s.equals("SiloAgent")) {
			String pos = (String) map.get("position");
			this.agent.print("Silo found on position :" + pos);
			g.setSiloPosition(pos);
		}
		this.agent.setSwitchPath(true);
		//this.agent.print("sender :" + s);
		this.signal = 1;
		return;
	}

}
