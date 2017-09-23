package commons;

import java.util.List;

public class Route {

	private Long m_id;
	private List<Flight> m_lstFlights;
	private Double m_SumValue;
	private Double m_SumFuelKG;

	public Long getM_id() {
		return m_id;
	}

	public void setM_id(Long m_id) {
		this.m_id = m_id;
	}

	public List<Flight> getM_lstFlights() {
		return m_lstFlights;
	}

	public void setM_lstFlights(List<Flight> m_lstFlights) {
		this.m_lstFlights = m_lstFlights;
	}

	public Double getM_SumValue() {
		if (m_SumValue == null) {
			return 0.0;
		}
		return m_SumValue;
	}

	public void setM_SumValue(Double m_SumValue) {
		this.m_SumValue = m_SumValue;
	}

	public Double getM_SumFuelKG() {
		if (m_SumFuelKG == null) {
			return 0.0;
		}
		return m_SumFuelKG;
	}

	public void setM_SumFuelKG(Double m_SumFuelKG) {
		this.m_SumFuelKG = m_SumFuelKG;
	}

}
