package behaviours;

import java.util.HashMap;

import commons.Flight;
import jade.core.behaviours.OneShotBehaviour;

public class FinishAssignment extends OneShotBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4418337351324599137L;
	private HashMap<Flight,String> m_assignment;

	public FinishAssignment(HashMap<Flight,String> p_assignment) {
		this.m_assignment = p_assignment;
	}

	@Override
	public void action() {
		System.out.println(m_assignment.toString());

	}

}
