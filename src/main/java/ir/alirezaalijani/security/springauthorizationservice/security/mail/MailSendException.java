package ir.alirezaalijani.security.springauthorizationservice.security.mail;

public class MailSendException extends RuntimeException{
    public MailSendException() {
        super();
    }

    public MailSendException(String message) {
        super(message);
    }

    public MailSendException(String message, Throwable cause) {
        super(message, cause);
    }
}
