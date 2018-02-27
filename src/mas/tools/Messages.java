package mas.tools;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import mas.abstractAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class Messages {
	private abstractAgent agt;
	private Agent agent;
	
	public Messages(Agent a){
		this.agent = a;
		this.agt = (abstractAgent)(a);
	}
	
	public void sendObject(Object o, String destinataire){
		ACLMessage msg = new ACLMessage();
		msg.addReceiver(new AID(destinataire,AID.ISLOCALNAME));
		try {
			msg.setContentObject((Serializable) o);
			//agt.send(msg);
			this.agt.sendMessage(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean sendString(String content, String destinataire){
		ACLMessage msg = new ACLMessage();
		AID aid = new AID(destinataire,AID.ISLOCALNAME);
		System.out.println("AID : " + aid);
		msg.addReceiver(new AID(destinataire,AID.ISLOCALNAME));
		msg.setContent(content);
		System.out.println("Message sendString : " + msg);
		try{
			this.agt.sendMessage(msg);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void broadcastString(String content, ArrayList<String> listAgents){
		for (String agtname: listAgents) {
			 sendString(content, agtname);
		}
	}	
	public void broadcastObject(Object o, ArrayList<String> listAgents){
		for (String agtname: listAgents) {
			sendObject(o, agtname);
		}
	}
	
	public Object getMsgObject() {
		ACLMessage msg = this.agt.receive();
		try {
			return msg.getContentObject();
		} catch (UnreadableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public String getMsgString(){
		ACLMessage msg = this.agt.receive();
		return msg.getContent();
	}
}
