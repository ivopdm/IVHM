package behaviours;

import agents.AircraftAgent;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

public class CheckAdmission extends OneShotBehaviour {
	
	/**
	 * Behaviour/acao responsavel por validar se a 
	 * instancia de AircraftAgent deve participar
	 * da negociacao/competicao para realizar o voo
	 */
	private static final long serialVersionUID = 8628498715010608266L;
	private String m_fltID = "";
	

	private final Logger logger = Logger.getMyLogger(getClass().getName());
	
	@Override
	public void action() {
		ACLMessage v_cfp = new ACLMessage(ACLMessage.CFP);
		DataStore ds = getDataStore();
		v_cfp = (ACLMessage) ds.get("CFP");
		m_fltID = v_cfp.getContent();
	}
	
	@Override
	public int onEnd() {
		
		if(m_fltID.equals("FLT_1") || 
				m_fltID.equals("FLT_2") ||
				m_fltID.equals("FLT_3")){
			logger.info(myAgent.getLocalName() + " => ADMISSION OK");
			
			return AircraftAgent.ADMISSION_OK;
			
		}else{
			logger.info(myAgent.getLocalName() + " => ADMISSION NOK");
			return AircraftAgent.ADMISSION_NOK;
		}
			
		
	}

}
