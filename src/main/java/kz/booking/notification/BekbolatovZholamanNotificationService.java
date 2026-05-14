package kz.booking.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class BekbolatovZholamanNotificationService {
    private static final Logger log = LoggerFactory.getLogger(BekbolatovZholamanNotificationService.class);

    @Async
    public CompletableFuture<Void> sendBookingCreatedEmail(String toEmail, Long bookingId) {
        // Simulate external I/O (email provider)
        log.info("Async: sending booking created email to={} bookingId={}", toEmail, bookingId);
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Void> sendBookingConfirmedEmail(String toEmail, Long bookingId) {
        log.info("Async: sending booking confirmed email to={} bookingId={}", toEmail, bookingId);
        return CompletableFuture.completedFuture(null);
    }
}

