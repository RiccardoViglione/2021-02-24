package it.polito.tdp.PremierLeague.model;

public class Adiacenza {
	private Action a1;
	private Action a2;
	private Double peso;
	public Adiacenza(Action a1, Action a2, Double peso) {
		super();
		this.a1 = a1;
		this.a2 = a2;
		this.peso = peso;
	}
	public Action getA1() {
		return a1;
	}
	public void setA1(Action a1) {
		this.a1 = a1;
	}
	public Action getA2() {
		return a2;
	}
	public void setA2(Action a2) {
		this.a2 = a2;
	}
	public Double getPeso() {
		return peso;
	}
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	@Override
	public String toString() {
		return "Adiacenza [a1=" + a1 + ", a2=" + a2 + ", peso=" + peso + "]";
	}
	

}
