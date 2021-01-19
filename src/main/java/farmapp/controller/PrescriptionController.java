/*
 * PrescriptionController
 *
 * Controller della vista Prescription, da qui è possibile creare/modificare una prescrizione.
 *
 * 18/07/2020
 */

package farmapp.controller;

import farmapp.business.*;
import farmapp.domain.Medic;
import farmapp.domain.Prescription;
import farmapp.view.ViewDispatcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PrescriptionController implements Initializable, DataInitializable<Prescription> {
    @FXML
    private TextField resumePrescriptionField;
    @FXML
    private TextField patientNameField;
    @FXML
    private TextField patientSurnameField;
    @FXML
    private TextField fiscalCodePatientField;
    @FXML
    private TextField qtaOne;
    @FXML
    private TextArea prescriptionNoteField;
    @FXML
    private ChoiceBox<String> drugOne;
    @FXML
    private Label fiscalCodeMedicLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private Button sendPrescriptionButton;

    private ViewDispatcher dispatcher;
    private PrescriptionService prescriptionService;
    private DrugService drugService;
    private Prescription prescription;

    public PrescriptionController() {
        dispatcher = ViewDispatcher.getInstance();
        FarmAppBusinessFactory factory = FarmAppBusinessFactory.getInstance();
        prescriptionService = factory.getPrescriptionService();
        drugService = factory.getDrugService();
    }

    /*
    Nel metodo initialize viene creata una lista di stringhe che contiene i nomi di tutti i farmaci presenti nel sistema,
    questa lista andrà a popolare la ChoiceBox per selezionare quale farmaco prescrivere.
    Viene anche disabilitato il pulsante "Invia prescrizione" se tutti i campi non sono stati riempiti.
    */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            List<String> drugNames = drugService.findAllDrugsByName();
            ObservableList<String> drugNamesData = FXCollections.observableArrayList(drugNames);
            drugOne.setItems(drugNamesData);
        } catch (BusinessException e) {
            dispatcher.renderError(e);
        }
        sendPrescriptionButton.disableProperty().bind(patientNameField.textProperty().isEmpty().or
                (resumePrescriptionField.textProperty().isEmpty().or
                        (patientSurnameField.textProperty().isEmpty().or
                                                        (fiscalCodePatientField.textProperty().isEmpty().or
                                                                (prescriptionNoteField.textProperty().isEmpty().or
                                                                        (qtaOne.textProperty().isEmpty()))))));
        qtaOne.disableProperty().bind(drugOne.valueProperty().isNull());
    }

    /*
    Nel metodo initializeData vengono riempiti i TextField con le informazioni della prescrizione passata.
    Questo sono nel caso in cui la prescrizione è già esistende ed ha un suo id.
    */
    @Override
    public void initializeData(Prescription prescription) {
        this.prescription = prescription;
        fiscalCodeMedicLabel.setText(prescription.getMedic().getFiscalCode());
        if (prescription.getId() != null) {
            resumePrescriptionField.setText(prescription.getPrescriptionObject());
            patientNameField.setText(prescription.getPatientName());
            patientSurnameField.setText(prescription.getPatientSurname());
            fiscalCodePatientField.setText(prescription.getPatientFiscalCode());
            prescriptionNoteField.setText(prescription.getPrescriptionDetails());
            qtaOne.setText(Integer.toString(prescription.getDrugNumber()));
            drugOne.setValue(prescription.getPrescribedDrug());
        }
    }

    /*
    Nel metodo managePrescription vengono gestite le operazioni di creazione e modifica di una prescrizione.
    In caso di valori numerici errati viene sollevata l'eccezione NumberFormatException che stampa un messaggio di errore.
    Il metodo decide, in base al valore dell'id della prescrizione, se bisogna chiamare il metodo che crea una nuova
    prescrizione o il metodo che la modifica.
    */
    @FXML
    public void managePrescription(ActionEvent event) throws NumberFormatException {
        try {
            try {
                if (Integer.parseInt(qtaOne.getText()) < 1) {
                    throw new NumberFormatException();
                }
                populatePrescription();
                if (prescription.getId() == null) {
                    prescriptionService.createPrescription(prescription);
                } else {
                    prescriptionService.updatePrescription(prescription);
                }
                dispatcher.renderView("PrescriptionListMedic", prescription.getMedic());
            } catch (NumberFormatException e) {
                errorLabel.setText("La quantità del farmaco prescritto deve essere un numero maggiore o uguale a 1");
            }
        } catch (BusinessException e) {
            e.printStackTrace();
        }
    }

    //populatePrescription imposta tutti i valori delle variabili di istanza di una prescrizione.

    private void populatePrescription() {
        prescription.setPatientName(patientNameField.getText());
        prescription.setPatientSurname(patientSurnameField.getText());
        prescription.setPrescriptionDetails(prescriptionNoteField.getText());
        prescription.setPatientFiscalCode(fiscalCodePatientField.getText());
        prescription.setMedic(new Medic());
        prescription.getMedic().setFiscalCode(fiscalCodeMedicLabel.getText());
        prescription.setPrescriptionObject(resumePrescriptionField.getText());
        prescription.setDrugNumber(Integer.parseInt(qtaOne.getText()));
        prescription.setPrescribedDrug(drugOne.getValue().toString());
    }
}
