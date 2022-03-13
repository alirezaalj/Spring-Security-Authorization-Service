package ir.alirezaalijani.security.authorization.service.security.mail.model;

import java.util.Map;

public interface TemplateMailMessage {
    Map<String,Object> getObjectModel();
    String getTemplateHtml();
}
