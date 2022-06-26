package it.polito.tdp.PremierLeague.model;

import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	private PremierLeagueDAO dao;
	private Graph<Action,DefaultWeightedEdge>grafo;
	private double pesoMigliore ;
	private int giocatoreMigliore ;
	public Model() {
		dao=new PremierLeagueDAO ();
	}
	public List<Match> listAllMatches(){
		return dao.listAllMatches();
	}
	public void creaGrafo(Match m) {
		this.grafo=new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo,this.dao.getVertici(m));
		for(Adiacenza a:dao.getArchi(m)) {
			if(grafo.containsVertex(a.getA1())&&grafo.containsVertex(a.getA2())) {
			if(a.getPeso()<0) {
				Graphs.addEdgeWithVertices(grafo, a.getA2(), a.getA1(),((double)-1)*a.getPeso());
				
			}
			else if(a.getPeso()>=0) {
				Graphs.addEdgeWithVertices(grafo, a.getA1(), a.getA2(),a.getPeso());
			}
		}
			}
		
	}
	public int nVertici() {
		return grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return grafo.edgeSet().size();
	}
	public void getBest() {
		
		
		
	}public Set<Action> getVertici(){
		return this.grafo.vertexSet() ;
	}
	
	public void calcolaGiocatoreMigliore(){
	
		for(Action a : this.getVertici()) {
			double peso = 0.0 ;
			for(DefaultWeightedEdge  dwe : this.grafo.outgoingEdgesOf(a)) {
				peso += this.grafo.getEdgeWeight(dwe) ;
			}
			for(DefaultWeightedEdge  dwe : this.grafo.incomingEdgesOf(a)) {
				peso -= this.grafo.getEdgeWeight(dwe) ;
			}
			if(peso > pesoMigliore) {
				pesoMigliore = peso ;
				giocatoreMigliore = a.getPlayerID() ;
			}
		}
		
		
	}

	public double getPesoMigliore() {
		return  pesoMigliore;
	}public GiocatoreMigliore getMigliore() {
		if(grafo == null)
			return null;
		
		String best = null;
		double maxDelta = 0;
		for(Action p : this.grafo.vertexSet()) {
			double pesoUscente = 0.0;
			for(DefaultWeightedEdge out : this.grafo.outgoingEdgesOf(p))
				pesoUscente += this.grafo.getEdgeWeight(out);
			
			double pesoEntrante = 0.0;
			for(DefaultWeightedEdge in : this.grafo.incomingEdgesOf(p))
				pesoEntrante += this.grafo.getEdgeWeight(in);
			
			double delta = pesoUscente - pesoEntrante;
			if(delta > maxDelta) {
				maxDelta = delta;
				for(Player a : dao.listAllPlayers()) {
					if(a.getPlayerID().equals( p.getPlayerID())) {
				best = a.getName();
				}
					}
			}
		}
		return new GiocatoreMigliore(best, maxDelta);
	}


	public Player getGiocatoreMigliore() {
		for(Player p : dao.listAllPlayers()) {
			if(p.getPlayerID() == this.giocatoreMigliore) {
				return p ;
			}
		}
		return null;
	}

	public Graph<Action, DefaultWeightedEdge> getGrafo() {
		return this.grafo;
	}
	
}
