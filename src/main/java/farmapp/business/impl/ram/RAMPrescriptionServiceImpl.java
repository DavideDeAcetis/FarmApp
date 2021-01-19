package farmapp.business.impl.ram;

import farmapp.business.BusinessException;
import farmapp.business.NoPrescriptionAssignedToFiscalCodeExc;
import farmapp.business.PrescriptionService;
import farmapp.domain.Medic;
import farmapp.domain.Prescription;
import farmapp.domain.User;

import java.util.ArrayList;
import java.util.List;

public class RAMPrescriptionServiceImpl implements PrescriptionService {

    private List<Prescription> prescriptions = new ArrayList<>();

    public RAMPrescriptionServiceImpl() {

        Prescription oki = new Prescription();
        oki.setId(0);
        oki.setPatientName("Mario");
        oki.setPatientSurname("Rossi");
        oki.setPrescriptionObject("Prescrizione Oki");
        oki.setPatientFiscalCode("123456");
        oki.setPrescriptionDetails("Da prendere in caso di mal di testa");
        oki.setPrescribedDrug("Oki");
        oki.setProcessedPrescription(0);
        oki.setDrugNumber(2);
        oki.setMedic(new Medic());
        oki.getMedic().setFiscalCode("CD");
        prescriptions.add(oki.getId(), oki);

        Prescription kestine = new Prescription();
        kestine.setId(1);
        kestine.setPatientName("Alfredo");
        kestine.setPatientSurname("Giancaterino");
        kestine.setPrescriptionObject("Prescrizone kestine");
        kestine.setPatientFiscalCode("687541");
        kestine.setPrescriptionDetails("Da prendere 2 volte al giorno");
        kestine.setPrescribedDrug("Kestine");
        kestine.setProcessedPrescription(0);
        kestine.setDrugNumber(5);
        kestine.setMedic(new Medic());
        kestine.getMedic().setFiscalCode("CD");

        prescriptions.add(kestine.getId(), kestine);

        Prescription neoduplamox = new Prescription();
        neoduplamox.setId(2);
        neoduplamox.setPatientName("Antonio");
        neoduplamox.setPatientSurname("Giancristofaro");
        neoduplamox.setPrescriptionObject("Prescrizione neoduplamox");
        neoduplamox.setPatientFiscalCode("78976189");
        neoduplamox.setPrescriptionDetails("da prendere ogni 5 minuti");
        neoduplamox.setPrescribedDrug("Neoduplamox");
        neoduplamox.setProcessedPrescription(0);
        neoduplamox.setDrugNumber(5);
        neoduplamox.setMedic(new Medic());
        neoduplamox.getMedic().setFiscalCode("CD");

        prescriptions.add(neoduplamox.getId(), neoduplamox);

        Prescription tachipirina = new Prescription();
        tachipirina.setId(3);
        tachipirina.setPatientName("Leonardo");
        tachipirina.setPatientSurname("Zappacosta");
        tachipirina.setPrescriptionObject("Prescrizione tachipirina");
        tachipirina.setPatientFiscalCode("8415387");
        tachipirina.setPrescriptionDetails("Da prendere in caso di febbre");
        tachipirina.setPrescribedDrug("Tachipirina");
        tachipirina.setProcessedPrescription(0);
        tachipirina.setDrugNumber(1);
        tachipirina.setMedic(new Medic());
        tachipirina.getMedic().setFiscalCode("CD");

        prescriptions.add(tachipirina.getId(), tachipirina);
    }

    @Override
    public void createPrescription(Prescription prescription) throws BusinessException {
        prescription.setId(prescriptions.size());
        prescriptions.add(prescription.getId(), prescription);
    }

    @Override
    public void updatePrescription(Prescription prescription) throws BusinessException {
        prescriptions.set(prescription.getId(), prescription);
    }

    @Override
    public List<Prescription> findPrescriptionsByFiscalCode(User user) throws BusinessException{
        List<Prescription> presFiscalCodeList = new ArrayList<>();
        for (int i = 0; i < prescriptions.size(); i++){
            if(prescriptions.get(i).getMedic().getFiscalCode().equals(user.getFiscalCode())){
                presFiscalCodeList.add(prescriptions.get(i));
            }
        }
        if (presFiscalCodeList.isEmpty()){
            throw new NoPrescriptionAssignedToFiscalCodeExc();
        }
        return presFiscalCodeList;
    }

    @Override
    public List<Prescription> findPrescriptionsByFiscalCode(String fiscalCode) throws BusinessException{
        List<Prescription> presFiscalCodeList = new ArrayList<>();
        for (int i = 0; i < prescriptions.size(); i++){
            if(prescriptions.get(i).getPatientFiscalCode().equals(fiscalCode)){
                presFiscalCodeList.add(prescriptions.get(i));
            }
        }
        if (presFiscalCodeList.isEmpty()){
            throw new NoPrescriptionAssignedToFiscalCodeExc();
        }
        return presFiscalCodeList;
    }

    @Override
    public void deletePrescription(int id){
        prescriptions.remove(id);

    }

    @Override
    public void processPrescription(Prescription prescription){
        for (int i = 0; i < prescriptions.size(); i++){
            if(prescriptions.get(i).getId().equals(prescription.getId())){
                prescriptions.get(i).setProcessedPrescription(1);
            }
        }
    }

    @Override
    public List<Prescription> findPrescriptionHistory(User user) throws BusinessException {
        List<Prescription> presHistoryList = new ArrayList<>();
        for (int i = 0; i < prescriptions.size(); i++){
            if(prescriptions.get(i).getProcessedPrescription() == 1){
                presHistoryList.add(prescriptions.get(i));
            }
        }
        if (presHistoryList.isEmpty()){
            throw new NoPrescriptionAssignedToFiscalCodeExc();
        }
        return presHistoryList;
    }
}
