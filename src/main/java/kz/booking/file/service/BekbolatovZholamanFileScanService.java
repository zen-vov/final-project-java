package kz.booking.file.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class BekbolatovZholamanFileScanService {
    private static final Logger log = LoggerFactory.getLogger(BekbolatovZholamanFileScanService.class);

    @Async
    public CompletableFuture<Void> scanAsync(Long fileId, String storageKey) {
        // Simulate virus scan / external job
        log.info("Async: scanning fileId={} storageKey={}", fileId, storageKey);
        return CompletableFuture.completedFuture(null);
    }
}

