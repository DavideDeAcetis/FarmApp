/*
 * ViewDispatcher
 *
 * Gestore principale dello stage, di tutte le viste e dei loro cambiamenti in fase di esecuzione
 *
 * 18/07/2020
 */


package farmapp.view;

import farmapp.controller.DataInitializable;
import farmapp.domain.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;


public class ViewDispatcher {

    private static final String RESOURCE_BASE = "/viste/";
    private static final String FXML_SUFFIX = ".fxml";

    private static ViewDispatcher instance = new ViewDispatcher(); //Unica istanza della classe.

    private Stage stage;
    private BorderPane layout;

    private ViewDispatcher() {
    } //Costruttore privato, non è possibile creare un istanza di questa classe.

    public static ViewDispatcher getInstance() {
        return instance;
    } //Restituisce l'unica istanza.

    //Riceve lo stage dal main e imposta la pagina di login.

    public void loginView(Stage stage) throws ViewException {
        try {
            this.stage = stage;
            FXMLLoader startPage = new FXMLLoader(getClass().getResource(RESOURCE_BASE + "login.fxml"));
            Parent login = startPage.load();
            Scene scene = new Scene(login);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            throw new ViewException(e);
        }
    }

    public void newUser() {
        try {
            Parent loginView = loadView("Registration").getView();
            Scene scene = new Scene(loginView);
            stage.setScene(scene);
        } catch (ViewException e) {
            renderError(e);
        }
    }

    public void loggedIn(User user) {
        try {
            View<User> layoutView = loadView("Layout");
            DataInitializable<User> layoutController = layoutView.getController();
            layoutController.initializeData(user);
            layout = (BorderPane) layoutView.getView();
            renderView("Home", user);
            Scene scene = new Scene(layout);
            scene.getStylesheets().add(getClass().getResource(RESOURCE_BASE + "tables.css").toExternalForm());
            stage.setScene(scene);
        } catch (ViewException e) {
            renderError(e);
        }
    }

    public void logout() {
        try {
            Parent loginView = loadView("Login").getView();
            Scene scene = new Scene(loginView);
            stage.setScene(scene);
        } catch (ViewException e) {
            renderError(e);
        }
    }

    //Chiamato quando è necessario cambiare la vista nel layout in base al pulsante premuto.

    public <T> void renderView(String viewName, T data) {
        try {
            View<T> view = loadView(viewName);
            DataInitializable<T> controller = view.getController();
            controller.initializeData(data);
            layout.setCenter(view.getView());
        } catch (ViewException e) {
            renderError(e);
        }

    }

    public void renderError(Exception e) {
        e.printStackTrace();
        System.exit(1);
    }

    //Si occupa del caricamento della nuova vista.

    private <T> View<T> loadView(String viewName) throws ViewException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(RESOURCE_BASE + viewName + FXML_SUFFIX));
            Parent parent = (Parent) loader.load();
            return new View<>(parent, loader.getController());

        } catch (IOException ex) {
            throw new ViewException(ex);
        }
    }
}
