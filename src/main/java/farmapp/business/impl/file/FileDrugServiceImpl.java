/*
 * FileDrugService
 *
 * Metodi per cercare, cancellare o modificare la disponibilià di un farmaco dopo la vendita.
 *
 * 18/07/2020
 */

package farmapp.business.impl.file;

import farmapp.business.*;
import farmapp.domain.Drug;
import farmapp.domain.Prescription;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileDrugServiceImpl implements DrugService {

    private String path;
    private List<Drug> drugs;
    private StringBuilder row;


    public FileDrugServiceImpl(String path) {
        this.path = path;
    }

    @Override
    public void createDrug(Drug drug) {
        row = new StringBuilder();
        try {
            FileData fileData = Utility.readAllRows(path);
            try (PrintWriter writer = new PrintWriter(new File(path))) {
                long counter = fileData.getCounter();
                writer.println((counter + 1));
                for (String[] column : fileData.getRows()) {
                    writer.println(String.join(Utility.SEPARATOR, column));
                }
                row.append(counter);
                createDrugString(drug);
                writer.println(row.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateDrug(Drug drug) {
        row = new StringBuilder();
        try {
            FileData fileData = Utility.readAllRows(path);
            try (PrintWriter writer = new PrintWriter(new File(path))) {
                long counter = fileData.getCounter();
                writer.println((counter));
                if (drug.getId() != null) {
                    row.append(drug.getId());
                    createDrugString(drug);
                    for (String[] column : fileData.getRows()) {
                        if (column[0].equals(Integer.toString(drug.getId()))) {
                            writer.println(row.toString());
                        } else {
                            writer.println(String.join(Utility.SEPARATOR, column));
                        }
                    }
                } else {
                    for (String[] column : fileData.getRows()) {
                        if(column[1].equals(drug.getDrugName())){
                            Drug updatedDrug = populateDrug(column);
                            updatedDrug.setDrugAvailability(drug.getDrugAvailability() + updatedDrug.getDrugAvailability());
                            row.append(updatedDrug.getId());
                            createDrugString(updatedDrug);
                            writer.println(row.toString());
                        } else {
                            writer.println(String.join(Utility.SEPARATOR, column));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    deleteDrug elimina un farmaco dal file. La decreaseCondition è usata per stampare le stringe successive
    del file con indice diminuito di 1.
    */
    @Override
    public void deleteDrug(int id) {
        try {
            String drugId = Integer.toString(id);
            FileData fileData = Utility.readAllRows(path);
            try (PrintWriter writer = new PrintWriter(new File(path))) {
                long counter = fileData.getCounter();
                writer.println((counter - 1));
                boolean decreaseCondition = false;
                for (String[] column : fileData.getRows()) {
                    if (column[0].equals(drugId)) {
                        drugs.remove(id - 1);
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
    soldDrugs viene invocato in fase di evasione di una prescrizione in prescriptionSearchController e diminuisce
    la quantità di farmaci in magazzino in base alla quantità venduta.
    Vengono eseguiti controlli sulla quantità del farmaco in magazzino sollevando eccezione se il farmaco non è
    disponibile o non è sufficente e restituisce una stringa che indica se il farmaco è in esaurimento o no.
    */
    @Override
    public String soldDrugs(Prescription prescription) throws NoDrugsAvailableExc, DrugDoNotExistExc {
        boolean drugFound = false;
        try {
            String lowDrugsError = "";
            FileData fileData = Utility.readAllRows(path);
            //controllo delle quantità dei farmaci.
            for (String[] column : fileData.getRows()) {
                if (column[1].equals(prescription.getPrescribedDrug())) {
                    drugFound = true;
                    int updatedDrugAvailability = Integer.parseInt(column[5]) - prescription.getDrugNumber();
                    if (updatedDrugAvailability <= 0) {
                        throw new NoDrugsAvailableExc();
                    }
                    if (updatedDrugAvailability <= Integer.parseInt(column[6])) {
                        lowDrugsError = prescription.getPrescribedDrug() + " è in esaurimento.";
                    }
                }
            }
            if (!drugFound) {
                throw new DrugDoNotExistExc();
            }
            //modifica il farmaco con la nuova quantità
            try (PrintWriter writer = new PrintWriter(new File(path))) {
                long counter = fileData.getCounter();
                writer.println((counter));
                for (String[] column : fileData.getRows()) {
                    if (column[1].equals(prescription.getPrescribedDrug())) {
                        String updatedDrugAvailability = Integer.toString(Integer.parseInt(column[5]) - prescription.getDrugNumber());
                        column[5] = updatedDrugAvailability;
                    }
                    writer.println(String.join(Utility.SEPARATOR, column));
                }
            }
            return lowDrugsError;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //findLowDrugs restituisce una lista completa di farmaci

    @Override
    public List<Drug> findAllDrugs() throws BusinessException {
        drugs = new ArrayList<>();
        try {
            FileData fileData = Utility.readAllRows(path);
            for (String[] column : fileData.getRows()) {
                Drug drug = populateDrug(column);
                drugs.add(drug.getId() - 1, drug);
            }
            return drugs;
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

    /*
    findLowDrugs restituisce una lista di farmaci in esaurimento, controlla se il valore della quantità minima è maggiore
    o uguale alla quantità effettiva del farmaco.
    */
    @Override
    public List<Drug> findLowDrugs() throws BusinessException {
        drugs = new ArrayList<>();
        int counter = 0;
        try {
            FileData fileData = Utility.readAllRows(path);
            for (String[] column : fileData.getRows()) {
                if (Integer.parseInt(column[6]) >= Integer.parseInt(column[5])) {
                    Drug drug = populateDrug(column);
                    drugs.add(counter, drug);
                    counter++;
                }
            }
            return drugs;
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

    /*
    findAllDrugsByName restituisce una lista contenente i nomi di tutti i farmaci. Viene usato per popolare la
    choice box quando si vuole creare una prescrizione.
    */
    @Override
    public List<String> findAllDrugsByName() throws BusinessException {
        try {
            int counter = 0;
            List<String> drugNames = new ArrayList<String>();
            FileData fileData = Utility.readAllRows(path);
            for (String[] column : fileData.getRows()) {
                drugNames.add(counter, column[1]);
                counter++;
            }
            drugNames.add(null);
            return drugNames;
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

    private Drug populateDrug(String[] column) {
        Drug drug = new Drug();
        drug.setId(Integer.parseInt(column[0]));
        drug.setDrugName(column[1]);
        drug.setDrugPharmaceuticalCompany(column[2]);
        drug.setDrugActivePrinciple(column[3]);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
        drug.setDrugExpiry(LocalDate.parse(column[4], formatter));
        drug.setDrugAvailability(Integer.parseInt(column[5]));
        drug.setDrugMinimumQuantity(Integer.parseInt(column[6]));
        return drug;
    }

    private void createDrugString(Drug drug) {
        row.append(Utility.SEPARATOR);
        row.append(drug.getDrugName());
        row.append(Utility.SEPARATOR);
        row.append(drug.getDrugPharmaceuticalCompany());
        row.append(Utility.SEPARATOR);
        row.append(drug.getDrugActivePrinciple());
        row.append(Utility.SEPARATOR);
        row.append(drug.getDrugExpiry());
        row.append(Utility.SEPARATOR);
        row.append(drug.getDrugAvailability());
        row.append(Utility.SEPARATOR);
        row.append(drug.getDrugMinimumQuantity());
    }

}