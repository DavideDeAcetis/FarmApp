/*
 * DataInitializable
 *
 * Permette il passaggio di un oggetto da un controllore ad un altro al cambio della vista.
 *
 * 18/07/2020
 */

package farmapp.controller;

public interface DataInitializable<T> {

    default void initializeData(T t) {
    }
}
