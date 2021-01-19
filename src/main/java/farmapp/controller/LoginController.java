/*
 * LoginController
 *
 * Controller della vista login, da qui viene gestito l'ingesso dell'utente nel menu o la registrazione di un nuovo utente.
 *
 * 18/07/2020
 */

package farmapp.controller;

import farmapp.business.BusinessException;
import farmapp.business.FarmAppBusinessFactory;
import farmapp.business.UserNotFoundExc;
import farmapp.business.UserService;
import farmapp.domain.User;
import farmapp.view.ViewDispatcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable, DataInitializable<Object> {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button logInButton;
    @FXML
    private Button registerButton;
    @FXML
    private Label loginErrorLabel;

    private ViewDispatcher dispatcher;
    private UserService userService;

    public LoginController() {
        dispatcher = ViewDispatcher.getInstance();
        FarmAppBusinessFactory factory = FarmAppBusinessFactory.getInstance();
        userService = factory.getUserService();
    }

    //Il pulsante di login viene disabilitato fintantoché i campi username e password sono vuoti

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logInButton.disableProperty().bind(usernameField.textProperty().isEmpty().or(passwordField.textProperty().isEmpty()));
    }

    /*
    Il metodo login chiama il metodo authenticate all'interno di FileUserService e gli passa username e password,
    in caso di successo si viene reindirizzati alla pagina del menù altrimenti si ha un messaggio di errore.
    */
    @FXML
    public void login(ActionEvent event) {
        try {
            User user = userService.authenticate(usernameField.getText(), passwordField.getText());
            dispatcher.loggedIn(user);
        } catch (UserNotFoundExc userNotFoundExc) {
            loginErrorLabel.setText("Username o Password errati");
        } catch (BusinessException businessException) {
            businessException.printStackTrace();
        }
    }

    //Il metodo register invoca il metodod newUser in ViewDispatcher che modifica la vista in quella di login.

    @FXML
    public void register(ActionEvent event) {
        dispatcher.newUser();
    }
}
