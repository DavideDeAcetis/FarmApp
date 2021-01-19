package farmapp.business.impl.ram;

import farmapp.business.BusinessException;
import farmapp.business.DrugDoNotExistExc;
import farmapp.business.DrugService;
import farmapp.domain.Drug;
import farmapp.domain.Prescription;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RAMDrugServiceImpl implements DrugService {

    private List<Drug> drugs = new ArrayList<>();

    public RAMDrugServiceImpl() {
        LocalDate expiryDate;

        Drug oki = new Drug();
        oki.setId(0);
        oki.setDrugName("Oki");
        oki.setDrugPharmaceuticalCompany("Dompé");
        oki.setDrugActivePrinciple("Ketoprofene");
        expiryDate = LocalDate.of(2021, 3, 1);
        oki.setDrugExpiry(expiryDate);
        oki.setDrugAvailability(30);
        oki.setDrugMinimumQuantity(5);
        drugs.add(oki.getId(), oki);

        Drug kestine = new Drug();
        kestine.setId(1);
        kestine.setDrugName("Kestine");
        kestine.setDrugPharmaceuticalCompany("Almirall");
        kestine.setDrugActivePrinciple("Ebastina");
        expiryDate = LocalDate.of(2021, 8, 1);
        kestine.setDrugExpiry(expiryDate);
        kestine.setDrugAvailability(30);
        kestine.setDrugMinimumQuantity(5);
        drugs.add(kestine.getId(), kestine);

        Drug neoduplamox = new Drug();
        neoduplamox.setId(2);
        neoduplamox.setDrugName("Neoduplamox");
        neoduplamox.setDrugPharmaceuticalCompany("Valeas");
        neoduplamox.setDrugActivePrinciple("Amoxicillina");
        expiryDate = LocalDate.of(2021, 6, 1);
        neoduplamox.setDrugExpiry(expiryDate);
        neoduplamox.setDrugAvailability(20);
        neoduplamox.setDrugMinimumQuantity(5);
        drugs.add(neoduplamox.getId(), neoduplamox);

        Drug tachipirina = new Drug();
        tachipirina.setId(3);
        tachipirina.setDrugName("Tachipirina");
        tachipirina.setDrugPharmaceuticalCompany("Angelini");
        tachipirina.setDrugActivePrinciple("Paracetamolo");
        expiryDate = LocalDate.of(2022, 10, 1);
        tachipirina.setDrugExpiry(expiryDate);
        tachipirina.setDrugAvailability(15);
        tachipirina.setDrugMinimumQuantity(3);
        drugs.add(tachipirina.getId(), tachipirina);
    }

    @Override
    public void createDrug(Drug drug) {
        drug.setId(drugs.size());
        drugs.add(drug.getId(), drug);
    }

    @Override
    public void updateDrug(Drug drug) {
        drugs.set(drug.getId(), drug);
    }

    @Override
    public void deleteDrug(int id) {
        for (int i = 0; i < drugs.size(); i++) {
            if (drugs.get(i).getId().equals(id)) {
                drugs.remove(id);
            }
        }
    }

    @Override
    public List<Drug> findAllDrugs() {
        return drugs;
    }

    @Override
    public List<Drug> findLowDrugs() {
        List<Drug> lowDrugsList = new ArrayList<>();
        for (int i = 0; i < drugs.size(); i++) {
            if (drugs.get(i).getDrugMinimumQuantity() >= drugs.get(i).getDrugAvailability()) {
                lowDrugsList.add(drugs.get(i));
            }
        }
        return lowDrugsList;
    }

    @Override
    public List<String> findAllDrugsByName() {
        List<String> drugsNameList = new ArrayList<>();
        for (int i = 0; i < drugs.size(); i++) {
            drugsNameList.add(drugs.get(i).getDrugName());
        }
        drugsNameList.add(null);
        return drugsNameList;
    }


    @Override
    public String soldDrugs(Prescription prescription) throws BusinessException{
        boolean drugFound = false;
        for (int i = 0; i < drugs.size(); i++) {
            if (drugs.get(i).getDrugName().equals(prescription.getPrescribedDrug())) {
                drugs.get(i).setDrugAvailability(prescription.getDrugNumber());
                drugFound = true;
            }
        }
        if (!drugFound) {
            throw new DrugDoNotExistExc();
        }
        return prescription.getPrescribedDrug() + " è in esaurimento.";
    }
}
