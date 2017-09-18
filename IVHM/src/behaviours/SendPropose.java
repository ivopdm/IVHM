package behaviours;

import java.io.IOException;

import commons.Proposal;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

public class SendPropose extends OneShotBehaviour {

	/**
	 * Behaviour do agente AircraftAgent responsavel pela acao de enviar uma
	 * proposta para a instancia do agente TasAgent.
	 */
	private static final long serialVersionUID = 3591700095749070623L;
	private final Logger m_logger = Logger.getMyLogger(getClass().getName());

	@Override
	public void action() {
		
		DataStore v_ds = getDataStore();
		
		Proposal v_prop = (Proposal) v_ds.get(myAgent.getLocalName() + "_PROPOSAL");
		ACLMessage v_cfp = (ACLMessage) v_ds.get("CFP");

		ACLMessage v_aclPropose = v_cfp.createReply();
		v_aclPropose.setPerformative(ACLMessage.PROPOSE);
		
		try {
			v_aclPropose.setContentObject(v_prop);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		myAgent.send(v_aclPropose);

		m_logger.info(myAgent.getLocalName() + " proposes -> " + v_prop.getPrice());
					
	}

}
