package farmapp.business.impl.ram;

import farmapp.business.BusinessException;
import farmapp.business.UserNotFoundExc;
import farmapp.business.UserService;
import farmapp.domain.*;

import java.util.List;

public class RAMUserServiceImpl implements UserService {


    @Override
    public User authenticate(String username, String password) throws UserNotFoundExc {
        if ("Admin".equalsIgnoreCase(username) && "Admin".equalsIgnoreCase(password)) {
            User admin = new Admin();
            admin.setUsername(username);
            admin.setUserPassword(password);
            admin.setName("Amministratore");
            admin.setSurname("Test");
            admin.setFiscalCode("AB");
            admin.setUserType("AMMINISTRATORE");
            return admin;
        }
        if ("Medic".equalsIgnoreCase(username) && "Medic".equalsIgnoreCase(password)) {
            Medic medic = new Medic();
            medic.setUsername(username);
            medic.setUserPassword(password);
            medic.setName("Medico");
            medic.setSurname("Test");
            medic.setFiscalCode("CD");
            medic.setUserType("MEDICO");
            return medic;
        }
        if ("Pharmacist".equalsIgnoreCase(username) && "Pharmacist".equalsIgnoreCase(password)) {
            Pharmacist pharmacist = new Pharmacist();
            pharmacist.setUsername(username);
            pharmacist.setUserPassword(password);
            pharmacist.setName("Farmacista");
            pharmacist.setSurname("Test");
            pharmacist.setFiscalCode("DE");
            pharmacist.setUserType("FARMACISTA");
            return pharmacist;
        }
        if ("Patient".equalsIgnoreCase(username) && "Patient".equalsIgnoreCase(password)) {
            Patient patient = new Patient();
            patient.setUsername(username);
            patient.setUserPassword(password);
            patient.setName("Paziente");
            patient.setSurname("Test");
            patient.setFiscalCode("FG");
            patient.setUserType("PAZIENTE");
            return patient;
        }

        throw new UserNotFoundExc();
    }

    @Override
    public void registration(User user) throws BusinessException {

    }

    @Override
    public void modifyProfile(User user) throws BusinessException {

    }

    @Override
    public void deleteAccount(int id) {

    }

    @Override
    public List<User> findAllUsers() throws BusinessException {
        return null;
    }
}
