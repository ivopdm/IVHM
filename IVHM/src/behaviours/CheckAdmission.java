package behaviours;

import java.util.Date;

import agents.AircraftAgent;
import commons.Aircraft;
import commons.Flight;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.util.Logger;
import util.Data;

public class CheckAdmission extends OneShotBehaviour {

	/**
	 * Behaviour/acao responsavel por validar se a instancia de AircraftAgent
	 * deve participar da negociacao/competicao para realizar o voo
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
			m_logger.warning(myAgent.getLocalName() + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public int onEnd() {
		// TODO checar substituicao por equals()
		if (m_flt.getM_origem() == m_acft.getCurrLoc()) {
			m_logger.info(myAgent.getLocalName() + " => ADMISSION OK");
			return AircraftAgent.ADMISSION_OK;
		} else if (isSwap(m_flt, m_acft)) {
			m_logger.info(myAgent.getLocalName() + " => ADMISSION OK");
			return AircraftAgent.ADMISSION_OK;
		} else if (isAdd(m_flt, m_acft)) {
			m_logger.info(myAgent.getLocalName() + " => ADMISSION OK");
			return AircraftAgent.ADMISSION_OK;
		} else {
			m_logger.info(myAgent.getLocalName() + " => ADMISSION NOK");
			return AircraftAgent.ADMISSION_NOK;
		}

	}

	/**
	 * TODO Devolver true se houver algum voo na lista de voo de p_acft que o
	 * destino e igual a origem de p_flt e Checar TAT
	 */
	private boolean isAdd(Flight p_flt, Aircraft p_acft) {
		Boolean retorno = Boolean.FALSE;
		if (p_acft.getRoute() != null) {
			for (Flight flight : p_acft.getRoute()) {
				if (p_flt.getM_origem().equals(flight.getM_destino())
						&& isMaiorTAT(flight.getM_dataEtd(), flight.getM_dataEtd())) {
					retorno = Boolean.TRUE;
					break;
				}
			}
		}

		return retorno;
	}

	/**
	 * TODO Devolver true se houver algum voo na lista de voo de p_acft que a
	 * origem e destino e igual a origem e destino de p_flt e Checar TAT
	 * 
	 * TA FALTANDO CHECAR APENAS O TAT.
	 */
	private boolean isSwap(Flight p_flt, Aircraft p_acft) {
		Boolean retorno = Boolean.FALSE;
		if (p_acft.getRoute() != null) {
			for (Flight flight : p_acft.getRoute()) {
				if (flight.getM_origem().equals(p_flt.getM_origem())
						&& flight.getM_destino().equals(p_flt.getM_destino())
						&& isMaiorTAT(flight.getM_dataEtd(), flight.getM_dataEtd())) {
					retorno = Boolean.TRUE;
					break;
				}
			}
		}

		return retorno;
	}

	// implementar regra do calculo
	private boolean isMaiorTAT(Date p_inicio, Date p_fim) {
		return Data.calculaDiasDiferencaEntreDatas(p_fim, p_inicio) > 40;
	}

}
