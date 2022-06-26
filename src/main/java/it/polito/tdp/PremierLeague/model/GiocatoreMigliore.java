package it.polito.tdp.PremierLeague.model;

public class GiocatoreMigliore {
	private String p;
	private double delta;
	public GiocatoreMigliore(String p, double delta) {
		super();
		this.p = p;
		this.delta = delta;
	}
	public String getP() {
		return p;
	}
	public void setP(String p) {
		this.p = p;
	}
	public double getDelta() {
		return delta;
	}
	public void setDelta(double delta) {
		this.delta = delta;
	}
	@Override
	public String toString() {
		return p;
	}
	
	

}
