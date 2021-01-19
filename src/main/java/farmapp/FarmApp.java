/*
 * FarmApp
 *
 * 1.0 Exam Version.
 *
 * 18/07/2020
 */

package farmapp;

import farmapp.view.ViewDispatcher;
import javafx.application.Application;
import javafx.stage.Stage;

public class FarmApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        ViewDispatcher dispatcher = ViewDispatcher.getInstance();
        dispatcher.loginView(stage);
    }

}
