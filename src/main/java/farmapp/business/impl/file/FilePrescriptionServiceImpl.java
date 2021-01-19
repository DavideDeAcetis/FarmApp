/*
 * FilePrescriptionService
 *
 * Metodi per modificare/cancellare prescrizioni o generare liste di prescrizioni.
 *
 * 18/07/2020
 */

package farmapp.business.impl.file;

import farmapp.business.BusinessException;
import farmapp.business.NoPrescriptionAssignedToFiscalCodeExc;
import farmapp.business.PrescriptionService;
import farmapp.domain.Medic;
import farmapp.domain.Patient;
import farmapp.domain.Prescription;
import farmapp.domain.User;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class FilePrescriptionServiceImpl implements PrescriptionService {

    private String path;
    private List<Prescription> prescriptions;
    private StringBuilder row;

    public FilePrescriptionServiceImpl(String path) {
        this.path = path;
    }

    //createPrescription aggiunge una nuova prescrizione come ultima riga del file.

    @Override
    public void createPrescription(Prescription prescription) {
        try {
            row = new StringBuilder();
            FileData fileData = Utility.readAllRows(path);
            try (PrintWriter writer = new PrintWriter(new File(path))) {
                long counter = fileData.getCounter();
                writer.println((counter + 1));
                for (String[] column : fileData.getRows()) {
                    writer.println(String.join(Utility.SEPARATOR, column));
                }
                row.append(counter);
                createPrescriptionString(prescription);
                row.append(prescription.getProcessedPrescription());
                writer.println(row.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //updatePrescription riscrive la prescrizione modificata nella stessa riga.

    @Override
    public void updatePrescription(Prescription prescription) {
        try {
            row = new StringBuilder();
            FileData fileData = Utility.readAllRows(path);
            try (PrintWriter writer = new PrintWriter(new File(path))) {
                long counter = fileData.getCounter();
                writer.println((counter));
                row.append(prescription.getId());
                createPrescriptionString(prescription);
                row.append("0");
                for (String[] column : fileData.getRows()) {
                    if (column[0].equals(Integer.toString(prescription.getId()))) {
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

    /*
    deletePrescription elimina una prescrizione dal file. La decreaseCondition è usata per stampare
    le stringhe successive del file con indice diminuito di 1
    */
    @Override
    public void deletePrescription(int id) {
        try {
            String prescriptionId = Integer.toString(id);
            FileData fileData = Utility.readAllRows(path);
            try (PrintWriter writer = new PrintWriter(new File(path))) {
                long counter = fileData.getCounter();
                writer.println((counter - 1));
                boolean decreaseCondition = false;
                for (String[] column : fileData.getRows()) {
                    if (column[0].equals(prescriptionId)) {
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
    processPrescription viene invocato quando un farmacista evade una prescrizione impostando il valore finale
    della stringa ad 1.
    Questo valore verrà utilizzato nei controlli delle tabelle per visualizzare o meno le prescrizioni evase.
    */
    @Override
    public void processPrescription(Prescription prescription) {
        try {
            FileData fileData = Utility.readAllRows(path);
            try (PrintWriter writer = new PrintWriter(new File(path))) {
                long counter = fileData.getCounter();
                writer.println((counter));
                row = new StringBuilder();
                for (String[] column : fileData.getRows()) {
                    if (column[0].equals(Integer.toString(prescription.getId()))) {
                        row.append(prescription.getId());
                        createPrescriptionString(prescription);
                        row.append("1");
                        writer.println(row);
                        continue;
                    }
                    writer.println(String.join(Utility.SEPARATOR, column));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    findPrescriptionsByFiscalCode restituisce una lista di prescrizoni in base al codice fiscale dell'utente passato.
    Solleva eccezione se non ci sono prescrizioni e stampa un messaggio di errore.
    Questo metodo viene usato per popolare le tabelle delle prescrizioni di medico e paziente.
    */
    @Override
    public List<Prescription> findPrescriptionsByFiscalCode(User user) throws BusinessException {
        prescriptions = new ArrayList<>();
        int counter = 0;
        try {
            FileData fileData = Utility.readAllRows(path);
            Prescription prescription;
            for (String[] column : fileData.getRows()) {
                if (user instanceof Medic) {
                    prescription = populatePrescription(column);
                    if (user.getFiscalCode().equals(column[5]) && prescription.getProcessedPrescription() == 0) {
                        prescription.setMedic((Medic) user);
                        prescriptions.add(counter, prescription);
                        counter++;
                    }
                }
                if (user instanceof Patient) {
                    prescription = populatePrescription(column);
                    if (user.getFiscalCode().equals(column[4]) && prescription.getProcessedPrescription() == 0) {
                        prescriptions.add(counter, prescription);
                        counter++;
                    }
                }
            }
            if (prescriptions.isEmpty()) {
                throw new NoPrescriptionAssignedToFiscalCodeExc();
            }
            return prescriptions;
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

    /*
    findPrescriptionsByFiscalCode restituisce una lista di prescrizoni in base al codice fiscale passato.
    Solleva eccezione se non ci sono prescrizioni e stampa un messaggio di errore.
    Questo metodo viene usato per popolare la tabella del farmacista e viene usato il codice fiscale inserito nella
    barra di ricerca.
    */
    @Override
    public List<Prescription> findPrescriptionsByFiscalCode(String fiscalCode) throws BusinessException {
        prescriptions = new ArrayList<>();
        int counter = 0;
        try {
            FileData fileData = Utility.readAllRows(path);
            Prescription prescription;
            for (String[] column : fileData.getRows()) {
                prescription = populatePrescription(column);
                if (fiscalCode.equals(column[4]) && prescription.getProcessedPrescription() == 0) {
                    prescriptions.add(counter, prescription);
                    counter++;
                }
            }
            if (prescriptions.isEmpty()) {
                throw new NoPrescriptionAssignedToFiscalCodeExc();
            }

            return prescriptions;
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

    /*
    findPrescriptionHistory restituisce una lista di prescrizoni in base al codice finale della stringa salvata nel file.
    Solleva eccezione se non ci sono prescrizioni e stampa un messaggio di errore.
    Questo metodo viene usato per popolare la tabella del paziente quando visualizza lo storico prescrizioni.
    */
    @Override
    public List<Prescription> findPrescriptionHistory(User user) throws BusinessException {
        prescriptions = new ArrayList<>();
        int counter = 0;
        try {
            FileData fileData = Utility.readAllRows(path);
            Prescription prescription;
            for (String[] column : fileData.getRows()) {
                if (user instanceof Patient) {
                    prescription = populatePrescription(column);
                    if (user.getFiscalCode().equals(column[4]) && prescription.getProcessedPrescription() == 1) {
                        prescriptions.add(counter, prescription);
                        counter++;
                    }
                }
            }
            if (prescriptions.isEmpty()) {
                throw new NoPrescriptionAssignedToFiscalCodeExc();
            }
            return prescriptions;
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

    // createPrescriptionString crea la stringa di una prescrizione da salvare in memoria.

    private void createPrescriptionString(Prescription prescription) {
        row.append(Utility.SEPARATOR);
        row.append(prescription.getPatientName());
        row.append(Utility.SEPARATOR);
        row.append(prescription.getPatientSurname());
        row.append(Utility.SEPARATOR);
        row.append(prescription.getPrescriptionDetails());
        row.append(Utility.SEPARATOR);
        row.append(prescription.getPatientFiscalCode());
        row.append(Utility.SEPARATOR);
        row.append(prescription.getMedic().getFiscalCode());
        row.append(Utility.SEPARATOR);
        row.append(prescription.getPrescriptionObject());
        row.append(Utility.SEPARATOR);
        row.append(prescription.getDrugNumber());
        row.append(Utility.SEPARATOR);
        row.append(prescription.getPrescribedDrug());
        row.append(Utility.SEPARATOR);
    }

    /*
    populatePrescription crea una nuova istanza di Prescription e imposta i valori delle variabili di istanza con
    il contenuto della stringa salvata nel file.
    */
    private Prescription populatePrescription(String[] column) {
        Prescription prescription = new Prescription();
        prescription.setId(Integer.parseInt(column[0]));
        prescription.setPatientName(column[1]);
        prescription.setPatientSurname(column[2]);
        prescription.setPrescriptionDetails(column[3]);
        prescription.setPatientFiscalCode(column[4]);
        prescription.setMedic(new Medic());
        prescription.getMedic().setFiscalCode(column[5]);
        prescription.setPrescriptionObject(column[6]);
        prescription.setDrugNumber(Integer.parseInt(column[7]));
        prescription.setPrescribedDrug(column[8]);
        prescription.setProcessedPrescription(Integer.parseInt(column[9]));
        return prescription;
    }
}