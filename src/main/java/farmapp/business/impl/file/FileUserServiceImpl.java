/*
 * FileUserService
 *
 * Metodi per autenticazione e registrazione di un utente.
 *
 * 18/07/2020
 */


package farmapp.business.impl.file;

import farmapp.business.BusinessException;
import farmapp.business.UserAlreadyExistExc;
import farmapp.business.UserNotFoundExc;
import farmapp.business.UserService;
import farmapp.domain.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class FileUserServiceImpl implements UserService {

    private String path;
    private List<User> usersList;
    private StringBuilder row;
    public FileUserServiceImpl(String path) {
        this.path = path;
    }

    /*
    Metodo che controlla se l'utente che tenta di effettuare login sia presente o meno nel sistema,
    in caso positivo controlla il tipo di utente e crea un nuovo oggetto utente in base ad esso.
    In caso non vi è presente una corrisopondeza viene lanciata eccezione e stampato un messaggio di errore.
    */
    @Override
    public User authenticate(String username, String password) throws BusinessException {
        try {
            FileData fileData = Utility.readAllRows(path);
            for (String[] column : fileData.getRows()) {
                if (column[1].equals(username) && column[2].equals(password)) {
                    User user = null;
                    String userType = "";
                    switch (column[3]) {
                        case "AMMINISTRATORE":
                            user = new Admin();
                            userType = "AMMINISTRATORE";
                            break;
                        case "MEDICO":
                            user = new Medic();
                            userType = "MEDICO";
                            break;
                        case "PAZIENTE":
                            user = new Patient();
                            userType = "PAZIENTE";
                            break;
                        case "FARMACISTA":
                            user = new Pharmacist();
                            userType = "FARMACISTA";
                            break;
                        default:
                            break;
                    }
                    if (user != null) {
                        user.setId(Integer.parseInt(column[0]));
                        user.setUsername(username);
                        user.setUserPassword(password);
                        user.setName(column[4]);
                        user.setSurname(column[5]);
                        user.setFiscalCode(column[6]);
                        user.setUserType(userType);
                    } else {
                        throw new BusinessException("errore nella lettura del file");
                    }
                    return user;
                }
            }
            throw new UserNotFoundExc();
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

    /*
    registration inserisce nel sistema un nuovo utente e se si tenta di registrare un utente con username e/o password
    già assegnati ad un altro utente viene lanciata eccezione e stampato un messaggio di errore.
    */
    @Override
    public void registration(User user) throws UserAlreadyExistExc {
        try {
            row = new StringBuilder();
            FileData fileData = Utility.readAllRows(path);
            for (String[] column : fileData.getRows()) {
                if (column[1].equals(user.getUsername()) || (column[6].equals(user.getFiscalCode()) && !column[6].equals("null"))) {
                    throw new UserAlreadyExistExc();
                }
            }
            try (PrintWriter writer = new PrintWriter(new File(path))) {
                long counter = fileData.getCounter();
                writer.println((counter + 1));
                for (String[] rows : fileData.getRows()) {
                    writer.println(String.join(Utility.SEPARATOR, rows));
                }
                createUserString(user, counter);
                writer.println(row.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    modifyProfile modifica i dati personali di un utente, se username e/o codice fiscale sono ugali a quelli di un altro
    utente già registrato viene lanciata eccezione e stampato un messaggio di errore.
    */
    @Override
    public void modifyProfile(User user) throws UserAlreadyExistExc {
        try {
            FileData fileData = Utility.readAllRows(path);
            for (String[] column : fileData.getRows()) {
                if ((column[1].equals(user.getUsername()) || column[6].equals(user.getFiscalCode())) && !column[0].equals(Integer.toString(user.getId()))) {
                    if (!(user.getUserType().equals("AMMINISTRATORE") || user.getUserType().equals("FARMACISTA"))) {
                        throw new UserAlreadyExistExc();
                    }
                }
            }
            try (PrintWriter writer = new PrintWriter(new File(path))) {
                row = new StringBuilder();
                long counter = fileData.getCounter();
                writer.println((counter));
                for (String[] column : fileData.getRows()) {
                    if (column[0].equals(user.getId().toString())) {
                        createUserString(user, user.getId());
                        writer.println(row.toString());
                    } else {
                        writer.println(String.join(Utility.SEPARATOR, column));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //deleteAccount permette ad un amministratore di cancellare l'account di un utente registrato.

    @Override
    public void deleteAccount(int id) {
        try {
            String drugId = Integer.toString(id);
            FileData fileData = Utility.readAllRows(path);
            try (PrintWriter writer = new PrintWriter(new File(path))) {
                long counter = fileData.getCounter();
                writer.println((counter - 1));
                boolean decreaseCondition = false;
                for (String[] column : fileData.getRows()) {
                    if (column[0].equals(drugId)) {
                        decreaseCondition = true;
                        continue;
                    }
                    if (decreaseCondition) {
                        column[0] = Integer.toString(Integer.parseInt(column[0]) - 1);
                    }
                    writer.println(String.join(Utility.SEPARATOR, column));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    Trova tutti gli utenti presenti nell'elenco escluso il primo che è l'amministratore con id 1.
    Il metodo viene usato per popolare la tabella della lista users dell'admin.
    */
    @Override
    public List<User> findAllUsers() throws BusinessException {
        usersList = new ArrayList<>();
        try {
            FileData fileData = Utility.readAllRows(path);
            for (String[] column : fileData.getRows()) {
                if(!column[0].equals("1")) {
                    User user = populateUser(column);
                    usersList.add(user.getId() - 2, user);
                }
            }
            return usersList;
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

    private User populateUser(String[] column) {
        User user = new User();
        user.setId(Integer.parseInt(column[0]));
        user.setUsername(column[1]);
        user.setUserPassword(column[2]);
        user.setUserType(column[3]);
        user.setName(column[4]);
        user.setSurname(column[5]);
        user.setFiscalCode(column[6]);
        return user;
    }

    //Crea la stringa da salvare all'interno del file Users.

    private void createUserString(User user, long counter) {
        row.append(counter);
        row.append(Utility.SEPARATOR);
        row.append(user.getUsername());
        row.append(Utility.SEPARATOR);
        row.append(user.getUserPassword());
        row.append(Utility.SEPARATOR);
        row.append(user.getUserType());
        row.append(Utility.SEPARATOR);
        row.append(user.getName());
        row.append(Utility.SEPARATOR);
        row.append(user.getSurname());
        row.append(Utility.SEPARATOR);
        row.append(user.getFiscalCode());
    }
}