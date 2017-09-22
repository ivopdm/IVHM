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
	private final Double VALORCOMBUSTIVEL = Double.valueOf(531D);
	private DataStore ds; 

	private List<Flight> m_listaRotaProposta = new ArrayList<Flight>();
	private Proposal proposal = new Proposal();

	private final Logger m_logger = Logger.getMyLogger(getClass().getName());

	@Override
	public void action() {
		ds = getDataStore();
		ACLMessage v_cfp = (ACLMessage) ds.get("CFP");
		m_acft = (Aircraft) ds.get(myAgent.getLocalName());


		try {
			// Recebe voo
			m_flt = (Flight) v_cfp.getContentObject();

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
		Double preco = Double.valueOf(0D);
		Double v_fltValue = Double.valueOf(0D);
		Boolean propostaAceita = false;


		try {

			// Rota do aviao vazia?
			if (m_acft.getRoute() != null && !m_acft.getRoute().isEmpty()) {
				CalcFlight.ordenaPorData(m_acft.getRoute());

				for (Flight flight : m_acft.getRoute()) {
					// TEM VOO NA ROTA ANTES DO RECEBIDO
					//System.out.println("TEM VOO NA ROTA ANTES DO RECEBIDO");
					if (flight.getM_destino().equals(m_flt.getM_origem())
							&& CalcFlight.isMaiorTAT(m_flt.getM_dataEtd(), flight.getM_dataEta())) {
						propostaAceita = true;

						for (int i = 0; i <= m_acft.getRoute().indexOf(flight); i++) {
							m_listaRotaProposta.add(m_acft.getRoute().get(i));
							System.out.println(m_acft.getRoute().get(i));
						}

						m_listaRotaProposta.add(m_flt);
						break;
					}
				}

				if (!propostaAceita) {
					// Origem do voo igual a local do aviao, caso lista vazia
					if (m_flt.getM_origem().equals(m_acft.getCurrLoc())) {
						m_listaRotaProposta.add(m_flt);
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
								m_listaRotaProposta.add(m_acft.getRoute().get(i));
								System.out.println(m_acft.getRoute().get(i));
							}
							break;
						}
					}

				}
			} else {
				// Origem do voo igual a local do aviao, caso lista vazia
				if (m_flt.getM_origem().equals(m_acft.getCurrLoc())) {
					m_listaRotaProposta.add(m_flt);
					propostaAceita = Boolean.TRUE;
				}
			}

			// REALIZA O CALCULO DA PROPOSTA SE PROPOSTA ACEITA E LISTA != VAZIA
			if (propostaAceita && !m_listaRotaProposta.isEmpty()) {
				for (Flight flight : m_listaRotaProposta) {
					preco += flight.getM_fuelKG();
					v_fltValue += flight.getM_flightValue();
				}

				preco = ((preco / 1000) * m_acft.getFator() * VALORCOMBUSTIVEL) + m_acft.getPrice();
				v_fltValue -= preco;

				proposal.setPrice(v_fltValue);
				proposal.setRoute(m_listaRotaProposta);

				ds.put(myAgent.getLocalName() + "_PROPOSAL", proposal);

			} 
		} catch (Exception e) {
			m_logger.warning(myAgent.getLocalName() + e.getMessage());
		} finally {
			preco = null;
			v_fltValue = null;
		}

		return propostaAceita;
	}

}
