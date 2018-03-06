package mas.tools;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import env.Couple;
import mas.abstractAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
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
		msg.setSender(this.agent.getAID());
		msg.setLanguage("Object");
		try {
			if (o != null){
				msg.setContentObject((Serializable) o);
				//System.out.println("Objet envoyé : " + o.toString());
				this.agt.sendMessage(msg);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean sendString(String content, String destinataire){
		ACLMessage msg = new ACLMessage();
		AID aid = new AID(destinataire,AID.ISLOCALNAME);
		msg.setSender(this.agent.getAID());
		//System.out.println("AID : " + aid);
		msg.addReceiver(new AID(destinataire,AID.ISLOCALNAME));
		msg.setContent(content);
		msg.setLanguage("String");
		//System.out.println("Message sendString : " + msg);
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
		
	public Couple getMsg(){
		/* 
		 * res.getLeft() -> String
		 * res.getRight() -> Object 
		 * provoque une null pointeur exception quand on print car 1 des 2 elements est forcement null*/
		ACLMessage msg = this.agt.receive();
		String c1 = null;
		Object c2 = null;
		if (msg != null){
			if (msg.getLanguage().equals("String")){
				c1 =  getMsgString();
			}
			if (msg.getLanguage().equals("Object")){
				c2 = getMsgObject();
			}
		}
		Couple res = new Couple(c1,c2);
		return res;
	}
	
	public Object getMsgObject() {
		ACLMessage msg = this.agt.receive();
		if (msg != null){
			 try {
				 if (msg.getLanguage().equals("Object")){
					return msg.getContentObject();
				 }
				 else {
					 this.agt.sendMessage(msg);
				 }
			} catch (UnreadableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			return null;
	}
	
	public String getMsgString(){
		MessageTemplate mt = MessageTemplate.MatchAll();
		ACLMessage msg = this.agt.receive(mt);
		if (msg != null){
			if (msg.getLanguage().equals("String")){
				return msg.getContent();
			}
			else {
				this.agt.sendMessage(msg);
			}
		}
		return null;
	}
}
