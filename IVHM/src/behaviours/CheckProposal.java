package behaviours;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import agents.TasAgent;
import commons.Flight;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;

public class CheckProposal extends SimpleBehaviour {

	/**
	 * Behaviour do agente TasAgent que e responsavel por receber propostas
	 * (Refuse e Propose) e escolher a melhor proposta
	 */
	private static final long serialVersionUID = -2239480822286081170L;

	private boolean m_finished;
	private int m_proposal_counter = 0;
	private MessageTemplate m_op1 = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
	private MessageTemplate m_op2 = MessageTemplate.MatchPerformative(ACLMessage.REFUSE);
	private MessageTemplate m_mt = MessageTemplate.or(m_op1, m_op2);
	private List<ACLMessage> m_proposeList = new ArrayList<ACLMessage>(TasAgent.ACFT_QTY);

	private ACLMessage m_winnerProp;
	private HashMap<Flight, String> m_assignment;

	private final Logger m_logger = Logger.getMyLogger(getClass().getName());

	private int m_proposalStatus;

	public CheckProposal(HashMap<Flight, String> p_assignment) {
		m_assignment = p_assignment;
	}

	@Override
	public void action() {
		m_finished = false;
		ACLMessage v_aclMsgProposal = myAgent.receive(m_mt);

		if (v_aclMsgProposal != null) {
			m_proposal_counter++;

			if (v_aclMsgProposal.getPerformative() == ACLMessage.PROPOSE) {
				m_proposeList.add(v_aclMsgProposal);
			}

		} else {
			block();
		}

		if (m_proposal_counter == TasAgent.ACFT_QTY) {
			if (m_proposeList.isEmpty()) {
				m_finished = true;
				m_proposal_counter = 0;
				
				m_proposalStatus = TasAgent.NO_PROPOSAL;
				m_logger.info("NO PROPOSAL");
				
			} else {
				m_finished = true;
				m_proposal_counter = 0;
				List<Double> v_bidList = new ArrayList<Double>();
				double v_max = Double.NEGATIVE_INFINITY;
				DataStore v_ds = getDataStore();

				Flight v_unassFlt = (Flight) v_ds.get(TasAgent.KEY_CURRENT_UNASSIGNED);
				Double v_assignmentValue = v_unassFlt.getM_flightValue();

				for (ACLMessage aclMessage : m_proposeList) {

					Double v_price = Double.parseDouble(aclMessage.getContent());
					double v_bid = v_assignmentValue - v_price;

					v_bidList.add(v_bid);

					if (v_bid > v_max) {
						v_max = v_bid;
						m_winnerProp = aclMessage;
					}

				}

				Collections.sort(v_bidList, Collections.reverseOrder());
				// Max value at current prices
				v_ds.put(TasAgent.KEY_MAX_UTILITY, v_bidList.get(0));
				m_logger.info(TasAgent.KEY_MAX_UTILITY + " => " + v_bidList.get(0));
				// Difference between First and Second max value at current
				// prices
				if(v_bidList.size() > 1){
					v_ds.put(TasAgent.KEY_BID_INCREMENT, v_bidList.get(0) - v_bidList.get(1));
				}else{
					v_ds.put(TasAgent.KEY_BID_INCREMENT, v_bidList.get(0) - v_bidList.get(0));
				}				
				m_logger.info(TasAgent.KEY_BID_INCREMENT + " => " + v_ds.get(TasAgent.KEY_BID_INCREMENT));
				// WinnerProposal
				v_ds.put(TasAgent.KEY_WIN_PROPOSAL, m_winnerProp);
				m_logger.info(TasAgent.KEY_WIN_PROPOSAL + " => " + m_winnerProp.getSender().getLocalName());

				// List of proponents
				v_ds.put(TasAgent.KEY_PROPONENT_LIST, m_proposeList);


				// Update assignment set with new assignment involving winner
				// proponent
				m_assignment.put((Flight) v_ds.get(TasAgent.KEY_CURRENT_UNASSIGNED),
						m_winnerProp.getSender().getLocalName());
				
				m_proposalStatus = TasAgent.PROPOSAL;
			}
		}

	}
	
	@Override
	public int onEnd() {
		return m_proposalStatus;
	}
	@Override
	public boolean done() {
		if (m_finished)
			m_logger.info("CHECK PROPOSAL HAS FINISHED");

		return m_finished;
	}

}
