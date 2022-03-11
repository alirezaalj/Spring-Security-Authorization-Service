package ir.alirezaalijani.security.springauthorizationservice.security.mail.model;

import ir.alirezaalijani.security.springauthorizationservice.security.mail.MailMessageVisitor;

import java.util.Date;
import java.util.Map;

public class NotificationMail extends BasicMailMessage implements MailMessage , TemplateMailMessage{

    private final String hostUrl;
    private final String actionUrl;
    private final String username;
    private final String notMeUrl;
    private final Date date;
    private final String templateHtml;
    private final Map<String, Object> messageModel;

    public NotificationMail(String toMail,
                            String fromMail,
                            String subject,
                            String hostUrl,
                            String actionUrl,
                            String username,
                            String message,
                            String notMeUrl,
                            Date date,
                            String templateHtml) {
        super(toMail, fromMail, subject,message);
        this.hostUrl = hostUrl;
        this.actionUrl = actionUrl;
        this.username = username;
        this.notMeUrl = notMeUrl;
        this.date = date;
        this.templateHtml=templateHtml;
        this.messageModel=Map.of(
                "subject", getSubject(),
                "hostUrl", hostUrl,
                "actionUrl", actionUrl,
                "message", message,
                "username",username,
                "notMeUrl",notMeUrl,
                "date",date
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

    public Date getDate() {
        return date;
    }

    @Override
    public Map<String, Object> getObjectModel() {
        return this.messageModel;
    }

    @Override
    public String getTemplateHtml() {
        return this.templateHtml;
    }
}
