package farmapp.business;

import farmapp.business.impl.file.FileFarmAppBusinessFactoryImpl;

public abstract class FarmAppBusinessFactory {
    private static FarmAppBusinessFactory factory = new FileFarmAppBusinessFactoryImpl();
    //private static FarmaciaBusinessFactory factory = new RAMFarmAppBusinessFactory();

    public static FarmAppBusinessFactory getInstance() {
        return factory;
    }

    public abstract UserService getUserService();

    public abstract DrugService getDrugService();

    public abstract PrescriptionService getPrescriptionService();

}
