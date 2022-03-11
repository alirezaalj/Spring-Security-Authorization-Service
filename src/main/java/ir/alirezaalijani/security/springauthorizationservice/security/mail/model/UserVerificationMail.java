package ir.alirezaalijani.security.springauthorizationservice.security.mail.model;

import ir.alirezaalijani.security.springauthorizationservice.security.mail.MailMessageVisitor;

import java.util.Map;

public class UserVerificationMail extends BasicMailMessage implements TemplateMailMessage,MailMessage{

    private final String hostUrl;
    private final String actionUrl;
    private final String templateHtml;
    private final Map<String, Object> messageModel;

    public UserVerificationMail(String toMail,
                                String fromMail,
                                String subject,
                                String hostUrl,
                                String actionUrl,
                                String message,
                                String templateHtml) {
        super(toMail, fromMail, subject,message);
        this.hostUrl = hostUrl;
        this.actionUrl = actionUrl;
        this.templateHtml=templateHtml;
        this.messageModel = Map.of(
                "subject", getSubject(),
                "hostUrl", hostUrl,
                "actionUrl", actionUrl,
                "message", message
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

    @Override
    public String getTemplateHtml() {
        return templateHtml;
    }

    @Override
    public Map<String, Object> getObjectModel() {
        return this.messageModel;
    }
}
