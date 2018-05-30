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
		//this.agent.print("MoveAtomic");

		this.agent.print("MoveAtomic original path:"+this.agent.getPath());
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
		else { //the move failed = there is an agent on this case or a problem
			//this.agent.print("before putMovePath:"+this.agent.getPath());
			//this.agent.putMovePath(move);
			//this.agent.print("after putMovePath:"+this.agent.getPath());
			this.agent.print("move failed, reset path");
			this.agent.resetPath();
			//
			this.signal = 2;
		}
		
	}

}
