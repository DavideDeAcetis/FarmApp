/*
 * PrescriptionSearchController
 *
 * Controller della vista PrescriptionSearch, da qui viene impostata la tabella dove vengono visualizzate le prescrizioni
 * in base al contenuto della barra di ricerca che dovrebbe corrispondere al codice fiscale del paziente a cui riferisce
 * la prescrizione. Data una prescrizione, sarà possibile visualizzare i dettagli di essa e/o evaderla.
 *
 * 18/07/2020
 */


package farmapp.controller;

import farmapp.business.*;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PrescriptionSearchController implements Initializable, DataInitializable<Object> {

    @FXML
    private TableView<Prescription> PrescriptionListTable;
    @FXML
    private TableColumn<Prescription, String> objectTableColumn;
    @FXML
    private TableColumn<Prescription, Button> detailsTableColumn;
    @FXML
    private TableColumn<Prescription, Button> processTableColumn;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label drugListLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private TextField searchBarField;
    @FXML
    private ImageView searchButtonImage;

    private ViewDispatcher dispatcher;
    private PrescriptionService prescriptionService;
    private DrugService drugService;

    public PrescriptionSearchController() {
        dispatcher = ViewDispatcher.getInstance();
        FarmAppBusinessFactory factory = FarmAppBusinessFactory.getInstance();
        prescriptionService = factory.getPrescriptionService();
        drugService = factory.getDrugService();
    }

    /*
    Il metodo initialize imposta le colonne della tabella in base a cosa devono contenere.
    I pulsanti creati in fase di popolazione della tabella permettono di visualizzare i dettagli di una prescrizione,
    in particolare il nome, il numero del farmaco prescritto e la descrizione della prescrizione.
    Quando una prescrizione viene evasa, essa non sarà più visibile nella lista né del farmacista né del medico ma solo
    nello storico del paziente.
    Inoltre viene diminuito il numero di farmaci in magazzino in base al numero di farmaci venduti, questo solleva
    eccezione se i farmaci in magazzino non sono sufficienti oppure da un messaggio di warning se i farmaci sono in
    esaurimento.
    */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        objectTableColumn.setCellValueFactory(new PropertyValueFactory<>("prescriptionObject"));
        detailsTableColumn.setStyle("-fx-alignment: CENTER;");
        detailsTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Prescription, Button>, ObservableValue<Button>>() {
            @Override
            public ObservableValue<Button> call(TableColumn.CellDataFeatures<Prescription, Button> param) {
                final Button detailsButton = new Button("Dettagli");
                detailsButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        descriptionLabel.setText(param.getValue().getPrescriptionDetails());
                        drugListLabel.setText(param.getValue().getPrescribedDrug() + " Qta.: " + param.getValue().getDrugNumber());
                    }
                });
                return new SimpleObjectProperty<Button>(detailsButton);
            }
        });
        processTableColumn.setStyle("-fx-alignment: CENTER;");
        processTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Prescription, Button>, ObservableValue<Button>>() {
            @Override
            public ObservableValue<Button> call(TableColumn.CellDataFeatures<Prescription, Button> param) {
                final Button processButton = new Button("Evadi");
                processButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            errorLabel.setTextFill(Color.RED);
                            errorLabel.setText(drugService.soldDrugs(param.getValue()));
                            prescriptionService.processPrescription(param.getValue());
                            PrescriptionListTable.getItems().remove(param.getValue());

                        } catch (DrugDoNotExistExc e) {
                            errorLabel.setTextFill(Color.RED);
                            errorLabel.setText(param.getValue().getPrescribedDrug() + " non è presente nella lista.");

                        } catch (NoDrugsAvailableExc e) {
                            errorLabel.setTextFill(Color.RED);
                            errorLabel.setText("Il numero di farmaci prescritti supera la disponibiltà in magazzino.");

                        } catch (BusinessException e) {
                            e.printStackTrace();
                        }
                    }
                });
                return new SimpleObjectProperty<Button>(processButton);
            }
        });
    }

    /*
    Il metodo "searchPrescriptions" è chiamato dal click dell'immagine del punto interrogativo presente di fianco alla
    barra di ricerca, il metodo crea una lista che verrà passata alla tabella per la popolazione delle colonne.
    Il metodo chiamato "findAllPrescriptions" restituirà la lista di prescrizioni associate al codice fiscale inserito
    nella barra di ricerca. Se la lista è vuota, e quindi non vi è nessuna prescrizione associata al codice fiscale,
    viene sollevata eccezione e dato un messaggio di errore.
    */
    @FXML
    public void searchPrescriptions(MouseEvent event) {
        try {
            errorLabel.setText("");
            descriptionLabel.setText("");
            drugListLabel.setText("");
            try {
                List<Prescription> prescriptionList = prescriptionService.findPrescriptionsByFiscalCode(searchBarField.getText());
                ObservableList<Prescription> prescriptionData = FXCollections.observableArrayList(prescriptionList);
                PrescriptionListTable.setItems(prescriptionData);
            } catch (NoPrescriptionAssignedToFiscalCodeExc e) {
                errorLabel.setTextFill(Color.RED);
                errorLabel.setText("Nessuna prescrizione associata a: " + searchBarField.getText() + ".");
            }
        } catch (BusinessException e) {
            dispatcher.renderError(e);
        }
    }
}
