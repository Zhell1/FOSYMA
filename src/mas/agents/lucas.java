package mas.agents;

import java.util.ArrayList;

import jade.core.ContainerID;
import mas.abstractAgent;
import mas.behaviours.startMovingAgent;

public class lucas extends abstractAgent {
	private static final long serialVersionUID = -5686331366676803589L;
	private ArrayList<ContainerID> L;
	int i;
	int cpt;
	int limit;
	
	protected void setup(){//Automatically called at agent’s creation
		super.setup();
		L = new ArrayList<ContainerID>();
		ContainerID cID1= new ContainerID();
		cID1.setName("08-RaoulPlatform");
		cID1.setPort("8888");
		cID1.setAddress("132.227.113.200"); //IP of the host of the targeted container
		L.add(cID1);
		
		//this.doMove(cID1);
		
		
//		ContainerID cID2 = new ContainerID();;
//		cID2.setName("NOM_COURT");
//		cID2.setPort("8888");
//		cID2.setAddress("132.227.113.195"); //IP of the host of the targeted container
//		this.L.add(cID2);
		
		ContainerID cID2 = new ContainerID();;
		cID2.setName("MyDistantContainer0");
		cID2.setPort("8888");
		cID2.setAddress("132.227.113.205"); //IP of the host of the targeted container
		L.add(cID2);
	
		
//		ContainerID cID3 = new ContainerID();
//		cID3 .setName("SimonContainer");
//		cID3.setPort("8888");
//		cID3.setAddress("132.227.113.196"); //IP of the host of the targeted container
//		this.L.add(cID3);
		this.i = 0;
		this.cpt = 0;
		this.limit = 100;
		//this.L = L;
		
		this.addBehaviour(new startMovingAgent(this));
		
	}
	
	public void start(){
		this.i++;
		this.cpt++;
		
		if (this.cpt > this.limit) this.doDelete();
		System.out.println(" i : " + i);
		
		int index = (this.i) % this.L.size();
		System.out.println("Roger : Index : " + index);
		
		this.doMove(this.L.get(index));
	
	}
	
	public ArrayList<ContainerID> getL(){
		return this.L;
	}
	public int geti(){
		return this.i;
	}
	public void seti(int i2){
		this.i = i2;
	}
	
	protected void beforeMove(){//Automatically called before doMove()
		super.beforeMove();
		System.out.println("Roger : I migrate");
		
	}	
	
	protected void afterMove(){//Automatically called after doMove()
		super.afterMove();
		System.out.println("Roger : I migrated");
		this.addBehaviour(new startMovingAgent(this));

	}

}