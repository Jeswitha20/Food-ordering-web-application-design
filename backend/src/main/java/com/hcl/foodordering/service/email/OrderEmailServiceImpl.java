package com.hcl.foodordering.service.email;

import com.hcl.foodordering.entity.Order;
import com.hcl.foodordering.entity.OrderItem;
import com.hcl.foodordering.entity.User;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.ResourceLoader;

@Service
public class OrderEmailServiceImpl implements OrderEmailService {

    private static final Logger log = LoggerFactory.getLogger(OrderEmailServiceImpl.class);

    private final JavaMailSender mailSender;
    private final ResourceLoader resourceLoader;

    private final String mailFrom;

    public OrderEmailServiceImpl(
            JavaMailSender mailSender,
            ResourceLoader resourceLoader,
            @Value("${app.mail.from:}") String mailFrom
    ) {
        this.mailSender = mailSender;
        this.resourceLoader = resourceLoader;
        this.mailFrom = mailFrom == null || mailFrom.isBlank() ? null : mailFrom.trim();
    }

    @Override
    public boolean sendOrderConfirmation(User user, Order order) {
        String to = user.getEmail();
        String template = loadTemplate();

        String itemsHtml = renderItemsHtml(order.getItems());
        String totalAmount = formatAmount(order.getTotalAmount());

        String html = template
                .replace("${userName}", escapeHtml(user.getName()))
                .replace("${orderId}", String.valueOf(order.getId()))
                .replace("${restaurantName}", escapeHtml(order.getRestaurant().getName()))
                .replace("${itemsHtml}", itemsHtml)
                .replace("${totalAmount}", totalAmount);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setTo(to);
            if (mailFrom != null && !mailFrom.isBlank()) {
                helper.setFrom(mailFrom);
            }
            helper.setSubject("Order Confirmation #" + order.getId());
            helper.setText(html, true);
            mailSender.send(message);
            return true;
        } catch (MessagingException ex) {
            log.error("Failed to compose order confirmation email. orderId={}", order.getId(), ex);
            return false;
        } catch (Exception ex) {
            log.error("Failed to send order confirmation email. orderId={}", order.getId(), ex);
            return false;
        }
    }

    @Override
    public boolean sendOrderCancellation(User user, Order order) {
        String to = user.getEmail();
        String template = loadTemplate("classpath:templates/order-cancellation.html");

        String itemsHtml = renderItemsHtml(order.getItems());
        String totalAmount = formatAmount(order.getTotalAmount());

        String html = template
                .replace("${userName}", escapeHtml(user.getName()))
                .replace("${orderId}", String.valueOf(order.getId()))
                .replace("${restaurantName}", escapeHtml(order.getRestaurant().getName()))
                .replace("${itemsHtml}", itemsHtml)
                .replace("${totalAmount}", totalAmount);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setTo(to);
            if (mailFrom != null && !mailFrom.isBlank()) {
                helper.setFrom(mailFrom);
            }
            helper.setSubject("Order Cancelled #" + order.getId());
            helper.setText(html, true);
            mailSender.send(message);
            return true;
        } catch (MessagingException ex) {
            log.error("Failed to compose order cancellation email. orderId={}", order.getId(), ex);
            return false;
        } catch (Exception ex) {
            log.error("Failed to send order cancellation email. orderId={}", order.getId(), ex);
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

    private String loadTemplate() {
        return loadTemplate("classpath:templates/order-confirmation.html");
    }

    private String renderItemsHtml(List<OrderItem> items) {
        // Basic HTML table rows; kept simple by design (no external template engine).
        return items.stream()
                .map(i -> "<tr>" +
                        "<td style='padding:8px 10px;border-bottom:1px solid #eee;'>" + escapeHtml(i.getMenuItem().getName()) + "</td>" +
                        "<td style='padding:8px 10px;border-bottom:1px solid #eee;text-align:center;'>" + i.getQuantity() + "</td>" +
                        "<td style='padding:8px 10px;border-bottom:1px solid #eee;text-align:right;'>" + escapeHtml(formatAmount(i.getUnitPrice())) + "</td>" +
                        "<td style='padding:8px 10px;border-bottom:1px solid #eee;text-align:right;'>" + escapeHtml(formatAmount(i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))) + "</td>" +
                        "</tr>"
                )
                .collect(Collectors.joining());
    }

    private String formatAmount(BigDecimal amount) {
        if (amount == null) {
            return "0.00";
        }
        return amount.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
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

