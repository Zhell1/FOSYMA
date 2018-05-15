package mas.behaviours;

import java.util.ArrayList;

import mas.agents.lucas;
import jade.core.ContainerID;
import jade.core.behaviours.OneShotBehaviour;

public class startMovingAgent extends OneShotBehaviour {
	
	private lucas a;

	public startMovingAgent(lucas a){
		this.a =a;
	}

	@Override
	public void action() {
		//this.a.start();
//		  try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		ArrayList<ContainerID> L = this.a.getL();
//		int i = this.a.geti();
//		
//		i++;
//		this.a.seti(i);
//		int index = (i) % L.size();
//		System.out.println("Roger : Index : " + index);
//		
//		this.a.doMove(L.get(index));
		this.a.start();
		
	}

}
