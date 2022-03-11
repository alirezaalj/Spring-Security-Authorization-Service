package ir.alirezaalijani.security.springauthorizationservice.security.mail;

import ir.alirezaalijani.security.springauthorizationservice.security.mail.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Service
public class MailServiceVisitor implements MailMessageVisitor {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine thymeleafTemplateEngine;

    public MailServiceVisitor(JavaMailSender javaMailSender,
                              SpringTemplateEngine thymeleafTemplateEngine) {
        this.javaMailSender = javaMailSender;
        this.thymeleafTemplateEngine = thymeleafTemplateEngine;
    }

    @Override
    public boolean visit(UserVerificationMail o) {
       return processMail(o,o);
    }

    @Override
    public boolean visit(PasswordChangeMail o) {
        return processMail(o,o);
    }

    @Override
    public boolean visit(NotificationMail o) {
        return processMail(o,o);
    }

    @Override
    public boolean visit(SimpleTextMail o) {
        return sendBasicMail(o);
    }

    private boolean processMail(BasicMailMessage mailMessage,TemplateMailMessage templateMailMessage){
        try {
            if (templateMailMessage!=null){
                String html=processTemplate(templateMailMessage);
                if (html!=null){
                    sendHtmlMessage(mailMessage,html);
                }else {
                    sendBasicMail(mailMessage);
                }
            }else {
                sendBasicMail(mailMessage);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    private boolean sendBasicMail(BasicMailMessage o){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(o.getFromMail());
            message.setTo(o.getToMail());
            message.setSubject(o.getSubject());
            message.setText(o.getMessage());
            javaMailSender.send(message);
            return true;
        } catch (MailSendException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    private String processTemplate(TemplateMailMessage templateMailMessage){
        var thymeleafContext = new Context();
        thymeleafContext.setVariables(templateMailMessage.getObjectModel());
        try {
            return thymeleafTemplateEngine.process(templateMailMessage.getTemplateHtml(), thymeleafContext);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private void sendHtmlMessage(BasicMailMessage mailMessage, String htmlBody) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(mailMessage.getFromMail());
        helper.setTo(mailMessage.getToMail());
        helper.setSubject(mailMessage.getSubject());
        helper.setText(htmlBody, true);
        javaMailSender.send(message);
    }
}
