package farmapp.business.impl.ram;

import farmapp.business.*;

public class RAMFarmAppBusinessFactoryImpl extends FarmAppBusinessFactory {
    private UserService userService;
    private PrescriptionService prescriptionService;
    private DrugService drugService;

    public RAMFarmAppBusinessFactoryImpl() {
        userService = new RAMUserServiceImpl();
        prescriptionService = new RAMPrescriptionServiceImpl();
        drugService = new RAMDrugServiceImpl();

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
