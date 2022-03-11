package ir.alirezaalijani.security.springauthorizationservice.security.mail.model;

import ir.alirezaalijani.security.springauthorizationservice.security.mail.MailMessageVisitor;

public interface MailMessage {
    boolean accept(MailMessageVisitor visitor);
}
