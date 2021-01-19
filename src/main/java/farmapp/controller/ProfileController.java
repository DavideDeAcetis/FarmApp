/*
 * ProfileController
 *
 * CController della vista Profilo, da questa vista è possibile visualizzare e/o modificare i propri dati personali.
 *
 * 18/07/2020
 */

package farmapp.controller;

import farmapp.business.BusinessException;
import farmapp.business.FarmAppBusinessFactory;
import farmapp.business.UserAlreadyExistExc;
import farmapp.business.UserService;
import farmapp.domain.User;
import farmapp.view.ViewDispatcher;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController implements Initializable, DataInitializable<User> {

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
    private Label errorLabel;
    @FXML
    private Button modifyButton;

    private UserService userService;
    private ViewDispatcher dispatcher;
    private User user;

    public ProfileController() {
        dispatcher = ViewDispatcher.getInstance();
        FarmAppBusinessFactory factory = FarmAppBusinessFactory.getInstance();
        userService = factory.getUserService();
    }

    //Viene disabilitato il tasto "Modifica Profilo" finchè tutti i campi non sono riempiti con gli opportuni dati.
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        modifyButton.disableProperty().bind(usernameField.textProperty().isEmpty()
                .or(passwordField.textProperty().isEmpty()
                        .or(nameField.textProperty().isEmpty()
                                .or(surnameField.textProperty().isEmpty()
                                        .or(fiscalCodeField.textProperty().isEmpty())))));

    }

    //Viene popolata la vista con i dati dell'utente.
    @Override
    public void initializeData(User user) {
        this.user = user;
        nameField.setText(user.getName());
        surnameField.setText(user.getSurname());
        usernameField.setText(user.getUsername());
        passwordField.setText(user.getUserPassword());
        fiscalCodeField.setText(user.getFiscalCode());
    }

    /*
    Viene passata l'istanza dell'utente al metodo modifyProfile() in FileUserService che modifica il contenuto del file
    con la lista utenti aggiornata. In caso di username già in uso imposta un messaggio di errore e, in caso contrario,
    un messaggio di successo.
    */
    @FXML
    public void modifyProfile() throws BusinessException {
        User modifiedUser = new User();
        modifiedUser.setId(user.getId());
        modifiedUser.setUsername(usernameField.getText());
        modifiedUser.setUserPassword(passwordField.getText());
        modifiedUser.setName(nameField.getText());
        modifiedUser.setSurname(surnameField.getText());
        modifiedUser.setFiscalCode(fiscalCodeField.getText());
        modifiedUser.setUserType(user.getUserType());
        try {
            userService.modifyProfile(modifiedUser);
            user = modifiedUser;
            errorLabel.setTextFill(Color.GREEN);
            errorLabel.setText("Dati modificati con successo! effettuare di nuovo il login per confermare.");
        } catch (UserAlreadyExistExc e) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Username e/o codice fiscale già in uso.");
        }
    }
}