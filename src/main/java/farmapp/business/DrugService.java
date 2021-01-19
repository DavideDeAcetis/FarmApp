package farmapp.business;

import farmapp.domain.Drug;
import farmapp.domain.Prescription;

import java.util.List;

public interface DrugService {

    void createDrug(Drug drug) throws BusinessException;

    void updateDrug(Drug drug) throws BusinessException;

    void deleteDrug(int id) throws BusinessException;

    String soldDrugs(Prescription prescription) throws BusinessException;

    List<Drug> findAllDrugs() throws BusinessException;

    List<Drug> findLowDrugs() throws BusinessException;

    List<String> findAllDrugsByName() throws BusinessException;
}
