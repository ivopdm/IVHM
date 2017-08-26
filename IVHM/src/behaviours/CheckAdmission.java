package behaviours;

import java.util.ArrayList;
import java.util.List;

import agents.AircraftAgent;
import commons.Aircraft;
import commons.Flight;
import commons.Proposal;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.util.Logger;
import util.CalcFlight;

public class CheckAdmission extends OneShotBehaviour {

	/**
	 * Behaviour/acao responsavel por validar se a instancia de AircraftAgent
	 * deve participar da negociacao/competicao para realizar o voo
	 */
	private static final long serialVersionUID = 8628498715010608266L;
	private Flight m_flt;
	private Aircraft m_acft;
	private Double VALORCOMBUSTIVEL = 531D;
	DataStore ds;
	ACLMessage v_cfp;

	private final Logger m_logger = Logger.getMyLogger(getClass().getName());

	@Override
	public void action() {
		v_cfp = new ACLMessage(ACLMessage.CFP);
		ds = getDataStore();
		v_cfp = (ACLMessage) ds.get("CFP");
		m_acft = (Aircraft) ds.get(myAgent.getLocalName());

		try {
			m_flt = (Flight) v_cfp.getContentObject();
			//TODO TESTES
			m_flt.setM_origem("A");
			m_flt.setM_destino("B");
		} catch (UnreadableException e) {
			m_logger.warning(myAgent.getLocalName() + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public int onEnd() {
		if (isAceitaPropostaVooCandidato()) {
			m_logger.info(myAgent.getLocalName() + " => ADMISSION OK");
			return AircraftAgent.ADMISSION_OK;
		} else {
			m_logger.info(myAgent.getLocalName() + " => ADMISSION NOK");
			return AircraftAgent.ADMISSION_NOK;
		}

		/*
		 * if (m_flt.getM_origem().equals(m_acft.getCurrLoc())) {
		 * m_logger.info(myAgent.getLocalName() + " => ADMISSION OK"); return
		 * AircraftAgent.ADMISSION_OK; } else if (isSwap(m_flt, m_acft)) {
		 * m_logger.info(myAgent.getLocalName() + " => ADMISSION OK"); return
		 * AircraftAgent.ADMISSION_OK; } else if (isAdd(m_flt, m_acft)) {
		 * m_logger.info(myAgent.getLocalName() + " => ADMISSION OK"); return
		 * AircraftAgent.ADMISSION_OK; } else {
		 * m_logger.info(myAgent.getLocalName() + " => ADMISSION NOK"); return
		 * AircraftAgent.ADMISSION_NOK; }
		 */
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
						&& CalcFlight.isMaiorTAT(flight.getM_dataEta(), p_flt.getM_dataEtd())) {
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
						&& CalcFlight.isMaiorTAT(flight.getM_dataEtd(), flight.getM_dataEtd())) {
					retorno = Boolean.TRUE;
					break;
				}
			}
		}

		return retorno;
	}

	private Boolean isAceitaPropostaVooCandidato() {
		Double preco = 0D;
		Boolean propostaAceita = false;
		List<Flight> listaCloneRotaAtual = new ArrayList<Flight>();
		try {
			if (m_acft.getRoute() != null && !m_acft.getRoute().isEmpty()) {
				CalcFlight.ordenaPorData(m_acft.getRoute());
				listaCloneRotaAtual.addAll(m_acft.getRoute());
				int posicao = 0;
				for (Flight flight : listaCloneRotaAtual) {
					// TEM VOO NA ROTA ANTES DO RECEBIDO
					if (flight.getM_destino().equals(m_flt.getM_origem())
							&& CalcFlight.isMaiorTAT(m_flt.getM_dataEtd(), flight.getM_dataEta())) {
						propostaAceita = true;
						if (listaCloneRotaAtual.size() > 1 && posicao < listaCloneRotaAtual.size()) {
							listaCloneRotaAtual.remove(posicao + 1);
							listaCloneRotaAtual.add(posicao + 1, m_flt);
						} else {
							listaCloneRotaAtual.add(posicao + 1, m_flt);
						}

						break;
						// TEM VOO NA ROTA DEPOIS DO RECEBIDO
					} else if (flight.getM_origem().equals(m_flt.getM_destino())
							&& CalcFlight.isMaiorTAT(flight.getM_dataEtd(), m_flt.getM_dataEta())) {
						propostaAceita = true;
						if (listaCloneRotaAtual.size() > 1 && posicao < listaCloneRotaAtual.size()) {
							listaCloneRotaAtual.remove(posicao - 1);
							listaCloneRotaAtual.add(posicao - 1, m_flt);							
							break;
						} else {
							listaCloneRotaAtual.add(posicao + 1, m_flt);
						}
					}
					posicao++;
				}
			} else {
				if (m_flt.getM_origem().equals(m_acft.getCurrLoc())) {
					listaCloneRotaAtual.add(m_flt);
					propostaAceita = Boolean.TRUE;
				}
			}
			// REALIZA O CALCULO DA PROPOSTA SE PROPOSTA ACEITA E LISTA != VAZIA
			if (propostaAceita && !listaCloneRotaAtual.isEmpty()) {
				for (Flight flight : listaCloneRotaAtual) {
					preco += flight.getM_fuelKG();
				}
				preco = (preco / 1000) * m_acft.getFator() * VALORCOMBUSTIVEL;
				Proposal proposal = new Proposal();
				proposal.setPrice(preco);
				proposal.setRoute(listaCloneRotaAtual);
				// ENVIAO o PROPOSE
				ds.put(myAgent.getLocalName() + "_PROPOSAL", proposal);

				ACLMessage v_aclPropose = v_cfp.createReply();
				v_aclPropose.setPerformative(ACLMessage.PROPOSE);
				v_aclPropose.setContent(preco.toString());
				myAgent.send(v_aclPropose);
				m_logger.info(myAgent.getLocalName() + " proposes -> " + preco);

			} else {
				// ENVIAO o REFUSE
				ds.put(myAgent.getLocalName() + "_REFUSE", "");
			}
		} catch (Exception e) {
			m_logger.warning(myAgent.getLocalName() + e.getMessage());
		} finally {
			preco = null;
			listaCloneRotaAtual = null;
		}
		return propostaAceita;
	}

}
