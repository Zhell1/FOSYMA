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
		sendObject(o, destinataire, "broadcast");
	}
	
	public void sendObject(Object o, String destinataire, String idconv){
		ACLMessage msg = new ACLMessage();
		msg.addReceiver(new AID(destinataire,AID.ISLOCALNAME));
		msg.setSender(this.agent.getAID());
		msg.setConversationId(idconv);
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
		return sendString(content, destinataire, "broadcast"); //channel de base = "broadcast"
	}
	
	public boolean sendString(String content, String destinataire, String idconv){
		ACLMessage msg = new ACLMessage();
		AID aid = new AID(destinataire,AID.ISLOCALNAME);
		msg.setSender(this.agent.getAID());
		msg.setConversationId(idconv);
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
		return getMsg("broadcast");
	}
	public Couple getMsg(String idconv){
		/* 
		 * res.getLeft() -> String
		 * res.getRight() -> Object 
		 * provoque une null pointeur exception quand on print car 1 des 2 elements est forcement null*/
		ACLMessage msg = this.agt.receive(MessageTemplate.MatchConversationId(idconv));
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
		return getMsgObject("broadcast");
	}
	
	public Object getMsgObject(String idconv) {
		ACLMessage msg = this.agt.receive(MessageTemplate.MatchConversationId(idconv));
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
		return getMsgString("broadcast");
	}
	
	public String getMsgString(String idconv){
		ACLMessage msg = this.agt.receive(MessageTemplate.MatchConversationId(idconv));
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
	
	public Couple getMsgStringAndSender(){
		return getMsgStringAndSender("broadcast");
	}
	
	public Couple getMsgStringAndSender(String idconv){
		ACLMessage msg = this.agt.receive(MessageTemplate.MatchConversationId(idconv));
		if (msg != null){
			String sender = msg.getSender().getLocalName();
			String mess = "";
			if (msg.getLanguage().equals("String")){
				mess = msg.getContent();
			}
			else {
				this.agt.sendMessage(msg);
			}
			Couple res = new Couple(mess,sender);
			return res;
		}
		return null;
	}
	
	public Couple getMsgObjectAndSender(){
		return getMsgObjectAndSender("broadcast");
	}
	
	public Couple getMsgObjectAndSender(String idconv){
		ACLMessage msg = this.agt.receive(MessageTemplate.MatchConversationId(idconv));
		String sender = msg.getSender().getLocalName();
		String mess = "";
		if (msg != null){
			if (msg.getLanguage().equals("String")){
				mess = msg.getContent();
			}
		}
		Couple res = new Couple(mess,sender);
		return res;
	}
	
	public static void print(Agent agent, String m){
		System.out.println(agent.getLocalName()+" : "+m);
	}
}
