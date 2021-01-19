package farmapp.business;

public class DrugDoNotExistExc extends BusinessException {

    public DrugDoNotExistExc() {
    }

    public DrugDoNotExistExc(String message) {
        super(message);
    }

    public DrugDoNotExistExc(String message, Throwable cause) {
        super(message, cause);
    }

    public DrugDoNotExistExc(Throwable cause) {
        super(cause);
    }

    public DrugDoNotExistExc(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
