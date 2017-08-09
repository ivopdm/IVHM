package behaviours;

import jade.core.behaviours.DataStore;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;

/**
 * @author IMEDEIRO
 * It receives from TasAgent ACCEPT or REJECT performative
 * and updates the price in the case of ACCEPT performative match
 *
 */
public class UpdPrice extends SimpleBehaviour {
	/**
	 * Behaviour de instancia do agente AircraftAgent
	 * responsavel por atualizar preco
	 * da instancia do agente AircraftAgent
	 */
	private static final long serialVersionUID = -4438000231811264073L;
	private boolean m_finished;
	private Logger m_logger = Logger.getMyLogger(getClass().getName());
	private MessageTemplate m_op1 = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
	private MessageTemplate m_op2 = MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL);
	private MessageTemplate m_mt = MessageTemplate.or(m_op1, m_op2);
	private final double EPSON = 0.1;

	@Override
	public void action() {
		m_finished = false;
		DataStore v_ds = getDataStore();
		
		double v_price = (Double) v_ds.get(myAgent.getLocalName() +"_PRICE");
		double v_oldPrice = v_price;
		
		ACLMessage v_propResp = myAgent.receive(m_mt);


		if(v_propResp != null){
			if(v_propResp.getPerformative() == ACLMessage.ACCEPT_PROPOSAL){
				v_price = v_price + 
						Double.parseDouble(v_propResp.getContent()) +
						EPSON;
			}

			v_ds.put(myAgent.getLocalName() +"_PRICE", v_price);
			m_logger.info(myAgent.getLocalName() +" HAS PRICE UPDATED FROM => " + v_oldPrice +  " TO -> " + v_price);
			m_finished = true;

		}else{
			block();
		}

	}

	@Override
	public boolean done() {
		return m_finished;
	}

}
