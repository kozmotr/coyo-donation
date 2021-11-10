package com.coyoapp.donations.mail;

import com.coyoapp.donations.aop.TrackMe;
import com.coyoapp.donations.invitation.InvitationToken;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import io.netty.handler.codec.Headers;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Sends mails to owners.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MailService {

    private static final String MAIL_SUBJECT_INVITATION_CODE = "Your activation code";
    private static final String MAIL_SUBJECT_SIGN_UP_CONFIRMATION = "Sign up successful";
    private static final String MAIL_SUBJECT_NEW_DONATIONS = "New donations";

    @Value("${donations.mail.catchall:admin@donations.coyoapp.com}")
    private String defaultCatchAllRecipient;

    private final MailTemplateGenerator mailTemplateGenerator;
    private final JavaMailSender mailSender;
    private final RestTemplate restTemplate;

    @Async
    public void sendInvitationTokenMail(InvitationToken invitationToken) {
        final Map<String, Object> args = new HashMap<>();
        args.put("activationCode", invitationToken.getToken());
        send("mail/invitationtoken", Locale.getDefault(), args, defaultCatchAllRecipient, MAIL_SUBJECT_INVITATION_CODE);
    }
    @Async
    public void sendSignupConfirmationMail() {
        final Map<String, Object> args = new HashMap<>();
        send("mail/signupconfirmation", Locale.getDefault(), args, defaultCatchAllRecipient,
                MAIL_SUBJECT_SIGN_UP_CONFIRMATION);
    }
    @Async
    public void sendNewDonationsMail() {
        final Map<String, Object> args = new HashMap<>();
        args.put("amount", "109.81");
        args.put("currency", "€");
        send("mail/newdonations", Locale.getDefault(), args, defaultCatchAllRecipient, MAIL_SUBJECT_NEW_DONATIONS);
    }


    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    @SneakyThrows
    public void send(String templateName, Locale locale, Map<String, Object> args, String to, String subject) {
        String htmlContent = mailTemplateGenerator.createTemplate(templateName, locale, args);
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            msg.setFrom("hej@donations.coyoapp.com");
            MimeMessageHelper helper = new MimeMessageHelper(msg);
            helper.setText(htmlContent, true);
            helper.setTo(to);
            helper.setSubject(subject);
            MimeMessage mimeMessage = helper.getMimeMessage();
            logMessage(mimeMessage);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            mimeMessage.writeTo(bos);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
            HttpEntity<Object> entity = new HttpEntity<>(bos.toByteArray(), headers);
            executorService.submit(()->restTemplate.postForLocation("http://MAIL-SERVICE/mail/send", entity));
        } catch (MessagingException e) {
            throw new MailSendException("Error creating/sending email message " + to + ", " + subject, e);
        }
    }

    private void logMessage(MimeMessage msg) {
        if (log.isDebugEnabled()) {
            try {
                log.debug("Sending new email");
                log.debug("-------------------------");
                log.debug("From: " + Arrays.toString(msg.getFrom()));
                log.debug("To: " + Arrays.toString(msg.getRecipients(Message.RecipientType.TO)));
                log.debug("Cc: " + Arrays.toString(msg.getRecipients(Message.RecipientType.CC)));
                log.debug("Bcc: " + Arrays.toString(msg.getRecipients(Message.RecipientType.BCC)));
                log.debug("ReplyTo: " + Arrays.toString(msg.getReplyTo()));
                log.debug("Subject: " + msg.getSubject());
                log.debug("Date: " + msg.getSentDate());
                log.debug("Content: " + msg.getContent());
                log.debug("-------------------------");
            } catch (Exception e) {
                throw new MailSendException("Error sending mail to console", e);
            }
        }
    }
}
