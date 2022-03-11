package ir.alirezaalijani.security.springauthorizationservice.security.mail.model;

public abstract class BasicMailMessage {
    private final String toMail;
    private final String fromMail;
    private final String subject;
    private final String message;

    public BasicMailMessage(String toMail, String fromMail, String subject,String message) {
        this.toMail = toMail;
        this.fromMail = fromMail;
        this.subject = subject;
        this.message=message;
    }

    public String getToMail() {
        return toMail;
    }

    public String getFromMail() {
        return fromMail;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }
}
