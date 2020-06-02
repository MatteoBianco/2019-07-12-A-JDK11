package it.polito.tdp.food.model;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	private FoodDao dao;
	private Map<Integer, Food> idMapFood;
	private Graph<Food, DefaultWeightedEdge> graph;
	private List<FoodPair> foodPairs;
	private Simulator simulator;
	
	public Model() {
		this.dao = new FoodDao();
		idMapFood = new HashMap<>();
		simulator = new Simulator();
	}

	public void creaGrafo(Integer numPortions) {
		this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.graph, this.dao.getFoodsByPortions(idMapFood, numPortions));
		this.foodPairs = this.dao.getFoodPairs(idMapFood, numPortions);
		for(FoodPair fp : foodPairs) {
			if(this.graph.containsVertex(fp.getF1()) && this.graph.containsVertex(fp.getF2())) {
				Graphs.addEdge(this.graph, fp.getF1(), fp.getF2(), fp.getAvgCalories());
			}
		}
	}
	
	public void simulate(Food start, int K) {
		this.simulator.init(this.graph, start, this.foodPairs, K);
		this.simulator.run();
	}
	
	public int getNumFoodPrepared() {
		return this.simulator.getNumFoodPrepared();
	}
	
	public List<Food> getFoodPrepared() {
		return this.simulator.getFoodPrepared();
	}
	
	public Duration getTotalTime() {
		return this.simulator.getTotalTime();
	}
	
	public Set<Food> getVertices() {
		if(this.graph != null)
			return this.graph.vertexSet();
		return null;
	}

	public List<FoodPair> getEdges() {
		return this.foodPairs;
	}

}
