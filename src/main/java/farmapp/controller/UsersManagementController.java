/*
 * UsersManagementController
 *
 * Controller della vista UsersManagement, metodi che permettono ad un amministratore di gestire gli utenti all'interno del
 * sistema e di registrare nuovi farmacisti.
 *
 * 18/07/2020
 */

package farmapp.controller;

import farmapp.business.BusinessException;
import farmapp.business.FarmAppBusinessFactory;
import farmapp.business.UserService;
import farmapp.domain.Admin;
import farmapp.domain.Pharmacist;
import farmapp.domain.User;
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
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UsersManagementController implements Initializable, DataInitializable<Admin> {

    @FXML
    private TextField nameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button registerButton;
    @FXML
    private TableView<User> usersTableView;
    @FXML
    private TableColumn<User, String> nameColumn;
    @FXML
    private TableColumn<User, String> surnameColumn;
    @FXML
    private TableColumn<User, String> userTypeColumn;
    @FXML
    private TableColumn<User, Button> actionColumn;
    @FXML
    private Label errorLabel;

    private UserService userService;
    private ViewDispatcher dispatcher;
    private Admin admin;
    private List<User> users;

    public UsersManagementController() {
        dispatcher = ViewDispatcher.getInstance();
        FarmAppBusinessFactory factory = FarmAppBusinessFactory.getInstance();
        userService = factory.getUserService();
    }

    /*
    Disabilita il pulsante registrazione se i campi sono vuoti.
    Imposta la tabella per essere popolata con i dati di un utente ed un pulsante per eliminare l'utente.
    */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        registerButton.disableProperty().bind(nameField.textProperty().isEmpty()
                .or(surnameField.textProperty().isEmpty()
                        .or(usernameField.textProperty().isEmpty()
                                .or(passwordField.textProperty().isEmpty()))));

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        userTypeColumn.setCellValueFactory(new PropertyValueFactory<>("userType"));
        actionColumn.setStyle("-fx-alignment: CENTER;");
        actionColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<User, Button>, ObservableValue<Button>>() {
                    @Override
                    public ObservableValue<Button> call(TableColumn.CellDataFeatures<User, Button> param) {
                        final Button deleteButton = new Button("Elimina");
                        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                userService.deleteAccount(param.getValue().getId());
                                dispatcher.renderView("UsersManagement", admin);
                            }
                        });
                        return new SimpleObjectProperty<Button>(deleteButton);
                    }
                });
    }

    @Override
    public void initializeData(Admin admin) {
        this.admin = admin;
        try {
            users = userService.findAllUsers();
            ObservableList<User> usersData = FXCollections.observableArrayList(users);
            usersTableView.setItems(usersData);
        } catch (BusinessException e) {
            dispatcher.renderError(e);
        }
    }

    /*
    registerPharmacist permette la registrazione di un farmacista.
    */
    public void registerPharmacist() {
        Pharmacist pharmacist = new Pharmacist();
        pharmacist.setName(nameField.getText());
        pharmacist.setUsername(usernameField.getText());
        pharmacist.setUserPassword(passwordField.getText());
        pharmacist.setSurname(surnameField.getText());
        pharmacist.setUserType("FARMACISTA");
        try {
            userService.registration(pharmacist);
            dispatcher.renderView("UsersManagement", admin);
        } catch (BusinessException e) {
            errorLabel.setText("Username già in uso");
        }

    }
}
