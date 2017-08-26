package behaviours;

import java.util.ArrayList;
import java.util.List;

import commons.Aircraft;
import commons.Flight;
import commons.Proposal;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.util.Logger;
import util.CalcFlight;
import util.Data;

public class SendPropose extends OneShotBehaviour {

	/**
	 * Behaviour do agente AircraftAgent responsavel pela acao de enviar uma
	 * proposta para a instancia do agente TasAgent.
	 */
	private static final long serialVersionUID = 3591700095749070623L;
	private final Logger m_logger = Logger.getMyLogger(getClass().getName());
	private Aircraft m_acft;
	private DataStore v_ds;	
	private Double VALORCOMBUSTIVEL = 531D;
	private Proposal m_prop;

	@Override
	public void action() {
		v_ds = getDataStore();
		m_acft = (Aircraft) v_ds.get(myAgent.getLocalName());
		m_prop = (Proposal) v_ds.get(myAgent.getLocalName());
		ACLMessage v_cfp = (ACLMessage) v_ds.get("CFP");

		if (m_acft != null && m_acft.getRoute() != null) {
			CalcFlight.ordenaPorData(m_acft.getRoute());
		}

		try {
			Flight v_flt = (Flight) v_cfp.getContentObject();
			//Double v_price = routePrice(v_flt);
			ACLMessage v_aclPropose = v_cfp.createReply();
			v_aclPropose.setPerformative(ACLMessage.PROPOSE);
			v_aclPropose.setContent(m_prop.getPrice().toString());

			myAgent.send(v_aclPropose);

			m_logger.info(myAgent.getLocalName() + " proposes -> " + m_prop.getPrice());

		} catch (UnreadableException e) {
			m_logger.warning(myAgent.getLocalName() + e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * TODO Checar o conjunto de voo ja atribuidos para o aviao e calcula o
	 * preco baseado nisso usar o fator de consumo do aviao x combustivel medio
	 * necessario para voo Usar objeto Aircraft e objeto Flight Guarda no Data
	 * Store a rota e o preco proposta
	 */
	private Double routePrice(Flight v_flt) {
		Double preco = 0D;
		// se ja exister adicionar o voo na rota para atender este situação
		if (m_acft != null) {
//			if (validarDestinoOrigem(v_flt)) {
//			} else if (isVooCandidatoMesmoLocalAviao(v_flt)) {
//			} else if (vooCandidatoMesmoOrigemDestinoRotaAtual(v_flt)) {
//			}
		}
		return preco;
	}

	// O voo candidato pode ter a origem igual ao destino do último voo da rota
	// mais o TAT (40 min de intervalo entre os voos); nesse caso:
	private Boolean validarDestinoOrigem(Flight v_flt) {
		Double preco = 0D;
		Boolean retorno = Boolean.FALSE;
		Boolean isCanditatoValido = Boolean.FALSE;
		List<Flight> listaPrice = new ArrayList<Flight>();
		try {
			if (v_flt != null) {
				if (m_acft.getRoute() != null && !m_acft.getRoute().isEmpty()) {
					listaPrice.addAll(m_acft.getRoute());
					Flight ultimoVoo = m_acft.getRoute().get(m_acft.getRoute().size() - 1);
					if (v_flt.getM_origem().equals(ultimoVoo.getM_destino())
							&& CalcFlight.isMaiorTAT(v_flt.getM_dataEtd(), ultimoVoo.getM_dataEta())) {
						listaPrice.add(v_flt);
						isCanditatoValido = Boolean.TRUE;
						retorno = Boolean.TRUE;
					}
					if (isCanditatoValido) {
						for (Flight flight : listaPrice) {
							preco += flight.getM_fuelKG();
						}
						preco = (preco / 1000) * m_acft.getFator() * VALORCOMBUSTIVEL;
						Proposal proposal = new Proposal();
						proposal.setPrice(preco);
						proposal.setRoute(m_acft.getRoute());
						v_ds.put(myAgent.getLocalName() + "_PROPOSAL", proposal);
					}
				}
			}
		} catch (Exception e) {
			m_logger.warning(myAgent.getLocalName() + e.getMessage());
		} finally {
			preco = null;
			isCanditatoValido = null;
			listaPrice = null;
		}
		return retorno;
	}

	// O voo candidato tem o mesmo local do avião:
	private Boolean isVooCandidatoMesmoLocalAviao(Flight v_flt) {
		Double preco = 0D;
		Boolean retorno = Boolean.FALSE;
		Boolean isCanditatoValido = Boolean.FALSE;
		// List<Flight> listaPrice = new ArrayList<Flight>();
		List<Flight> listaPriceNovaPosicao = new ArrayList<Flight>();
		try {
			if (m_acft.getCurrLoc().equals(v_flt.getM_origem())) {
				retorno = Boolean.TRUE;
				if (m_acft.getRoute() != null || !m_acft.getRoute().isEmpty()) {
					listaPriceNovaPosicao.addAll(m_acft.getRoute());
					for (Flight flight : listaPriceNovaPosicao) {
						if (flight.getM_origem().equals(v_flt.getM_destino())) {
							int tatRota = Data.calculaDiasDiferencaEntreDatas(flight.getM_dataEtd(),
									v_flt.getM_dataEta());
							if (tatRota > 40) {
								isCanditatoValido = Boolean.TRUE;
								break;
							}
						} else {
							listaPriceNovaPosicao.remove(flight);
						}
					}
					if (isCanditatoValido) {
						listaPriceNovaPosicao.add(0, v_flt);
					}
				} else {
					listaPriceNovaPosicao.add(v_flt);
				}
			}
			if (!listaPriceNovaPosicao.isEmpty()) {
				for (Flight flight : listaPriceNovaPosicao) {
					preco += flight.getM_fuelKG();
				}
				preco = (preco / 1000) * m_acft.getFator() * VALORCOMBUSTIVEL;
				Proposal proposal = new Proposal();
				proposal.setPrice(preco);
				proposal.setRoute(m_acft.getRoute());
				v_ds.put(myAgent.getLocalName() + "_PROPOSAL", proposal);
			}
		} catch (Exception e) {
			m_logger.warning(myAgent.getLocalName() + e.getMessage());
		} finally {
			preco = null;
			isCanditatoValido = null;
			listaPriceNovaPosicao = null;
		}
		return retorno;
	}

	// O voo candidato tem o mesmo origem e destino de um voo da rota atual do
	// avião:
	// TODO parei aki vou terminar amanha com base no
	// isVooCandidatoMesmoLocalAviao
	private Boolean vooCandidatoMesmoOrigemDestinoRotaAtual(Flight v_flt) {
		Double preco = 0D;
		Boolean retorno = Boolean.FALSE;
		Boolean isCanditatoValido = Boolean.FALSE;
		// List<Flight> listaPrice = new ArrayList<Flight>();
		List<Flight> listaPriceNovaPosicao = new ArrayList<Flight>();
		try {
			listaPriceNovaPosicao.addAll(m_acft.getRoute());

			for (int i = 0; i < m_acft.getRoute().size(); i++) {
				if (m_acft.getRoute().get(i).getM_origem().equals(v_flt.getM_origem())
						&& m_acft.getRoute().get(i).getM_destino().equals(v_flt.getM_destino())) {
					if (i > 0) {
						int tatPrimeiro = Data.calculaDiasDiferencaEntreDatas(
								m_acft.getRoute().get(i - 1).getM_dataEtd(), v_flt.getM_dataEta());
						if (i < m_acft.getRoute().size() - 1) {
							int tatUltimo = Data.calculaDiasDiferencaEntreDatas(m_acft.getRoute().get(i).getM_dataEtd(),
									v_flt.getM_dataEta());
							if (tatPrimeiro > 40 && tatUltimo > 40) {
								isCanditatoValido = Boolean.TRUE;
								listaPriceNovaPosicao.remove(i);
								listaPriceNovaPosicao.add(v_flt);
								break;
							}
						} else {
							tatPrimeiro = Data.calculaDiasDiferencaEntreDatas(m_acft.getRoute().get(i).getM_dataEtd(),
									v_flt.getM_dataEta());
							if (tatPrimeiro > 40) {
								isCanditatoValido = Boolean.TRUE;
								listaPriceNovaPosicao.remove(i);
								listaPriceNovaPosicao.add(v_flt);
								break;
							}
						}

					}
				}
			}

		} catch (Exception e) {
			m_logger.warning(myAgent.getLocalName() + e.getMessage());
		} finally {
			preco = null;
			isCanditatoValido = null;
			listaPriceNovaPosicao = null;
		}
		return retorno;
	}

}
