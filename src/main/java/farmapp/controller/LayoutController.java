/*
 * LoginController
 *
 * Il layout controller si occupa di caricare, in base al tipo di utente che ha eseguito il login, infatti vengono dichiarati
 * degli array di Menu, univoci per ogni utente. Ad ogni pulsante del menu è associato il nome della vista corrispondente.
 *
 * 18/07/2020
 */

package farmapp.controller;

import farmapp.domain.Menu;
import farmapp.domain.*;
import farmapp.view.ViewDispatcher;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;

import java.net.URL;
import java.util.ResourceBundle;

public class LayoutController implements Initializable, DataInitializable<User> {

    private static final Menu MENU_HOME = new Menu("Home", "Home");

    private static final Menu[] MENU_MEDIC = {new Menu("Prescrizioni", "PrescriptionListMedic")};

    private static final Menu[] MENU_PATIENT = {new Menu("Farmaci", "DrugListPatient"),
                                                new Menu("Prescrizioni", "PrescriptionListPatient")};

    private static final Menu[] MENU_PHARMACIST = {new Menu("Cerca prescrizione", "PrescriptionSearch")};

    private static final Menu[] MENU_ADMIN = {new Menu("Magazzino", "DrugListAdmin"),
                                              new Menu("Gestione utenti", "UsersManagement")};

    @FXML
    private HBox menuBar;

    private ViewDispatcher dispatcher;
    private User user;


    public LayoutController() {
        dispatcher = ViewDispatcher.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    //initializeData si occupa di creare la serie di pulsanti caratteristici di ogni utente.

    @Override
    public void initializeData(User user) {
        this.user = user;
        menuBar.getChildren().addAll(createButton(MENU_HOME));
        Separator separator = new Separator();
        separator.setOrientation(javafx.geometry.Orientation.VERTICAL);
        menuBar.getChildren().add(separator);

        if (user instanceof Admin) {
            for (Menu menu : MENU_ADMIN) {
                menuBar.getChildren().add(createButton(menu));
                separator = new Separator();
                separator.setOrientation(javafx.geometry.Orientation.VERTICAL);
                menuBar.getChildren().add(separator);
            }
        }
        if (user instanceof Medic) {
            for (Menu menu : MENU_MEDIC) {
                menuBar.getChildren().add(createButton(menu));
                separator = new Separator();
                separator.setOrientation(javafx.geometry.Orientation.VERTICAL);
                menuBar.getChildren().add(separator);
            }
        }
        if (user instanceof Pharmacist) {
            for (Menu menu : MENU_PHARMACIST) {
                menuBar.getChildren().add(createButton(menu));
                separator = new Separator();
                separator.setOrientation(javafx.geometry.Orientation.VERTICAL);
                menuBar.getChildren().add(separator);
            }
        }
        if (user instanceof Patient) {
            for (Menu menu : MENU_PATIENT) {
                menuBar.getChildren().add(createButton(menu));
                separator = new Separator();
                separator.setOrientation(javafx.geometry.Orientation.VERTICAL);
                menuBar.getChildren().add(separator);
            }
        }
    }

    //profileView carica la vista che permette di visualizzare/modificare il profilo.

    @FXML
    public void profileView(ActionEvent event) {
        dispatcher.renderView("Profile", user);
    }

    //exitAction corrisponde al pulsante di logout, carica la pagina iniziale di login.

    @FXML
    public void exitAction(ActionEvent event) {
        dispatcher.logout();
    }

    //createButton è usato nella creazione dei singoli pulsanti all'interno di initializeData

    @FXML
    private Button createButton(Menu viewItem) {
        Button button = new Button(viewItem.getNome());
        button.setStyle("");
        button.getStylesheets().add(getClass().getResource("/viste/button.css").toExternalForm());
        button.getStyleClass().add("button");
        button.setTextFill(Paint.valueOf("black"));
        button.setPrefHeight(20);
        button.setPrefWidth(250);
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                dispatcher.renderView(viewItem.getVista(), user);
                mouseEvent.consume();
            }
        });
        return button;
    }


}

