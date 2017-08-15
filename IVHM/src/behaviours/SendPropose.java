package behaviours;

import commons.Aircraft;
import commons.Flight;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.util.Logger;

public class SendPropose extends OneShotBehaviour {

	
	/**
	 * Behaviour do agente AircraftAgent responsavel
	 * pela acao de enviar uma proposta para a
	 * instancia do agente TasAgent.
	 */
	private static final long serialVersionUID = 3591700095749070623L;	
	private final Logger m_logger = Logger.getMyLogger(getClass().getName());
	private Aircraft m_acft;
	
	@Override
	public void action() {
		DataStore v_ds = getDataStore();
		Aircraft m_acft = (Aircraft) v_ds.get(myAgent.getLocalName());
		ACLMessage v_cfp = (ACLMessage) v_ds.get("CFP");
		
		try {
			Flight v_flt = (Flight) v_cfp.getContentObject();
			Double v_price = routePrice(v_flt);
			ACLMessage v_aclPropose = v_cfp.createReply();
			v_aclPropose.setPerformative(ACLMessage.PROPOSE);
			v_aclPropose.setContent(v_price.toString());
					
			myAgent.send(v_aclPropose);		
			
			m_logger.info(myAgent.getLocalName() + " proposes -> " + v_price);
			
		} catch (UnreadableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private Double routePrice(Flight v_flt) {
		/** TODO Checar o conjunto de voo ja atribuidos
		 * para o aviao e calcula o preco baseado nisso
		 * usar o fator de consumo do aviao x combustivel medio necessario para voo 
		 * Usar objeto Aircraft e objeto Flight
		 * Guarda no Data Store a rota e o preco proposta
		 */
		
		return null;
	}

}
