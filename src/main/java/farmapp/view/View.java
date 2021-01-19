/*
 * View<T>
 *
 * Rappresentà l'entità vista e contiene il controllore associato ad essa.
 *
 * 18/07/2020
 */

package farmapp.view;

import farmapp.controller.DataInitializable;
import javafx.scene.Node;
import javafx.scene.Parent;

public class View<T> extends Parent {
    private Parent view;
    private DataInitializable<T> controller;

    public View(Parent view, DataInitializable<T> controller) {
        super();
        this.view = view;
        this.controller = controller;
    }

    public Parent getView() {
        return view;
    }

    public void setView(Parent view) {
        this.view = view;
    }

    public DataInitializable<T> getController() {
        return controller;
    }

    public void setController(DataInitializable<T> controller) {
        this.controller = controller;
    }

    @Override
    public Node getStyleableNode() {
        return null;
    }
}
