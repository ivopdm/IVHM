package commons;

import java.util.List;

/**
 * 
 * @author ivopdm
 * TODO Implementar classe aircraft
 */
public class Aircraft {
	private Long id;
	private String nome;
	private Double fator;
	private Double price;
	private List<Flight> route;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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
	
	public Object getCurrLoc() {
		// TODO Auto-generated method stub
		return null;
	}


}
