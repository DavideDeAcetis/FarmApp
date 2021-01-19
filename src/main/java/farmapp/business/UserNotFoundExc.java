package farmapp.business;

public class UserNotFoundExc extends BusinessException {
    public UserNotFoundExc() {
    }

    public UserNotFoundExc(String message) {
        super(message);
    }

    public UserNotFoundExc(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundExc(Throwable cause) {
        super(cause);
    }

    public UserNotFoundExc(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
