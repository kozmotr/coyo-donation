package com.coyo.mailservice.controller;

import com.coyo.mailservice.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@RequestMapping("mail/")
@RestController
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @GetMapping(value = "sendInvatation/{invatationToken}")
    public ResponseEntity send(@PathVariable String invatationToken) {
        mailService.sendInvitationTokenMail(invatationToken);
        /*we shoud handle exeptional case here for but sake of simplicty I leave it as it is.*/
        return ResponseEntity.ok("success");
    }

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public ResponseEntity sendMail(@RequestBody byte[] payload) throws MessagingException, IOException {
        MimeMessage mimeMessage = new JavaMailSenderImpl().createMimeMessage(new ByteArrayInputStream(payload));
        mailService.send(mimeMessage);
        return ResponseEntity.ok("success");
    }

    /*
    * Other services such as sendSignup and others are not handled here for simplicity.
    * */
}
