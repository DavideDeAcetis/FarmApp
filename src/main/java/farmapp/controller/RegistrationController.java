/*
 * RegistrationController
 *
 * Controller della vista di registrazione.
 *
 * 18/07/2020
 */

package farmapp.controller;

import farmapp.business.BusinessException;
import farmapp.business.FarmAppBusinessFactory;
import farmapp.business.UserAlreadyExistExc;
import farmapp.business.UserService;
import farmapp.domain.*;
import farmapp.view.ViewDispatcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class RegistrationController implements Initializable, DataInitializable<Object> {

    @FXML
    private TextField nameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField fiscalCodeField;
    @FXML
    private ComboBox<UserType> userTypeComboBox;
    @FXML
    private Label errorLabel;
    @FXML
    private Button registerButton;
    @FXML
    private Button discardButton;

    private ViewDispatcher dispatcher;
    private UserService userService;
    private User user;
    private String userType;

    public RegistrationController() {
        dispatcher = ViewDispatcher.getInstance();
        FarmAppBusinessFactory factory = FarmAppBusinessFactory.getInstance();
        userService = factory.getUserService();
    }

    //Viene disabilitato il tasto "registrati" finchè tutti i campi non sono riempiti con gli opportuni dati.
    //All'interno di "userTypeComboBox" vengono inseriti gli elementi della classe enumerativa UserType.
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userTypeComboBox.getItems().addAll(UserType.values());

        registerButton.disableProperty().bind(usernameField.textProperty().isEmpty()
                .or(passwordField.textProperty().isEmpty()
                        .or(nameField.textProperty().isEmpty()
                                .or(surnameField.textProperty().isEmpty()
                                        .or(fiscalCodeField.textProperty().isEmpty())
                                            .or(userTypeComboBox.valueProperty().isNull())))));
    }

    /*
    Il metodo "registerNewUser" viene invocato nel momento in cui si tenta di registrare un nuovo utente, in questa fase
    viene creata l'istanza della classe utente in base al tipo di utente, successivamente viene invocato il metodo in
    FileUserService che riceve in ingresso la nuova istanza di utente. Se l'username e/o il codice fiscale è già
    esistente viene sollevata l'eccezione "UserAlreadyExistExc" che oltre a stampare un messaggio di errore e a bloccare
    la registrazione imposta a null l'istanza di utente precedentemente creata per evitare di occupare memoria.
    Al termine della registrazione viene reimpostata la pagina di login.
    */
    @FXML
    public void registerNewUser(ActionEvent event) throws BusinessException {
        switch (userTypeComboBox.getValue().toString()) {
            case "AMMINISTRATORE":
                user = new Admin();
                userType = "AMMINISTRATORE";
                populateUserInstance(user);
                break;
            case "MEDICO":
                user = new Medic();
                userType = "MEDICO";
                populateUserInstance(user);
                break;
            case "PAZIENTE":
                user = new Patient();
                userType = "PAZIENTE";
                populateUserInstance(user);
                break;
            case "FARMACISTA":
                user = new Pharmacist();
                userType = "FARMACISTA";
                populateUserInstance(user);
                break;
            default:
                break;
        }
        try {
            userService.registration(user);
            dispatcher.logout();
        } catch (UserAlreadyExistExc e) {
            user = null;
            errorLabel.setText("Username e/o codice fiscale già in uso");
        }
    }

    //Se si annulla una registrazione si verrà rindirizzati alla pagina di login.
    @FXML
    public void discardRegistration(ActionEvent event) {
        dispatcher.logout();
    }

    //Metodo che popola l'istanza di user, usato per migliorare la legibilità del codice.
    private void populateUserInstance(User user) {
        user.setUsername(usernameField.getText());
        user.setUserPassword(passwordField.getText());
        user.setName(nameField.getText());
        user.setSurname(surnameField.getText());
        user.setFiscalCode(fiscalCodeField.getText());
        user.setUserType(userType);
    }


}
