package behaviours;

import agents.AircraftAgent;
import commons.Aircraft;
import commons.Flight;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.util.Logger;

public class CheckAdmission extends OneShotBehaviour {

	/**
	 * Behaviour/acao responsavel por validar se a 
	 * instancia de AircraftAgent deve participar
	 * da negociacao/competicao para realizar o voo
	 */
	private static final long serialVersionUID = 8628498715010608266L;
	private Flight m_flt;
	private Aircraft m_acft;

	private final Logger m_logger = Logger.getMyLogger(getClass().getName());

	@Override
	public void action() {
		ACLMessage v_cfp = new ACLMessage(ACLMessage.CFP);
		DataStore ds = getDataStore();
		v_cfp = (ACLMessage) ds.get("CFP");		
		m_acft = (Aircraft) ds.get(myAgent.getLocalName());

		try {
			 m_flt = (Flight) v_cfp.getContentObject();
		} catch (UnreadableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public int onEnd() {
		//TODO checar substituicao por equals()
		if(m_flt.getOrig() == m_acft.getCurrLoc()){
			m_logger.info(myAgent.getLocalName() + " => ADMISSION OK");

			return AircraftAgent.ADMISSION_OK;
		} else if(isSwap(m_flt, m_acft)){
			m_logger.info(myAgent.getLocalName() + " => ADMISSION OK");

			return AircraftAgent.ADMISSION_OK;
		}else if(isAdd(m_flt, m_acft)){
			m_logger.info(myAgent.getLocalName() + " => ADMISSION OK");

			return AircraftAgent.ADMISSION_OK;
			
		}else{
			m_logger.info(myAgent.getLocalName() + " => ADMISSION NOK");
			return AircraftAgent.ADMISSION_NOK;
		}

	}


	private boolean isAdd(Flight p_flt, Aircraft p_acft) {
		/** TODO Devolver true se houver algum voo na lista de voo de p_acft
		 * que o destino e igual a origem de p_flt e 
		 * Checar TAT
		 */
		
		return false;
	}


	private boolean isSwap(Flight p_flt, Aircraft p_acft) {
		/** TODO Devolver true se houver algum voo na lista de voo de p_acft
		 * que a origem e destino e igual a origem e destino de p_flt e
		 * Checar TAT
		 */
		
		return false;
	}

}
