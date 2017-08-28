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
			// Recebe voo
			m_flt = (Flight) v_cfp.getContentObject();
			// TODO TESTES
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

	}

	private Boolean isAceitaPropostaVooCandidato() {
		Double preco = 0D;
		Boolean propostaAceita = false;

		List<Flight> v_listaRotaProposta = new ArrayList<Flight>();
		try {
			// Rota do aviao vazia?
			if (m_acft.getRoute() != null && !m_acft.getRoute().isEmpty()) {
				CalcFlight.ordenaPorData(m_acft.getRoute());

				for (Flight flight : m_acft.getRoute()) {
					// TEM VOO NA ROTA ANTES DO RECEBIDO
					if (flight.getM_destino().equals(m_flt.getM_origem())
							&& CalcFlight.isMaiorTAT(m_flt.getM_dataEtd(), flight.getM_dataEta())) {
						propostaAceita = true;

						for (int i = 0; i <= m_acft.getRoute().indexOf(flight); i++) {
							v_listaRotaProposta.add(m_acft.getRoute().get(i));
						}

						v_listaRotaProposta.add(m_flt);

					}
				}

				if (!propostaAceita) {
					// Origem do voo igual a local do aviao, caso lista vazia
					if (m_flt.getM_origem().equals(m_acft.getCurrLoc())) {
						v_listaRotaProposta.add(m_flt);
						propostaAceita = Boolean.TRUE;
					}
				}

				// TEM VOO NA ROTA DEPOIS DO RECEBIDO
				if (propostaAceita) {
					for (Flight flight : m_acft.getRoute()) {
						if (flight.getM_origem().equals(m_flt.getM_destino())
								&& CalcFlight.isMaiorTAT(flight.getM_dataEtd(), m_flt.getM_dataEta())) {
							propostaAceita = true;
							for (int i = m_acft.getRoute().indexOf(flight); i <= m_acft.getRoute().size() - 1; i++) {
								v_listaRotaProposta.add(m_acft.getRoute().get(i));
							}

						}
					}

				}
			} else {
				// Origem do voo igual a local do aviao, caso lista vazia
				if (m_flt.getM_origem().equals(m_acft.getCurrLoc())) {
					v_listaRotaProposta.add(m_flt);
					propostaAceita = Boolean.TRUE;
				}
			}

			// REALIZA O CALCULO DA PROPOSTA SE PROPOSTA ACEITA E LISTA != VAZIA
			if (propostaAceita && !v_listaRotaProposta.isEmpty()) {
				for (Flight flight : v_listaRotaProposta) {
					preco += flight.getM_fuelKG();
				}
				preco = (preco / 1000) * m_acft.getFator() * VALORCOMBUSTIVEL;
				Proposal proposal = new Proposal();
				proposal.setPrice(preco);
				proposal.setRoute(v_listaRotaProposta);

				// ENVIAO o PROPOSE
				ds.put(myAgent.getLocalName() + "_PROPOSAL", proposal);

			} 
		} catch (Exception e) {
			m_logger.warning(myAgent.getLocalName() + e.getMessage());
		} finally {
			preco = null;
			v_listaRotaProposta = null;
		}
		return propostaAceita;
	}

}
