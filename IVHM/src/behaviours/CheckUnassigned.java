package behaviours;

import java.util.HashMap;
import java.util.Map;

import agents.TasAgent;
import commons.Flight;
import jade.core.behaviours.OneShotBehaviour;
import jade.util.Logger;

/**
 * @author ivopdm
 *
 */
public class CheckUnassigned extends OneShotBehaviour {

	/**
	 * Verifica os voos que ainda nao tem aviao alocado.
	 * Em caso de haver voo para ser alocado retorna um inteiro para sinalizar isso, representado por TasAgent.CONTAINS_UNASSIGNED.
	 * Em caso de NAO haver voo para ser alocado retorna um inteiro para sinalizar isso, representado por TasAgent.ALL_ASSIGNED.
	 */
	private static final long serialVersionUID = 1L;
	private Map<Flight,String> m_assignment;
	private int m_trigger;
	private final Logger m_logger = Logger.getMyLogger(getClass().getName());
	
	/** @ConstructorProperties
	 * Inicializa o HashMap<Flight,String>
	 * Flight: e um objeto com ID do voo e um Hashmap<String,Double> mapeando nome do aviao e valor
	 * String: pode ser unassigned ou nome do aviao que vai fazer aquele voo
	 *
	 * @param p_assignment
	 */
	
	public CheckUnassigned(HashMap<Flight,String> p_assignment){
		this.m_assignment = p_assignment;		
	}
	
	@Override
	public void action() {
		
		for (Map.Entry<Flight, String> entry : m_assignment.entrySet()) {
			m_logger.info(entry.getKey().getM_FlightID() + " => " + entry.getValue());			
		}
		
		if(m_assignment.containsValue(TasAgent.FLIGHT_UNASSIGNED)){
			m_trigger = TasAgent.CONTAINS_UNASSIGNED;
			m_logger.info("CONTAINS_UNASSIGNED");
		}else{
			m_trigger = TasAgent.ALL_ASSIGNED;
			m_logger.info("ALL_ASSIGNED");
		}
			
	}

	@Override
	public int onEnd() {
		return m_trigger;
	}
	
	
}
