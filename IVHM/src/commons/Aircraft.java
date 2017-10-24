package commons;

import java.util.List;

/**
 * 
 * @author ivopdm TODO Implementar classe aircraft aviao
 */
public class Aircraft {
	private String id;
	private String nome;
	private Double fator = Double.valueOf(1.0);
	private Double price = Double.valueOf(0.0);
	private List<Flight> route;
	private String currLoc;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Double getFator() {
		return fator;
	}

	public void setFator(Double fator) {
		this.fator = fator;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public List<Flight> getRoute() {
		return route;
	}

	public void setRoute(List<Flight> route) {
		this.route = route;
	}

	public String getCurrLoc() {
		return currLoc;
	}

	public void setCurrLoc(String currLoc) {
		this.currLoc = currLoc;
	}

}
