package ir.alirezaalijani.security.springauthorizationservice.security.mail;

import ir.alirezaalijani.security.springauthorizationservice.security.mail.model.NotificationMail;
import ir.alirezaalijani.security.springauthorizationservice.security.mail.model.PasswordChangeMail;
import ir.alirezaalijani.security.springauthorizationservice.security.mail.model.SimpleTextMail;
import ir.alirezaalijani.security.springauthorizationservice.security.mail.model.UserVerificationMail;

public interface MailMessageVisitor {
    boolean visit(UserVerificationMail o);
    boolean visit(PasswordChangeMail o);
    boolean visit(NotificationMail o);
    boolean visit(SimpleTextMail o);
}
