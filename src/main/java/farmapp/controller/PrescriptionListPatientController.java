/*
 * PrescriptionListPatientController
 *
 * Controller della vista PrescriptionListPatient, da qui viene gestita e popolata la lista delle prescrizioni di ogni
 * paziente, la vista permette di visualizzare i dettagli di una prescrizione e di visualizzare prescrizioni precedentemente
 * evase da un farmacista.
 *
 * 18/07/2020
 */

package farmapp.controller;

import farmapp.business.*;
import farmapp.domain.Patient;
import farmapp.domain.Prescription;
import farmapp.view.ViewDispatcher;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PrescriptionListPatientController implements Initializable, DataInitializable<Patient> {

    @FXML
    private TableView<Prescription> PrescriptionListTable;
    @FXML
    private TableColumn<Prescription, String> objectTableColumn;
    @FXML
    private TableColumn<Prescription, Button> actionTableColumn;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label drugListLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private Button historicalPrescriptionButton;

    private ViewDispatcher dispatcher;
    private PrescriptionService prescriptionService;
    private Patient patient;
    private boolean reloadPage;

    public PrescriptionListPatientController() {
        dispatcher = ViewDispatcher.getInstance();
        FarmAppBusinessFactory factory = FarmAppBusinessFactory.getInstance();
        prescriptionService = factory.getPrescriptionService();
    }

    /*
    Il metodo initialize imposta le colonne della tabella in base a cosa devono contenere.
    I pulsanti creati in fase di popolazione della tabella permettono di visualizzare i
    dettagli di una prescrizione, in particolare il nome, il numero del farmaco prescritto e la descrizione della prescrizione.
    Se il pulsante "Visualizza il mio storico delle prescrizoni" viene premuto la tabella viene popolata con le vecchie
    prescrizioni dell'utente.
    */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        objectTableColumn.setCellValueFactory(new PropertyValueFactory<>("prescriptionObject"));
        actionTableColumn.setStyle("-fx-alignment: CENTER;");
        actionTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Prescription, Button>, ObservableValue<Button>>() {
            @Override
            public ObservableValue<Button> call(TableColumn.CellDataFeatures<Prescription, Button> param) {
                final Button modifyButton = new Button("Dettagli");
                modifyButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        descriptionLabel.setText(param.getValue().getPrescriptionDetails());
                        drugListLabel.setText(param.getValue().getPrescribedDrug() + " Qta.: " + param.getValue().getDrugNumber());
                    }
                });
                return new SimpleObjectProperty<Button>(modifyButton);
            }
        });
    }

    /*
    InitializeData popola inizialmente la tabella con le prescrizioni correnti, in più imposta il valore di default di
    "reloadPage" a false.
    */
    @Override
    public void initializeData(Patient patient) {
        this.patient = patient;
        this.reloadPage = false;
        try {
            List<Prescription> prescriptionList = prescriptionService.findPrescriptionsByFiscalCode(patient);
            ObservableList<Prescription> prescriptionData = FXCollections.observableArrayList(prescriptionList);
            PrescriptionListTable.setItems(prescriptionData);
        } catch (NoPrescriptionAssignedToFiscalCodeExc e) {
            errorLabel.setText("Non ci sono prescrizioni a tuo nome.");
        } catch (BusinessException e) {
            dispatcher.renderError(e);
        }
    }

    /*
    "historicalPrescription" è invocato nel momento in cui il pulsante "Visualizza il mio storico delle prescrizoni" viene
    premuto, modifica gli elementi della tabella con le prescrizioni già evase. Cerca tali prescrizioni usando il metodo
    "findPrescriptionHistory" in FilePrescriptionListService che valuta il valore dell'ultima colonna della stringa
    rappresentante la prescrizone nel file e se questo valore è 1 allora popola la colonna, altrimenti no.
    Il valore booleano "reloadPage" è utilizzato quando il pulsante viene premuto per la seconda volta in modo tale che
    il metodo non esegua la ripopolazione della tabella stessa con le vecchie prescrizioni bensì ricarica la pagina
    in modo da visualizzare le prescrizioni nuove. Il metodo da una label di errore se non ci sono prescrizioni da mostrare
    */
    @FXML
    public void historicalPrescription(ActionEvent event) {
        try {
            if (!reloadPage) {
                reloadPage = true;
                List<Prescription> prescriptionList = prescriptionService.findPrescriptionHistory(patient);
                ObservableList<Prescription> prescriptionData = FXCollections.observableArrayList(prescriptionList);
                PrescriptionListTable.setItems(prescriptionData);
                historicalPrescriptionButton.setText("Visualizza le mie prescrizioni correnti");
                objectTableColumn.setText("Prescrizioni evase");
            } else {
                dispatcher.renderView("PrescriptionListPatient", patient);
            }
        } catch (NoPrescriptionAssignedToFiscalCodeExc e) {
            errorLabel.setText("Non ci sono vecchie prescrizioni a tuo nome.");
        } catch (BusinessException e) {
            dispatcher.renderError(e);
        }
    }
}
