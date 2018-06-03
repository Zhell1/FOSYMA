package mas.behaviours.newBehaviour;

import java.util.List;

import env.Attribute;
import env.Couple;
import mas.abstractAgent;
import mas.behaviours.atomic.AtomicBehaviour;

public class SiloMoveAtomic extends AtomicBehaviour  {

	public SiloMoveAtomic(abstractAgent agent) {
		super(agent);

	}
	
	public void action() {
		/*
		 * regare si il y a un trésor sur la case actuelle
		 * si oui on regarde si on connait une case à coté sans trésor
		 * si oui on s'y déplace
		 */
		List<Couple<String,List<Attribute>>> lobs= this.agent.observe(); //myPosition
		Couple<String, List<Attribute>> currnode = lobs.get(0); //curent node
		List<Attribute> curratt = currnode.getRight();
		for(Attribute a : curratt) {
			if(a.getName().equals("Treasure") || a.getName().equals("Diamonds")){
				if((int)a.getValue() > 0){
					agent.print("treasure on my case: "+a);
					//TODO parcours les noeud autour, recup nom, cherche si ils existe dans graphe
					//si oui regarde si pas de trésor dessus
					// si oui on fait un move
				}
			}
		}
		
	}
}
