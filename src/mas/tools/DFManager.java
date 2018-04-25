package mas.tools;

import java.util.ArrayList;
import java.util.List;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class DFManager {
	public static void register(Agent agt, String type){
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(agt.getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType(type);
		sd.setName(agt.getLocalName());
		dfd.addServices(sd);
		try {
			DFService.register(agt, dfd);
		} catch(FIPAException fe) {fe.printStackTrace();}
	}
	
	public static ArrayList<String> getAllAgents(Agent agt){
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		dfd.addServices(sd);
		ArrayList<String> res = new ArrayList();
		try {
			DFAgentDescription[] results = DFService.search(agt, dfd);
			for (DFAgentDescription d : results){
				res.add(d.getName().getLocalName());
			}
			res.remove(agt.getLocalName()); // se retire lui-même
			return res;
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
