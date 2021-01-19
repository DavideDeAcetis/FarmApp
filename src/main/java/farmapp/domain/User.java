/*
 * User
 *
 * La classe utente contiene tutte le informazioni standard di un utente, le classi medic, admin, pharmacist e patient sono
 * figlie di Utente e ne ereditano metodi e variabili.
 *
 * 18/07/2020
 */

package farmapp.domain;

public class User {
    private Integer id;
    //la username è unica per ogni utente, in fase di registrazione non è possibile usare una username già esistente
    private String username;
    private String userPassword;
    private String name;
    private String surname;
    //Il codice fiscale è univoco ed è usato per assegnare al paziente e al medico le proprie prescrizioni.
    private String fiscalCode;
    /*
    Viene salvato un formato stringa della tipologia di utente per essere utilizzato in caso di controlli o per creare
    la stringa da scrivere su file come avviene nella modifica del profilo. E' possibile sostituirlo con un controllo
    switch/case e instanceof per e impostare la stringa solo li dove serve.
     */
    private String userType;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFiscalCode() {
        return fiscalCode;
    }

    public void setFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

}

