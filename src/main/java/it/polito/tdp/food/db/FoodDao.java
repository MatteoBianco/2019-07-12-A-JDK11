package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.FoodPair;
import it.polito.tdp.food.model.Portion;

public class FoodDao {
	public void allFoods(Map<Integer, Food> idMapFood){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
						
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				if(!idMapFood.containsKey(res.getInt("food_code")))
					idMapFood.put(res.getInt("food_code"), new Food(res.getInt("food_code"),
						res.getString("display_name")));
			}
			
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Food> getFoodsByPortions(Map<Integer, Food> idMapFood, Integer numPortions) {
		String sql = "SELECT DISTINCT f.food_code, f.display_name, COUNT(DISTINCT p.portion_id) AS CNT " + 
				"FROM `portion` AS p, food AS f " + 
				"WHERE f.food_code = p.food_code " + 
				"GROUP BY f.food_code, f.display_name " + 
				"HAVING  CNT = ? ORDER BY f.display_name";
		List<Food> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, numPortions);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				if(!idMapFood.containsKey(res.getInt("f.food_code"))) {
					Food f = new Food(res.getInt("f.food_code"), res.getString("f.display_name"));
					idMapFood.put(f.getFood_code(), f);
					result.add(f);
				}
				else {
					result.add(idMapFood.get(res.getInt("f.food_code")));
				}
				
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<FoodPair> getFoodPairs(Map<Integer, Food> idMapFood, Integer numPortions) {
		String sql = "SELECT fc1.food_code, fc2. food_code, AVG(c.condiment_calories) AS cal " + 
				"FROM food_condiment AS fc1, food_condiment AS fc2, condiment AS c " + 
				"WHERE fc1.food_code > fc2.food_code AND fc1.condiment_code = fc2.condiment_code "
				+ "AND fc1.condiment_code = c.condiment_code " + 
				"GROUP BY fc1.food_code, fc2.food_code";
		List<FoodPair> result = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				if(idMapFood.containsKey(res.getInt("fc1.food_code")) 
						&& idMapFood.containsKey(res.getInt("fc2.food_code"))) {
				
					FoodPair fp = new FoodPair(idMapFood.get(res.getInt("fc1.food_code")), 
							idMapFood.get(res.getInt("fc2.food_code")), res.getDouble("cal"));
					result.add(fp);
				}				
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Condiment> listAllCondiments(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"),
							res.getString("display_name"),
							res.getDouble("condiment_calories"), 
							res.getDouble("condiment_saturated_fats")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Portion> listAllPortions(){
		String sql = "SELECT * FROM portion" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Portion> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"),
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"), 
							res.getDouble("calories"),
							res.getDouble("saturated_fats"),
							res.getInt("food_code")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
}
