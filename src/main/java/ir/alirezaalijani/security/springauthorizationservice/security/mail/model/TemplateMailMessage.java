package ir.alirezaalijani.security.springauthorizationservice.security.mail.model;

import java.util.Map;

public interface TemplateMailMessage {
    Map<String,Object> getObjectModel();
    String getTemplateHtml();
}
