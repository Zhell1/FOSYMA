package mas.behaviours.atomic;

import java.util.HashMap;

import mas.abstractAgent;
import mas.agents.GraphAgent;

public class TraiteMsgAtomic extends AtomicBehaviour {
	/* signal -1 = pas de message
	 * signal 1 = ping
	 * signal 2 = roger
	 * 
	 */

	private int cpt;

	public TraiteMsgAtomic(abstractAgent a) {
		super(a);
		this.cpt = 0;
		// TODO Auto-generated constructor stub
	}
	
	public void action() {
		HashMap<String, Object> msg = this.agent.consultLastMsg();
		//this.agent.print("TraiteMsg: "+msg);
		if (msg == null) {
			this.signal = -1;
			this.cpt = 0;
			return;
		}
		if (msg.get("type").equals("String")) {
			String msgstring = (String) msg.get("content");
			GraphAgent graphagent = ((GraphAgent)this.myAgent);
			if(msgstring.startsWith("ping silo")){
				String sender = (String)msg.get("sender");
				graphagent.setlastPing(sender);
				String[] res = msgstring.split(":");
				this.agent.getmyGraph().setSiloPosition(res[1]);
				//graphagent.print("*** setlastping : "+sender+" ***");
				this.signal = 1;
				return; 
			}
			else if (msgstring.equals("ping")){
				String sender = (String)msg.get("sender");
				graphagent.setlastPing(sender);
				//graphagent.print("*** setlastping : "+sender+" ***");
				this.signal = 1;
				return;
			}			
			if (cpt <= 5){ //traite X messages
				this.signal = 0;
				cpt ++;
				return;
			}
			this.signal = -1;
			this.cpt = 0;
			return;
		}
	}

}
