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

	@Override
	public void action() {
		v_ds = getDataStore();
		m_acft = (Aircraft) v_ds.get(myAgent.getLocalName());
		ACLMessage v_cfp = (ACLMessage) v_ds.get("CFP");

		if (m_acft != null && m_acft.getRoute() != null) {
			CalcFlight.ordenaPorData(m_acft.getRoute());
		}

		try {
			Flight v_flt = (Flight) v_cfp.getContentObject();
			Double v_price = routePrice(v_flt);
			ACLMessage v_aclPropose = v_cfp.createReply();
			v_aclPropose.setPerformative(ACLMessage.PROPOSE);
			v_aclPropose.setContent(v_price.toString());

			myAgent.send(v_aclPropose);

			m_logger.info(myAgent.getLocalName() + " proposes -> " + v_price);

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
			if(validarDestinoOrigem(v_flt)){
				
			}else if(isVooCandidatoMesmoLocalAviao(v_flt)){
				
			}else if(vooCandidatoMesmoOrigemDestinoRotaAtual(v_flt)){
				
			}else{
				// TODO fazer oque se nao tender nenhum
			}
		} else {
			// TODO fazer oque neste caso  adicionar o voo na rota
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
				if (m_acft.getRoute() != null) {
					// se for adiciona-se o voo a rota;
					// corrir pois primeiro tenho que ordenar por horario

					for (int i = 0; i < m_acft.getRoute().size(); i++) {
						if (i == m_acft.getRoute().size() - 1) {
							if (v_flt.getM_origem().equals(m_acft.getRoute().get(i).getM_destino())
									&& CalcFlight.isMaiorTAT(v_flt.getM_dataEtd(), v_flt.getM_dataEtd())) {
								listaPrice.add(v_flt);
								isCanditatoValido = Boolean.TRUE;
								retorno = Boolean.TRUE;
							} else {
								retorno = Boolean.FALSE;
								isCanditatoValido = Boolean.FALSE;
							}
						}
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
		List<Flight> listaPrice = new ArrayList<Flight>();
		try {
			if (m_acft.getRoute() != null) {
				if (m_acft.getCurrLoc().equals(v_flt.getM_origem())) {
					for (Flight flight : m_acft.getRoute()) {
						if (flight.getM_destino().equals(v_flt.getM_destino())) {
							int tatRota = Data.calculaDiasDiferencaEntreDatas(flight.getM_dataEtd(),
									flight.getM_dataEta());
							int tatCandidato = Data.calculaDiasDiferencaEntreDatas(v_flt.getM_dataEtd(),
									v_flt.getM_dataEta());
							if (tatCandidato > 40 && tatCandidato < tatRota) {
								listaPrice.add(v_flt);
								isCanditatoValido = Boolean.TRUE;
							}
						} else {
							listaPrice.add(v_flt);
							preco = (v_flt.getM_fuelKG() / 1000) * m_acft.getFator() * VALORCOMBUSTIVEL;
							Proposal proposal = new Proposal();
							proposal.setPrice(preco);
							proposal.setRoute(listaPrice);
							v_ds.put(myAgent.getLocalName() + "_PROPOSAL", proposal);
							retorno = Boolean.FALSE;
						}
						break;
					}
					if (isCanditatoValido) {
						for (int i = 0; i < m_acft.getRoute().size(); i++) {
							if (i > 0) {
								listaPrice.add(m_acft.getRoute().get(i));
							}
						}
						for (Flight flight : listaPrice) {
							preco += flight.getM_fuelKG();
						}
						preco = (preco / 1000) * m_acft.getFator() * VALORCOMBUSTIVEL;
						Proposal proposal = new Proposal();
						proposal.setPrice(preco);
						proposal.setRoute(m_acft.getRoute());
						v_ds.put(myAgent.getLocalName() + "_PROPOSAL", proposal);
					}
				} else {
					retorno = Boolean.FALSE;
				}
			} else {
				if (m_acft.getCurrLoc().equals(v_flt.getM_origem())) {
					listaPrice.add(v_flt);
					preco = (v_flt.getM_fuelKG() / 1000) * m_acft.getFator() * VALORCOMBUSTIVEL;
					Proposal proposal = new Proposal();
					proposal.setPrice(preco);
					proposal.setRoute(listaPrice);
					v_ds.put(myAgent.getLocalName() + "_PROPOSAL", proposal);
					retorno = Boolean.TRUE;
				} else {
					retorno = Boolean.FALSE;
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

	// O voo candidato tem o mesmo origem e destino de um voo da rota atual do
	// avião:
	//TODO parei aki vou terminar amanha com base no isVooCandidatoMesmoLocalAviao
	private Boolean vooCandidatoMesmoOrigemDestinoRotaAtual(Flight v_flt) {
		Flight flightDesalocado = new Flight();
		if (m_acft.getRoute() != null) {
			for (int i = 0; i < m_acft.getRoute().size(); i++) {				
				if (m_acft.getRoute().get(i).getM_origem().equals(v_flt.getM_origem())
						&& m_acft.getRoute().get(i).getM_destino().equals(v_flt.getM_destino())) {
					flightDesalocado = m_acft.getRoute().get(i);
					m_acft.getRoute().remove(i);
					m_acft.getRoute().add(i, v_flt);
					break;
				}
			}
		}
		return true;
	}	

}
