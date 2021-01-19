/*
 * DrugListPatientController
 *
 * Controller della vista DrugListPatient, da qui viene gestita la lista dei farmaci che il paziente può visualuizzare
 * con corrispondente descrizione e disponibiltà approssimata.
 *
 * 18/07/2020
 */

package farmapp.controller;

import farmapp.business.BusinessException;
import farmapp.business.DrugService;
import farmapp.business.FarmAppBusinessFactory;
import farmapp.domain.Drug;
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
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DrugListPatientController implements Initializable, DataInitializable<Object> {

    @FXML
    private TableView<Drug> drugListTable;
    @FXML
    private TableColumn<Drug, String> nameTableColumn;
    @FXML
    private TableColumn<Drug, Button> actionTableColumn;
    @FXML
    private Label nameDrugLabel;
    @FXML
    private Label drugPharmaceuticalCompanyLabel;
    @FXML
    private Label drugActivePrincipleLabel;
    @FXML
    private Label availabilityLabel;

    private ViewDispatcher dispatcher;
    private DrugService drugService;

    public DrugListPatientController() {
        dispatcher = ViewDispatcher.getInstance();
        FarmAppBusinessFactory factory = FarmAppBusinessFactory.getInstance();
        drugService = factory.getDrugService();
    }

    /*
    Nel metodo initialize vengono assegnati i valori di riferimento alle colonne, in una verrà visualizzato l'oggetto
    della prescrizione e nell'altra il pulsante "Dettagli." Premendo dettagli verrà visualizzata la descrizione del farmaco
    ed, in base alle quantità in magazzino, la disponibilità.
    */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("drugName"));
        actionTableColumn.setStyle("-fx-alignment: CENTER;");
        actionTableColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Drug, Button>, ObservableValue<Button>>() {
                    @Override
                    public ObservableValue<Button> call(TableColumn.CellDataFeatures<Drug, Button> param) {
                        final Button modifyButton = new Button("Dettagli");
                        modifyButton.setCenterShape(true);
                        modifyButton.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                nameDrugLabel.setText(param.getValue().getDrugName());
                                drugPharmaceuticalCompanyLabel.setText(param.getValue().getDrugPharmaceuticalCompany());
                                drugActivePrincipleLabel.setText(param.getValue().getDrugActivePrinciple());

                                int availability = param.getValue().getDrugAvailability();
                                int minimum = param.getValue().getDrugMinimumQuantity();
                                if (availability <= minimum && availability > 0) {
                                    availabilityLabel.setText("IN ESAURIMENTO");
                                    availabilityLabel.setTextFill(Color.GOLDENROD);
                                } else {
                                    if (availability == 0) {
                                        availabilityLabel.setText("ESAURITO");
                                        availabilityLabel.setTextFill(Color.RED);
                                    } else {
                                        availabilityLabel.setTextFill(Color.GREEN);
                                        availabilityLabel.setText("DISPONIBILE");

                                    }
                                }
                            }
                        });
                        return new SimpleObjectProperty<Button>(modifyButton);
                    }
                });
    }

    //Creazione della lista contenente tutti i farmaci.

    @Override
    public void initializeData(Object object) {
        try {
            List<Drug> drugs = drugService.findAllDrugs();
            ObservableList<Drug> drugsData = FXCollections.observableArrayList(drugs);
            drugListTable.setItems(drugsData);
        } catch (BusinessException e) {
            dispatcher.renderError(e);
        }
    }
}
