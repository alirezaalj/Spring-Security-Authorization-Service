package ir.alirezaalijani.security.authorization.service.security.mail.model;


import ir.alirezaalijani.security.authorization.service.security.mail.MailMessageVisitor;

import java.util.Map;

public class PasswordChangeMail extends BasicMailMessage implements TemplateMailMessage,MailMessage{

    private final String hostUrl;
    private final String actionUrl;
    private final String username;
    private final String notMeUrl;
    private final String templateHtml;
    private final Map<String, Object> objectModel;
    public PasswordChangeMail(String toMail,
                              String fromMail,
                              String subject,
                              String hostUrl,
                              String actionUrl,
                              String username,
                              String message,
                              String notMeUrl,
                              String templateHtml) {
        super(toMail, fromMail, subject,message);
        this.hostUrl = hostUrl;
        this.actionUrl = actionUrl;
        this.username = username;
        this.notMeUrl = notMeUrl;
        this.templateHtml=templateHtml;
        this.objectModel=Map.of(
                "subject", getSubject(),
                "hostUrl", hostUrl,
                "actionUrl", actionUrl,
                "message", message,
                "username",username,
                "notMeUrl",notMeUrl
        );
    }

    @Override
    public boolean accept(MailMessageVisitor visitor) {
        return visitor.visit(this);
    }

    public String getHostUrl() {
        return hostUrl;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getNotMeUrl() {
        return notMeUrl;
    }

    @Override
    public String getTemplateHtml() {
        return templateHtml;
    }

    @Override
    public Map<String, Object> getObjectModel() {
        return objectModel;
    }
}
