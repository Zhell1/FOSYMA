package mas.behaviours.atomic;

import mas.abstractAgent;

public class MoveAtomic extends AtomicBehaviour{
	/*  Deplace l'agent sur la case suivante du path
	 *  signal -1 = erreur (path vide)
	 *  signal 1 = succes
	 *  signal 2 = bloquage
	 */
	
	/* TODO
	 * BUG pour un path de taille  1, car la liste de deplacement dans path est inversé ...
	 */

	public MoveAtomic(abstractAgent a) {
		super(a);
	}
	
	public void action() {
		String move = this.agent.getNextPath();
		// System.out.println("Move : " + move);
		if (move == null) {
			this.signal = -1;
			return;
		}
		this.agent.move(move);
		if (this.agent.getSuccesLastMove()) {
			// System.out.println("Successful move");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.signal = 1;
		}
		
		else {
			this.agent.putMovePath(move);
			this.signal = 2;
		}
		
	}

}
