package farmapp.business;

import farmapp.domain.Prescription;
import farmapp.domain.User;

import java.util.List;

public interface PrescriptionService {

    void createPrescription(Prescription prescription) throws BusinessException;

    void updatePrescription(Prescription prescription) throws BusinessException;

    List<Prescription> findPrescriptionsByFiscalCode(User user) throws BusinessException;

    List<Prescription> findPrescriptionsByFiscalCode(String fiscalCode) throws BusinessException;

    void deletePrescription(int id) throws BusinessException;

    void processPrescription(Prescription prescription) throws BusinessException;

    List<Prescription> findPrescriptionHistory(User user) throws BusinessException;
}
