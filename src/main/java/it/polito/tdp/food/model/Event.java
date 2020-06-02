package it.polito.tdp.food.model;

import java.time.Duration;
import java.time.LocalTime;

public class Event implements Comparable<Event> {

	public enum EventType {
		START, END
	}
	
	private Duration time;
	private EventType type;
	private Food food;
	private Double calories;
		
	public Event(Duration time, EventType type, Food food, Double calories) {
		super();
		this.time = time;
		this.type = type;
		this.food = food;
		this.calories = calories;
	}
	
	public Duration getTime() {
		return time;
	}
	public EventType getType() {
		return type;
	}
	public Food getFood() {
		return food;
	}
	public Double getCalories() {
		return calories;
	}

	@Override
	public int compareTo(Event o) {
		return this.time.compareTo(o.time);
	}
	
	
}
