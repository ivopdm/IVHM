package behaviours;

import java.io.IOException;
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
import util.CalcFlight;

import java.util.ArrayList;

public class SendCfp extends OneShotBehaviour {

	/**
	 * Envia mensagem do tipo CFP para 
	 * instancias de AircraftAgent.
	 * Conteudo de a mensagem CFP e a ID do voo que precisa de um aviao
	 */
	private static final long serialVersionUID = 3903080487616288791L;
	private HashMap<Flight,String> m_assignment;

	private List<AID> m_recList;
	private List<Flight> m_unassignedList = new ArrayList<Flight>();
	private final Logger m_logger = Logger.getLogger(getClass().getName()); 
	private ACLMessage m_cfp = new ACLMessage(ACLMessage.CFP);

	public SendCfp(HashMap<Flight,String> p_assignment, List<AID> p_recList) {
		this.m_assignment = p_assignment;
		this.m_recList = p_recList;
		m_cfp.clearAllReceiver();
		//Set receiver for CFP
		for (AID aid : m_recList) {
			m_cfp.addReceiver(aid);
			m_logger.log(Level.INFO, "Receiver: {0}", aid.getLocalName() );
		}
	}

	@Override
	public void action() {
		
		DataStore v_ds;
		v_ds = getDataStore();
		
		
		fillUnAssList();


		try {
			Flight v_flight = m_unassignedList.remove(0);
			m_cfp.setContentObject(v_flight);
			myAgent.send(m_cfp);

//			v_ds.put(TasAgent.KEY_CURRENT_UNASSIGNED, v_flight);

			m_logger.log(Level.INFO, "Unassigned FLIGHT -> {0}", 
					v_flight.getM_FlightID());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void fillUnAssList() {
		if(m_unassignedList.isEmpty()){
			for (Map.Entry<Flight, String> v_unAssign : m_assignment.entrySet()) {

				if(v_unAssign.getValue().equals(TasAgent.FLIGHT_UNASSIGNED)){
					m_unassignedList.add(v_unAssign.getKey());
				}
			}
			
			CalcFlight.ordenaPorData(m_unassignedList);
		}

	}

}
