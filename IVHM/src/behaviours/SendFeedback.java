package behaviours;

import java.util.ArrayList;
import java.util.List;

import agents.TasAgent;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

public class SendFeedback extends OneShotBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8097174469802047304L;
	private DataStore m_ds;
	private List<ACLMessage> m_proposeList = new ArrayList<ACLMessage>(TasAgent.ACFT_QTY);
	private final Logger logger = Logger.getMyLogger(getClass().getName());
	
	@Override
	public void action() {
		m_ds = getDataStore();
		ACLMessage v_win_proposal = (ACLMessage) m_ds.get(TasAgent.KEY_WIN_PROPOSAL);
		ACLMessage v_msg2win = v_win_proposal.createReply();
		ACLMessage v_msg2loser = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
		
		Double v_bidIncrement = (Double) m_ds.get(TasAgent.KEY_BID_INCREMENT);
		v_msg2win.setContent(v_bidIncrement.toString());
		v_msg2win.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
		
		myAgent.send(v_msg2win);
		
		m_proposeList = (ArrayList<ACLMessage>) m_ds.get(TasAgent.KEY_PROPONENT_LIST);
		
		for (ACLMessage aclMessage : m_proposeList) {
			if(aclMessage.getSender() != v_win_proposal.getSender())
				v_msg2loser.addReceiver(aclMessage.getSender());
		}
		
		myAgent.send(v_msg2loser);
		logger.info("FEEDBACK SENT TO ACFTs");
	}

}
