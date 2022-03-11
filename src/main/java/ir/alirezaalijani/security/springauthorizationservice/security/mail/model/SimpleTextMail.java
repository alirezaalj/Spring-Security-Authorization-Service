package ir.alirezaalijani.security.springauthorizationservice.security.mail.model;

import ir.alirezaalijani.security.springauthorizationservice.security.mail.MailMessageVisitor;

public class SimpleTextMail extends BasicMailMessage implements MailMessage {
    public SimpleTextMail(String toMail, String fromMail, String subject, String message) {
        super(toMail, fromMail, subject, message);
    }

    @Override
    public boolean accept(MailMessageVisitor visitor) {
        return visitor.visit(this);
    }
}
