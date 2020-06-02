package it.polito.tdp.food.model;

public class FoodPair implements Comparable<FoodPair> {
	
	private Food f1;
	private Food f2;
	private Double avgCalories;
	
	public FoodPair(Food f1, Food f2, Double avgCalories) {
		super();
		this.f1 = f1;
		this.f2 = f2;
		this.avgCalories = avgCalories;
	}

	public Food getF1() {
		return f1;
	}

	public void setF1(Food f1) {
		this.f1 = f1;
	}

	public Food getF2() {
		return f2;
	}

	public void setF2(Food f2) {
		this.f2 = f2;
	}

	public Double getAvgCalories() {
		return avgCalories;
	}

	public void setAvgCalories(Double avgCalories) {
		this.avgCalories = avgCalories;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((f1 == null) ? 0 : f1.hashCode());
		result = prime * result + ((f2 == null) ? 0 : f2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FoodPair other = (FoodPair) obj;
		if (f1 == null) {
			if (other.f1 != null)
				return false;
		} else if (!f1.equals(other.f1))
			return false;
		if (f2 == null) {
			if (other.f2 != null)
				return false;
		} else if (!f2.equals(other.f2))
			return false;
		return true;
	}

	@Override
	public int compareTo(FoodPair o) {
		return -this.avgCalories.compareTo(o.avgCalories);
	}
	
	
	
	
}
