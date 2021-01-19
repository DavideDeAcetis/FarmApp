/*
 * Drug
 *
 * Classe madre di tutti i farmaci.
 *
 * 18/07/2020
 */

package farmapp.domain;


import java.time.LocalDate;

public class Drug {
    private Integer id;
    private String drugName;
    private String drugPharmaceuticalCompany;
    private String drugActivePrinciple;
    private LocalDate drugExpiry;
    private int drugAvailability;
    private int drugMinimumQuantity;
    private Admin admin;

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer drugCode) {
        this.id = drugCode;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDrugPharmaceuticalCompany() {
        return drugPharmaceuticalCompany;
    }

    public void setDrugPharmaceuticalCompany(String drugPharmaceuticalCompany) {
        this.drugPharmaceuticalCompany = drugPharmaceuticalCompany;
    }

    public String getDrugActivePrinciple() {
        return drugActivePrinciple;
    }

    public void setDrugActivePrinciple(String drugActivePrinciple) {
        this.drugActivePrinciple = drugActivePrinciple;
    }

    public LocalDate getDrugExpiry() {
        return drugExpiry;
    }

    public void setDrugExpiry(LocalDate drugExpiry) {
        this.drugExpiry = drugExpiry;
    }

    public int getDrugAvailability() {
        return drugAvailability;
    }

    public void setDrugAvailability(int drugAvailability) {
        this.drugAvailability = drugAvailability;
    }

    public int getDrugMinimumQuantity() {
        return drugMinimumQuantity;
    }

    public void setDrugMinimumQuantity(int drugMinimumQuantity) {
        this.drugMinimumQuantity = drugMinimumQuantity;
    }
}