package behaviours;

import java.util.HashMap;
import java.util.Map;

import commons.Flight;
import jade.core.behaviours.OneShotBehaviour;

public class FinishAssignment extends OneShotBehaviour {

	/**
	 * Behaviour do agente TasAgent
	 * responsavel por apresentar o resultado final
	 * alocacao de avioes aos voos.
	 */
	private static final long serialVersionUID = 4418337351324599137L;
	private HashMap<Flight,String> m_assignment;

	public FinishAssignment(HashMap<Flight,String> p_assignment) {
		this.m_assignment = p_assignment;
	}

	@Override
	public void action() {
		
		for (Map.Entry<Flight, String> v_tas : m_assignment.entrySet()) {
			System.out.println("Voo => " + v_tas.getKey().getM_FlightID()
								+ " | Aviao => " + v_tas.getValue());
		}

	}

}
