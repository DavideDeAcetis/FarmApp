/*
 * SupplyController
 *
 * Controller della vista supply, permette la gestione degli approvigionamenti di un farmaco.
 *
 * 18/07/2020
 */

package farmapp.controller;

import farmapp.business.BusinessException;
import farmapp.business.DrugService;
import farmapp.business.FarmAppBusinessFactory;
import farmapp.domain.Drug;
import farmapp.view.ViewDispatcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SupplyController implements Initializable, DataInitializable<Object>{

    @FXML
    private Button updatesAvailability;
    @FXML
    private TextField quantityField;
    @FXML
    private ChoiceBox<String> drugChoiceBox;
    @FXML
    private Label errorLabel;

    private ViewDispatcher dispatcher;
    private DrugService drugService;

    public SupplyController () {
        dispatcher = ViewDispatcher.getInstance();
        FarmAppBusinessFactory factory = FarmAppBusinessFactory.getInstance();
        drugService = factory.getDrugService();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updatesAvailability.disableProperty().bind(quantityField.textProperty().isEmpty().or(drugChoiceBox.valueProperty().isNull()));

    }

    @Override
    public void initializeData(Object object){
        try {
            List<String> drugs = drugService.findAllDrugsByName();
            ObservableList<String> drugsData = FXCollections.observableArrayList(drugs);
            drugChoiceBox.setItems(drugsData);
        } catch (BusinessException e) {
            dispatcher.renderError(e);
        }
    }

    @FXML
    public void updateAction (ActionEvent event){
        Drug drug = new Drug();
        drug.setDrugName(drugChoiceBox.getValue());

        try {
            if (Integer.parseInt(quantityField.getText()) < 0) {
                throw new NumberFormatException();
            } else {
                drug.setDrugAvailability(Integer.parseInt(quantityField.getText()));
            }
            drugService.updateDrug(drug);
            quantityField.setText("");
            errorLabel.setTextFill(Color.GREEN);
            errorLabel.setText("Farmaco aggiornato con successo.");
        } catch (BusinessException e){
            e.printStackTrace();
        } catch (NumberFormatException e){
            quantityField.setText("");
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Inserire un valore numerico valido.");
        }
    }
}
