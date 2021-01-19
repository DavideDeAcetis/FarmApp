package farmapp.business;

public class UserAlreadyExistExc extends BusinessException {

    public UserAlreadyExistExc() {
    }

    public UserAlreadyExistExc(String message) {
        super(message);
    }

    public UserAlreadyExistExc(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAlreadyExistExc(Throwable cause) {
        super(cause);
    }

    public UserAlreadyExistExc(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
