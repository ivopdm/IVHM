package commons;

import java.util.HashMap;


/**
 * 
 * @author ivopdm
 * TODO Vai ter que mudar para considerar que o voo tem um unico valor independente do aviao
 */
public class Flight {
	
	private String m_FlightID;
	private HashMap<String, Double> m_assignmentValue;
	
	public Flight(String p_FlightID, HashMap<String, Double> p_assignmentValue){
		this.m_FlightID = p_FlightID;
		this.m_assignmentValue = p_assignmentValue;
	}

	public HashMap<String, Double> getM_assignmentValue() {
		return m_assignmentValue;
	}
	
	public String getM_FlightID() {
		return m_FlightID;
	}
	
	
}
