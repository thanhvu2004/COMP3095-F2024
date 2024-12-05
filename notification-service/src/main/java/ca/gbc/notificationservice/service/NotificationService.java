package ca.gbc.notificationservice.service;

import ca.gbc.notificationservice.event.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final JavaMailSender javaMailSender;

    @KafkaListener(topics = "order-placed")
    public void listen(OrderPlacedEvent orderPlacedEvent) {
        log.info("Received message from order-placed topic {}", orderPlacedEvent);
        // Send email logic
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom("comp3095@georgebrown.ca");
            messageHelper.setTo(orderPlacedEvent.getEmail());
            messageHelper.setSubject(String.format("Your Order (%s) was placed successfully",
                    orderPlacedEvent.getOrderNumber()));
            messageHelper.setText("""
                Good day,
                
                Your order has been placed successfully.
                
                Thank you for shopping with us.
                COMP3095
                """);
        };
        try {
            javaMailSender.send(messagePreparator);
            log.info("Email sent successfully");
        } catch (MailException e) {
            log.error("Error sending email", e);
            throw new RuntimeException("Error sending email", e);
        }
    }
}
