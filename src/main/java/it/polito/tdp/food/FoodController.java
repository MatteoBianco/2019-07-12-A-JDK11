/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import java.net.URL;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.FoodPair;
import it.polito.tdp.food.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtPorzioni"
    private TextField txtPorzioni; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalorie"
    private Button btnCalorie; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="boxFood"
    private ComboBox<Food> boxFood; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	boxFood.getItems().removeAll(boxFood.getItems());
    	Integer numPortions;
    	try {
    		numPortions = Integer.parseInt(txtPorzioni.getText());
    	} catch(NumberFormatException e) {
    		txtResult.appendText("Errore, inserire un numero intero!\n");
    		return;
    	}
    	this.model.creaGrafo(numPortions);
    	this.boxFood.getItems().addAll(this.model.getVertices());
    	if(boxFood.getItems().size() == 0) {
        	txtResult.setText("Nessun cibo presente col numero di porzioni indicato\n");
        	return;
    	}
    	txtResult.setText("Grafo creato\n");
    }
    
    @FXML
    void doCalorie(ActionEvent event) {
    	txtResult.clear();
    	Food food = boxFood.getValue();
    	if(food == null) {
    		txtResult.appendText("Errore: è necessario selezionare un cibo dall'elenco per trovare "
    				+ "i 5 cibi con calorie congiunte massime");
    		return;
    	}
    	List<FoodPair> foodPairs = this.model.getEdges();
    	Collections.sort(foodPairs);
    	int i = 0;
    	for(FoodPair fp : foodPairs) {
    		if(i < 5) {
    			if(fp.getF1().equals(food)) {
    	    		txtResult.appendText(String.format("Cibo: %s --- Calorie congiunte: %.3f\n", fp.getF2(), fp.getAvgCalories()));
    				i++;
    			}
    			else if(fp.getF2().equals(food)){
    	    		txtResult.appendText(String.format("Cibo: %s --- Calorie congiunte: %.3f\n", fp.getF1(), fp.getAvgCalories()));
    				i++;
    			}
    		}
    	}
    	if(i == 0) {
    		txtResult.appendText("Nessuna adiacenza trovata per l'alimento selezionato: impossibile procedere "
    				+ "al calcolo delle calorie congiunte per i suoi vicini!");
    	}
    }

    @FXML
    void doSimula(ActionEvent event) {
    	txtResult.clear();
    	Integer K;
    	try {
    		K = Integer.parseInt(txtK.getText());
    	} catch(NumberFormatException e) {
    		txtResult.appendText("Errore, inserire un numero intero compreso tra 1 e 10!\n");
    		return;
    	}
    	if(K<1 || K>10) {
    		txtResult.appendText("Errore, inserire un numero intero compreso tra 1 e 10!\n");
    		return;
    	}
    	Food start = this.boxFood.getValue();
    	if(start == null) {
    		txtResult.appendText("Errore: è necessario selezionare un cibo dall'elenco per far partire "
    				+ "la simulazione\n");
    		return;
    	}
    	this.model.simulate(start, K);
    	txtResult.appendText(String.format("Sono stati preparati, con %d workstation, %d piatti a partire "
    			+ "dal piatto %s.\n", 
    			K, this.model.getNumFoodPrepared(), start));
    	txtResult.appendText("Per queste preparazioni, è stato necessario un tempo di " + this.model.getTotalTime().toMinutes() + " minuti.\n");
    	if(this.model.getFoodPrepared().isEmpty())
    		return;
    	txtResult.appendText("Cibi preparati:\n");
    	for(Food f : this.model.getFoodPrepared()) {
    		txtResult.appendText(f + "\n");
    	}
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtPorzioni != null : "fx:id=\"txtPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCalorie != null : "fx:id=\"btnCalorie\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxFood != null : "fx:id=\"boxFood\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
