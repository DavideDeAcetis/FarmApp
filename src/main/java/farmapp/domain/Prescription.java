/*
 * Prescription
 *
 * Classe madre di tutte le prescrizioni.
 *
 * 18/07/2020
 */

package farmapp.domain;

public class Prescription {
    private Integer id;
    private String patientName;
    private String patientSurname;
    //L'oggetto della prescrizione è un brevissimo riassunto di cosa contiene una prescrizione, viene visualizzato nelle
    //liste delle prescrizioni degli utenti e dei medici.
    private String prescriptionObject;;
    //Viene specificato solo il codice fiscale del paziente in quanto un medico può creare prescrizioni di più pazienti
    //ma ad essi verranno associati solo il codice fiscale del medico che ha fatto la prescrizione.
    private String patientFiscalCode;
    private String prescriptionDetails;
    private String prescribedDrug;
    //la variabile processed prescription ha valore di default 0 e viene messa ad 1 solo quando la prescrizione viene
    //evasa, in questo modo il farmacista ed il medico non possono vedere/modificare prescrizioni già evase al contrario
    //del paziente che le potrà consultare nel suo storico.
    private int processedPrescription;
    private int drugNumber;
    //L'istanza della classe medico associata alla prescrizione viene usata principalmente per prelevare il campo del
    //codice fiscale o per poter passare il tipo medico al renderView al cambio di pagine nella gestione di liste e creazione
    //delle prescrizioni.
    private Medic medic;

    public int getProcessedPrescription() {
        return processedPrescription;
    }

    public void setProcessedPrescription(int processedPrescription) {
        this.processedPrescription = processedPrescription;
    }

    public String getPrescribedDrug() {
        return prescribedDrug;
    }

    public void setPrescribedDrug(String prescribedDrug) {
        this.prescribedDrug = prescribedDrug;
    }

    public int getDrugNumber() {
        return drugNumber;
    }

    public void setDrugNumber(int drugNumber) {
        this.drugNumber = drugNumber;
    }

    public String getPrescriptionObject() {
        return prescriptionObject;
    }

    public void setPrescriptionObject(String prescriptionObject) {
        this.prescriptionObject = prescriptionObject;
    }

    public Medic getMedic() {
        return medic;
    }

    public void setMedic(Medic medic) {
        this.medic = medic;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientSurname() {
        return patientSurname;
    }

    public void setPatientSurname(String patientSurname) {
        this.patientSurname = patientSurname;
    }

    public String getPatientFiscalCode() {
        return patientFiscalCode;
    }

    public void setPatientFiscalCode(String patientFiscalCode) {
        this.patientFiscalCode = patientFiscalCode;
    }

    public String getPrescriptionDetails() {
        return prescriptionDetails;
    }

    public void setPrescriptionDetails(String prescriptionDetails) {
        this.prescriptionDetails = prescriptionDetails;
    }
}
