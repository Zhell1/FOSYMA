package mas.behaviours.newBehaviour;

import mas.abstractAgent;
import mas.behaviours.atomic.EndAtomic;
import mas.behaviours.atomic.SendAtomic;
import mas.behaviours.atomic.SendMapAtomic;
import mas.behaviours.atomic.WaitForAtomic;
import mas.behaviours.atomic.WaitForStringAtomic;

public class ShareMapBehaviour extends GraphAgentBehaviour {
	/* signal -1 : pas de réponse au roger
	 * signal -2 : j'ai reçut une carte , mais l'autre n'a pas répondu quand je lui envoi ma carte
	 * signal 1 : Succes
	 */

	public ShareMapBehaviour(abstractAgent agent) {
		/*
		 *  sendMap n'envois pas la meme carte, que la carte courrante....
		 */
		super(agent);
		// TODO Auto-generated constructor stub
		registerFirstState(new SendAtomic(a, "roger"), "SendRoger");
		registerState(new WaitForAtomic(a, "HashMap", 5), "WaitMap");
		registerState(new SendMapAtomic(a), "SendMap");
		registerState(new SendAtomic(a,"roger"), "SendRoger");
		registerState(new SendAtomic(a, "ack"), "SendAck");
		registerState(new WaitForStringAtomic(a, "ack", 5), "WaitAck");
		registerState(new WaitForStringAtomic(a, "roger",5), "WaitRoger");
		registerLastState(new EndAtomic(a, this, -1), "Fail1");
		registerLastState(new EndAtomic(a, this, -2), "Fail2");
		registerLastState(new EndAtomic(a, this, 1), "Success");
		
	
		
		registerTransition("SendRoger", "WaitRoger", 1);
		
		registerTransition("WaitRoger", "Fail1", -1);
		registerTransition("WaitRoger", "WaitRoger", 0);
		registerTransition("WaitRoger", "SendMap" , 1);
		
		registerTransition("SendMap", "WaitMap", 1);
		
		registerTransition("WaitMap", "Fail1", -1);
		registerTransition("WaitMap", "WaitMap", 0);
		registerTransition("WaitMap", "SendAck" , 1);
		
		registerTransition("SendAck", "WaitAck", 1);
		
		registerTransition("WaitAck", "Fail2", -1);
		registerTransition("WaitAck", "WaitAck", 0);
		registerTransition("WaitAck", "Success", 1);
		
		registerDefaultTransition("Fail1", "Fail1");
		registerDefaultTransition("Fail2", "Fail2");
		registerDefaultTransition("Success", "Success");
	
	}
	
	public int onEnd() {
		this.reset();
		return this.signal;
	}
	
	
	public void reset() {
		//this.a.print("reset");
		this.forceTransitionTo("SendRoger");
		//this.a.print("etat courrant : " + this.currentName);
	}

}