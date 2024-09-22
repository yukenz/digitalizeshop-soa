package id.co.awan.digitalizeshopsoa.exception;

public class UnauthorizedSoapException extends RuntimeException {
    public UnauthorizedSoapException(String message) {
        super("[" + UnauthorizedSoapException.class.getName() + "] " + message);
    }

    public UnauthorizedSoapException(Exception e) {
        super("[" + UnauthorizedSoapException.class.getName() + "] " + e.getMessage());
    }

}
