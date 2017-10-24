package behaviours;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import commons.Flight;
import commons.Route;
import jade.core.behaviours.OneShotBehaviour;

public class FinishAssignment extends OneShotBehaviour {

	/**
	 * Behaviour do agente TasAgent responsavel por apresentar o resultado final
	 * alocacao de avioes aos voos.
	 */
	private static final long serialVersionUID = 4418337351324599137L;
	private HashMap<Route, String> m_assignment;

	public FinishAssignment(HashMap<Route, String> p_assignment) {
		this.m_assignment = p_assignment;
	}

	@Override
	public void action() {
		// aviao -voo -hora saida e -hora chegada
		for (Map.Entry<Route, String> v_tas : m_assignment.entrySet()) {
			// System.out.println("Rota => " + v_tas.getKey().getM_id() + " |
			// Aviao => " + v_tas.getValue());
			System.out.println("Avião =>" + v_tas.getValue());
			for (Flight obj : v_tas.getKey().getM_lstFlights()) {
				// String dataFormatada = new SimpleDateFormat("MM/dd/yyyy
				// HH:mm").format(obj.getM_dataEtd());
				System.out.println("Voo-> " + obj.getM_FlightID() + " Saíndo de " + obj.getM_origem() + " ás "
						+ new SimpleDateFormat("HH:mm").format(obj.getM_dataEtd()) + " no dia "
						+ new SimpleDateFormat("dd/MM/yy").format(obj.getM_dataEtd()) + ", com Destino a "
						+ obj.getM_destino() + " ás " + new SimpleDateFormat("HH:mm").format(obj.getM_dataEta())
						+ " no dia " + new SimpleDateFormat("dd/MM/yy").format(obj.getM_dataEta()) + " .");
			}
			System.out.println("---------------------------------------------------------------------------");
		}

	}

}
