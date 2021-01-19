/*
 * DrugListAdminController
 *
 * Controller della vista DrugListAdmin, da qui viene gestita la lista dei farmaci caricati nel sistema dall'admin
 * e la possibilità di poterli modificare o cancellare.
 * I tipo generico passato da DataInitializable è Object in quanto in fase di caricamento della vista Drug per la creazione
 * del farmaco viene passato un oggetti di tipo admin mentre, in fase di ritorno alla vista DrugListAdmin, viene passato
 * un oggetto di tipo Admin.
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DrugListAdminController implements Initializable, DataInitializable<Object> {

    @FXML
    private TableView<Drug> drugListTable;
    @FXML
    private TableColumn<Drug, String> nameTableColumn;
    @FXML
    private TableColumn<Drug, Integer> codeTableColumn;
    @FXML
    private TableColumn<Drug, String> houseTableColumn;
    @FXML
    private TableColumn<Drug, String> activePrincipleTableColumn;
    @FXML
    private TableColumn<Drug, Integer> availabilityTableColumn;
    @FXML
    private TableColumn<Drug, Button> modifyTableColumn;
    @FXML
    private TableColumn<Drug, Button> deleteTableColumn;
    @FXML
    private Button addNewDrugButton;
    @FXML
    private Button viewLowDrugsButton;
    @FXML
    private Button supplyButton;

    private ViewDispatcher dispatcher;
    private DrugService drugService;
    private boolean reloadPage;
    private Object object;

    public DrugListAdminController() {
        dispatcher = ViewDispatcher.getInstance();
        FarmAppBusinessFactory factory = FarmAppBusinessFactory.getInstance();
        drugService = factory.getDrugService();
    }

    /*
    Nel metodo initialize vengono assgnati i valori di riferimento alle colonne, in ogni colonna sarà presente
    un dettaglio del farmaco e i pulsanti elimina e modifica.
    In caso di modifica viene caricata la pagina di creazione di un farmaco e vengono popolati i campi con l'attuale
    dicitura del farmaco da modificare.
    */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        codeTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        codeTableColumn.setStyle("-fx-alignment: CENTER;");
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("drugName"));
        houseTableColumn.setCellValueFactory(new PropertyValueFactory<>("drugPharmaceuticalCompany"));
        activePrincipleTableColumn.setCellValueFactory(new PropertyValueFactory<>("drugActivePrinciple"));
        availabilityTableColumn.setCellValueFactory(new PropertyValueFactory<>("drugAvailability"));
        availabilityTableColumn.setStyle("-fx-alignment: CENTER;");
        modifyTableColumn.setStyle("-fx-alignment: CENTER;");
        modifyTableColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Drug, Button>, ObservableValue<Button>>() {
                    @Override
                    public ObservableValue<Button> call(TableColumn.CellDataFeatures<Drug, Button> param) {
                        final Button modifyButton = new Button("Modifica");
                        modifyButton.setCenterShape(true);
                        modifyButton.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                dispatcher.renderView("Drug", param.getValue());
                            }
                        });
                        return new SimpleObjectProperty<Button>(modifyButton);
                    }
                });
        deleteTableColumn.setStyle("-fx-alignment: CENTER;");
        deleteTableColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Drug, Button>, ObservableValue<Button>>() {
                    @Override
                    public ObservableValue<Button> call(TableColumn.CellDataFeatures<Drug, Button> param) {
                        final Button modifyButton = new Button("Elimina");
                        modifyButton.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                try {
                                    int id = param.getValue().getId();
                                    drugService.deleteDrug(id);
                                    dispatcher.renderView("DrugListAdmin", param.getValue());
                                } catch (BusinessException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        return new SimpleObjectProperty<Button>(modifyButton);
                    }
                });
    }

    //nel metodo initializeData viene creata la lista di tutti i farmaci in magazzino.

    public void initializeData(Object object) {
        this.object = object;
        reloadPage = false;
        try {
            List<Drug> drugs = drugService.findAllDrugs();
            ObservableList<Drug> drugsData = FXCollections.observableArrayList(drugs);
            drugListTable.setItems(drugsData);
        } catch (BusinessException e) {
            dispatcher.renderError(e);
        }
    }

    //Nel metodo newDrugMenu viene caricata la pagina per la creazione dei farmaci e gli viene passato un nuovo oggetto farmaco.

    @FXML
    public void newDrugMenu(ActionEvent event) {
        Drug drug = new Drug();
        dispatcher.renderView("Drug", drug);
    }

    /*
    Nel metodo viewLowDrugs viene modificata la tabella inserendo solo i farmaci che sono al di sotto del limite minimo.
    Nel caso in cui si vuole tornare indietro la pagina verrà ricaricata.
    */

    @FXML
    public void viewLowDrugs(ActionEvent event) {
        try {
            if (!reloadPage) {
                List<Drug> drugs = drugService.findLowDrugs();
                ObservableList<Drug> drugsData = FXCollections.observableArrayList(drugs);
                drugListTable.setItems(drugsData);
                viewLowDrugsButton.setText("Visualizza tutti\ni farmaci");
                reloadPage = true;
            } else {
                dispatcher.renderView("DrugListAdmin", object);
            }

        } catch (BusinessException e) {
            dispatcher.renderError(e);
        }
    }

    @FXML
    public void supplyDrug (ActionEvent event) {
        dispatcher.renderView("Supply", object);
    }
}