package farmapp.business;

public class NoDrugsAvailableExc extends BusinessException {

    public NoDrugsAvailableExc() {
    }

    public NoDrugsAvailableExc(String message) {
        super(message);
    }

    public NoDrugsAvailableExc(String message, Throwable cause) {
        super(message, cause);
    }

    public NoDrugsAvailableExc(Throwable cause) {
        super(cause);
    }

    public NoDrugsAvailableExc(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
