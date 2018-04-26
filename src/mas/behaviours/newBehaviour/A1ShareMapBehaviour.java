package mas.behaviours.newBehaviour;

import mas.abstractAgent;
import mas.behaviours.atomic.EndAtomic;
import mas.behaviours.atomic.SendAtomic;
import mas.behaviours.atomic.WaitForAtomic;
import mas.behaviours.atomic.WaitForStringAtomic;

public class A1ShareMapBehaviour extends GraphAgentBehaviour {
	/* signal -1 : j'ai envoyé ma carte mais pas de retour
	 * signal 1 : succes
	 */

	public A1ShareMapBehaviour(abstractAgent agent) {
		super(agent);
		// TODO Auto-generated constructor stub
		registerFirstState(new SendAtomic(a,g.toHashMap()), "SendMap");
		registerState(new WaitForAtomic(a, "HashMap<String, Object>", 20), "WaitMap");
		registerState(new SendAtomic(a, "ack"), "SendAck");
		
		registerLastState(new EndAtomic(a, this, -1), "Fail1");
		registerLastState(new EndAtomic(a, this, 1), "Succes");
	
		
		registerTransition("SendMap", "WaitMap", 1);
		
		registerTransition("WaitMap", "Fail1", -1);
		registerTransition("WaitMap", "WaitMap", 0);
		registerTransition("WaitMap", "SendAck" , 1);
		
		registerTransition("SendAck", "Succes", 1);
		
		registerDefaultTransition("Fail1", "Fail1");
		registerDefaultTransition("Sucess", "Sucess");
	}
	

}