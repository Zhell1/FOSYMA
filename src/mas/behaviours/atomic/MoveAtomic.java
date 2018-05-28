package mas.behaviours.atomic;

import mas.abstractAgent;

public class MoveAtomic extends AtomicBehaviour{
	/*  Déplace l'agent sur la case suivante du path
	 *  signal -1 = erreur (path vide)
	 *  signal 1  = succès
	 *  signal 2  = bloquage
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
			this.signal = 1;
		}
		
		else {
			this.agent.putMovePath(move);
			this.signal = 2;
		}
		
	}

}
