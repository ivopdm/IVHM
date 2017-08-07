package behaviours;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import agents.TasAgent;
import commons.Flight;
import jade.core.AID;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class SendCfp extends OneShotBehaviour {

	/**
	 * Envia mensagem do tipo CFP para 
	 * instancias de AircraftAgent.
	 * Conteudo de a mensagem CFP e a ID do voo que precisa de um aviao
	 */
	private static final long serialVersionUID = 3903080487616288791L;
	private HashMap<Flight,String> m_assignment;
	
	private List<AID> m_recList;
	
	private final Logger m_logger = Logger.getLogger(getClass().getName()); 

	public SendCfp(HashMap<Flight,String> p_assignment, List<AID> p_recList) {
		this.m_assignment = p_assignment;
		this.m_recList = p_recList;
	}

	@Override
	public void action() {
		ACLMessage v_cfp = new ACLMessage(ACLMessage.CFP);
		DataStore v_ds;
		v_ds = getDataStore();
		
		//Set receiver for CFP
		for (AID aid : m_recList) {
			v_cfp.addReceiver(aid);
			m_logger.log(Level.INFO, "Receiver: {0}", aid.getLocalName() );
		}

		for (Map.Entry<Flight, String> v_unAssign : m_assignment.entrySet()) {
			if(v_unAssign.getValue().equals(TasAgent.FLIGHT_UNASSIGNED)){
				v_cfp.setContent(v_unAssign.getKey().getM_FlightID());				
				myAgent.send(v_cfp);
				v_ds.put(TasAgent.KEY_CURRENT_UNASSIGNED, v_unAssign.getKey());
				m_logger.log(Level.INFO, "Unassigned FLIGHT -> {0}", 
						v_unAssign.getKey().getM_FlightID());
				break;
			}
		}
	}

}
