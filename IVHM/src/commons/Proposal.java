package commons;

import java.util.ArrayList;
import java.util.List;

public class Proposal {
	private List<Flight> route = new ArrayList<Flight>();
	private Double price;

	public List<Flight> getRoute() {
		return route;
	}

	public void setRoute(List<Flight> route) {
		this.route = route;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

}
