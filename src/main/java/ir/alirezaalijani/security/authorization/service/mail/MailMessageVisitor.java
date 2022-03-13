package ir.alirezaalijani.security.authorization.service.security.mail;

import ir.alirezaalijani.security.authorization.service.security.mail.model.*;

public interface MailMessageVisitor {
    void visit(UserVerificationMail o);
    void visit(PasswordChangeMail o);
    void visit(NotificationMail o);
    void visit(DefaultTemplateMail o);
    void visit(SimpleTextMail o);
}
