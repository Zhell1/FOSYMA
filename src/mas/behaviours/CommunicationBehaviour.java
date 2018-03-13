package mas.behaviours;

import java.io.Serializable;
import java.util.HashMap;

import mas.abstractAgent;
import mas.agents.FirstAgent;
import mas.tools.Messages;
import mas.tools.MyGraph;
import jade.core.behaviours.SimpleBehaviour;

public class CommunicationBehaviour extends SimpleBehaviour {

	private boolean finished;
	private String idCommunication;
	private Messages mailbox;
	private boolean mapsent;
	private boolean mapreceived;
	private MyGraph graph;
	private boolean ackreceived;
	private int timeout;
	private int cpt;
	private String sender;
	
	

	public CommunicationBehaviour(final mas.abstractAgent myagent, String sender ){
		super(myagent);
		this.finished = false;
		if((this.myAgent.getLocalName()).compareTo(sender) > 0){
			this.idCommunication = this.myAgent.getLocalName() + "_" + sender;
		}
		else {
			this.idCommunication = sender + "_" + this.myAgent.getLocalName();
		}
		this.mailbox = new Messages(this.myAgent);
		this.mapsent = false;
		this.mapreceived = false;
		this.ackreceived = false;
		this.timeout = 10;
		this.cpt = 0;
		this.graph = ((FirstAgent)myagent).getmyGraph();
		this.sender = sender;
		
	}
	
	@Override
	public void action() {
		this.cpt++;
		if (this.cpt > this.timeout){
			this.finished = true;
		}
		else if (this.mapreceived && this.ackreceived && this.mapsent){
			this.finished = true;
		}
		else if (!(this.mapsent)){
			HashMap<String, Object> send = this.graph.toHashMap();
			print("sending my map" + send);
			this.mailbox.sendObject((Serializable)(send),this.sender, this.idCommunication);
			this.mapsent = true;
		}
		else {
			Object msgObject = this.mailbox.getMsgObject(this.idCommunication);
			if (msgObject != null){
				if (msgObject instanceof HashMap){
					//merger todo
					print("receiving a map");
					print("bordure before merge : " + this.graph.getBordure());
					HashMap<String, Object> received = (HashMap<String, Object>)msgObject;
					MyGraph newMap = new mas.tools.MyGraph((abstractAgent) this.myAgent, received);
					this.graph.merge(newMap);
					print("new bordure : " + this.graph.getBordure());
					this.mapreceived = true;
					this.mailbox.sendString("ack", this.sender, this.idCommunication);
				}
				else if (msgObject instanceof String){
					String msg = (String)msgObject;
					if (msg.equals("ack")){
						print("i receive an ack");
						this.ackreceived = true;
						((FirstAgent)this.myAgent).updateStep(this.sender);
					}
				}
			}
			else {
				//Pour eviter de boucler comme une brute
				block(100);
			}
		}
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return this.finished;
	}
	
	private void print(String m){
		System.out.println(this.myAgent.getLocalName()+" : "+m);
	}

}
