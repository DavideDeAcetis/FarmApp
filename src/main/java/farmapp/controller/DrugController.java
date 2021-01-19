/*
 * DrugController
 *
 * Controller della vista Drug, qui vengono creati o modificati i farmaci.
 *
 * 18/07/2020
 */

package farmapp.controller;

import farmapp.business.BusinessException;
import farmapp.business.DrugService;
import farmapp.business.FarmAppBusinessFactory;
import farmapp.domain.Drug;
import farmapp.view.ViewDispatcher;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DrugController implements Initializable, DataInitializable<Drug> {

    @FXML
    private TextField drugNameField;
    @FXML
    private TextField drugAvailabilityField;
    @FXML
    private TextField drugMinimumAvailabilityField;
    @FXML
    private TextField drugPharmaceuticalCompanyField;
    @FXML
    private TextField drugActivePrincipleField;
    @FXML
    private DatePicker drugExpiryDatePicker;
    @FXML
    private Button drugSaveButton;
    @FXML
    private Label errorLabel;

    private ViewDispatcher dispatcher;
    private DrugService drugService;
    private Drug drug;

    public DrugController() {
        dispatcher = ViewDispatcher.getInstance();
        FarmAppBusinessFactory factory = FarmAppBusinessFactory.getInstance();
        drugService = factory.getDrugService();
    }

    /*
    Initialize disabilita il pulsante di salvataggio e imposta DatePicker sulla data odierna e disabilita la
    possibiiltà di poter modificare la parte testuale della data.
    */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        drugExpiryDatePicker.setValue(LocalDate.now());
        drugExpiryDatePicker.setEditable(false);
        drugSaveButton.disableProperty().bind(drugNameField.textProperty().isEmpty().or
                (drugAvailabilityField.textProperty().isEmpty().or
                        (drugMinimumAvailabilityField.textProperty().isEmpty().or
                                (drugPharmaceuticalCompanyField.textProperty().isEmpty().or
                                        (drugActivePrincipleField.textProperty().isEmpty())))));

    }

    /*
    InitializeData esegue un controllo sull'id del farmaco passato, se il suo id è impostato vuol dire che vogliamo
    modificare un farmaco già esistente e quindi si occupa di inserire all'interno di ogni campo il rispettivo
    dato.
    */
    @Override
    public void initializeData(Drug drug) {
        this.drug = drug;
        if (drug.getId() != null) {
            drugNameField.setText(drug.getDrugName());
            drugAvailabilityField.setText(Integer.toString(drug.getDrugAvailability()));
            drugMinimumAvailabilityField.setText(Integer.toString(drug.getDrugMinimumQuantity()));
            drugPharmaceuticalCompanyField.setText(drug.getDrugPharmaceuticalCompany());
            drugActivePrincipleField.setText(drug.getDrugActivePrinciple());
            drugExpiryDatePicker.setValue(drug.getDrugExpiry());
        }
    }

    /*
    manageDrug permette, in un unico metodo, di creare o modificare un farmaco.
    Il metodo solleva eccezione nel caso in cui i valori inseriti non sono interi >= 0.
    Creato un farmaco, il metodo riporta l'utente nella vista DrugListAdmin.
    */
    @FXML
    public void manageDrug() throws NumberFormatException {
        try {
            try {
                if (Integer.parseInt(drugAvailabilityField.getText()) < 0) {
                    throw new NumberFormatException();
                }
                try {
                    if (Integer.parseInt(drugMinimumAvailabilityField.getText()) < 0) {
                        throw new NumberFormatException();
                    }
                    populateDrug();
                    if (drug.getId() == null) {
                        drugService.createDrug(drug);
                    } else {
                        drugService.updateDrug(drug);
                    }
                    dispatcher.renderView("DrugListAdmin", drug);
                } catch (NumberFormatException e) {
                    errorLabel.setText("Il campo 'quantità minima' deve essere un numero maggiore o uguale a 0");
                }
            } catch (NumberFormatException e) {
                errorLabel.setText("Il campo 'disponibilità' deve essere un numero maggiore o uguale a 0");
            }
        } catch (BusinessException e) {
            e.printStackTrace();
        }
    }

    private void populateDrug(){
        drug.setDrugName(drugNameField.getText());
        drug.setDrugAvailability(Integer.parseInt(drugAvailabilityField.getText()));
        drug.setDrugMinimumQuantity(Integer.parseInt(drugMinimumAvailabilityField.getText()));
        drug.setDrugExpiry(drugExpiryDatePicker.getValue());
        drug.setDrugActivePrinciple(drugActivePrincipleField.getText());
        drug.setDrugPharmaceuticalCompany(drugPharmaceuticalCompanyField.getText());
    }
}
