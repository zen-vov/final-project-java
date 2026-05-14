package kz.booking.report;

import kz.booking.booking.entity.BekbolatovZholamanBooking;
import kz.booking.booking.repo.BekbolatovZholamanBookingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class BekbolatovZholamanBookingReportService {
    private static final Logger log = LoggerFactory.getLogger(BekbolatovZholamanBookingReportService.class);

    private final BekbolatovZholamanBookingRepository bookingRepository;
    private final Path reportsDir;

    public BekbolatovZholamanBookingReportService(
            BekbolatovZholamanBookingRepository bookingRepository,
            @Value("${app.files.storageDir}") String storageDir
    ) {
        this.bookingRepository = bookingRepository;
        this.reportsDir = Path.of(storageDir).resolve("reports");
    }

    @Async
    @Transactional(readOnly = true)
    public CompletableFuture<ReportResult> generateBookingsCsv(LocalDate from, LocalDate to) {
        try {
            Files.createDirectories(reportsDir);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create reports dir: " + e.getMessage(), e);
        }

        List<BekbolatovZholamanBooking> bookings = bookingRepository.search(
                null, null, null, null, from, to, null,
                org.springframework.data.domain.PageRequest.of(0, 50_000)
        ).getContent();

        StringBuilder sb = new StringBuilder();
        sb.append("id,userEmail,hotel,room,startDate,endDate,status\n");
        for (BekbolatovZholamanBooking b : bookings) {
            sb.append(b.getId()).append(',')
                    .append(escape(b.getUser().getEmail())).append(',')
                    .append(escape(b.getRoom().getHotel().getName())).append(',')
                    .append(escape(b.getRoom().getRoomNumber())).append(',')
                    .append(b.getStartDate()).append(',')
                    .append(b.getEndDate()).append(',')
                    .append(b.getStatus()).append('\n');
        }

        String key = UUID.randomUUID() + "_bookings.csv";
        Path path = reportsDir.resolve(key);
        try {
            Files.writeString(path, sb.toString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write report: " + e.getMessage(), e);
        }

        log.info("Async: generated booking report key={} rows={}", key, bookings.size());
        return CompletableFuture.completedFuture(new ReportResult(key, path));
    }

    public Path resolveReportPath(String key) {
        return reportsDir.resolve(key);
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\"", "\"\"");
    }

    public record ReportResult(String key, Path path) {
    }
}

