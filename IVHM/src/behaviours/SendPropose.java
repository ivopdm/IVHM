package behaviours;

import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

public class SendPropose extends OneShotBehaviour {

	
	/**
	 * Behaviour do agente AircraftAgent responsavel
	 * pela acao de enviar uma proposta para a
	 * instancia do agente TasAgent.
	 */
	private static final long serialVersionUID = 3591700095749070623L;	
	private final Logger logger = Logger.getMyLogger(getClass().getName());
	
	@Override
	public void action() {
		DataStore v_ds = getDataStore();
		Double m_price = (Double) v_ds.get(myAgent.getLocalName() + "_PRICE");
		ACLMessage v_cfp = (ACLMessage) v_ds.get("CFP");
		
		ACLMessage v_aclPropose = v_cfp.createReply();
		v_aclPropose.setPerformative(ACLMessage.PROPOSE);
		v_aclPropose.setContent(m_price.toString());
				
		myAgent.send(v_aclPropose);		
		
		logger.info(myAgent.getLocalName() + " proposes -> " + m_price);
	}

}
