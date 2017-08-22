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

public class SendPropose extends OneShotBehaviour {

	/**
	 * Behaviour do agente AircraftAgent responsavel pela acao de enviar uma
	 * proposta para a instancia do agente TasAgent.
	 */
	private static final long serialVersionUID = 3591700095749070623L;
	private final Logger m_logger = Logger.getMyLogger(getClass().getName());
	private Aircraft m_acft;
	private DataStore v_ds;

	@Override
	public void action() {
		v_ds = getDataStore();
		m_acft = (Aircraft) v_ds.get(myAgent.getLocalName());
		ACLMessage v_cfp = (ACLMessage) v_ds.get("CFP");

		try {
			Flight v_flt = (Flight) v_cfp.getContentObject();
			Double v_price = routePrice(v_flt);
			ACLMessage v_aclPropose = v_cfp.createReply();
			v_aclPropose.setPerformative(ACLMessage.PROPOSE);
			v_aclPropose.setContent(v_price.toString());
			
			validarDestinoOrigem(v_flt);
			vooCandidatoMesmoLocalAviao(v_flt);
			vooCandidatoMesmoOrigemDestinoRotaAtual(v_flt);

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
		return preco;
	}
	
	//O voo candidato pode ter a origem igual ao destino do último voo da rota mais o TAT (40 min de intervalo entre os voos); nesse caso:
	private void validarDestinoOrigem(Flight v_flt){
		Double preco = 0D;
		if (v_flt != null) {
			// return m_acft.getFator() * v_flt.getM_fuelKG();
			if (m_acft.getRoute() != null) {				
				//se for adiciona-se o voo a rota;
				if(v_flt.getM_origem().equals(m_acft.getRoute().get(m_acft.getRoute().size()-1).getM_destino()) && CalcFlight.isMaiorTAT(m_acft.getRoute().get(m_acft.getRoute().size()-1).getM_dataEta(), v_flt.getM_dataEta())){
					m_acft.getRoute().add(v_flt);
				}
				
				//o preço = soma do combustível de cada voo da rota e multiplica-se pelo fator do avião; 				
				
				for (Flight flight : m_acft.getRoute()) {
					preco += flight.getM_fuelKG();
				}
				preco = preco * m_acft.getFator();
				Proposal proposal = new Proposal();
				proposal.setPrice(preco);
				proposal.setRoute(m_acft.getRoute());
				//TODO FALTA COLOCAR DENTRO DO DATASTORE NAO SEI SE EH ASSIM MESMO	 
				v_ds.put(myAgent.getLocalName() + "_PROPOSAL", proposal);
			}
		}		
	}
	
	// O voo candidato tem o mesmo local do avião:
	private void vooCandidatoMesmoLocalAviao(Flight v_flt) {
		if (m_acft.getRoute() != null) {
//			/adiciona-se o voo candidato a rota (é o primeiro voo da rota);
			List<Flight> listaPrice = new ArrayList<Flight>();
			if (m_acft.getCurrLoc().equals(v_flt.getM_origem())) {
				listaPrice.add(v_flt);
			}

			for (Flight flight : m_acft.getRoute()) {
				// TODO retira-se da rota os voos que não forem compatíveis com o voo candidato.(nao entendi compativel). falta retirar da rota
				listaPrice.add(flight);
			}

			m_acft.setRoute(new ArrayList<Flight>());
			m_acft.setRoute(listaPrice);
		}
	}

	// O voo candidato tem o mesmo origem e destino de um voo da rota atual do
	// avião:
	private void vooCandidatoMesmoOrigemDestinoRotaAtual(Flight v_flt) {
		Flight flightDesalocado = new Flight();
		if (m_acft.getRoute() != null) {
			for (int i = 0; i < m_acft.getRoute().size(); i++) {
			//adiciona-se o voo candidato a rota, substituindo pelo voo que tem mesma origem e destino;
				if (m_acft.getRoute().get(i).getM_origem().equals(v_flt.getM_origem())
						&& m_acft.getRoute().get(i).getM_destino().equals(v_flt.getM_destino())) {
					flightDesalocado = m_acft.getRoute().get(i);
					m_acft.getRoute().remove(i);
					m_acft.getRoute().add(i, v_flt);
					break;
				}
			}
		}
	}
	//TODO obs: retira-se da rota os voos que não forem compatíveis.
	// TODO fazer alguma coisa com o flightDesalocado

}
