package mas.behaviours.newBehaviour;

import mas.abstractAgent;
import mas.behaviours.atomic.EndAtomic;
import mas.behaviours.atomic.SendAtomic;
import mas.behaviours.atomic.SendMapAtomic;
import mas.behaviours.atomic.WaitForAtomic;
import mas.behaviours.atomic.WaitForStringAtomic;

public class ShareMapBehaviour extends GraphAgentBehaviour {
	/* signal -1 : pas de réponse au roger
	 * signal -2 : j'ai reçu une carte, mais l'autre n'a pas répondu quand je lui ai envoyé ma carte
	 * signal 1  : Succès
	 */

	public ShareMapBehaviour(abstractAgent agent) {
		/*
		 *  sendMap n'envoie pas la même carte, que la carte courante....
		 */
		super(agent);
		
		//on récupère l'idconv = dernière personne à nous avoir envoyé un ping
		String destname = this.a.getlastPing(); //receive with idconv = dest
		this.a.print("starting PRIVATE sharemap with " +destname);
	
		
		String myname = this.a.getLocalName(); // send with myname
		
		
		// TODO Auto-generated constructor stub
		registerFirstState(new SendAtomic(a, "roger", destname, myname), "SendRoger");
		registerState(new WaitForAtomic(a, "HashMap", 5, destname), "WaitMap"); // attends jusqu'à 5 secondes la carte
		registerState(new SendMapAtomic(a, destname, myname), "SendMap");
		registerState(new SendAtomic(a,"roger", destname, myname), "SendRoger");
		registerState(new SendAtomic(a, "ack", destname, myname), "SendAck");
		registerState(new WaitForStringAtomic(a, "ack", 5, destname), "WaitAck"); //wait up to 5 sec
		registerState(new WaitForStringAtomic(a, "roger",5, destname), "WaitRoger"); //wait up to 5 sec
		registerLastState(new EndAtomic(a, this, -1), "Fail1");
		registerLastState(new EndAtomic(a, this, -2), "Fail2");
		registerLastState(new EndAtomic(a, this, 1), "Success");
	
		// ******************* transitions ***********************
		
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
		//this.a.print("état courant : " + this.currentName);
	}

}