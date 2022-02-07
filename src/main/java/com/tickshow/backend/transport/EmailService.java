package com.tickshow.backend.transport;

import com.tickshow.backend.exception.DispatcherException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Service
@AllArgsConstructor
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String template) throws DispatcherException {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED,
                    StandardCharsets.UTF_8.name()
            );
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("hello@tickShow.com");
            helper.setText(template, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Error occurred while sending email, cause: {}", e.getMessage());
            throw new DispatcherException("Error occurred while sending email");
        }
    }
}