package kz.booking.report;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/admin/reports")
@PreAuthorize("hasRole('ADMIN')")
public class BekbolatovZholamanReportController {
    private final BekbolatovZholamanBookingReportService reportService;

    public BekbolatovZholamanReportController(BekbolatovZholamanBookingReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/bookings")
    public CompletableFuture<Map<String, String>> generateBookings(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return reportService.generateBookingsCsv(from, to).thenApply(r -> Map.of("reportKey", r.key()));
    }

    @GetMapping("/bookings/download")
    public ResponseEntity<Resource> downloadBookings(@RequestParam String reportKey) {
        Path path = reportService.resolveReportPath(reportKey);
        Resource res = new FileSystemResource(path);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/csv"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + reportKey + "\"")
                .body(res);
    }
}

