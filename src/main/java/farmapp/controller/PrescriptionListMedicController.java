/*
 * PrescriptionListMedicController
 *
 * Controller della vista PrescriptionListMedic, da qui viene gestita e popolata la lista delle prescrizioni di ogni
 * medico, la vista permette di modificare o eliminare una prescrizione e di crearne una nuova.
 *
 * 18/07/2020
 */

package farmapp.controller;

import farmapp.business.*;
import farmapp.domain.Medic;
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

public class PrescriptionListMedicController implements Initializable, DataInitializable<Medic> {

    @FXML
    private TableView<Prescription> prescriptionListTable;
    @FXML
    private TableColumn<Prescription, String> patientNameColumn;
    @FXML
    private TableColumn<Prescription, String> patientSurnameColumn;
    @FXML
    private TableColumn<Prescription, String> prescriptionObjectColumn;
    @FXML
    private TableColumn<Prescription, String> prescribedDrugColumn;
    @FXML
    private TableColumn<Prescription, String> quantityDrugColumn;
    @FXML
    private TableColumn<Prescription, Button> prescriptionDeleteColumn;
    @FXML
    private TableColumn<Prescription, Button> prescriptionModifyColumn;
    @FXML
    private Label errorLabel;
    @FXML
    private Button addPrescriptionButton;

    private ViewDispatcher dispatcher;
    private PrescriptionService prescriptionService;
    private Medic medic;

    public PrescriptionListMedicController() {
        dispatcher = ViewDispatcher.getInstance();
        FarmAppBusinessFactory factory = FarmAppBusinessFactory.getInstance();
        prescriptionService = factory.getPrescriptionService();
    }

    /*
    Il metodo "initialize" imposta le colonne della tabella in base a cosa devono contenere.
    I pulsanti creati in fase di popolazione della tabella permettono di modificare o cancellare una prescrizione.
    Se una prescrizione viene cancellata, viene ricaricata la vista.
    */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        prescriptionObjectColumn.setCellValueFactory(new PropertyValueFactory<>("prescriptionObject"));
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        patientSurnameColumn.setCellValueFactory(new PropertyValueFactory<>("patientSurname"));
        prescribedDrugColumn.setCellValueFactory(new PropertyValueFactory<>("prescribedDrug"));
        quantityDrugColumn.setCellValueFactory(new PropertyValueFactory<>("drugNumber"));

        prescriptionModifyColumn.setStyle("-fx-alignment: CENTER;");
        prescriptionModifyColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Prescription, Button>, ObservableValue<Button>>() {
            @Override
            public ObservableValue<Button> call(TableColumn.CellDataFeatures<Prescription, Button> param) {
                final Button modifyButton = new Button("Modifica");
                modifyButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        dispatcher.renderView("Prescription", param.getValue());
                    }
                });
                return new SimpleObjectProperty<Button>(modifyButton);
            }
        });
        prescriptionDeleteColumn.setStyle("-fx-alignment: CENTER;");
        prescriptionDeleteColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Prescription, Button>, ObservableValue<Button>>() {
            @Override
            public ObservableValue<Button> call(TableColumn.CellDataFeatures<Prescription, Button> param) {
                final Button deleteButton = new Button("Elimina");
                deleteButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            prescriptionService.deletePrescription(param.getValue().getId());
                            dispatcher.renderView("PrescriptionListMedic", param.getValue().getMedic());
                        } catch (BusinessException e) {
                            e.printStackTrace();
                        }

                    }
                });
                return new SimpleObjectProperty<Button>(deleteButton);
            }
        });
    }

    /*
    "initializeData" popola la tabella invocando findPrescriptionsByFiscalCode che restituisce la lista delle prescrizoni
    associate al codice fiscale del medico. Se il medico non ha prescrizioni assegnate viene sollevata eccezione e
    stampato un messaggio di errore.
    */
    @Override
    public void initializeData(Medic medic) {
        this.medic = medic;
        try {
            List<Prescription> prescriptionList = prescriptionService.findPrescriptionsByFiscalCode(medic);
            ObservableList<Prescription> prescriptionData = FXCollections.observableArrayList(prescriptionList);
            prescriptionListTable.setItems(prescriptionData);
        } catch (NoPrescriptionAssignedToFiscalCodeExc e) {
            errorLabel.setText("Non sono presenti prescrizioni.");
        } catch (BusinessException e) {
            dispatcher.renderError(e);
        }
    }

    /*
    "addNewPrescription" viene invocato nel momento in cui si vuole creare una nuova prescrizione, essa crea una istanza
    della classe prescription e assegna ad essa il medico che la sta creando.
    */
    @FXML
    public void addNewPrescription(ActionEvent event) {
        Prescription prescription = new Prescription();
        prescription.setMedic(medic);
        dispatcher.renderView("Prescription", prescription);
    }
}
