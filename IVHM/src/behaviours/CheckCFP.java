package behaviours;

import commons.Flight;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.util.Logger;

public class CheckCFP extends SimpleBehaviour {
	/**
	 * Classe representa behaviour/acao do agente AircraftAgent(aviao)
	 * que e responsavel por recebe uma mensagem do tipo CFP (Call for Proposal)
	 * e depois guardar no DataStore do behaviour
	 */
	private static final long serialVersionUID = -789907945940792560L;
	private MessageTemplate m_mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
	private final Logger m_logger = Logger.getMyLogger(getClass().getName());
	private boolean m_finished;
	
	@Override
	public void action() {
		m_finished = false;
		
		ACLMessage v_cfp = myAgent.receive(m_mt);
		
		if(v_cfp != null){
			m_finished = true;
			
			try {
				Flight v_cfp_content = (Flight) v_cfp.getContentObject();
				m_logger.info(myAgent.getLocalName() + " received content -> " + v_cfp_content.getM_FlightID());
			} catch (UnreadableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
				
			DataStore v_ds = getDataStore();
			v_ds.put("CFP", v_cfp);			
		}else{
			block();
		}		
	}


	@Override
	public boolean done() {
		return m_finished;
	}
	
	

}
