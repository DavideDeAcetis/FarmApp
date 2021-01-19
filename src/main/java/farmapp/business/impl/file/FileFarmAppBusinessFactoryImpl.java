/*
 * FileFarmAppBusinessFactory
 *
 * Il business factory che si occupa di gestire i file. Contiene le path di ogni file e li passa come variabili attuali
 * ai costruttori dei vari service. Il business factory utilizza il pattern singleton rendendo le istanze dei service unici.
 *
 * 18/07/2020
 */

package farmapp.business.impl.file;

import farmapp.business.*;

import java.io.File;

public class FileFarmAppBusinessFactoryImpl extends FarmAppBusinessFactory {
    private UserService userService;
    private DrugService drugService;
    private PrescriptionService prescriptionService;

    private static final String REPOSITORY_BASE = "src" + File.separator + "main"
            + File.separator + "resources" + File.separator + "data";
    private static final String USERS_FILE_NAME = REPOSITORY_BASE
            + File.separator + "Users.txt";
    private static final String PRESCRIPTIONS_FILE_NAME = REPOSITORY_BASE
            + File.separator + "Prescriptions.txt";
    private static final String DRUGS_FILE_NAME = REPOSITORY_BASE
            + File.separator + "Drugs.txt";
    private static final String PRESCRIPTION_LIST = REPOSITORY_BASE
            + File.separator + "Prescriptions.txt";

    public FileFarmAppBusinessFactoryImpl() {
        userService = new FileUserServiceImpl(USERS_FILE_NAME);
        drugService = new FileDrugServiceImpl(DRUGS_FILE_NAME);
        prescriptionService = new FilePrescriptionServiceImpl(PRESCRIPTION_LIST);
    }

    @Override
    public UserService getUserService() {
        return userService;
    }

    @Override
    public DrugService getDrugService() {
        return drugService;
    }

    @Override
    public PrescriptionService getPrescriptionService() {
        return prescriptionService;
    }

}