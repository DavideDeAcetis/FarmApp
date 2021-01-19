/*
 * Menu
 *
 * Rappresenta l'entità Menu, utilizzato in fase di popolamento del layout con i pulsanti
 *
 * 18/07/2020
 */

package farmapp.domain;

public class Menu {
    private String nome;
    private String vista;

    public Menu(String nome, String vista) {
        this.nome = nome;
        this.vista = vista;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getVista() {
        return vista;
    }

    public void setVista(String vista) {
        this.vista = vista;
    }
}
