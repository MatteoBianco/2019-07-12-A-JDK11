package it.polito.tdp.food.model;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.food.model.Event.EventType;

public class Simulator {

	//CODA DEGLI EVENTI
	
	private Queue<Event> queue;
	
	//PARAMETRI DI SIMULAZIONE
	
	private int NS; //numero di stazioni di lavoro disponibili
	private Food start; //cibo di partenza
	private Graph<Food, DefaultWeightedEdge> graph; //grafo
	private List<FoodPair> edges; //adiacenze
	private Duration currentTime;
	
	//MODELLO DEL MONDO
	
	private int K; //numero di stazioni di lavoro totale
	
	//PARAMETRI DA CALCOLARE
	
	private int numFoodPrepared; //numero di cibi preparati
	private List<Food> foodPrepared; //cibi preparati
	
	public void init(Graph<Food, DefaultWeightedEdge> graph, Food start, List<FoodPair> pairs, int K) {
		
		this.queue = new PriorityQueue<>();
		this.graph = graph;
		this.edges = pairs;
		this.start = start;
		this.K = K;
		this.NS = K;
		
		this.foodPrepared = new ArrayList<>();
		this.foodPrepared.add(start);
		this.numFoodPrepared = 0;
		this.currentTime = Duration.of(0, ChronoUnit.MINUTES);		
		
		Collections.sort(this.edges);
		List<Food> neighbors = Graphs.neighborListOf(this.graph, start);

		for(FoodPair fp : this.edges) {
			if(this.NS > 0) {
				if(neighbors.contains(fp.getF1()) && !this.foodPrepared.contains(fp.getF1())) {
					queue.add(new Event(this.currentTime, EventType.START, fp.getF1(), fp.getAvgCalories()));
					this.foodPrepared.add(fp.getF1());
					this.NS--;
				}
				if(neighbors.contains(fp.getF2()) && !this.foodPrepared.contains(fp.getF2())) {
					queue.add(new Event(this.currentTime, EventType.START, fp.getF2(), fp.getAvgCalories()));
					this.foodPrepared.add(fp.getF2());
					this.NS--;
				}	
			}
		}
	}
	
	public void run() {
		if(this.queue == null) {
			throw new RuntimeException("Inizializzare la simulazione!");
		}
		while(!this.queue.isEmpty()) {
			processEvent(this.queue.poll());
		}
	}

	private void processEvent(Event e) {
		switch(e.getType()) {
		case START:
			queue.add(new Event(currentTime.plus(Duration.of(e.getCalories().longValue(), ChronoUnit.MINUTES)), EventType.END, e.getFood(), e.getCalories()));
			break;
		case END:
			this.currentTime = e.getTime();
			this.numFoodPrepared ++;
			this.NS ++;
			
			List<Food> neighbors = Graphs.neighborListOf(this.graph, e.getFood());
			neighbors.removeAll(this.foodPrepared);
			
			if(!neighbors.isEmpty()) {
				Food next = this.getFoodWithHigherCalories(e.getFood(), neighbors);
				Double calories = this.getHigherCalories(e.getFood(), next);
				queue.add(new Event(this.currentTime, EventType.START, next, calories));
				this.foodPrepared.add(next);
			}
			break;
		}
	}

	public int getNumFoodPrepared() {
		return this.numFoodPrepared;
	}

	public List<Food> getFoodPrepared() {
		foodPrepared.remove(0);
		return this.foodPrepared;
	}

	public Duration getTotalTime() {
		return this.currentTime;
	}

	public void setK(int k) {
		K = k;
	}

	private Double getHigherCalories(Food food, Food next) {
		for(FoodPair fp : this.edges) {
			if(fp.getF1().equals(food) && fp.getF2().equals(next))
				return fp.getAvgCalories();
			if(fp.getF2().equals(food) && fp.getF1().equals(next))
				return fp.getAvgCalories();		
			}
		return null;
	}

	private Food getFoodWithHigherCalories(Food food, List<Food> neighbors) {
		Food result = null;
		Double weight = -1.0;
		for(Food f : neighbors) {
			if(this.graph.getEdgeWeight(graph.getEdge(food, f)) > weight) {
				weight = this.graph.getEdgeWeight(graph.getEdge(food, f));
				result = f;
			}
		}
		return result;
	}
}
