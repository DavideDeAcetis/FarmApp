/*
 * HomeController
 *
 * Prima vista caricata dopo il login, Mostra semplicemente un messaggio di benvenuto seguito dal nome utente.
 *
 * 18/07/2020
 */

package farmapp.controller;

import farmapp.domain.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable, DataInitializable<User> {

    @FXML
    private Label wellComeLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @Override
    public void initializeData(User user) {
        StringBuilder text = new StringBuilder();
        text.append("Benvenuto ");
        text.append(user.getName());
        text.append(" ");
        text.append(user.getSurname());
        wellComeLabel.setText(text.toString());
    }
}
