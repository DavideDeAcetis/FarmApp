package farmapp.business;

public class NoPrescriptionAssignedToFiscalCodeExc extends BusinessException {
    public NoPrescriptionAssignedToFiscalCodeExc() {
    }

    public NoPrescriptionAssignedToFiscalCodeExc(String message) {
        super(message);
    }

    public NoPrescriptionAssignedToFiscalCodeExc(String message, Throwable cause) {
        super(message, cause);
    }

    public NoPrescriptionAssignedToFiscalCodeExc(Throwable cause) {
        super(cause);
    }

    public NoPrescriptionAssignedToFiscalCodeExc(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
