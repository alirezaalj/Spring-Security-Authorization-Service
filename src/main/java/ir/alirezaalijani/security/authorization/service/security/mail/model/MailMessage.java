package ir.alirezaalijani.security.authorization.service.security.mail.model;

import ir.alirezaalijani.security.authorization.service.security.mail.MailMessageVisitor;

public interface MailMessage {
    boolean accept(MailMessageVisitor visitor);
}
