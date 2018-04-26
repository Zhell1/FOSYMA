package mas.behaviours.newBehaviour;

import mas.abstractAgent;
import mas.behaviours.atomic.EndAtomic;
import mas.behaviours.atomic.SendAtomic;
import mas.behaviours.atomic.WaitForAtomic;
import mas.behaviours.atomic.WaitForStringAtomic;

public class A2ShareMapBehaviour extends GraphAgentBehaviour {
	/* signal -1 : pas de réponse au roger
	 * signal -2 : j'ai reçut une carte , mais l'autre n'a pas répondu quand je lui envoi ma carte
	 * signal 1 : Succes
	 */

	public A2ShareMapBehaviour(abstractAgent agent) {
		super(agent);
		// TODO Auto-generated constructor stub
		registerFirstState(new SendAtomic(a, "roger"), "SendRoger");
		registerState(new WaitForAtomic(a, "HashMap<String, Object>", 20), "WaitMap");
		registerState(new SendAtomic(a,g.toHashMap()), "SendMap");
		registerState(new WaitForStringAtomic(a, "ack", 20), "WaitAck");
		registerLastState(new EndAtomic(a, this, -1), "Fail1");
		registerLastState(new EndAtomic(a, this, -2), "Fail2");
		registerLastState(new EndAtomic(a, this, 1), "Succes");
		
	
		
		registerTransition("SendRoger", "WaitMap", 1);
		
		registerTransition("WaitMap", "Fail1", -1);
		registerTransition("WaitMap", "WaitMap", 0);
		registerTransition("WaitMap", "SendMap" , 1);
		
		registerTransition("SendMap", "WaitAck", 1);
		
		registerTransition("WaitAck", "Fail2", -1);
		registerTransition("WaitAck", "WaitAck", 0);
		registerTransition("WaitAck", "Succes", 1);
		
		registerDefaultTransition("Fail1", "Fail1");
		registerDefaultTransition("Fail2", "Fail2");
		registerDefaultTransition("Success", "Sucess");

		
	}

}
