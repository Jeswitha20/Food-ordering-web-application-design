package com.hcl.foodordering.service.email;

import com.hcl.foodordering.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class RegistrationEmailServiceImpl implements RegistrationEmailService {

    private static final Logger log = LoggerFactory.getLogger(RegistrationEmailServiceImpl.class);

    private final JavaMailSender mailSender;
    private final ResourceLoader resourceLoader;
    private final String mailFrom;

    public RegistrationEmailServiceImpl(
            JavaMailSender mailSender,
            ResourceLoader resourceLoader,
            @Value("${app.mail.from:}") String mailFrom
    ) {
        this.mailSender = mailSender;
        this.resourceLoader = resourceLoader;
        this.mailFrom = mailFrom == null || mailFrom.isBlank() ? null : mailFrom.trim();
    }

    @Override
    public boolean sendRegistrationSuccess(User user) {
        String template = loadTemplate("classpath:templates/registration-success.html");

        String html = template
                .replace("${userName}", escapeHtml(user.getName()))
                .replace("${userEmail}", escapeHtml(user.getEmail()));

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setTo(user.getEmail());
            if (mailFrom != null && !mailFrom.isBlank()) {
                helper.setFrom(mailFrom);
            }
            helper.setSubject("Welcome to Food Ordering, " + user.getName());
            helper.setText(html, true);
            mailSender.send(message);
            return true;
        } catch (MessagingException ex) {
            log.error("Failed to compose registration email. userEmail={}", user.getEmail(), ex);
            return false;
        } catch (Exception ex) {
            log.error("Failed to send registration email. userEmail={}", user.getEmail(), ex);
            return false;
        }
    }

    private String loadTemplate(String resourceLocation) {
        Resource resource = resourceLoader.getResource(resourceLocation);
        try {
            return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Email template not found: " + resourceLocation, e);
        }
    }

    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}

